package app.core.login;

import app.core.auth.Credentials;
import app.core.exceptions.CouponSystemException;

public interface LoginManagerInterface {

	Object login(Credentials credentials) throws CouponSystemException;

	enum Role {

		ADMIN, COMPANY, CUSTOMER

	}

}
