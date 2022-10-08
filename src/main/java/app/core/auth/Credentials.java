package app.core.auth;

import app.core.login.LoginManagerInterface.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Credentials {

	private String email;
	private String password;
	private Role role;

}
