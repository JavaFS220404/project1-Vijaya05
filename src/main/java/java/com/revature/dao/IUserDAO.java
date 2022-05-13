package com.revature.dao;

import com.revature.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
	public List<User> findAll();
	
    public Optional<User> getByUsername(String username); //{
       // return Optional.empty();
    //}
    public boolean updateUser(User user);

    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     *
     * Note: The userToBeRegistered will have an id=0, and username and password will not be null.
     * Additional fields may be null.
     */
    public User create(User userToBeRegistered);// {
       // return userToBeRegistered;
 
  // }
    

}

