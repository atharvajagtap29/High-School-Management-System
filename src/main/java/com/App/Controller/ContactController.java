package com.App.Controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.App.Model.Contact;
import com.App.Service.ContactService;

import jakarta.validation.Valid;

@Controller
public class ContactController {
	
	//private static Logger log = (Logger) LoggerFactory.getLogger(ContactController.class);
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ContactController.class);
	
	@Autowired
	private ContactService contactService;
//	@Autowired
//	public ContactController(ContactService contactService) {
//		this.contactService = contactService;
//	}
	
	
	// show contact page with this URL
	@RequestMapping("/contact")
	public String showContactPage(Model model) {
		model.addAttribute("contact", new Contact());
		return "contact.html";
	}
	
	
	// here is another approach to deal with the same, by creating a pojo class to accept and using its object
	@RequestMapping(value = "/saveMsg" , method = POST)
	public String saveMessege(@Valid @ModelAttribute("contact") Contact contact, Errors err) {
		
		if(err.hasErrors()) {
			log.error("Contact form validations failed due to "+err.toString());
			return "contact.html";
		}
		
		contactService.saveMessageDetails(contact);
		return "redirect:/contact";
	}
	
	
	// this method will display messages with status as OPEN
	@RequestMapping("/displayMessages/page/{pageNum}")
	public ModelAndView displayOpenMessages(Model model , @PathVariable(name = "pageNum") int pageNum , 
			@RequestParam("sortField") String sortField , @RequestParam("sortDir") String sortDir) {
		
		Page<Contact> msgPage = contactService.findMsgsWithOpenStatus(pageNum , sortField , sortDir);
		List<Contact> contactMsgs = msgPage.getContent();
		ModelAndView modelandview = new ModelAndView("messages.html");
		modelandview.addObject("currentPage", pageNum);
		modelandview.addObject("totalPages", msgPage.getTotalPages());
		modelandview.addObject("totalMsgs", msgPage.getTotalElements());
		modelandview.addObject("sortField", sortField);
		modelandview.addObject("sortDir", sortDir);
		modelandview.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		modelandview.addObject("contactMsgs", contactMsgs);
		return modelandview;
		
	}
	
	
	// this method will work to close the message
	@RequestMapping(value = "/closeMsg" , method = RequestMethod.GET)
	public String closeMsg(@RequestParam("id") int id) {
		contactService.updateMsgStatus(id);
		return "redirect:/displayMessages/page/1?sortField=name&sortDir=desc";
	}

}
