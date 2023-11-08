package com.App.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.App.Constants.EnlightenHillsConstants;
import com.App.Model.Contact;
import com.App.Repository.ContactRepo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {

	/**
	 * Save Contact Details into DB
	 * 
	 * @param contact
	 * @return boolean
	 */

	@Autowired
	private ContactRepo contactRepo;

	// method to save the contact details into database.
	public boolean saveMessageDetails(Contact contact) {

		boolean isSaved = false;

		contact.setStatus(EnlightenHillsConstants.OPEN);

		Contact savedContact = contactRepo.save(contact); // above we had written sql query to populate the object
															// fields coming from UI. Here this sql query and populating
															// the object is taken care by spring Data JPA

		if (savedContact != null && savedContact.getContactId() > 0) {
			isSaved = true;
		}

		return isSaved;

	}

	// lists messages in messeges.html view, with open status
	public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir) {
		int pageSize = 4; // initializing the size that is number of records that can be displayed on one
							// page
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
				sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		// above pageNum - 1 is used because framework follows zero-based indexing so
		// first page user requests will be stored at 0th position by default by JPA. So
		// in order to access page 1 coming from URL we need to minus 1 from it so that
		// it becomes 0 which corresponds to first page and the page will be served

		Page<Contact> msgPage = contactRepo.findByStatus(EnlightenHillsConstants.OPEN, pageable);
		return msgPage;
	}

	// here we are updating the message status by calling the custom method defined in repository class
	public boolean updateMsgStatus(int contactId) {
		boolean isUpdated = false;
		int rows = contactRepo.updateContactStatus(EnlightenHillsConstants.CLOSE, contactId);
		if (rows > 0) {
			isUpdated = true;
		}
		return isUpdated;
	}

}
