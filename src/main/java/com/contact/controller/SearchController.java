package com.contact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.contact.dao.contactrepository;
import com.contact.dao.userrepository;
import com.contact.entities.Contact;
import com.contact.entities.User;

@RestController
public class SearchController {
	@Autowired
	private userrepository userepo;
	@Autowired
	private contactrepository conrepo;

	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {
		User user = this.userepo.getuserbyusername(principal.getName());
		List<Contact> contacts = this.conrepo.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}

}
