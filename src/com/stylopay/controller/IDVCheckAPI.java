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

public class IDVCheckAPI {	
	
	String memberId = null;
	String isoCountryCode = null;
	String dob = null;
	String fname = null;
	String aptNo = null;
	String streetNo = null;
	String street = null;
	String city = null;
	String state = null;
	String postcode = null;
	String lname = null;
	
	
	String jsonResponse = null;
	String response = null;
	
	String doc1 = null;
	String doc2 = null;
	String doc3 = null;
	
	
	public String kycIdvChcking(List<String> docUid, String memberId, String isoCountryCode, String dob, String fname, String aptNo, String streetNo, String street, String city, String state, String postcode, String lname) {
		
		List<String> documentUid = new ArrayList<String>();
		
		documentUid = docUid;
		this.memberId = memberId;
		this.isoCountryCode = isoCountryCode;
		this.dob = dob;
		this.fname = fname;
		this.aptNo = aptNo;
		this.streetNo = streetNo;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postcode = postcode;
		this.lname = lname;		
		
			
		doc1 = documentUid.get(0);
		doc2 = documentUid.get(1);
		doc3 = documentUid.get(2);
	
		
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/KYC/IDVCheck");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"user_Document\":[{\"DocumentReference\":\"IDFront\", \"DocumentUID\":\"" + doc1 + "\"}, \r\n"
					+ "{\"DocumentReference\":\"IDBack\", \"DocumentUID\":\"" + doc2 + "\"}, \r\n"
					+ "{\"DocumentReference\":\"POA\", \"DocumentUID\":\"" + doc3 + "\"}], \"MemberID\": \"" + memberId + "\", \"ISO_CountryCode\":\"" + isoCountryCode + "\", \r\n"
					+ "\"BirthDate\":\"" + dob + "\", \"Forename\": \"" + fname + "\", \"HouseNameNumber\": \"" + aptNo + "\", \"NumStreet\":\"" + streetNo + "\", \"Street\": \"" + street + "\","
					+ "\"City\":\"" + city + "\", \"State\":\"" + state + "\", \"Postcode\":\"" + postcode + "\", \"Surname\": \"" + lname + "\"}";
						
			
			System.out.println("IDV Check API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("IDV Check API Json Response is - ");
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


