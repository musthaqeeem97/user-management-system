package com.example.usermanagementsystem.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.usermanagementsystem.service.JPAUserDetailsService;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private JPAUserDetailsService jpaUserDetailsService;
	
	public SecurityConfig(JPAUserDetailsService jpaUserDetailsService) {
		this.jpaUserDetailsService = jpaUserDetailsService;
	}
	
	@Bean
	public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		
        .authorizeHttpRequests((authorize)->authorize.requestMatchers("/signup").permitAll()
        		.anyRequest().authenticated())
      
        .formLogin().loginPage("/login").permitAll();
		
		return http.build();
	}
    
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(){
		//giving info about who is the user details service and password encoder
	        //these info can be used to generate user details object and set it to authentication object.
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(jpaUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	   
		return daoAuthenticationProvider;
	}
}
