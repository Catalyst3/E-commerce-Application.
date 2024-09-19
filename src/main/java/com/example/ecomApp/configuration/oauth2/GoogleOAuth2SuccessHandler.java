package com.example.ecomApp.configuration.oauth2;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.ecomApp.model.Roles;
import com.example.ecomApp.model.User;
import com.example.ecomApp.repository.RolesRepository;
import com.example.ecomApp.repository.UserRepository;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttributes().get("email").toString(); // Use "email" key to get the user's email
        if (userRepository.findByUserEmail(email).isPresent()) {
            // User already exists, no action needed
        } else {
            // Create a new user if not already present
            User user = new User();
            user.setUserFirstName(token.getPrincipal().getAttributes().get("given_name").toString());
            user.setUserLastName(token.getPrincipal().getAttributes().get("family_name").toString());
            user.setUserEmail(email);
            user.setEnabled(true);

            Set<Roles> roles = new HashSet<>();
            roles.add(rolesRepository.findByRoleName("USER"));
            user.setUserRole(roles);

            userRepository.save(user);
        }
        // Redirect to home page after successful authentication
        response.sendRedirect("/");
    }
}
