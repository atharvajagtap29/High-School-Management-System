package com.App.RestServices;

// Spring Data REST

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.App.Constants.EnlightenHillsConstants;
import com.App.Model.Contact;
import com.App.Model.Response;
import com.App.Repository.ContactRepo;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // This annotation indicates that this class is responsible for creating RESTful
				// web services using Spring MVC
@RequestMapping(path = "/api/contact")
public class ContactRestController {

	@Autowired
	ContactRepo contactRepo;

	// getting by populating request parameter with status
	@GetMapping("/getMessagesByStatus")
	// @ResponseBody // this annotation specifies spring that please fetch the data
	// only. I dont
	// want any UI or view, cuz the api call you will make to the view or frontend
	// will have the necessary template resolver
	public List<Contact> getMessagesByStatus(@RequestParam(name = "status") String status) {
		return contactRepo.findByStatus(status);
	}

	// getting contact info by assigning status in body
	@GetMapping("/getAllMessagesByStatus")
	// @ResponseBody
	public List<Contact> getAllMessagesByStatus(@RequestBody Contact contact) {
		if (null != contact && null != contact.getStatus()) {
			return contactRepo.findByStatus(contact.getStatus());
		} else {
			return List.of();
		}
	}

	// saving message details
	@PostMapping("/saveMessage")
	public ResponseEntity<Response> saveMsg(@RequestHeader("invocationFrom") String invocationFrom,
			@Valid @RequestBody Contact contact) {
		contactRepo.save(contact);
		Response response = new Response();
		response.setStatusCode("200");
		response.setStatusMsg("Message Saved Successfully");
		return ResponseEntity.status(HttpStatus.CREATED).header("isMsgSaved", "true").body(response);
	}

	// deleting message details
	@DeleteMapping("/deleteMessage")
	public ResponseEntity<Response> deleteMessage(RequestEntity<Contact> requestEntity) {
		Contact contact = requestEntity.getBody();
		contactRepo.deleteById(contact.getContactId());
		Response response = new Response();
		response.setStatusCode("200");
		response.setStatusMsg("Message Successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// close message status
	@PatchMapping("/closeMessage") // this method [HTTP.PATCH] is used when small updation needs to be done on any
									// column of a record. Like here we wanna update Open to Close
	public ResponseEntity<Response> closeMessageStatus(@RequestBody Contact contactReq) {
		Response response = new Response();
		Optional<Contact> contact = contactRepo.findById(contactReq.getContactId());
		if (contact.isPresent()) {
			contact.get().setStatus(EnlightenHillsConstants.CLOSE);
			contactRepo.save(contact.get());
		} else {
			response.setStatusCode("400");
			response.setStatusMsg("Invalid Contact ID recieved");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		response.setStatusCode("200");
		response.setStatusMsg("Message closed successfully");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
