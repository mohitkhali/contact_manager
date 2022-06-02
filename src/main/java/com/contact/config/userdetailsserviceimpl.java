package com.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contact.dao.userrepository;
import com.contact.entities.User;

public class userdetailsserviceimpl implements UserDetailsService{
	
	@Autowired
	private userrepository userrepo;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userrepo.getuserbyusername(username);
		
		if(user==null) {
			
			throw new UsernameNotFoundException("could not found user");
		}
		
		customuserdetail cutomuserCustomuserdetail = new customuserdetail(user);
		return cutomuserCustomuserdetail;
	}

	
	
}
