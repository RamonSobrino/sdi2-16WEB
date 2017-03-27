package uo.sdi.presentation.filter;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uo.sdi.dto.User;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/admin/*" }, initParams = { @WebInitParam(name = "LoginParam", value = "/index.xhtml") })
public class AdminLoginFilter implements Filter {
	FilterConfig config = null;

	/**
	 * Default constructor.
	 */
	public AdminLoginFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		User user = (User) session.getAttribute("LOGGEDIN_USER");

		if (user == null) {
			String loginForm = config.getInitParameter("LoginParam");
			/*
			 * FacesContext.getCurrentInstance().addMessage( null, new
			 * FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
			 * .getString("error"), bundle .getString("error_not_logedin")));
			 */
			res.sendRedirect(req.getContextPath() + loginForm);
			return;
		} else {
			if (!user.getIsAdmin()) {
				// TODO: Decidir a donde se redirecciona
				res.sendRedirect(req.getContextPath() + "/index.xhtml");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// // TODO Auto-generated method stub
		// Iniciamos la variable de instancia config
		config = fConfig;
	}

}
