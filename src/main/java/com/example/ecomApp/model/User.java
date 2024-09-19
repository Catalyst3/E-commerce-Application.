package com.example.ecomApp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userID;

    @NotEmpty
    @Column(nullable = false)
    private String userFirstName;

    @NotEmpty
    @Column(nullable = false)
    private String userLastName;

    @Column(nullable = false, unique = true)
    @Email(message = "{error.invalid_Email}")
    @NotEmpty
    private String userEmail;

    
    private String userPassword;

    @ManyToMany(cascade =CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> userRole = new HashSet<>();

    private boolean enabled;

 // Copy constructor to create a new User object based on an existing User object
    public User(User user) {
    	// Copy basic user information
        this.userFirstName = user.getUserFirstName();
        this.userLastName = user.getUserLastName();
        this.userEmail = user.getUserEmail();
        this.userPassword = user.getUserPassword();
        
        // Copy roles assigned to the user
        this.userRole = user.getUserRole();
        
        // Copy enabled status (whether the user account is enabled or disabled)
        this.enabled = user.isEnabled();
    }
}
