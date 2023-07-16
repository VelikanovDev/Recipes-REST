package com.velikanovdev.recipes.service.impl;

import com.velikanovdev.recipes.entity.User;
import com.velikanovdev.recipes.repository.UserRepository;
import com.velikanovdev.recipes.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> registerUser(User userCredentials) {
        // Check if user with the specified email already exists
        if (userRepository.findByEmail(userCredentials.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }

        if(StringUtils.isBlank(userCredentials.getEmail()) || userCredentials.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Password must contain at least 8 characters");
        }

        // Create a new user entity
        User user = new User();
        user.setEmail(userCredentials.getEmail());
        user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));

        // Save the user in the database
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user details from the database based on the email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

}
