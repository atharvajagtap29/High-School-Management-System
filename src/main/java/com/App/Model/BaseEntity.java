package com.App.Model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	// these are the 4 columns that are gonna be common in every table of our
	// database. This class and contact class are gonna be linked with schema.sql
	// file

	// since we have been populating these values from interface in ContactService
	// class, now we can actually populate them based on the user logged in, and can
	// get that info from SpringSecurity FW

	@CreatedDate
	@Column(updatable = false) // indicates when running an update query on my table, do not consider these
								// fields
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;
	

	
	@LastModifiedDate
	@Column(insertable = false) // similarly indicates when running an insert query on my table, do not consider
								// these fields
	private LocalDateTime updatedAt;

	@LastModifiedBy
	@Column(insertable = false)
	private String updatedBy;

}
