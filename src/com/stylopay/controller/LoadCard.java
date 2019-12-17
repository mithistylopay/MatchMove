package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadCard {
	
	String userId = null;
	String accountId = null;
	String cardNum = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String clxCardLoad(String userId, String accountId, String Email, String Amount) throws JSONException{
		
		this.userId = userId;
		this.accountId = accountId;		


		
		try {

			URL url = new URL("https://1ay7th4s1d.execute-api.ap-southeast-1.amazonaws.com/Demo/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"UserId\":\"" + userId + "\", \"Email\":\"" + Email + "\",\"Amount\":\"" + Amount + "\", \"AccountId\":\"" + accountId + "\"}";
						
			
			System.out.println("ClxCardLoad API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("ClxCardInformation API Json Response is - ");
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
