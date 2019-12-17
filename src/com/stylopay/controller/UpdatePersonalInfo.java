package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdatePersonalInfo {
	
	
	String email = null; 
	String newEmail = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String updatePersonalDetails(String email, String newEmail) {
		
		this.email = email;
		this.newEmail = newEmail;
		
		try {

			URL url = new URL("http://developer.staging.stylopay.com/StyloDemoWalletService/API/CommonServices/UpdateUserEmail");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"username\":\"" + email + "\", \"email\":\"" + newEmail + "\"}";
						
			
			System.out.println("Update Personal Info API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("Update Personal Info API Json Response is - ");
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
