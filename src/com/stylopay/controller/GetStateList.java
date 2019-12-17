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

public class GetStateList {
	
	String countryid = null;
	
	String jsonResponse = null;
	String response = null;
	
	List<String> stateList = new ArrayList<String>();
	
	public List<String> getCountryWiseStateNames(String countryid) throws JSONException {
		
		this.countryid = countryid;
		
		try {

			URL url = new URL("http://developeruat.stylopay.com/StyloDemoApiServer/API/Data/StatesList");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"CountryId\":\"" + countryid + "\"}";
						
			
			System.out.println("State List API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("State List API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;
				
				JSONObject json = new JSONObject(response);
				JSONArray stateInformation = json.getJSONArray("States");
				
				for(int i = 0; i<stateInformation.length(); i++) {
					
					JSONObject stateDetails = stateInformation.getJSONObject(i);
					System.out.println("State Details: " + stateDetails);
					String stateName = stateDetails.getString("StateName");
					
					stateList.add(stateName);
					
				}
				
				
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }
		
		return stateList;
	}

}
