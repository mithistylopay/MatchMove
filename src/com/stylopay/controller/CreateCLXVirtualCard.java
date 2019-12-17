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

import org.json.JSONException;
import org.json.JSONObject;

import com.stylopay.config.AgentDetails;

public class CreateCLXVirtualCard {
	
	String email = null;
	String username = null;
	
	String jsonResponse = null;
	String response = null;
	String responseMsg = null;
	
	
	public String registrationWithoutCardAPI(String email, String username, ResultSet resultSet) throws JSONException, SQLException{
		
		//calling from TribeGetUserDetails
		
		this.email = email;		
		this.username = username;
		
		AgentDetails agentDetails = new AgentDetails();
		try {

			URL url = new URL("http://35.180.75.185/CreditumDemoAPI/API/CommonServices/AddVirtualCard");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"Email_ID\":\"" + email + "\", \"UserName\": \"" + username + "\", \"Agent_code\":\""+resultSet.getString("AgentCode")+"\", \"Sub_Agent_code\": \""+resultSet.getString("SubAgentCode")+"\", \"Client_Agent_SubAgent_Name\":\""+agentDetails.getClient_Agent_SubAgent_Name()+"\"}";
						
			
			System.out.println("Tribe/Cuallix Add Virtual Card API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("Tribe/Cuallix Add Virtual Card API Json Response is - ");
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
