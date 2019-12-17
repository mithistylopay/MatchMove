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

import com.stylopay.bean.MoneyTransferToAnotherAccBean;

public class MoneyTransferToAnotherAcc {
	
	
	MoneyTransferToAnotherAccBean moneyTransferToAnotherAccBean;
	
	String jsonResponse = null;
	String response = null;
	String responseMsg = null;
	
	public String fundTransferAnotherUser(String senderAccount, String beneficiaryEmailid, String receiverAccount, String transferAmount, String currency) throws JSONException {
		
		//moneyTransferToAnotherAccBean = transferFund;
		
		String senderAcc = senderAccount;
		String beneficiaryEmail = beneficiaryEmailid;
		String receiverAcc = receiverAccount;
		String amount = transferAmount;
		String currencyType = currency;
		
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/MoneyTransfer/FundTransferUser");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"sender_account\":\"" + senderAcc + "\", \"receiver\": \"" + beneficiaryEmail + "\", \"receiver_account\":\"" + receiverAcc + "\", \"amount\":\"" + amount + "\", \"currency\": \"" + currencyType + "\"}";
						
			
			System.out.println("FundTransferUser API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("GetUserAccounts API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;				
				
			}
			
			if(response!=null){
				
				
						JSONObject jsonResponse = new JSONObject(response);
						System.out.println(jsonResponse.get("status").toString());
					if(jsonResponse.get("status").toString().compareToIgnoreCase("success")==0)
							{
						responseMsg = "success";
							}
					else{
						responseMsg = "Unknown error!";
					}
						/*if(jsonResponse.toString().contains("error") || !jsonResponse.toString().contains("success")) {
							
								if(jsonResponse.toString().contains("description")) {
									responseMsg = jsonResponse.getString("description");
									
									System.out.println("responseMsg is: " + responseMsg);
							   }else {
								   
								   responseMsg = "Unknown error!";
								   
							   }
							
						}else {
							
							responseMsg = "Money has been transferred successfully!";
							
						}*/
				
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
