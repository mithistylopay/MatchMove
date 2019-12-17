package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SetCLXCardPINSetAPI {
	
	String userId = null;
	String accountId = null;
	String validThru = null;
	String securityCode = null;
	String pin = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String clxCardPinSet(String userId, String accountId, String validThru, String securityCode, String pin) {
		
		
		this.userId = userId;
		this.accountId = accountId;
		this.validThru = validThru;
		this.securityCode = securityCode;
		this.pin = pin;
		
		
try {
			
			URL url = new URL("http://35.180.75.185/CreditumDemoAPI/API/Card/PIN");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"UserId\":\""+ userId + "\", \"AccountId\":\"" + accountId + "\", \"ValidThru\":\"" + validThru + "\", \"SecurityCode\":\"" + securityCode + "\", \"PIN\":\"" + pin + "\"}";
						
			
			System.out.println("clxCardPin API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("clxCardPin API Json Response is - ");
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
