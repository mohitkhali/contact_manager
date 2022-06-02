package com.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contact.dao.userrepository;
import com.contact.entities.User;
import com.contact.helper.Message;

@Controller
public class Homecontroller {
	
	@Autowired
	private BCryptPasswordEncoder passwordencoder;
	
	@Autowired
	private userrepository userrepo;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home -smart contact Manager");
		
		return "home";
		
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","about");
		
		return "about";
		
	}
	
	
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Signup");
		model.addAttribute("user",new User());
		return "signup";
		
	}
	
	@PostMapping("/do_register")
	public String registerUser( @Valid @ModelAttribute("user") User user,
			BindingResult result1 ,@RequestParam(value= "agrement",defaultValue = "false") boolean agrement,
			Model model,HttpSession session ) {
		try {
			
			if(!agrement) {
				System.out.println("you have not accept agrement");
				throw new Exception("you have not accept agrement");
				
			}
			if(result1.hasErrors()) {
				System.out.println("Error "+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordencoder.encode(user.getPassword()));
			
			System.out.println("agrement"+agrement);
			System.out.println("user"+user);
			 User result = this.userrepo.save(user);
	 
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered :)","alert-success"));
			
			return "signup";
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("something went wrong!!"+e.getMessage(),"alert-danger"));
		
			return "redirect:signup";
		}
		
		
		}
		
		
		
	
	
	
	@GetMapping("/signin")
	public String cutomlogin(Model model) {
		
		model.addAttribute("title","Login");
		return "login";
		
		
	}
	
	

	
	
}
