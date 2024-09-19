package com.example.ecomApp.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecomApp.model.Roles;
import com.example.ecomApp.model.User;
import com.example.ecomApp.repository.RolesRepository;
import com.example.ecomApp.repository.UserRepository;
import com.example.ecomApp.service.IUserService;

@Service
public class UserServiceimpl implements IUserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RolesRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder; // Inject Spring Security PasswordEncoder

//	@Override
//	public void registerUser(User user) {
//		System.out.println("Inside The register Method");
//		user.setEnabled(true); // Set user as enabled
//		user.setUserPassword(passwordEncoder.encode(user.getUserPassword())); // Encode password
//		userRepository.save(user);
//	}
	@Override
    @Transactional
    public void registerUser(User user) {
        // Ensure user is enabled
        user.setEnabled(true);

        // Encode user password
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

        // Retrieve or create "USER" role
        Roles userRole = roleRepository.findByRoleName("USER");
        if (userRole == null) {
            userRole = new Roles();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
        }

        // Assign "USER" role to the user
        user.getUserRole().add(userRole);

        // Save user entity
        userRepository.save(user);
    }

	@Override
	public boolean isUserAlreadyPresent(String userEmail) {
		Optional<User> existingUser = userRepository.findByUserEmail(userEmail);
		return existingUser.isPresent();
	}

}
