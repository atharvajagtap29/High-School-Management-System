package com.App.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.App.Model.Holidays;
import com.App.Repository.HolidaysRepo;

@Controller
public class HolidaysController {
	
	@Autowired
	HolidaysRepo holidaysRepo;
	
	// @RequestMapping(value = "/holidays", method = RequestMethod.GET)
	@GetMapping("/holidays/{display}")
	public String displayHolidays(@PathVariable String display, Model model) {

		if (display != null && display.equals("all")) {
			model.addAttribute("festival", true);
			model.addAttribute("federal", true);
		} else if (display != null && display.equals("festival")) {
			model.addAttribute("festival", true);
		} else if (display != null && display.equals("federal")) {
			model.addAttribute("federal", true);
		}
		
		
		// calling findallholidays in HolidaysRepo.java [Spring JDBC]
		//List<Holidays> holidays = holidaysRepo.findAllHolidays();
		
		
		// finding all the holidays by this findAll method in CrudRepository interface. No need to write SQL queries [Spring Data JPA]
		Iterable<Holidays> holidaysIt = holidaysRepo.findAll();
		List<Holidays> holidays = StreamSupport.stream(holidaysIt.spliterator(), false).collect(Collectors.toList());

		// we can access an enum by class name. iterating through enum
		Holidays.Type[] types = Holidays.Type.values();
		for (Holidays.Type type : types) {
			// here setting attribute. 1st param will be the value fetched by for-each
			// loop[i.e either FESTIVAL or FEDERAL]
			// 2nd attribute is the lambda
			model.addAttribute(type.toString(),
					(holidays.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList())));
		}
		return "holidays.html";

	}
}
