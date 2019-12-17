package com.stylopay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.stylopay.config.DBConfigWallet;

public class ViewUserDetailsDao {
	
	
String email = null;
String username = null;
	
	public String getUserDetails(String email) throws SQLException {
		
		this.email = email;
		
		Connection con = DBConfigWallet.databaseConnection();
		String sqlQuery = "select UserName_UserID from " + DBConfigWallet.TribePayschemaName + ".Credentials_View where Email_ID = " + "'" + email + "'";
		System.out.println("SQL query to get username is: " + sqlQuery);
		
		PreparedStatement pstmt = con.prepareStatement(sqlQuery);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			
			
			username = rs.getString("UserName_UserID");
			
		}
		
		
		return username;

  }
	
}
