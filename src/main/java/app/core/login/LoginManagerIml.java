package app.core.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.core.auth.Credentials;
import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import app.core.utilities.JwtUtil;

@Component
public class LoginManagerIml implements LoginManagerInterface {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private ApplicationContext ctx;

	@Value("${client.admin.email}")
	private String adminEmail;
	@Value("${client.admin.password}")
	private String adminPassword;

	@Override
	public String login(Credentials credentials) throws CouponSystemException {

		String email = credentials.getEmail();
		String password = credentials.getPassword();
		Role role = credentials.getRole();

		if (email.equals(adminEmail) && password.equals(adminPassword)) {
			return jwtUtil.generateToken(0, email, "Admin", Role.ADMIN);

		}
		if (role.equals(Role.COMPANY)) {
			CompanyService service = ctx.getBean(CompanyService.class);
			Company company = service.login(email, password);
			return jwtUtil.generateToken(company.getId(), email, company.getName(), Role.COMPANY);
		}

		if (role.equals(Role.CUSTOMER)) {
			CustomerService service = ctx.getBean(CustomerService.class);
			Customer customer = service.login(email, password);
			return jwtUtil.generateToken(customer.getId(), email,
					customer.getFirstName() + " " + customer.getLastName(), Role.CUSTOMER);
		}
		throw new CouponSystemException("login failed - email or password are incorrect");
	}

}
