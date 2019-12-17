package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewUSDWalletBalance {
	
	
	String username = null;
	String usdAccountBal = null;
	String msg = null;
	
	String jsonResponse = null;
	String response = null;
	
	
	public String getUSDWalletBalance(String username) throws JSONException {
		
		this.username = username;
		
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/CommonServices/GetUserAccounts");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"username\":\""+ username + "\" }";
						
			
			System.out.println("GetUserAccounts API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("GetUserAccounts API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;
				
				if(response.contains("error")) {
					
					msg = "Some internal errors occured there!";
					
				}else {
					
					JSONObject jsonResponse = new JSONObject(response);
					JSONArray accountInformation = jsonResponse.getJSONArray("accounts");
					
					for(int i = 0; i<accountInformation.length(); i++) {
						
						JSONObject accountDetails = accountInformation.getJSONObject(i);
						System.out.println("Account Details: " + accountDetails);
						String currencyType = accountDetails.getString("currency");
						
						System.out.println("Currency type is: " + currencyType);
						
						if(currencyType.equalsIgnoreCase("USD")) {
							
							usdAccountBal = accountDetails.getString("balance");
							System.out.println("USD Account Balance is: " + usdAccountBal);
							msg = usdAccountBal;
							break;
							
						}
						
					}
					
					
					
					}
				
				}
				
				
			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		return msg;
	
	}

}
