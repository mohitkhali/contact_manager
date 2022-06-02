package com.contact.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entities.User;


public interface userrepository extends JpaRepository<User, Integer>{

	
@Query("select u from User u where u.email= :email")
	public User getuserbyusername(@Param("email") String email);

	
}
