package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.revature.models.User;
import com.revature.services.UserService;
// http server comes from javax

public class UserServlet extends HttpServlet {
	
	private UserService userService = new UserService();
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	
			throws ServletException, IOException     
	{
		
		String uri = request.getRequestURI();

		System.out.println(uri);

		String[] urlSections = uri.split("/");
		if (urlSections.length == 3) {
		//get back all Users
		
		List<User>list = userService.getAllUsers();
		
		
		
		String URI = request.getRequestURI();
		String json = objectMapper.writeValueAsString(list);
		
		System.out.println(URI);
		
		//Response uses printwriters to write to the body of the response
		PrintWriter print = response.getWriter();
		print.print(json);
		response.setStatus(200);
		response.setContentType("application/json");
	} else if (urlSections.length == 4) {
		String spacedName = urlSections[3].replace("%20", " ");
		
		Optional<User> user = userService.getSingleUser(spacedName);
		
		String json = objectMapper.writeValueAsString(user.get());

		PrintWriter print = response.getWriter();
		print.print(json);
		response.setStatus(200);
		response.setContentType("application/json");
	}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		BufferedReader reader = req.getReader();

		StringBuilder stringBuilder = new StringBuilder();

		String line = reader.readLine(); // Gets first line from buffered reader

		while (line != null) {            //iterarting
			stringBuilder.append(line);
			line = reader.readLine(); // Gets the next line, returns null at end of body.
		}

		String body = new String(stringBuilder);
		User user = objectMapper.readValue(body, User.class);
		
		System.out.println(user);
		
		if (userService.createUser(user)) {
			resp.setStatus(201);
			//String json = objectMapper.writeValueAsString(user);
			
			//PrintWriter print = resp.getWriter();
			//print.print(user.toString());
			//print.print(json);
		} else {
			resp.setStatus(406);
		}	}


}
