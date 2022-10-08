package app.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

//@Component
//@Order(1)
public class BasicFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("=== filter 1");
		// casting to http type of request / response
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// CORS
		resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		resp.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
		resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
		resp.setIntHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, 5);
		// request info
		String requestPath = req.getRequestURI();
		String httpMethod = req.getMethod();
		System.out.println(httpMethod + ":" + requestPath);
		// handle CORS preflight request
		if (httpMethod.equalsIgnoreCase(HttpMethod.OPTIONS.toString())) {
			System.out.println("CORS preflight request");
			chain.doFilter(request, response);
			return;
		}

		chain.doFilter(request, response);

	}
}
