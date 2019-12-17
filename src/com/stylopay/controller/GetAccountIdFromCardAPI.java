package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetAccountIdFromCardAPI {
	
	String cardNo = null;
	String cardNoSubString = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String getAccIdFromCard(String cardNo) {
		
		this.cardNo = cardNo;
		
		cardNoSubString = cardNo.substring(10);
		
		try {

			URL url = new URL("http://35.180.75.185/CreditumDemoAPI/API/CommonServices/GetAccountIDFromCardNum");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"CardNum\":\"" + cardNoSubString + "\"}";
						
			
			System.out.println("getAccountIdFromCard API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("getAccountIdFromCard API Json Response is - ");
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
