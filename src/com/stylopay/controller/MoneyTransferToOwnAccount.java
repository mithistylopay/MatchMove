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

public class MoneyTransferToOwnAccount {
	
	String fromAccNo = null;
	String toAccNo = null;
	String currency = null;
	String amount = null;
	
	String jsonResponse = null;
	String response = null;
	String responseMsg = null;
	
	public String fundTransferOwnAccountAPI(String fromAccNo, String toAccNo, String currency, String amount) throws JSONException {
		
		this.fromAccNo = fromAccNo;
		this.toAccNo = toAccNo;
		this.currency = currency;
		this.amount = amount;
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/MoneyTransfer/FundTransferOwnAccount");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"sender_account\":\"" + fromAccNo + "\", \"receiver_account\":\"" + toAccNo + "\", \"amount\":\"" + amount + "\", \"currency\": \"" + currency + "\", \"test\": \"1\"}";
						
			
			System.out.println("FundTransferOwnAccount API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("FundTransferOwnAccount API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;				
				
			}
			
			if(response!=null){
				
				
						JSONObject jsonResponse = new JSONObject(response);
						
						if(jsonResponse.toString().contains("error") || !jsonResponse.toString().contains("success")) {
							
								if(jsonResponse.toString().contains("description")) {
									responseMsg = jsonResponse.getString("description");
									
									System.out.println("responseMsg is: " + responseMsg);
							   }else {
								   
								   responseMsg = "Unknown error!";
								   
							   }
							
						}else {
							
							responseMsg = "Money has been transferred successfully!";
							
						}
				
			}else {
				
				responseMsg = "Unknown error!";
				
			}
			
			
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		return responseMsg;
		
	}

}
