package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetUserAccountsList {
	
	
	String username = null;
	
	String jsonResponse = null;
	String response = null;
	String accountType = null;
	String currencyType = null;
	String accountInfo = null;	
	
	
	public List<String> accountDetails(String username) throws JSONException {
		
		this.username = username;
		
		List<String> userAccountTypes = new ArrayList<String>();
		
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
				
				JSONObject jsonResponse = new JSONObject(response);
				JSONArray accountInformation = jsonResponse.getJSONArray("accounts");
				
				for(int i = 0; i<accountInformation.length(); i++) {
					
					JSONObject accountDetails = accountInformation.getJSONObject(i);
					System.out.println("Account Details: " + accountDetails);
					
					currencyType = accountDetails.getString("currency");
					accountType = accountDetails.getString("id");
					
					System.out.println("Currency type is: " + currencyType + " and account id is: " + accountType);
					
					accountInfo = accountType + " (" + currencyType + ")";
					
					userAccountTypes.add(accountInfo);
					
					System.out.println("userAccountTypes is: " + userAccountTypes);
					
				}
				
				
				
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		return userAccountTypes;
	}

}
