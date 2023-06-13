package com.example.usermanagementsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.usermanagementsystem.model.User;
import com.example.usermanagementsystem.repository.UserRepository;

@SpringBootApplication
public class UsermanagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsermanagementSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository,PasswordEncoder encoder) {
		
		return args->{
			
			User user = User.builder().username("user")
			.email("user@gmail.com")
			.password(encoder.encode("password"))
			.roles("ROLE_USER").build();
			
			User admin = User.builder().username("admin")
					.email("admin@gmail.com")
					.password(encoder.encode("password123"))
					.roles("ROLE_USER,ROLE_ADMIN")
					.build();
					
			
			userRepository.save(user);
			userRepository.save(admin);
	
		
		};
	}
}
