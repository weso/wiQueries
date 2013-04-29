package es.weso.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import es.weso.util.Conf;

public class NiceURIInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURI();
		if (requestUri.contains(".")) {
			return true;
		} else { // Asking for a jsp, will redirect to pubby
			response.sendRedirect(Conf.getConfig("pubby.location")
					+ requestUri.replace("/webindex/version2012/", ""));
			return false;
		}
	}
}
