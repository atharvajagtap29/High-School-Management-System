package com.App.Model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Entity
@Table(name = "courses")
public class Courses extends BaseEntity 
{	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
	@GenericGenerator(name = "native" , strategy = "native")
	private int courseId;
	
	
	private String name;
	
	private String fees;
	
	// multiple persons can enroll to a particular course
	@ManyToMany(mappedBy = "courses" , fetch = FetchType.EAGER , cascade = CascadeType.PERSIST)
	private Set<Person> persons = new HashSet<>();
}
