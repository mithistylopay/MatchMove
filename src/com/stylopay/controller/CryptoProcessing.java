package com.stylopay.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stylopay.config.AgentDetails;




public class CryptoProcessing {
	CryptoProcessing CryptoProcessingBean;
	
	String jsonResponse = null;
	String response = null;
	String responseMsg = null;
	
	public String cryptoLoadQuote(String DepositAmount1, String DepositCurrencyCode1, String SellingCurrencyCode1) throws JSONException {
		String DepositAmount=DepositAmount1;
		String DepositCurrencyCode=DepositCurrencyCode1;
		String SellingCurrencyCode= SellingCurrencyCode1;
		//moneyTransferToAnotherAccBean = transferFund;
		
		try {

			URL url = new URL("https://qluzj12rg6.execute-api.eu-west-3.amazonaws.com/Staging/getdepositequote");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("x-api-key", "X7h4Z1DVoS9dofOj9GUju5yHbThwBiSx7SZZ8oqQ");
		   
			String input = "{\"DepositAmount\":"+ DepositAmount +",\"DepositCurrencyCode\":\"" + DepositCurrencyCode + "\", \"SellingCurrencyCode\": \"" + SellingCurrencyCode + "\"}";
						
			
			System.out.println("CryptoLoad API Json input is: " + input);
			

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("CryptoLoad API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;				
				
			}
			
			if(response!=null){
				
				
						JSONObject jsonResponse = new JSONObject(response);
						System.out.println("JSON"+jsonResponse.getString("ResponseData"));
						JSONObject jsonResponse1 = new JSONObject(jsonResponse.getString("ResponseData"));
						System.out.println("JSON"+jsonResponse1.getString("QuoteAmount"));
						
						/*Map<String,String> param=new HashMap<String,String>();
						param.put("DateCreate",jsonResponse1.getString("DateCreate"));
						param.put("RequestedCurrencyCode",jsonResponse1.getString("RequestedCurrencyCode"));
						param.put("SourceCurrencyCode",jsonResponse1.getString("SourceCurrencyCode"));
						param.put("TransactionRequestID",jsonResponse1.getString("TransactionRequestID"));
						param.put("TransactionRequestStatus",jsonResponse1.getString("TransactionRequestStatus"));
						param.put("TransactionRequestType",jsonResponse1.getString("TransactionRequestType"));*/
						/*if(jsonResponse.toString().contains("error") || !jsonResponse.toString().contains("success")) {
							
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
				
				responseMsg = "Unknown error!";*/
						
						responseMsg=jsonResponse1.getString("QuoteAmount");
			}
			
			
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		return responseMsg;
	}
	/********************************************Initate
	 * @param resultSet 
	 * @throws SQLException ****************************/
	public JSONObject cryptoLoadInitate(String DepositAmount1, String DepositCurrencyCode1, String SellingCurrencyCode1,String Email, ResultSet resultSet) throws JSONException, SQLException {
		String DepositAmount=DepositAmount1;
		String DepositCurrencyCode=DepositCurrencyCode1;
		String SellingCurrencyCode= SellingCurrencyCode1;
		//moneyTransferToAnotherAccBean = transferFund;
		JSONObject finalresponse = new JSONObject();
		//JSONObject finalresponse = new JSONObject();
		AgentDetails agent=new AgentDetails();
		
		try {

			URL url = new URL("http://35.178.115.238:8080/CryptoLoad/Staging/InitateLoad");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");
		   
			String input = "{\"Application_ID\":1"+",\"Agent_Code\":\"" + resultSet.getString("AgentCode")+"\"" + ",\"UserName\":\"" + Email +"\""+ ",\"Email\":\"" + Email +"\", \"Sub_Agent_Code\": \"" + resultSet.getString("SubAgentCode")+"\"" +",\"DepositAmount\":"+ DepositAmount +",\"DepositCurrencyCode\":\"" + DepositCurrencyCode + "\", \"SellingCurrencyCode\": \"" + SellingCurrencyCode + "\"}";
						
			
			System.out.println("CryptoLoad API Json input is: " + input);
			

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("CryptoLoad API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;				
				
			}
			
			if(response!=null){
				
				
						JSONObject jsonResponse = new JSONObject(response);
						System.out.println("JSON"+jsonResponse.getString("ResponseData"));
						JSONObject jsonResponse1 = new JSONObject(jsonResponse.getString("ResponseData"));
						//System.out.println("JSON"+jsonResponse1.getString("QuoteAmount"));
						finalresponse=jsonResponse1;
						
			}
			
			
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		return finalresponse;
	}
	/********************************************Status****************************/
	public JSONObject cryptoLoadStatus(String TransactionRequestID) throws JSONException {
		JSONObject finalresponse = new JSONObject();
		try {

			URL url = new URL("https://qluzj12rg6.execute-api.eu-west-3.amazonaws.com/Staging/"+TransactionRequestID);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("x-api-key", "X7h4Z1DVoS9dofOj9GUju5yHbThwBiSx7SZZ8oqQ");
		   
			//String input = "{\"DepositAmount\":"+ DepositAmount +",\"DepositCurrencyCode\":\"" + DepositCurrencyCode + "\", \"SellingCurrencyCode\": \"" + SellingCurrencyCode + "\"}";
						
			
			System.out.println("CryptoLoad API Json input is: " + url);
			

			/*OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			*/
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("CryptoLoad API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;				
				
			}
			
			if(response!=null){
				
				
						JSONObject jsonResponse = new JSONObject(response);
						System.out.println("JSON"+jsonResponse.getString("ResponseData"));
						JSONObject jsonResponse1 = new JSONObject(jsonResponse.getString("ResponseData"));
						//System.out.println("JSON"+jsonResponse1.getString("QuoteAmount"));
						finalresponse=jsonResponse1;
						
			}
			
			
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		return finalresponse;
	}

	/********************************************Crypto Transcation History****************************/
	public JSONObject cryptoLoadHistory(String Email) throws JSONException {
		JSONObject finalresponse = new JSONObject();
		AgentDetails agent=new AgentDetails();
		
		try {

			URL url = new URL("http://35.178.115.238:8080/CryptoLoad/Staging/GetTransactionHistory");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");
		   
			String input = "{\"Application_ID\":1"+",\"Agent_Code\":\"" + agent.getAgent_Code()+"\"" + ",\"UserName\":\"" + Email +"\""+ ",\"Email\":\"" + Email +"\", \"Sub_Agent_Code\": \"" + agent.getSub_Agent_Code() + "\"}";
						
			
			System.out.println("CryptoLoad API Json input is: " + input);
			

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("CryptoLoad API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;				
				
			}
			
			if(response!=null){
				
				
						JSONObject jsonResponse = new JSONObject(response);
						System.out.println("JSON"+jsonResponse.getString("TransactionList"));
						JSONObject jsonResponse1 = new JSONObject(response);
						//System.out.println("JSON"+jsonResponse1.getString("QuoteAmount"));
						finalresponse=jsonResponse1;
						
			}
			
			
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();		
			
		  }			
		
		
		return finalresponse;
	}

}
