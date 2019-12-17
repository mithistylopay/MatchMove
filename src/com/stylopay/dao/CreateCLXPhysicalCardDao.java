package com.stylopay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.stylopay.config.DBConfigWallet;
import com.stylopay.controller.CreateCLXPhysicalCard;
import com.stylopay.controller.CreateCLXVirtualCard;

public class CreateCLXPhysicalCardDao {
	
	String email;
	String cardNumber;
	String password;
	String fname;
	String lname;
	String dob;
	String gender;
	String isdCountryCode;
	String cellPhone;
	String street;
	String streetNo;
	String aptNo;
	String isoCountryId;
	String country;
	String stateId;
	String state;
	String city;
	String zipCode;
	String suburb;
	
	String msg;

	public String getClxPhysicalCard(String email, String cardNumber, ResultSet resultSet) throws SQLException{
		
		this.email = email;
		this.cardNumber = cardNumber;
		
		Connection con = DBConfigWallet.databaseConnection();
		
		String sqlQuery = "select UserName_UserID from " + DBConfigWallet.TribePayschemaName + ".Credentials_View where Email_ID = " + "'" + email + "'";
		PreparedStatement pstmt = con.prepareStatement(sqlQuery);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			
			String sqlQueryForCard = "select Email_ID, Password, User_FirstName, User_LastName, BirthDate_YYYYMMDD, Gender, CountryCodeISD, CellPhone_Mobile, Street, NumStreet, NumApt, CountryId, CountryName, StateId, State, City, ZipCode, Suburb from \r\n"
					+ DBConfigWallet.TribePayschemaName + ".Credentials_View where Email_ID = " + "'" + email + "'";
			
			System.out.println("Sql query for registering cuallix card is: " + sqlQueryForCard);
			PreparedStatement statement = con.prepareStatement(sqlQueryForCard);
			
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				
				password = result.getString("Password");
				fname = result.getString("User_FirstName");
				lname = result.getString("User_LastName");
				dob = result.getString("BirthDate_YYYYMMDD");
				gender = result.getString("Gender");
				isdCountryCode = result.getString("CountryCodeISD");
				cellPhone = result.getString("CellPhone_Mobile");
				street = result.getString("Street");
				streetNo = result.getString("NumStreet");
				aptNo = result.getString("NumApt");
				isoCountryId = result.getString("CountryId");
				country = result.getString("CountryName");
				stateId = result.getString("StateId");
				state = result.getString("State");
				city = result.getString("City");
				zipCode = result.getString("ZipCode");
				suburb = result.getString("Suburb");
				
				CreateCLXPhysicalCard createCLXPhysicalCard = new CreateCLXPhysicalCard();
				msg = createCLXPhysicalCard.registrationWithCardAPI(email,cardNumber,password,fname,lname,dob,gender,isdCountryCode,cellPhone,street,streetNo,aptNo,isoCountryId,country,stateId,state,city,zipCode,suburb, resultSet );
				
			}else {
				
				msg = "Please complete your profile registration!";
				
			}
			
			
		}else {
			
			msg = "Please complete your profile registration!";
			
		}
		
		
		
		
		return msg;
	}
}
