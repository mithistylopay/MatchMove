package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetCLXAccountDetailsAPI {
	
	String email = null;
	String userId = null;	
	
	String jsonResponse = null;
	String response = null;
	
	public String getCLXAccountDetails(String email, String userId) {
		
		this.email = email;
		this.userId = userId;
		
		
try {
			
			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/CommonServices/GetCLXAccountDetails");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"Email\":\""+ email + "\", \"UserId\":\"" + userId + "\"}";
						
			
			System.out.println("GetCLXAccountDetails API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("GetCLXAccountDetails API Json Response is - ");
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
