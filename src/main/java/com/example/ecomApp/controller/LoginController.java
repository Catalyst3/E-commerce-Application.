package com.example.ecomApp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.ecomApp.globle.GlobalData;
import com.example.ecomApp.model.Roles;
import com.example.ecomApp.model.User;
import com.example.ecomApp.repository.RolesRepository;
import com.example.ecomApp.repository.UserRepository;
import com.example.ecomApp.service.IUserService;

@Controller
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	IUserService iUserService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RolesRepository rolesRepository;

	@GetMapping("/register")
	public String showRegistrationForm() {
		return "register";
	}

//	@PostMapping("/register")
//	public String registerUserAccount(@ModelAttribute("user") @Valid User user,
//	                                   HttpServletRequest request, 
//	                                   BindingResult result,
//	                                   Model model) {
//		 System.out.println("Register method called.");
//		if (result.hasErrors()) {
//			System.out.println("Validation errors found.");
//	        return "register";
//	    }
//
//	    // Check if the user already exists
//	    if (iUserService.isUserAlreadyPresent(user.getUserEmail())) {
//	    	System.out.println("User already exists with email: " + user.getUserEmail());
//	        // Add custom error for duplicate email
//	        result.rejectValue("userEmail", "error.user", "There is already a user registered with this email.");
//	        return "register";
//	    }
//	    
//	    System.out.println("Saving new user: " + user.getUserEmail());
//	    // Save new User to Database
//	    iUserService.registerUser(user);
//
//	    // Auto-login the user after successful registration
//	    try {
//	    	System.out.println("Attempting to auto-login user: " + user.getUserEmail());
//	        authenticateUserAndSetSession(user.getUserEmail(), user.getUserPassword());
//	        System.out.println("Auto-login successful.");
//	    } catch (Exception e) {
//	    	System.out.println("Auto-login failed: " + e.getMessage());
//	        model.addAttribute("loginError", "Auto-login failed. Please login manually.");
//	        return "register";
//	    }
//
//	 // Redirect to a protected page
//	    System.out.println("Registration successful. Redirecting to /home.");
//	    return "redirect:/home";
//	}
//
//	// Method to authenticate user and set session
//	private void authenticateUserAndSetSession(String email, String password) throws Exception {
//	    UsernamePasswordAuthenticationToken authenticationToken = 
//	        new UsernamePasswordAuthenticationToken(email, password);
//
//	    Authentication authentication = 
//	        authenticationManager.authenticate(authenticationToken);
//
//	    SecurityContextHolder.getContext().setAuthentication(authentication);
//	    System.out.println("User authenticated and session set.");
//	}

	@PostMapping("/register")
	public String registerUserAccount(@ModelAttribute("user") User user, 
	                                   HttpServletRequest httpServletRequest) throws ServletException {
	    // Debugging output
	    System.out.println("Registering user: " + user.getUserEmail());

	    // Encode the password
	    String passWord = user.getUserPassword();
	    String encodedPassword = bCryptPasswordEncoder.encode(passWord);
	    user.setUserPassword(encodedPassword);
	    
	    // Debugging output
	    System.out.println("Encoded password: " + encodedPassword);

	 // Set user role by ID
	    Optional<Roles> userRoleOptional = rolesRepository.findById(1); // Assuming role ID 1 is the default user role
	    if (userRoleOptional.isPresent()) {
	        Roles userRole = userRoleOptional.get();
	        Set<Roles> roles = new HashSet<>();
	        roles.add(userRole);
	        user.setUserRole(roles);
	    } else {
	        // Handle case where role with ID 1 is not found
	        throw new IllegalStateException("Default role with ID 1 not found");
	    }
	    
	    user.setEnabled(true);

	    // Save user to the database
	    userRepository.save(user);

	    // Debugging output
	    System.out.println("User saved successfully");

	    // Perform auto-login
	    try {
	        httpServletRequest.login(user.getUserEmail(), passWord);
	        System.out.println("Auto-login successful for: " + user.getUserEmail());
	    } catch (Exception e) {
	        System.out.println("Auto-login failed: " + e.getMessage());
	    }

	    // Redirect to home page
	    return "redirect:/home";
	}

	/*
	 * private void authenticateUserAndSetSession(User user) {
	 * UsernamePasswordAuthenticationToken authToken = new
	 * UsernamePasswordAuthenticationToken(user.getUserEmail(),
	 * user.getUserPassword());
	 * SecurityContextHolder.getContext().setAuthentication(authenticationManager.
	 * authenticate(authToken)); }
	 */

	@GetMapping("/login")
	public String loginPage() {
		GlobalData.cart.clear();
		return "login";
	}

	@GetMapping("/403")
	public String accessDenied() {
		return "403";
	}

}
