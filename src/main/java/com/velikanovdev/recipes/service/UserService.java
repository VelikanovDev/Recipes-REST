package com.velikanovdev.recipes.service;

import com.velikanovdev.recipes.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    ResponseEntity<?> registerUser(User userCredentials);
    User getUserByEmail(String email);
}
