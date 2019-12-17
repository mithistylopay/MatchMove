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

public class GetISOCountryCode {
	
	String countryId = null;
	String isoCountryCode = null;
	
	String jsonResponse = null;
	String response = null;
	
	public String getISOCountryCodeAPI(String countryId) throws JSONException {		
		
		this.countryId = countryId;
		
		try {

			URL url = new URL("http://developersandbox.stylopay.com/StyloDemoApiServer/API/CommonServices/GetISOCountryCode");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"CountryId\":\"" + countryId + "\"}";
						
			
			System.out.println("GetISOCountryCode API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("GetISOCountryCode API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;
				
				JSONObject json = new JSONObject(response);
				
				JSONObject status = json.getJSONObject("Status");
				isoCountryCode = status.getString("ISO_CountryCode");	
			
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }			
		
		
		return isoCountryCode;
	}

}
