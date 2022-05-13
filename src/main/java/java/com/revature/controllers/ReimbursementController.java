package com.revature.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.services.ReimbursementService;


public class ReimbursementController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private ReimbursementService reimbService = new ReimbursementService();
	private ObjectMapper objectMapper = new ObjectMapper();
	public void getReimbursementList(HttpServletResponse response) throws IOException {
		
		List<Reimbursement> reimbursements = reimbService.getAllReimbursement();
		if(reimbursements.size()==0) {
			response.setStatus(204);
		}
		else {
			response.setStatus(200);
			String json = objectMapper.writeValueAsString(reimbursements);
			PrintWriter print = response.getWriter();
			print.print(json);
		}
}

	public void getReimbursementList(HttpSession session, HttpServletResponse response) throws IOException {
			User user = (User) session.getAttribute("user");
			List<Reimbursement> reimbursements = reimbService.getReimbursementsByStatus(user.getUserId());
			if(reimbursements.size()==0) {
				response.setStatus(204);
			}
			else {
				response.setStatus(200);
				String json = objectMapper.writeValueAsString(reimbursements);
				PrintWriter print = response.getWriter();
				print.print(json);
			}
	}

	public void addReimbursement(Reimbursement reimbursement, HttpServletResponse response) {
		if (reimbService.addReimbursement(reimbursement)) {
			response.setStatus(201);
		} else {
			response.setStatus(400);
		}
	}

	public void processReimbursement(Reimbursement reimbursement, HttpServletResponse response){
		if(reimbService.process(reimbursement)) {
			response.setStatus(200);
		}else {
			response.setStatus(400);
		}
	}

}
