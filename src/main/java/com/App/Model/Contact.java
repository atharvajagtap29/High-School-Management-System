// this package will contain all the simple pojo classes
package com.App.Model;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
@Data annotation is provided by Lombok library which generates getter, setter,
equals(), hashCode(), toString() methods & Constructor at compile time.
This makes our code short and clean.
* */

@Entity
@Table(name = "contact_msg") // mention when database table name does not match the pojo class name
@Data
public class Contact extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // telling the spring data JPA how is the
																			// primary key value gonna be generated
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "contact_id") // if column name is not matching field name, use this annotation to specify the
									// column name in your table
	private int contactId;

	/*
	 * @NotNull: Checks if a given field is not null but allows empty values & zero
	 * elements inside collections.
	 * 
	 * @NotEmpty: Checks if a given field is not null and its size/length is greater
	 * than zero.
	 * 
	 * @NotBlank: Checks if a given field is not null and trimmed length is greater
	 * than zero.
	 */
	@NotBlank(message = "Name must not be blank")
	@Size(min = 3, message = "Name must be at least 3 characters long")
	private String name;

	@NotBlank(message = "Mobile number must not be blank")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
	private String mobileNum;

	@NotBlank(message = "Email must not be blank")
	@Email(message = "Please provide a valid email address")
	private String email;

	@NotBlank(message = "Subject must not be blank")
	@Size(min = 5, message = "Subject must be at least 5 characters long")
	private String subject;

	@NotBlank(message = "Message must not be blank")
	@Size(min = 10, message = "Message must be at least 10 characters long")
	private String message;

	private String status;

}