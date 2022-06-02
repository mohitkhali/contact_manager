package com.contact.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.contact.dao.contactrepository;
import com.contact.dao.userrepository;
import com.contact.entities.Contact;
import com.contact.entities.User;
import com.contact.helper.Message;

@RequestMapping("/user")
@Controller

public class UserController {
	@Autowired
	private BCryptPasswordEncoder  bCryptPasswordEncoder ;

	@Autowired
	private contactrepository contactrepo;

	@Autowired
	private userrepository userrepo;
	private User user;
	private File file2;

	@ModelAttribute
	public void addcommandata(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println("username" + username);

		User user = userrepo.getuserbyusername(username);

		model.addAttribute("user", user);

	}

	@GetMapping("/dash")
	public String dashboard(Model model, Principal principal) {

		String username = principal.getName();
		User user = userrepo.getuserbyusername(username);
		int size = user.getContacts().size();
		model.addAttribute("user", user);
		model.addAttribute("title", "dashboard");
		model.addAttribute("size",size);
		return "normal/user_dashboard";
		
		
		
	}

	@GetMapping("/addcontact")
	public String addcontact(Model model) {

		model.addAttribute("title", "Add contact");
		model.addAttribute("contact", new Contact());
		return "normal/addcontact";
	}

	@PostMapping("/process-contact")
	public String processcontact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("profile_image") MultipartFile file, Principal principal, Model model, HttpSession session) {

		try {
			String name = principal.getName();
			User user = this.userrepo.getuserbyusername(name);

			if (file.isEmpty()) {

				System.out.println("file empty");
				contact.setImage("contact.jpg");
			}

			else {
				contact.setImage(file.getOriginalFilename());
				File file2 = new ClassPathResource("static/images").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image uploaded");
			}

			if (result.hasErrors()) {
				model.addAttribute("contact", contact);
				return "normal/addcontact";
			}

			contact.setUser(user);

			user.getContacts().add(contact);
			this.userrepo.save(user);
			System.out.println("saving users");
			session.setAttribute("message", new Message("your contact is added to database , add more..", "success"));
			return "redirect:/user/addcontact";

		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			session.setAttribute("message", new Message("Something went wrong! try again", "danger"));
	
		}
		return "normal/addcontact";

	}

	
	
	
	
	
	@GetMapping("/show-contacts/{page}")
	public String showcontacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		if(page<0) {
			return "normal/errorpage";
		}
		model.addAttribute("title", "contacts");
		String userName = principal.getName();
		User user = this.userrepo.getuserbyusername(userName);
		

		PageRequest pageable = PageRequest.of(page, 4);
		

		Page<Contact> contacts = this.contactrepo.findContactsByUser(user.getId(), pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentpage", page);
		
		System.out.println(page);
		
		model.addAttribute("totalpages", contacts.getTotalPages());
		System.out.println( contacts.getTotalPages());
		return "normal/show_contact";

	}

	@RequestMapping("/{cId}/contact")
	public String showcontactsDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {

		Optional<Contact> contactOP = this.contactrepo.findById(cId);
		Contact contact = contactOP.get();
		String username = principal.getName();
		User user = this.userrepo.getuserbyusername(username);

		if (user.getId() == contact.getUser().getId())
			model.addAttribute("contact", contact);

		return "normal/contactdetail";

	}
	

	
	
	
	
	
	

	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, HttpSession session, Principal principal)
			throws IOException {
		Optional<Contact> contactOp = this.contactrepo.findById(cId);
		Contact contact = contactOp.get();
		String username = principal.getName();
		User user = this.userrepo.getuserbyusername(username);
		String image = contact.getImage();
		File file2 = new ClassPathResource("static/images").getFile();
		Path path = Paths.get(file2.getAbsolutePath() + File.separator + image);
		Files.delete(path);
		if (user.getId() == contact.getUser().getId())
			this.contactrepo.delete(contact);
		session.setAttribute("message", new Message("contact deleted successfully", "success"));
		return "redirect:/user/show-contacts/0";

	}

	@PostMapping("/updatecontact/{cId}")
	public String update(Model model, @PathVariable("cId") Integer cId) {
		Contact contact = this.contactrepo.findById(cId).get();
		model.addAttribute("contact", contact);
		model.addAttribute("title", "update-contact");
		return "normal/updateform";

	}

	// update contact handler

	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String processupadte( @ModelAttribute @Valid Contact contact, BindingResult result,Model model,
			@RequestParam("profile_image") MultipartFile file, HttpSession session, Principal principal) {

		try {

			Contact oldcontact = this.contactrepo.findById(contact.getCId()).get();

			if (!file.isEmpty()) {
				File deletefile = new ClassPathResource("static/images").getFile();
				File file1 = new File(deletefile, oldcontact.getImage());
				file1.delete();

			
				File file2 = new ClassPathResource("static/images").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());

			} else {

				contact.setImage(oldcontact.getImage());
			}
			
			if (result.hasErrors()) {
				model.addAttribute("contact", contact);
				return "normal/updateform";
			}

			User user = this.userrepo.getuserbyusername(principal.getName());

			contact.setUser(user);
			this.contactrepo.save(contact);
			System.out.println("user contact is updating");

			session.setAttribute("message", new Message("Your contact updated successfully", "success"));

		} catch (Exception e) {
				session.setAttribute("message", new Message("Something went wrong! try again", "danger"));
				System.out.println("user contact is not updating");
				return "normal/updateform";

		}

		return "redirect:/user/" + contact.getCId() + "/contact";

	}
	
	
	//my profile handler
	
	@GetMapping("/profile")
	public String myprofile(Model model) {
		model.addAttribute("title","profile");
		return "normal/profile";
	}
	
	
	//open setting handler
	
	@GetMapping("/setting")
	public String openSettings() {
		return "normal/setting";
	}
	
	@GetMapping("/settings")
	public String OpenSettings() {
		
		return "normal/settings";
	}
	
	
	//change Password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldpass") String oldpass,@RequestParam("newpass") String newpass,Principal principal,HttpSession session) {
		System.out.println("oldpass"+oldpass);
		System.out.println("newpass"+newpass);
		String userName=principal.getName();
		User currentUser=this.userrepo.getuserbyusername(userName);
		if(this.bCryptPasswordEncoder.matches(oldpass, currentUser.getPassword())) {
	
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newpass));
			this.userrepo.save(currentUser);
			session.setAttribute("message", new Message("Successfully changed","success"));
			
			
		}
		else {
			session.setAttribute("message", new Message("Wrong old password","danger"));
			return "redirect:/user/settings";
			
		}
		
		return "redirect:/user/dash";
	}
	
	
	
}
