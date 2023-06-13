package com.example.usermanagementsystem.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.usermanagementsystem.dto.CreateUserRequest;
import com.example.usermanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MainController {

	private final UserService userService;
	
	@GetMapping("/")
	public String getHomePage() {
		return "home";
	}
	
	@GetMapping("/login")
	public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {

	
    	/*
    	SecurityContextHolder.getContext().getAuthentication() retrieves the 
    	currently authenticated user's Authentication object from the security context. 
    	
    	*/	 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
        	
        	if (error != null) {
    	        model.addAttribute("error", "Invalid username or password");
    	    }
    		
            return "login";
        }

        return "redirect:/";
    }
	
	@GetMapping("/signup")
	public String getSignUpPage(Model model) {
		
		model.addAttribute("signuprequest",new CreateUserRequest());
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute("signuprequest") CreateUserRequest signUpRequest,Model model) {
		
		if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
			 return "signup";
		}
		
		int result = userService.createUser(signUpRequest);
		
		if (result==1|| result==2) {
			if (result == 1) {
	            model.addAttribute("nameerror", "The username already exists");
	      
	        } else {
	            model.addAttribute("emailerror", "The email ID already exists");
	           
	        }
	     
			return "signup" ;
		}
		return "redirect:/login";
	}
	
}
