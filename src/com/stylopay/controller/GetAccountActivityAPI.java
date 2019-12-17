package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetAccountActivityAPI {
	
	
	String accountid = null;
	String fromDate = null;
	String toDate = null;
	String unescapeJsonString = null;
	
	public String getTransactionActivityDetails(String accountid, String fromDate, String toDate) {
		
		this.accountid = accountid;
		this.fromDate = fromDate;
		this.toDate = toDate;
		
		String jsonResponse = null;
		String response = null;
		
		
try {
			
			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/UPayAPI/getAccountActivity");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":\"1\", \"account\":\""+ accountid + "\", \"date_from\":\"" + fromDate + "\", \"date_to\":\"" + toDate + "\"}";
						
			
			System.out.println("getAccountActivity API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			System.out.println("getAccountActivity API Json Response is - ");
			while ((jsonResponse = br.readLine())!= null) {	
				System.out.println(jsonResponse);
				
				response = jsonResponse;				
				unescapeJsonString = unescape(response);
				
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }			
		
		
		return unescapeJsonString;
		
	}
	
	
	public static String unescape(String input) {
	    StringBuilder builder = new StringBuilder();

	    int i = 0;
	    while (i < input.length()) {
	      char delimiter = input.charAt(i); i++; // consume letter or backslash

	      if(delimiter == '\\' && i < input.length()) {

	        // consume first after backslash
	        char ch = input.charAt(i); i++;

	        if(ch == '\\' || ch == '/' || ch == '"' || ch == '\'') {
	          builder.append(ch);
	        }
	        else if(ch == 'n') builder.append('\n');
	        else if(ch == 'r') builder.append('\r');
	        else if(ch == 't') builder.append('\t');
	        else if(ch == 'b') builder.append('\b');
	        else if(ch == 'f') builder.append('\f');
	        else if(ch == 'u') {

	          StringBuilder hex = new StringBuilder();

	          // expect 4 digits
	          if (i+4 > input.length()) {
	            throw new RuntimeException("Not enough unicode digits! ");
	          }
	          for (char x : input.substring(i, i + 4).toCharArray()) {
	            if(!Character.isLetterOrDigit(x)) {
	              throw new RuntimeException("Bad character in unicode escape.");
	            }
	            hex.append(Character.toLowerCase(x));
	          }
	          i+=4; // consume those four digits.

	          int code = Integer.parseInt(hex.toString(), 16);
	          builder.append((char) code);
	        } else {
	          throw new RuntimeException("Illegal escape sequence: \\"+ch);
	        }
	      } else { // it's not a backslash, or it's the last character.
	        builder.append(delimiter);
	      }
	    }

	    return builder.toString();
	  }
	
}
