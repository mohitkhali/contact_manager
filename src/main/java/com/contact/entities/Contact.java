package com.contact.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	@NotBlank(message = "contact name can't be blank")
	@Size(min = 2,max = 20,message = "min 2 and max 20 chracters are allowed")
	private String name;
	private String secondName;
	
	@NotBlank(message="work can't be blank")
	private String work;
	@NotBlank(message = "email should not be blank")
	@Email(regexp ="^[A-Za-z0-9+_.-]+@(.+)$",message = "Invalid Email" )
	private String email;
	
	@NotBlank(message = "please fill the number")
	@Pattern(regexp ="(^$|[0-9]{10})" )
	@Size(max = 10,min = 10,message = "phone number should be 10 digit")
	private String phone;
	private String image;
	@Column(length = 5000)
	private String description;
	
	
	@ManyToOne
	@JsonIgnore
	private User user;
	public Contact() {
		super();
		
	}
	
	
}
