package com.revature.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.revature.dao.IReimbursementDAO;

import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementType;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;


public class ReimbursementDAOImpl implements IReimbursementDAO {
	

	 @Override
	 
	
	public boolean addReimbursement(Reimbursement reimb) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "INSERT INTO ers_reimbursment (REIMB_AMOUNT, "
					+ "REIMB_SUBMITTED,  REIMB_DESCRIPTION, REIMB_RECEIPT, REIMB_AUTHOR_fk,"
					+ "ers_reimbursement_status_id_fk,ers_reimbursement_type_fk) "
					+ "VALUES (?, ?, ?, ?, ?, ?,?);";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			int count = 0;
			
		
			statement.setDouble(++count, reimb.getReimbAmount());
			statement.setTimestamp(++count,new Timestamp(new Date().getTime()));
	
			statement.setString(++count, reimb.getReimbDescription());
			statement.setBytes(++count, new byte[5]);
			statement.setInt(++count, reimb.getReimbAuthor().getUserId());
			
			statement.setInt(++count,1);

			switch (reimb.getReimbType()) {
				case LODGING:
					statement.setInt(++count, 1);
					break;
				case TRAVEL:
					statement.setInt(++count, 2);
					break;
				case FOOD:
					statement.setInt(++count, 3);
					break;
				case OTHER:
					statement.setInt(++count, 4);
					break;
			}

			statement.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	
	}    
	@Override
	public List<Reimbursement> getAllReimbursement() {
		List<Reimbursement> list = new ArrayList<>();
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_reimbursment;";
			
			Statement statement = conn.createStatement();
			
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				Reimbursement reimb = new Reimbursement();
				reimb.setReimbID(result.getInt("reimb_id"));
				reimb.setReimbAmount(result.getDouble("REIMB_AMOUNT"));

				reimb.setReimbSubmitted(new Timestamp(new Date().getTime()));
				reimb.setReimbDescription(result.getString("REIMB_DESCRIPTION"));
				int reimbStatusId = result.getInt("ers_reimbursement_status_id_fk");
				int reimbTypeId = result.getInt("ers_reimbursement_type_fk");
				switch (reimbStatusId) {
					case 1:
						reimb.setReimbStatus(Status.PENDING);
						break;
					case 2:
						reimb.setReimbStatus(Status.APPROVED);
						break;
					case 3:
						reimb.setReimbStatus(Status.DENIED);
						break;
				}

				switch (reimbTypeId) {
					case 1:
						reimb.setReimbType(ReimbursementType.LODGING);
						break;
					case 2:
						reimb.setReimbType(ReimbursementType.TRAVEL);
						break;
					case 3:
						reimb.setReimbType(ReimbursementType.FOOD);
						break;
					case 4:
						reimb.setReimbType(ReimbursementType.OTHER);
						break;
				}

				list.add(reimb);
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean process(Reimbursement unprocessedReimbursement) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "UPDATE ers_reimbursment SET REIMB_RESOLVER_fk = ?,  ers_reimbursement_status_id_fk = ?"
					+ "WHERE reimb_id = ? ";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			int count = 0;

			statement.setInt(++count, unprocessedReimbursement.getReimbResolver().getUserId());
			switch(unprocessedReimbursement.getReimbStatus()) {
			
			case PENDING:
				statement.setInt(count++, 1);
				break;
			case APPROVED:
				statement.setInt(++count, 2);
				break;
			case DENIED:
				statement.setInt(++count, 3);
				break;
				
			}
			statement.setInt(++count, unprocessedReimbursement.getReimbID());
			statement.execute();
		}
		catch(SQLException e) {
  			e.printStackTrace();
  		}
  		return true;
	   }

	@Override
	public List<Reimbursement> getReimbursementsById(int id){
		List<Reimbursement> reimbursements = new ArrayList<>();
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			//String sql = "SELECT * FROM ers_reimbursment WHERE ers_reimbursement_status_id_fk = ?;";
		String sql = "SELECT ERS_REIMBURSMENT.*, ERS_USERS.ers_username, ERS_USERS.user_first_name, ERS_USERS.user_last_name, ERS_USERS.user_email, ERS_USERS.user_role_id_fk"
						+ " FROM ERS_REIMBURSMENT JOIN ERS_USERS ON ERS_REIMBURSMENT.reimb_author_fk = ers_users.ers_userid"
						+ " WHERE ERS_REIMBURSMENT.reimb_author_fk = "+ id +";";
			Statement statement = conn.createStatement();
			
			ResultSet result = statement.executeQuery(sql);

			while(result.next()) {
				Reimbursement reimb = new Reimbursement();
				reimb.setReimbID(result.getInt("reimb_id"));
				reimb.setReimbAmount(result.getDouble("REIMB_AMOUNT"));
				reimb.setReimbSubmitted(new Timestamp(new Date().getTime()));
				reimb.setReimbResolved(new Timestamp(new Date().getTime()));
				reimb.setReimbDescription(result.getString("REIMB_DESCRIPTION"));
				reimb.setReimbReceipt(result.getString("REIMB_RECEIPT"));
				
				int reimbStatusId = result.getInt("ers_reimbursement_status_id_fk");
				int reimbTypeId = result.getInt("ers_reimbursement_type_fk");
				switch (reimbStatusId) {
					case 1:
						reimb.setReimbStatus(Status.PENDING);
						break;
					case 2:
						reimb.setReimbStatus(Status.APPROVED);
						break;
					case 3:
						reimb.setReimbStatus(Status.DENIED);
						break;
				}

				switch (reimbTypeId) {
					case 1:
						reimb.setReimbType(ReimbursementType.LODGING);
						break;
					case 2:
						reimb.setReimbType(ReimbursementType.TRAVEL);
						break;
					case 3:
						reimb.setReimbType(ReimbursementType.FOOD);
						break;
					case 4:
						reimb.setReimbType(ReimbursementType.OTHER);
						break;
				}

				User user = new User(
						id,
						result.getString("ers_username"),
						null, // for password
						result.getString("user_first_name"),
						result.getString("user_last_name"),
						result.getString("user_email"),
						null // for role
				);

				reimb.setCreator(user);
				reimbursements.add(reimb);
			}
				
				return reimbursements;

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

			
     }


	

	






