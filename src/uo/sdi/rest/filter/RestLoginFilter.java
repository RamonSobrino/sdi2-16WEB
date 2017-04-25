package uo.sdi.rest.filter;

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

import org.jboss.resteasy.util.Base64;

import uo.sdi.business.LoginService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.infrastructure.Factories;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/rest/*" }, initParams = { @WebInitParam(name = "LoginParam", value = "/index.xhtml") })
public class RestLoginFilter implements Filter {
	FilterConfig config = null;

	LoginService login = Factories.services.getLoginService();

	/**
	 * Default constructor.
	 */
	public RestLoginFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
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

		String aut = req.getHeader("Authorization");
		if (aut != null) {
			byte[] bytes = Base64.decode(aut.split(" ")[1].getBytes());
			String[] userPass = new String(bytes).split(":");

			if(userPass.length<2){
				res.setStatus(401);
				return;
			}
			User u = null;
			try {
				u = login.doLogin(userPass[0], userPass[1]);
			} catch (BusinessException e) {
				res.setStatus(401);
				return;
			}

			if (u == null) {
				res.setStatus(401);
				return;
			} else {
				session.setAttribute("user", u);
			}
		} else {
			res.setStatus(401);
			return;
		}

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// Iniciamos la variable de instancia config
		config = fConfig;
	}

}
