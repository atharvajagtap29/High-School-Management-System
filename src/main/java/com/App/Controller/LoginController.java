package com.App.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

	// below in parameters we are using error and logout variables in order to be
	// relieved from creating multiple pages. we will use login page only for all
	// the tasks
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public String displayLoginPage(@RequestParam(value = "error", required = false) String error,
									@RequestParam(value = "logout", required = false) String logout,
									@RequestParam(value = "register", required = false) String register,
									Model model) {

		String errorMessage = null; // when a firstimer tries to access login page by going /login url, obviously
									// the
									// error and logout values are gonna be null. so when they are null, we will
									// send user to login.html page.

		// but if error variable populates as we do it in SecurityConfig class
		// '/login?error=true'
		if (error != null) {
			errorMessage = "Username or Password is incorrect";
		}

		// similarly if logout variable populates
		if (logout != null) {
			errorMessage = "You have been successfully logged out!";
		}
		
		// similarly if register variable populates
		if(register != null) {
			errorMessage = "Successfully Signed Up! Login with registered credentials";
		}

		model.addAttribute("errorMessage", errorMessage);

		return "login.html";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout=true";
	}

}
