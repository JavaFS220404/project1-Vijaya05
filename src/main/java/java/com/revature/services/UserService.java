package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.dao.impl.UserDAOImpl;

import com.revature.models.User;
import com.revature.throwables.WrongPasswordException;

/**
 * The UserService should handle the processing and retrieval of Users for the ERS application.
 *
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create User</li>
 *     <li>Update User Information</li>
 *     <li>Get Users by ID</li>
 *     <li>Get Users by Email</li>
 *     <li>Get All Users</li>
 * </ul>
 */
public class UserService {
	
	protected UserDAOImpl userDaoImpl = new UserDAOImpl();
	
	public Optional<User> login(User clientUser) throws WrongPasswordException {
		Optional<User> optUser = userDaoImpl.getByUsername(clientUser.getUserName());
		
		if(optUser.isPresent()) {
			if(optUser.get().getPassword().equals(clientUser.getPassword())) {
				return optUser;
				
			}else {
				throw new WrongPasswordException();
			}
			
		}
		Optional<User> nullOpt = Optional.of(null);
		return null;
	}
	
	public List<User> getAllUsers(){
		return userDaoImpl.findAll();
	}

	/**
	 *     Should retrieve a User with the corresponding username or an empty optional if there is no match.
     */
	public Optional<User> getSingleUser(String username) {
		return userDaoImpl.getByUsername(username);
	}
	
	
	public boolean createUser(User userToBeRegistered) {
		//UserDAOImpl userDaoImpl = new UserDAOImpl();
		userDaoImpl.create(userToBeRegistered);
		return true;
		
		
	}
}
