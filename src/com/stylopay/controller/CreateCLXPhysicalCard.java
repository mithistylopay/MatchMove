package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.stylopay.config.AgentDetails;

public class CreateCLXPhysicalCard {
	
	String email = null;
	String cardNo = null;
	String password = null;
	String fname = null;
	String lname = null;
	String dob = null;
	String gender = null;
	String isdCountryCode = null;
	String cellPhone = null;
	String street = null;
	String streetNo = null;
	String aptNo = null;
	String isoCountryId = null;
	String country = null;
	String stateId = null;
	String state = null;
	String city = null;
	String zipCode = null;
	String suburb = null;
	
	String jsonResponse = null;
	String response = null;
	String responseMsg = null;
	
	
	public String registrationWithCardAPI(String email,
			String cardNo,
			String password,
			String fname,
			String lname,
			String dob,
			String gender,
			String isdCountryCode,
			String cellPhone,
			String street,
			String streetNo,
			String aptNo,
			String isoCountryId,
			String country,
			String stateId,
			String state,
			String city,
			String zipCode,
			String suburb, ResultSet resultSet) throws SQLException{
		
		//calling from CreateCLXVirtualCardDao
		
		this.email = email;
		this.cardNo = cardNo;
		this.password = password;
		this.fname = fname;
		this.lname = lname;
		this.dob = dob;
		this.gender = gender;
		this.isdCountryCode = isdCountryCode;
		this.cellPhone = cellPhone;
		this.street = street;
		this.streetNo = streetNo;
		this.aptNo = aptNo;
		this.isoCountryId = isoCountryId;
		this.country = country;
		this.stateId = stateId;
		this.state = state;
		this.city = city;
		this.zipCode = zipCode;
		this.suburb = suburb;
		
		if(gender.equalsIgnoreCase("M")) {
			
			gender = "1";
			
		}else {
			
			gender = "2";
			
		}
		
		AgentDetails agentDetails = new AgentDetails();
		try {

			URL url = new URL("http://developeruat.stylopay.com/StyloDemoApiServer/API/CommonServices/RegisterWithoutCard");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"Email\":\"" + email + "\", \"AccountId\":\"" + cardNo + "\", \"Password\": \"" + password + "\", \"Name\":\"" + fname + "\", \"MiddleName\":\"\", \"LastName\": \"" + lname + "\", \"SecondLastName\":\"\", \"BirthDate\": \"" + dob + "\", \"GenderId\":\"" + gender + "\", \r\n"
					+ "\"CountryBirthId\": \"" + isoCountryId + "\", \"CountryCode\":\"" + isdCountryCode + "\", \"CellPhone\":\"" + cellPhone + "\", \"Currency\":\"USD\", \"Street\":\"" + street + "\", \"NumStreet\": \"" + streetNo + "\", \"NumApt\":\"" + aptNo + "\", \"CountryId\":\"" + isoCountryId + "\", \r\n"
					
		+ "\"Country\":\"" + country + "\", \"StateId\":\"" + stateId + "\", \"State\":\"" + state + "\", \"City\":\"" + city + "\", \"ZipCode\":\"" + zipCode + "\", \"Suburb\":\"" + suburb + "\", \"Agent_code\":\""+resultSet.getString("AgentCode")+"\", \"Sub_Agent_code\": \""+resultSet.getString("SubAgentCode")+"\", \"Client_Agent_SubAgent_Name\":\""+agentDetails.getClient_Agent_SubAgent_Name()+"\"}";
						
			
			System.out.println("Cuallix Registration With Card API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("RegistrationWithCard API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;					
			}
			
			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		
	return response;
	
	}

}
