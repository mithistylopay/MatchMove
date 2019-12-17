package com.stylopay.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfigWallet {
	
	final static String db_url = "jdbc:mysql://stylopaytribeuat.cwng8dg4unot.eu-west-2.rds.amazonaws.com:3306/StylopayTribeUAT";
	final static String username = "stylopaytribeuat";
	final static String password = "stylopaytribeuat123";
	//public final static String MMschemaName = "StylopayMatchUAT";
	public final static String TribePayschemaName = "StylopayTribeUAT";
	
	
public static Connection databaseConnection() {
		
		
		Connection con = null;	
		
		
		//Establishing connection with mySQL database
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(db_url, username, password);
			
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		//con.close();
		
		return con;
		
	}

}
