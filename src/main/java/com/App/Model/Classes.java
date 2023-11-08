package com.App.Model;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "class")
public class Classes extends BaseEntity {

	@Id
	@GenericGenerator(name = "native", strategy = "native")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private int classId;

	@NotBlank(message = "Name must not be blank")
	@Size(message = "Name must atleast have 3 characters")
	private String name;

	
	// since a class can have multiple persons we map class with person entity with
	// one to many mapping

	// here mapped by means the field of this class in child entity i.e person. We
	// usually mention this in parent entity class. And target entity is the child
	// class
	@OneToMany(mappedBy = "classes", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Person.class)
	private Set<Person> persons;

}
