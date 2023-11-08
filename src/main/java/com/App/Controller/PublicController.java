package com.App.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.App.Model.Person;
import com.App.Service.PersonService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("public")
public class PublicController {

	@Autowired
	private PersonService personService;

	@RequestMapping(value = "/register", method = { RequestMethod.GET })
	public String displayRegisterationPage(Model model) {
		model.addAttribute("person", new Person()); // sending a brand new Person object to the UI
		return "register.html";
	}

	@RequestMapping(value = "/createUser", method = { RequestMethod.POST })
	public String createUser(@Valid @ModelAttribute("person") Person person, Errors err) {
		if (err.hasErrors()) {
			return "register.html";
		}
		boolean isSaved = personService.createNewPerson(person);
		if (isSaved) {
			return "redirect:/login?register=true";
		} else {
			return "register.html";
		}

	}

}
