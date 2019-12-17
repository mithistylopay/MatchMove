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
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.json.JSONException;

import com.stylopay.bean.CreateUserBean;
import com.stylopay.bean.LogupBean;
import com.stylopay.config.AgentDetails;

public class CreateUserForTribeWallet {
	
	
	//insert user data (that is enter in Complete User Registration Modal) in Members table of tribe database by calling tribe createUser api at background
				
			String userName = null;
			String email = null;
			
			String jsonResponse = null;
			String response = null;
			//String apiResponse = null;
		
			@PostConstruct
			public String insertUserDetailsToMembersTable(LogupBean logupBean, CreateUserBean createUserBean, ResultSet resultSet) throws JSONException, SQLException {
				AgentDetails AgentDetails=new AgentDetails();
				String userName = logupBean.getEmail();
				String email = logupBean.getEmail();
				String password = logupBean.getPassword();
				String Agent_Code=resultSet.getString("AgentCode");
				String Sub_Agent_Code=resultSet.getString("SubAgentCode");
				String Client_Agent_SubAgent_Name=resultSet.getString("ClientName");
				System.out.println("username is: " + userName);
				
				String firstname = createUserBean.getFirstname();	
				String middlename = createUserBean.getMiddlename();
				if(middlename.isEmpty()) {
					
					System.out.println("middle name condition is: " + middlename.isEmpty());
					middlename = "NA";
					
				}
				String lastname = createUserBean.getLastname();
				System.out.println("lastname is: " + lastname);
				
				String gender = createUserBean.getGender();
				System.out.println("Gender is: " + gender);
				
				if(gender.equalsIgnoreCase("Male")) {
					
					gender = "M";
					
				}else {
					
					gender = "F";
				}
				
				String dob = createUserBean.getDob();
				String phonenumber = createUserBean.getPhonenumber();
				//String currency = createUserBean.getCurrency();
				String countryId = createUserBean.getCountry();
				
				GetCountryISDCode getCountryISDCode = new GetCountryISDCode();
				String isdcode = getCountryISDCode.countryListAPI(countryId);
				
				GetISOCountryCode getISOCountryCode = new GetISOCountryCode();
				String isoCountryCode = getISOCountryCode.getISOCountryCodeAPI(countryId);
				
				System.out.println("ISD Country Code is: " + isdcode);
				
				CallCountryListAPI callCountryListAPI = new CallCountryListAPI();
				//String countryname = callCountryListAPI.getCountryID(isdcode);
				
				String street = createUserBean.getStreet();
				String streetno = createUserBean.getStreetno();
				String aptno = createUserBean.getAptno();
				String city = createUserBean.getCity();
				String state = createUserBean.getState();
				String suburb = createUserBean.getSuburb();
				
				if(suburb.isEmpty()) {
					
					System.out.println("suburb condition is: " + suburb.isEmpty());
					middlename = "NA";
					
				}
				
				String postcode = createUserBean.getPostcode();
				
				
				try {

					URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/CommonServices/CreateUser");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

					String input = "{\"Application_ID\":1,\"username\":\""+ userName + "\",\"email\":\"" + email + "\",\r\n"
							+ "\"password\":\"" + password + "\", \"first_name\":\"" + firstname + "\", \"middle_name\":\"" + middlename + "\",\r\n"
				+ "\"last_name\": \"" + lastname + "\", \"gender\": \"" + gender + "\", \r\n"
				+ "\"bday\": \"" + dob + "\", \"country\": \"" + isoCountryCode + "\", \"CountryCodeISD\": \"" + isdcode + "\", \"CountryId\": \"" + countryId + "\", \r\n"
				+ "\"NumStreet\": \"" + streetno + "\", \"Street\": \"" + street + "\", \"NumApt\": \"" + aptno + "\", \"Suburb\": \"" + suburb + "\", \r\n"
				+ "\"city\": \"" + city + "\", \"address_line_2\" : \"\", \"state\": \"" + state + "\", \"post_code\": \"" + postcode + "\", \"preferred_currency\":\"USD\",\"phone_number\": \"" + phonenumber + "\",\"Client_Agent_SubAgent_Name\": \""+Client_Agent_SubAgent_Name+"\",\r\n"
				+ "\"Agent_code\" : \""+Agent_Code+"\", \"Sub_Agent_code\": \""+Sub_Agent_Code+"\" }";
								
					
					System.out.println("CreateUser API Json input is: " + input);

					OutputStream os = conn.getOutputStream();
					os.write(input.getBytes());
					os.flush();
					

					BufferedReader br = new BufferedReader(new InputStreamReader(
							(conn.getInputStream())));
					

					System.out.println("CreateUser API Json Response is - ");
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
