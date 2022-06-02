package com.contact.entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name="USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "name should not be blank!")@Size(min = 2,max = 20,message = "min 2 and max 20 chracters are allowed")
	
	private String name;
	
	@NotBlank(message = "email should not be blank")
	@Email(regexp ="^[A-Za-z0-9+_.-]+@(.+)$",message = "Invalid Email" )
	@Column(unique = true)
	private String email;
	
	
	@NotBlank(message = "password is required")
	@Size(min = 5,max = 100,message = "this password is so weak")
	private String password;
	
	
	private String role;
	private boolean enabled;
	private String imageUrl;
	
	@Column(length = 500)
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
	private java.util.List<Contact> contacts= new ArrayList<>();

	public User() {
		super();
		
	}
	
	
	
	
	
	
}
