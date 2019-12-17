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

public class GetTribeMemberId {
	
	String username = null;
	String memberid = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String getUserDetailsAPI(String username) throws JSONException {
		this.username = username;
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/CommonServices/GetUser_Details");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"username\":\""+ username + "\"}";
						
			
			System.out.println("GetUserDetails API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("GetUserDetails API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;	
				
				JSONObject json = new JSONObject(response);
				JSONObject userDetails = json.getJSONObject("user_details");
				
				String memberid = userDetails.getString("id");				
				
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }			
		
		return memberid;
	}

}
