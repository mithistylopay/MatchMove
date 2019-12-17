package com.stylopay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.stylopay.bean.LoginBean;
import com.stylopay.config.DBConfigWallet;

public class LoginDao {
	
	String email;
	String password;
	String response;
	
	public String userLogin(LoginBean loginBean) throws SQLException {
		
		email = loginBean.getEmail();
		password = loginBean.getPassword();
		
		Connection con = DBConfigWallet.databaseConnection();
		String sqlQuery = "Select Email_ID, Password from " + DBConfigWallet.TribePayschemaName + ".Credentials_View where Email_ID = " + "'" + email + "'" + " and Password = " + "'" + password + "'";
		
		System.out.println("LoginDao sql query is: " + sqlQuery);
		
		PreparedStatement pstmt = con.prepareStatement(sqlQuery);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			
			response = "success";
			
		}else {
			
			response = "invalid login credentials";
			
		}
		
		
		return response;
	}

}
