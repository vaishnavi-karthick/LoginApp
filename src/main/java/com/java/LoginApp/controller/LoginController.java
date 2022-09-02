package com.java.LoginApp.controller;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.LoginApp.UserImplementation;
import com.java.LoginApp.UserRepository;
import com.java.LoginApp.model.LoginRequest;
import com.java.LoginApp.model.User;
import com.java.LoginApp.model.UserInfoResponse;

@RestController
public class LoginController {
	
	@Autowired
	UserRepository repo;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/signUp")
	public ResponseEntity<String> addUser(@RequestBody User user) {

		User newUser = new User();
		if(repo.existsByUsername(user.getUsername())) {
			return ResponseEntity.badRequest().body("Username already exists");
		}
		newUser.setUsername(user.getUsername());
		newUser.setPassword(encoder.encode(user.getPassword()));
		repo.save(newUser);
		return ResponseEntity.ok().body("User registerd successfully");
	}
	
	@GetMapping("/users")
	public List<User> getUsers() {	
		return repo.findAll();
	}
	
	@GetMapping("/user/{username}")
	public Optional<User> getUser(@PathVariable String username) {	
		return repo.findByUsername(username);
	}
	
	@PostMapping("/signIn")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

	    Authentication authentication = authenticationManager
	        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    UserImplementation userDetails = (UserImplementation) authentication.getPrincipal();

	    if(userDetails!=null) {
	    return ResponseEntity.ok().body(new UserInfoResponse(userDetails.getId(),
	                                   userDetails.getUsername()));
	    }else {
			return ResponseEntity.badRequest().body(new String("Authentication failed"));
		}
	  }	
}
