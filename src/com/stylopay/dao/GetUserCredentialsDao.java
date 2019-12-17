package com.stylopay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.stylopay.config.DBConfigWallet;

public class GetUserCredentialsDao {
	
	
	String username;
	String firstname;
	String lastname;
	
	String name;
	
	public String userCredentialDetails(String username) throws SQLException {
		
		this.username = username;
		
		Connection con = DBConfigWallet.databaseConnection();
		String sqlQuery = "Select User_FirstName, User_LastName from " + DBConfigWallet.TribePayschemaName + ".Credentials_View where UserName_UserID = " + "'" + username + "'";
		
		System.out.println("LoginDao sql query is: " + sqlQuery);
		
		PreparedStatement pstmt = con.prepareStatement(sqlQuery);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()){
			
			firstname = rs.getString("User_FirstName");
			lastname = rs.getString("User_LastName");
			name = firstname + " " + lastname;			
		
	}
		
		return name;

   }
	
}
