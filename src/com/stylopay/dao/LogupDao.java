package com.stylopay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.stylopay.config.DBConfigWallet;

public class LogupDao {
	
	String userName = null;
	String email = null;
	String password = null;
	
	public void newUserSignupDao(String userName, String email, String password) throws SQLException {
		
	this.userName = userName;
	this.email = email;
	this.password = password;
	
	Connection con = DBConfigWallet.databaseConnection();
		/*
		 * String sqlQuery = "INSERT INTO " + DBConfig.MMschemaName + ".Email_Index " +
		 * "(Email_ID, Password, Client_Agent_SubAgent_Name, WalletHolder_Administrator_Partner_Flag_WAPn, Processor_code)\r\n"
		 * + "VALUES ('" + email + "', '" + password + "', 'GIVE', 'W', 'TP');";
		 * PreparedStatement pstmt = con.prepareStatement(sqlQuery);
		 */
	//pstmt.execute();
	
	con.close();
	
		
	}

}
