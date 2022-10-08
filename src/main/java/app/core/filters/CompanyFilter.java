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

public class CompanyFilter implements Filter {

	private JwtUtil jwtUtil;

	public CompanyFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final String token = request.getHeader("token");

		try {
			System.out.println("company filter");

			response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, OPTIONS, HEAD, PUT, POST,DELETE");
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type,my-token,myHeaders,token");

			if (request.getMethod().equals("OPTIONS")) {
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				return;
			}

			if (jwtUtil.extractRole(token).equals(Role.COMPANY)) {
				chain.doFilter(request, response);
			} else {
				// if not logged in - block the request
				response.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not logged in");
			}

		} catch (Exception e) {

			response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

		}
	}
}