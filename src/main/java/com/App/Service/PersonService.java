package com.App.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.App.Constants.EnlightenHillsConstants;
import com.App.Model.Person;
import com.App.Model.Roles;
import com.App.Repository.PersonRepo;
import com.App.Repository.RolesRepo;

@Service
public class PersonService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PersonRepo personRepo;

	@Autowired
	private RolesRepo rolesRepo;

	public boolean createNewPerson(Person person) {
		boolean isSaved = false;
		Roles role = rolesRepo.getByRoleName(EnlightenHillsConstants.STUDENT_ROLE); // Assigning student role for every
																					// user as only students will
																					// register themselves because admin
																					// we have registered by hard coding
																					// SQL query
		person.setRole(role);
		
		person.setPwd(passwordEncoder.encode(person.getPwd())); // getting the plain text password that the user entered
																// and converting it to hashcode value using encode
																// method available in BCryptPasswordEncoder class
		personRepo.save(person);
		if (null != person && person.getPersonId() > 0) {
			isSaved = true;
		}

		return isSaved;
	}
}
