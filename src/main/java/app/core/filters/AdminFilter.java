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
import org.springframework.http.HttpStatus;

import app.core.login.LoginManagerInterface.Role;
import app.core.utilities.JwtUtil;

public class AdminFilter implements Filter {

	private JwtUtil jwtUtil;

	public AdminFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			String token = req.getHeader("token");

			// CORS
			res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
			res.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
			res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
			res.setIntHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, 5);
			// request info
			String requestPath = req.getRequestURI();
			String httpMethod = req.getMethod();

			if (req.getMethod().equals("OPTIONS")) {
				res.setStatus(HttpServletResponse.SC_ACCEPTED);
				return;
			}

			if (jwtUtil.extractRole(token).equals(Role.ADMIN)) {
				chain.doFilter(request, response);
			} else {
				// if not logged in - block the request
				res.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not logged in");
			}

		} catch (Exception e) {

			((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

		}
	}
}