package com.example.ecomApp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.ecomApp.configuration.oauth2.GoogleOAuth2SuccessHandler;
import com.example.ecomApp.security.CustomUserDetailService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	GoogleOAuth2SuccessHandler auth2SuccessHandler;

	@Autowired
	CustomUserDetailService customUserDetailService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests(requests -> requests.antMatchers("/admin/**").hasRole("ADMIN") // Only allow users with
																								// ADMIN role to access
																								// /admin/**
				.antMatchers("/user/**").hasRole("USER") // Only allow users with USER role to access /user/**
				.antMatchers("/register", "/login", "/shop/**", "/home").permitAll() // Allow everyone to access
																						// registration and login
				.antMatchers("/h2-console/**").permitAll() // Permit access to H2 console
				.anyRequest().authenticated()) // Require authentication for any other request
				.formLogin(login -> login.loginPage("/login") // Specify custom login page
						.failureUrl("/login?error=true").defaultSuccessUrl("/home", true) // Redirect to /home on
																							// successful login
						.usernameParameter("userEmail").passwordParameter("userPassword"))
				.oauth2Login(oauth2Login -> oauth2Login.loginPage("/login").successHandler(auth2SuccessHandler)) // Redirect
																													// to
																													// /home
																													// on
																													// successful
																													// login
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Specify custom
																									// logout URL
						.logoutSuccessUrl("/login?logout") // Redirect to login page on logout
						.invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll())
				.exceptionHandling(handling -> handling.accessDeniedPage("/403")) // Specify access denied page
                .csrf(csrf -> csrf.ignoringAntMatchers("/h2-console/**"))
				.headers(headers -> headers.frameOptions().sameOrigin());
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**", "/productImages/**", "/css/**",
				"/js/**"); // Permit access to static resources
	}

}
