package com.account.login.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.account.login.microservice.jwt.util.JwtUtil;
import com.account.login.microservice.models.AuthenticationRequest;
import com.account.login.microservice.models.AuthenticationResponse;
import com.account.login.microservice.services.MyUserDetailsService;

@RestController
public class AccountLoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil  jwtUtil;
	
	@GetMapping("home")
	public String homePage() {
		return "Welcome!";
	}
	
	@PostMapping("authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
				(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		    }catch(BadCredentialsException e) {
		    	throw new Exception("Incorrect username or password",e);
		    }
		
		final UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt=jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
