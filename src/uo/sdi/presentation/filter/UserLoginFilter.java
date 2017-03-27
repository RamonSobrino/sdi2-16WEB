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
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/user/*" }, initParams = { @WebInitParam(name = "LoginParam", value = "/index.xhtml") })
public class UserLoginFilter implements Filter {
	FilterConfig config = null;

	/**
	 * Default constructor.
	 */
	public UserLoginFilter() {
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
		// Si no es petición HTTP nada que hacer
		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}
		// En el resto de casos se verifica que se haya hecho login previamente
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("LOGGEDIN_USER");
		// TODO: Encontrar manera de conseguir el RB y poner mensaje de error
		// ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		if (user == null) {
			String loginForm = config.getInitParameter("LoginParam");
			res.sendRedirect(req.getContextPath() + loginForm);
			return;
		} else {
			if (user.getIsAdmin()) {
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
