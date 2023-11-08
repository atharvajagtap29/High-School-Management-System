package com.App.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.App.Model.Contact;

// this class is gonna be responsible to store the data coming from UI to database

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {

	List<Contact> findByStatus(String status);

	// here c is an alias name. and below is JPQL query
	@Query("SELECT c FROM Contact c WHERE c.status = :status") // select * from contact_msg where status = OPEN
	Page<Contact> findByStatus(@Param("status") String status, Pageable pageable);

	// mention these 2 annotations while performing update or delete operations i.e
	// operations that will affect the values of your database tables
	@Transactional
	@Modifying
	@Query("UPDATE Contact c SET c.status = :status WHERE contactId = :id") // these queries are JPQL i.e here Contact
	int updateContactStatus(String status, int id); // is not database table but Entity class
													// and other fields are not columns but
													// fields of that class
	// above method return number of rows affected so int
}
