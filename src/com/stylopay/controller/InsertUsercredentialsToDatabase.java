package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;



public class InsertUsercredentialsToDatabase {
	
	//insert user data (that is enter in registration page) in email_index table of database by calling preregister api at background
	
		String email = null;
		String password = null;
		
		String jsonResponse = null;
		String response = null;
		//String apiResponse = null;
	
		@PostConstruct
		public String insertLoginCredentialsToEmailIndex(String email, String password) throws JSONException {
			
			
			this.email = email;
			this.password = password;
			
			
			try {

				//URL url = new URL("http://localhost:8081/MMRESTAPI/Users/preregister");
				URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/CommonServices/Login");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

				String input = "{\"Application_ID\":\"1\", \"Email_ID\":\""+ email + "\",\"Password\":\"" + password + "\" }";
							
				
				System.out.println("Login API Json input is: " + input);

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
				

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				

				System.out.println("Login API Json Response is - ");
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
