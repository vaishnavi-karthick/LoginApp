package com.java.LoginApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.java.LoginApp.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService{
	

	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repo.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("Username not found"));
		        
		
		return UserImplementation.build(user);
	}
}
