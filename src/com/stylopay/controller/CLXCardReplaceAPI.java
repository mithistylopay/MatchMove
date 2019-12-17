package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CLXCardReplaceAPI {
	
	
	String userId = null;
	String currAccId = null; 
	String newAccId = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String clxCardReplac(String userId, String currAccId, String newAccId) {
		
		
		this.userId = userId;
		this.currAccId = currAccId;
		this.newAccId = newAccId;
		
		try {

			URL url = new URL("http://35.180.75.185/CreditumDemoAPI/API/Card/Replace");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"UserId\":\"" + userId + "\", \"CurrentAccountId\":\"" + currAccId + "\", \"NewAccountId\":\"" + newAccId + "\"}";
						
			
			System.out.println("clxCardReplace API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("clxCardReplace API Json Response is - ");
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
