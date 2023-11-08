package com.App.Security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.App.Model.Person;
import com.App.Model.Roles;
import com.App.Repository.PersonRepo;

@Component
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PersonRepo personRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName(); // email is nothing but username user will enter in the login page
		String pwd = authentication.getCredentials().toString(); // this line of code will return the password of the
																	// user which we will hash
		Person person = personRepo.readByEmail(email); // select * from person where email = email; this is the query in
														// short
		if (null != person && person.getPersonId() > 0 && passwordEncoder.matches(pwd, person.getPwd())) {
			return new UsernamePasswordAuthenticationToken(email, null, getGrantedAuthorities(person.getRole()));
		} else {
			throw new BadCredentialsException("Invalid credentials!");
		}
	}

	private List<GrantedAuthority> getGrantedAuthorities(Roles roles) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));
		return grantedAuthorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
