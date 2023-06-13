package com.example.usermanagementsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.usermanagementsystem.dto.CreateUserRequest;
import com.example.usermanagementsystem.model.User;
import com.example.usermanagementsystem.repository.UserRepository;
import com.example.usermanagementsystem.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
 
	private final UserRepository userRepository ;
	private final UserService userService;
	
	
	@GetMapping("/adminpanel")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String adminpanel(Model model) {
		
        List<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        
		return "adminpanel";
	}
	
	@GetMapping("/adminpanel/create")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String create(Model model) {
		
		model.addAttribute("user",new CreateUserRequest());
		return "create-user";
	}
	
	@PostMapping("/adminpanel/create")
	public String createUser(@ModelAttribute("user") CreateUserRequest createUserRequest, Model model) {
		
		if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			 
			return "create-user";
		}
		
	    int result = userService.createUser(createUserRequest);

	    if (result == 1|| result==2 ) {
	        if (result == 1) {
	            model.addAttribute("nameerror", "The username already exists");
	        } else {
	            model.addAttribute("emailerror", "The email ID already exists");         
	        } 
	        return "create-user";
	    }
	    else {
	    	 return "redirect:/adminpanel";    	
	    }   
	}
	
	/*@GetMapping("/adminpanel/search")
	public String searchUsers(@RequestParam("search") String searchTerm, Model model) {
	    // Implement the search logic here
	    List<User> searchResults = userService.searchUsers(searchTerm);

	    // Pass the search results to the Thymeleaf template
	    model.addAttribute("users", searchResults);

	    return "adminpanel"; // Return the partial template for the table body
	}
	
*/
 
	@GetMapping("/adminpanel/search")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String searchUsers(@RequestParam("search") String searchTerm, Model model) {
	    // Implement the search logic here
	    List<User> searchResults = userService.searchUsers(searchTerm);

	    // Pass the search results to the Thymeleaf template
	    model.addAttribute("users", searchResults);

	    // Add a flash attribute to indicate a successful search

	    return "/adminpanel"; // Redirect to the adminpanel URL without the search query
	}
//	@GetMapping("/adminpanel/adminpanel/search")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//	public String search(@RequestParam("search") String searchTerm, Model model) {
//	    // Implement the search logic here
//	    List<User> searchResults = userService.searchUsers(searchTerm);
//
//	    // Pass the search results to the Thymeleaf template
//	    model.addAttribute("users", searchResults);
//
//	    return "redirect:/adminpanel/search?search="+searchTerm; // Redirect to the adminpanel URL without the search query
//	}

	
	@GetMapping("/adminpanel/edit/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String edit(@PathVariable("id") Long id ,Model model) {
        
		Optional<User> user = userRepository.findById(id);
		
		User userToEdit = user.get() ;
		
		model.addAttribute("user", userToEdit);
		
		return "update-user";
		
	}
	
	@PostMapping("/adminpanel/update")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String updateUsername(@ModelAttribute("user") User user) {
		
		userService.upadateUser(user.getId(),user);
		
		return "redirect:/adminpanel";
		
	}
	
	@GetMapping("/adminpanel/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String delete(@PathVariable("id") Long id ) {
		
		userService.deleteUser(id);
		
		return "redirect:/adminpanel";
		
	}
	

}
