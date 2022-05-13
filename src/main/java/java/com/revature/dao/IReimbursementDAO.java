package com.revature.dao;

import java.util.Collections;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.User;


public interface IReimbursementDAO {
	public boolean addReimbursement(Reimbursement reimb);
	public boolean process(Reimbursement unprocessedReimbursement);
	public List<Reimbursement> getReimbursementsById(int id);
	public List<Reimbursement> getAllReimbursement();
        

}
