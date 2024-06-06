package com.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entity.User;
import com.blog.repository.UserRepository;



@Service
public class UserService {
	
	 @Autowired
	    private UserRepository userRepository;

	    public void saveUser(User user) {
	        // Implement the logic to save the user to the database
	        userRepository.save(user);
	    }
	    public boolean validateUser(String username, String password) {
	        User user = userRepository.findByUsername(username);
	        System.out.println(user);
	        System.out.println(user.getUsername());
	        System.out.println(user.getPassword());
	        System.out.println(user.getId());
	        if (user != null && user.getUsername().equals(username) && user.getPassword().equals(password)) {
	            return true;
	        }
	        return false;
	    }
	

	    public User getUserById(Long userId) {
	        Optional<User> userOptional = userRepository.findById(userId);
	        return userOptional.orElse(null);
	    }

}
