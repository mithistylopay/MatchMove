package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


public class KYCDocumentUploadAPI {
	
	String documentData = null;
	String memberId = null;
	String documentType = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String uploadKYCFile(String documentData, String memberId, String documentType) {
		
		this.documentData = documentData;
		this.memberId = memberId;
		this.documentType = documentType;
		
		System.out.println("document type is: " + documentType);
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/KYC/UploadDocument");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"DocumentData\":\"" + documentData + "\", \"DocumentReference\":\"ID\", \"DocumentType\":\"" + documentType + "\", \"MemberID\":\"" + memberId + "\"}";
						
			
			//System.out.println("KYC Upload Document API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("KYC Upload Document API Json Response is - ");
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
