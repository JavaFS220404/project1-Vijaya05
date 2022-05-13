package com.revature.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.dao.IUserDAO;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

public class UserDAOImpl implements IUserDAO {

	@Override
	public Optional<User> getByUsername(String username)  {
		
		Optional<User> user = Optional.empty();
		
		User userFromDb = null; 
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			//User user = new User();
			
			String sql = "SELECT * FROM ers_users WHERE ers_username = ?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, username);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				userFromDb = new User(); 
				userFromDb.setUserId(result.getInt("ers_userid"));
				userFromDb.setUserName(result.getString("ers_username"));
			    userFromDb.setPassword(result.getString("ers_userpassword"));
				userFromDb.setFirstName(result.getString("user_first_name"));
				userFromDb.setLastName(result.getString("user_last_name"));
				userFromDb.seteMail(result.getString("user_email"));
				if(result.getInt("user_role_id_fk")==1) {
					userFromDb.setRole(Role.EMPLOYEE);
				}else if(result.getInt("user_role_id_fk")==2) {
					userFromDb.setRole(Role.FINANCE_MANAGER);
					
				}
				//return user;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(userFromDb);
	}



	@Override
	public User create(User userToBeRegistered) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "INSERT INTO ers_users (ers_userid, ers_username,ers_userpassword,user_first_name,user_last_name,\r\n"
					+ "user_email,user_role_id_fk)\r\n"
					+ "VALUES (?,?,?,?,?,?,?);";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			int count = 0;
			
			statement.setInt(++count, userToBeRegistered.getUserId());
			statement.setString(++count, userToBeRegistered.getUserName());
			statement.setString(++count, userToBeRegistered.getPassword());
			statement.setString(++count, userToBeRegistered.getFirstName());
			statement.setString(++count, userToBeRegistered.getLastName());
			statement.setString(++count, userToBeRegistered.geteMail());
			if(userToBeRegistered.getRole().toString().equals("Employee")) {
				statement.setInt(++count,1);
			}
			
			
			statement.execute();
			
	
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return userToBeRegistered;
		
	}

	@Override
	public List<User> findAll() {
		
		List<User> list = new ArrayList<>();// creating list
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
		String sql = "SELECT * FROM ers_users;";
		Statement statement = conn.createStatement();
		
		ResultSet result = statement.executeQuery(sql);
		
		
		
		while(result.next()) {
			
			User user = new User();
			user.setUserId(result.getInt("ers_userid"));
			user.setUserName(result.getString("ers_username"));
			user.setPassword(result.getString("ers_userpassword"));
			user.setFirstName(result.getString("user_first_name"));
			user.setLastName(result.getString("user_last_name"));
			user.seteMail(result.getString("user_email"));
			if(result.getInt("user_role_id_fk")==1) {
				user.setRole(Role.EMPLOYEE);
			}else if(result.getInt("user_role_id_fk")==2) {
				user.setRole(Role.FINANCE_MANAGER);
				
			}
			list.add(user);
		}
		
	}catch(SQLException e) {
		e.printStackTrace();
	}
		System.out.println("No.of User Records = " + list.size());
		return list;
	}

	@Override
	
	public boolean updateUser(User user) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "UPDATE ers_users SET "
					+ "ers_userpassword=?, user_first_name=?, user_last_name=?, user_email=?, user_role_id_fk=? "
					+ "WHERE ers_userid = ?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			int count = 0;

			statement.setString(++count, user.getPassword());
			statement.setString(++count, user.getFirstName());
			statement.setString(++count, user.getLastName());
			statement.setString(++count, user.geteMail());
			if(user.getRole().toString().equals("Employee")) {
				
				statement.setInt(++count,1);
			}else if(user.getRole().toString().equals("Finance Manager")) {
				statement.setInt(++count,2);
				
			}
			
			statement.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	
}
