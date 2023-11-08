package com.App.Controller;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.App.Model.Address;
import com.App.Model.Courses;
import com.App.Model.Person;
import com.App.Repository.ClassesRepo;
import com.App.Repository.CoursesRepo;
import com.App.Repository.PersonRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.*;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("student")
public class StudentController {

	@Autowired
	ClassesRepo classesRepo;

	@Autowired
	PersonRepo personRepo;

	@Autowired
	CoursesRepo coursesRepo;

	@GetMapping("/displayCourses")
	public ModelAndView displayCourses(Model model, HttpSession session) {
		Person person = (Person) session.getAttribute("loggedInPerson");
		ModelAndView modelAndView = new ModelAndView("courses_enrolled.html");
		modelAndView.addObject("person", person);
		return modelAndView;
	}

	@GetMapping("/displayAllCourses")
	public ModelAndView displayAllCourses(Model model) {
		ModelAndView modelAndView = new ModelAndView("allcourses.html");
		List<Courses> courses = coursesRepo.findAll();
		modelAndView.addObject("courses", courses);
		return modelAndView;
	}
	
	
	// creating order details once user clicks enrollNow button
	@GetMapping("/createOrder")
	@ResponseBody
	public ModelAndView createOrder(@RequestParam("courseId") int courseId, HttpSession session) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView("orderConfirmation.html");
		
		// Retrieve the logged-in person from the session
		Person person = (Person) session.getAttribute("loggedInPerson");

		// Get the person's details
		String personName = person.getName();
		Address address = person.getAddress();
		String personAddress1 = address.getAddress1();
		String personCity = address.getCity();
		String personState = address.getState();

		// Retrieve course details from the database
		Optional<Courses> courses = coursesRepo.findById(courseId);
		if (courses.isPresent()) {
			Courses course = courses.get();
			String courseName = course.getName();
			String courseFees = course.getFees().replaceAll("[^0-9]", "");
			int courseFeesAmt = Integer.parseInt(courseFees);

			// Create a Razorpay order
			RazorpayClient client = new RazorpayClient("rzp_test_LuPCsIaIk2Z0wC", "0FFTJ251PIgzrKMSL473Xa42");

			// Build order details
			JSONObject orderRequest = new JSONObject();

			orderRequest.put("amount", courseFeesAmt); // Amount in paise (INR)
			orderRequest.put("currency", "USD");
			orderRequest.put("receipt", "txn_263534");
			orderRequest.put("notes",
					new JSONObject().put("courseName", courseName).put("personName", personName)
							.put("personAddress1", personAddress1).put("personCity", personCity)
							.put("personState", personState));

			// Creating a new order. If you want you can save this info in DB. I am not
			// because my project is due tomorrow
			Order order = client.orders.create(orderRequest);
			log.info(order.toString());
			
			
			// converting to String
			String orderDetails = order.toString();
			
			// sending order as parameter to UI
			modelAndView.addObject("orderDetails", orderDetails);
			//modelAndView.setViewName("orderConfirmation");

		} else {
			// Handle the case where the course is not found
			// You can return an error message or redirect the user
		}

		return modelAndView;
	}
}
