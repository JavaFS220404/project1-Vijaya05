package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>This ConnectionFactory class follows the Singleton Design Pattern and facilitates obtaining a connection to a Database for the ERS application.</p>
 * <p>Following the Singleton Design Pattern, the provided Constructor is private, and you obtain an instance via the {@link ConnectionFactory#getInstance()} method.</p>
 */
public class ConnectionFactory {

    private static ConnectionFactory instance;

    private ConnectionFactory() {
        super();
    }

    /**
     * <p>This method follows the Singleton Design Pattern to restrict this class to only having 1 instance.</p>
     * <p>It is invoked via:</p>
     *
     * {@code ConnectionFactory.getInstance()}
     */
    public static ConnectionFactory getInstance() {
        if(instance == null) {
            instance = new ConnectionFactory();
        }

        return instance;
    }

    /**
     * <p>The {@link ConnectionFactory#getConnection()} method is responsible for leveraging a specific Database Driver to obtain an instance of the {@link java.sql.Connection} interface.</p>
     * <p>Typically, this is accomplished via the use of the {@link java.sql.DriverManager} class.</p>
     * @throws SQLException 
     */
    public Connection getConnection() throws SQLException {
    	//For many frameworks using JDBC or working with JDBC it is necessary
    			//to "register" the driver. Letting the framework know what Driver
    			//JDBC is using. This is not always required for base JDBC projects. 
    			try {
    				Class.forName("org.postgresql.Driver");
    			} catch (ClassNotFoundException e) {
    				e.printStackTrace();
    			}
    			
    			String url = "jdbc:postgresql://javafs220404.cbgmpkvoz2h7.eu-central-1.rds.amazonaws.com:5432/demeos1";
    			String username = "postgres"; //It is possible to hide raw credentials using Env variables
    			String password = "database123#"; //You can access those variables with System.getenv("var-name")
    		
    			return DriverManager.getConnection(url, username, password);
    }
}
