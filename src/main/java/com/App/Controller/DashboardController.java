package com.App.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.App.Model.Person;
import com.App.Repository.PersonRepo;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	@Autowired
	PersonRepo personrepo;

	@RequestMapping("/dashboard")
	public String displayDashboard(Model model, Authentication authentication, HttpSession session) {

		Person person = personrepo.readByEmail(authentication.getName()); // select * from person where email = [this
																			// the query that this method fires onto
																			// database]

		model.addAttribute("username", person.getName()); // gets username entered on previous page ('login
															// page')

		model.addAttribute("roles", authentication.getAuthorities()); // gets roles entered on previous page ('login
																		// page')

		// throw new RuntimeException("It has been a rough year"); // global exception
		// demo. enter valid credentials
		
		if(null != person.getClasses() && null != person.getClasses().getName()) {
			model.addAttribute("enrolledClass" , person.getClasses().getName());
		}

		session.setAttribute("loggedInPerson", person);

		return "dashboard.html";
	}

}
