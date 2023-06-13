package com.example.usermanagementsystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagementsystem.dto.CreateUserRequest;
import com.example.usermanagementsystem.model.User;
import com.example.usermanagementsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	
	public int createUser(CreateUserRequest createUserRequest) {
		
		Optional<User> userByName = userRepository.findByUsername(createUserRequest.getUsername());
		Optional<User> userByEmail =  userRepository.findByEmail(createUserRequest.getEmail());
		
		if (userByName.isPresent()) {
			
			return 1;
		}else if (userByEmail.isPresent()) {
			return 2;
		}
		
		
			User newUser = User.builder()
				    .username(createUserRequest.getUsername())
				    .email(createUserRequest.getEmail())
				    .password(encoder.encode(createUserRequest.getPassword()))
				    .roles("ROLE_USER")
				    .build();
		
		userRepository.save(newUser);

		return 3;
		
	}

	public List<User> searchUsers(String searchTerm){
		
		String pattern =  searchTerm + "%";
		return userRepository.findByUsernameLike(pattern);
	}
    


	public void upadateUser(Long id, User user) {

		Optional<User> user_db = userRepository.findById(id);
		User updateUser = user_db.get();
		
		updateUser.setUsername(user.getUsername());
		updateUser.setEmail(user.getEmail());
		updateUser.setPassword(encoder.encode(user.getPassword()));
		
		
		userRepository.save(updateUser);
	}


	public void deleteUser(Long id) {
		
		userRepository.deleteById(id);
		
	}
	
}
