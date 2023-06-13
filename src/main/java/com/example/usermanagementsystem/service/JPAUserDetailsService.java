package com.example.usermanagementsystem.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.usermanagementsystem.model.SecurityUser;
import com.example.usermanagementsystem.repository.UserRepository;



@Service
public class JPAUserDetailsService implements UserDetailsService{

	private UserRepository userRepository;
	
    public JPAUserDetailsService(UserRepository userRepository) {
		
		this.userRepository = userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository
				.findByUsername(username)
				.map(user-> new SecurityUser(user))
				.orElseThrow(()->new UsernameNotFoundException(username));
	}
}