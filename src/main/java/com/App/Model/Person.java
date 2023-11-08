package com.App.Model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.App.Annotations.FieldsValueMatch;
import com.App.Annotations.PasswordValidator;
import com.fasterxml.jackson.core.sym.Name;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@FieldsValueMatch.List({ @FieldsValueMatch(field = "pwd", fieldMatch = "confirmPwd", // if field[pwd] and
																						// fieldMatch[confirmPwd] do not
																						// match, throw below message as
																						// error
		message = "Passwords do not match!"),
		@FieldsValueMatch(field = "email", fieldMatch = "confirmEmail", message = "Email addresses do not match!") })
public class Person extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int personId;

	@NotBlank(message = "Name must not be blank")
	@Size(min = 3, message = "Name must be at least 3 characters long")
	private String name;

	@NotBlank(message = "Mobile number must not be blank")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
	private String mobileNumber;

	@NotBlank(message = "Email must not be blank")
	@Email(message = "Please provide a valid email address")
	private String email;

	@NotBlank(message = "Confirm Email must not be blank")
	@Email(message = "Please provide a valid confirm email address")
	@Transient // dont consider this field for any database operations
	private String confirmEmail;

	@NotBlank(message = "Password must not be blank")
	@Size(min = 5, message = "Password must be at least 5 characters long")
	@PasswordValidator // custom validator to check user is not entering weak password
	private String pwd;

	@NotBlank(message = "Confirm Password must not be blank")
	@Size(min = 5, message = "Confirm Password must be at least 5 characters long")
	@Transient
	private String confirmPwd;
	
	
	/*------------------------------ LINKED CLASSES I.E TABLES IN THE DATABASE------------------------------------------*/
	
	// one to one - one person can be assigned with one role only.
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Roles.class)
	@JoinColumn(name = "role_id", referencedColumnName = "roleId", nullable = false)
	private Roles role;

	// one to one - one person can have one address only.
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Address.class)
	@JoinColumn(name = "address_id", referencedColumnName = "addressId", nullable = true) // here name is the foreign
																							// key column in table
	private Address address;

	// many to one - many persons can be allocated to one class.
	// optional true because it is possible that my person is not associated with
	// any classes. it may happen at the time of registration process
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "class_id", referencedColumnName = "classId", nullable = true)
	private Classes classes; // this field name is the one you need to mention in the mappedBy clause in
								// Parent entity class.
								// and referencedColumn is the field name of primary key in parent class i.e
								// Classes class

	// many to many - Many person can enroll to multiple courses
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "person_courses", joinColumns = {
			@JoinColumn(name = "person_id", referencedColumnName = "personId") }, inverseJoinColumns = {
					@JoinColumn(name = "course_id", referencedColumnName = "courseId") })
	private Set<Courses> courses = new HashSet<>();
}
