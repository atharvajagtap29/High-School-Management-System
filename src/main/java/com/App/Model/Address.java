package com.App.Model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "Address")
public class Address extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
	@GenericGenerator(name = "native" , strategy = "native")
	private int addressId;
	
	@NotBlank(message="Address1 must not be blank")
    @Size(min=5, message="Address1 must be at least 5 characters long")
    private String address1;
	
	private String address2;
	
    @NotBlank(message="City must not be blank")
    @Size(min=5, message="City must be at least 5 characters long")
    private String city;
    
    @NotBlank(message="State must not be blank")
    @Size(min=5, message="State must be at least 5 characters long")
    private String state;

    @NotBlank(message="Zip Code must not be blank")
    @Pattern(regexp="(^$|[0-9]{6}$)",message = "Zip Code must be 6 digits")
    private String zipCode;
    
}
