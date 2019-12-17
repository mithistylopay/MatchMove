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

public class GetCountryId {
	
	String countryName = null;
	String countryId = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String getCountryId(String countryName) throws JSONException {
		
		this.countryName = countryName;
		
		try {

			URL url = new URL("http://developeruat.stylopay.com/StyloDemoApiServer/API/Data/CountriesList");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\"}";
						
			
			System.out.println("Country List API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("Country List API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;
				
				JSONObject json = new JSONObject(response);
				JSONArray countryInformation = json.getJSONArray("Countries");
				
				for(int i = 0; i<countryInformation.length(); i++) {
					
					JSONObject countryDetails = countryInformation.getJSONObject(i);
					//System.out.println("Country Details: " + countryDetails);
					
					String countrynam = countryDetails.getString("CountryName");
					
					//System.out.println("Country code is: " + countrycode);
					
					if(countrynam.equalsIgnoreCase(countryName)) {
						
						countryId = countryDetails.getString("CountryId");
						System.out.println("Country id is: " + countryId);
						break;
						
					}
					
				}
				
				
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }			
		
		
		return countryId;
	}

}
