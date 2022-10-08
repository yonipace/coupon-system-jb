package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.login.LoginManagerInterface.Role;
import app.core.services.AdminService;
import app.core.utilities.JwtUtil;

@CrossOrigin
@RestController
@RequestMapping("/signup")
public class SignUpController {

	@Autowired
	private AdminService service;
	@Autowired
	private JwtUtil jwt;

	@PostMapping("/company")
	public String registerCompany(@RequestBody Company company) {

		try {
			Company c = service.addCompany(company);
			return jwt.generateToken(c.getId(), c.getEmail(), c.getName(), Role.COMPANY);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/customer")
	public String registerCompany(@RequestBody Customer customer) {

		try {
			Customer c = service.addCustomer(customer);
			return jwt.generateToken(c.getId(), c.getEmail(), c.getFirstName() + " " + c.getLastName(), Role.CUSTOMER);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
