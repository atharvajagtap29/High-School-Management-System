package com.App.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.App.Model.Address;
import com.App.Model.Person;
import com.App.Model.Profile;
import com.App.Repository.PersonRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProfileController {

	@Autowired
	PersonRepo personrepo;

	@RequestMapping("displayProfile")
	public ModelAndView displayProfilePage(Model model, HttpSession session) {

		// getting logged In users session. By that session we can get his details
		Person person = (Person) session.getAttribute("loggedInPerson");

		Profile profileObj = new Profile();

		// populating profile object with user details that got from session, so if it
		// is a already registered user, the information will be pre-filled for him/her
		profileObj.setName(person.getName());
		profileObj.setEmail(person.getEmail());
		profileObj.setMobileNumber(person.getMobileNumber());

		// if address is already saved by user, the address too will be shown else
		// address area will be kept blank only
		if (person.getAddress() != null && person.getAddress().getAddressId() > 0) {
			profileObj.setAddress1(person.getAddress().getAddress1());
			profileObj.setAddress2(person.getAddress().getAddress2());
			profileObj.setCity(person.getAddress().getCity());
			profileObj.setState(person.getAddress().getState());
			profileObj.setZipCode(person.getAddress().getZipCode());
		}

		ModelAndView modelandview = new ModelAndView("profile.html");
		modelandview.addObject("profile", profileObj);
		return modelandview;
	}

	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors err, HttpSession session) {

		if (err.hasErrors()) {
			return "profile.html";
		}

		// getting the logged in person
		Person person = (Person) session.getAttribute("loggedInPerson");
		person.setName(profile.getName());
		person.setEmail(profile.getEmail());
		person.setMobileNumber(profile.getMobileNumber());

		// if it is newly registered user, and address is not set, we check if it is not
		// set and if not, now we populate person object from the address information we
		// got filled in profile object from UI
		if (person.getAddress() == null || !(person.getAddress().getAddressId() > 0)) {
			person.setAddress(new Address());
		}
		person.getAddress().setAddress1(profile.getAddress1());
		person.getAddress().setAddress2(profile.getAddress2());
		person.getAddress().setCity(profile.getCity());
		person.getAddress().setState(profile.getState());
		person.getAddress().setZipCode(profile.getZipCode());
		
		// saving person object in the database
		Person updatedPerson = personrepo.save(person);
		session.setAttribute("loggedInPerson", updatedPerson);
		
		return "redirect:/displayProfile";
	}

}
