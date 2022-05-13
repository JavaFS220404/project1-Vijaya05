package com.revature.web;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controllers.ReimbursementController;
import com.revature.controllers.UserController;
import com.revature.models.Reimbursement;
import com.revature.models.User;

public class FrontControllerServlet extends HttpServlet {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private UserController userController = new UserController();
	private ReimbursementController reimbController = new ReimbursementController();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	
	throws ServletException, IOException{
		
		resp.setContentType("application/json");

		resp.setStatus(404);

		final String URL = req.getRequestURI().replace("/check/core/", "");

		String[] UrlSections = URL.split("/");

		switch (UrlSections[0]) {
		case "login":
			if (req.getMethod().equals("POST")) {
				userController.login(req, resp);
			}
			break;
		case "reimb":
			HttpSession session = req.getSession(false);
			if (session != null) {
				User user = (User)session.getAttribute("user");
				if (req.getMethod().equals("GET")) {
					switch (user.getRole()) 
					{
						case EMPLOYEE:
							reimbController.getReimbursementList(session, resp);
							break;
							
						case FINANCE_MANAGER:
							reimbController.getReimbursementList(resp);
						
						break;
							

					}
					
					
				}else if(req.getMethod().equals("POST")) {
					BufferedReader reader = req.getReader();
					
					StringBuilder stBuilder = new StringBuilder();
					
					String line = reader.readLine();
					
					while(line!=null) {
						stBuilder.append(line);
						line = reader.readLine();
					}
					
					String body = new String(stBuilder);
					System.out.println(body);
					
					Reimbursement reimb = mapper.readValue(body, Reimbursement.class);
					
					reimb.setReimbAuthor((User) session.getAttribute("user"));
					reimbController.addReimbursement(reimb, resp);
				}else if(req.getMethod().equals("PUT")) {
					BufferedReader reader = req.getReader();
					
					StringBuilder stBuilder = new StringBuilder();
					
					String line = reader.readLine();
					
					while(line!=null) {
						stBuilder.append(line);
						line = reader.readLine();
					}
					
					String body = new String(stBuilder);
					System.out.println(body);
					
					Reimbursement reimb = mapper.readValue(body, Reimbursement.class);
					
					
					switch (user.getRole()) 
					{
						case EMPLOYEE:
							resp.setStatus(401);
							break;
							
						case FINANCE_MANAGER:
						reimb.setReimbResolver((User)session.getAttribute("user"));
							
						reimbController.processReimbursement(reimb, resp);
						break;
							

					}
					
					
				}
			} else {
				resp.setStatus(401);
			}
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
