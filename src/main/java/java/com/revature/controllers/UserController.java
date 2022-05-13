package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.services.UserService;

public class UserController {
	private UserService userService = new UserService();
	private ObjectMapper mapper = new ObjectMapper();

	public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		BufferedReader reader = req.getReader();
		
		StringBuilder stBuilder = new StringBuilder();
		String line = reader.readLine();
		
		while(line!=null) {
			
			stBuilder.append(line);
			line = reader.readLine();
		}
		
		String body = new String(stBuilder);
		System.out.println(body);
		
		
		
		User user = mapper.readValue(body,User.class);
		
		// TODO Auto-generated method stub
		Optional<User> authUser = userService.login(user);
		
		if(authUser.isPresent()) {
			
			HttpSession session = req.getSession();
			
			// can store user obj
			
			session.setAttribute("user", authUser.get()); //key value pairs
			
			resp.setStatus(200);
			
		}else {
			resp.setStatus(401);
		}
		
	}

}
