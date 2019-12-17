package com.stylopay.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserSettingsRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.GetUserAttributeVerificationCodeRequest;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.MFAOptionType;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.amazonaws.services.cognitoidp.model.VerifyUserAttributeRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stylopay.bean.ChangePasswordBean;
import com.stylopay.bean.CreateUserBean;
import com.stylopay.bean.ForgotPasswordBean;
import com.stylopay.bean.ForgotPasswordUserNameBean;
import com.stylopay.bean.LoginBeanCognito;
import com.stylopay.bean.LoginBean;
import com.stylopay.bean.LogupBean;
import com.stylopay.bean.MfaBean;
import com.stylopay.bean.PasswordRequest;
import com.stylopay.bean.UserResponse;
import com.stylopay.bean.UserSignUpRequest;
import com.stylopay.bean.VerificationEmailAndPhone;
import com.stylopay.config.StylopayTestCommonDB;
import com.stylopay.dao.CreateCLXPhysicalCardDao;
import com.stylopay.dao.GetUserCredentialsDao;
import com.stylopay.dao.LoginDao;
import com.stylopay.dao.ViewUserDetailsDao;
import com.stylopay.utility.CognitoConfig;

import sun.misc.BASE64Encoder;



@Controller
public class MatchMoveController {
	
	@Autowired
	ServletContext context;
	
	// Cognito user pool
	   
   @Autowired(required=true)
   CognitoConfig cognitoConfig;
	
	LogupBean lBean;

	String username = null;	
	String name = null;
	String email = null;
	String password = null;
	String memberId = null;
	String birthDate = null;
	String firstname = null;
	String lastName = null;
	String aptNo = null;
	String streetNo = null;
	String street = null;
	String city = null;
	String state = null;
	String postcode = null;
	String countryId = null;
	String kycUploadFlag = null;
	
	String accessToken = null;
	String USDAccountBalance = null;
	//String transactionActivityResponse = null;
	
	List<String> accountInfo = new ArrayList<String>();

	ViewUserDetailsDao viewUserDetailsDao = new ViewUserDetailsDao();
	ViewUSDWalletBalance viewUSDWalletBalance = new ViewUSDWalletBalance();
	GetUserAccountsList getUserAccountsList = new GetUserAccountsList();
	GetUserCredentialsDao getUserCredentialsdao = new GetUserCredentialsDao();
	InsertUsercredentialsToDatabase insertUsercredentialsToDatabase = new InsertUsercredentialsToDatabase();
	CreateUserForTribeWallet createUserForTribeWallet = new CreateUserForTribeWallet();

	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	String phoneNo;
	AdminInitiateAuthResult result;
	AWSCognitoIdentityProvider cognitoClient;
	AdminRespondToAuthChallengeResult resultChallenge;
	
	ResultSet resultSet = null;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView userLogin(HttpServletRequest request) throws SQLException, IOException {
		StringBuffer str = request.getRequestURL();
		ModelAndView modelAndView = new ModelAndView("login");
		System.out.println("URL"+str);
		
//		FileReader reader=new FileReader("C:\\Users\\kundu\\Desktop\\agentSubAgent.properties");  
//	      
//	    Properties p=new Properties();  
//	    p.load(reader);  
//	      
//	    System.out.println(p.getProperty("user"));  
//	    System.out.println(p.getProperty("password")); 
		
		StylopayTestCommonDB db = new StylopayTestCommonDB();
		Connection connection = db.databaseConnection();
		String sqlQuery = "select * from "+db.TribePayschemaName+".Config where url="+ "'"+str.toString()+"'";
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(sqlQuery);
		if(resultSet.next())
		{
			String agentCode = resultSet.getString("AgentCode");
			modelAndView.addObject("logo", resultSet.getString("logo_link"));
			modelAndView.addObject("dashBoardName", resultSet.getString("DashboardName"));
//			request.getSession().setAttribute("logo", resultSet.getString("logo_link"));
//			request.getSession().setAttribute("dashBoardName", resultSet.getString("DashboardName"));
		}
		return modelAndView;
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.POST)
	public ModelAndView userSignin(@ModelAttribute("login") LoginBean loginBean, HttpServletRequest request, Model model)
			throws SQLException, JSONException {

		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername(loginBean.getEmail());
		authenticationRequest.setPassword(loginBean.getPassword());
		email = loginBean.getEmail();
		System.out.println("email in dashboard: " + email);
		password = loginBean.getPassword();
		lBean = new LogupBean();
		lBean.setUserName(loginBean.getEmail());
		lBean.setEmail(loginBean.getEmail());
		lBean.setPassword(loginBean.getPassword());
		return signIn(authenticationRequest, model);
	}
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView userSigninGet(@ModelAttribute("login") LoginBean loginBean, HttpServletRequest request, Model model)
			throws SQLException, JSONException {

		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername(loginBean.getEmail());
		authenticationRequest.setPassword(loginBean.getPassword());
		email = loginBean.getEmail();
		System.out.println("email in dashboard: " + email);
		password = loginBean.getPassword();
		lBean = new LogupBean();
		lBean.setUserName(loginBean.getEmail());
		lBean.setEmail(loginBean.getEmail());
		lBean.setPassword(loginBean.getPassword());
		return signIn(authenticationRequest, model);
	}

	@RequestMapping(value = "/logup", method = RequestMethod.POST)
	public ModelAndView userSignup() {
		return new ModelAndView("logup");

	}
/**********************test******************/
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ModelAndView userSignu1p() {
		return new ModelAndView("index");

	}
	
/**************************************Pre-Signup Check**************************************/
	/*@RequestMapping(value = "/userRegistration", method = RequestMethod.POST)
	public ModelAndView userRegistration(@ModelAttribute("logup") LogupBean logupBean, HttpServletRequest request, Model model)
			throws SQLException, JSONException {

		lBean = logupBean;

		username = logupBean.getUserName();
		email = logupBean.getEmail();
		password = logupBean.getPassword();
		phoneNo = logupBean.getPhoneNo();

		System.out.println("user name is: " + username);
		String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
		//GetUserContactDetailsNewAPI getUserContactDetailsNewAPI = new GetUserContactDetailsNewAPI();
		//String response = getUserContactDetailsNewAPI.getUserContactDetails(email);
		
		JSONObject jsonResponse = new JSONObject(response);
		JSONObject beneficiaryUserData = jsonResponse.getJSONObject("Status");
		
		String Code = beneficiaryUserData.getString("Code");
		System.out.println("Response is: " + response);

		if (Code.compareToIgnoreCase("0")==0) {

			ModelAndView mv = new ModelAndView("logup");
			mv.addObject("message", "Email already exists!");
			
			return mv;

		} else {			

		// Cognito code
		UserSignUpRequest signUpRequest =  new UserSignUpRequest();
		signUpRequest.setUsername(username);
		signUpRequest.setEmail(email);
		signUpRequest.setPhoneNumber(phoneNo);
		signUpRequest.setPasswprd(password);
		UserType userType = signUp(signUpRequest, model);
		// End
			
			return new ModelAndView("index");

		}

	}*/
	

	@RequestMapping(value = "/userRegistration", method = RequestMethod.POST)
	public ModelAndView userRegistration(@ModelAttribute("logup") LogupBean logupBean, HttpServletRequest request, Model model)
			throws SQLException, JSONException {

		lBean = logupBean;

		username = logupBean.getUserName();
		email = logupBean.getEmail();
		password = logupBean.getPassword();
		phoneNo = logupBean.getPhoneNo();
		String full_number=logupBean.getFull_number();
		System.out.println("Phon"+logupBean.getPhoneNo());
		System.out.println("user name is: " + full_number);
		String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);

		System.out.println("Response is: " + response);

		if (response == null || response.contains("email already exists")) {

			ModelAndView mv = new ModelAndView("logup");
			mv.addObject("message", "Either email already exists or some problem occurs there!");

			return mv;

		} else {			

		// Cognito code
		UserSignUpRequest signUpRequest =  new UserSignUpRequest();
		signUpRequest.setUsername(username);
		signUpRequest.setEmail(email);
		signUpRequest.setPhoneNumber(full_number);
		signUpRequest.setPasswprd(password);
		System.out.println(signUpRequest.getPhoneNumber());
		return signUp(signUpRequest, model); 
		//System.out.println(signUpRequest.getPhoneNumber(phoneNo));
		// End
			
			//return new ModelAndView("index");

		}

	}

	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public ModelAndView createUserAPICalling(@ModelAttribute("createUser") CreateUserBean createUserBean,
			HttpServletRequest request) throws JSONException, SQLException {

		

		//String response = createUserForTribeWallet.insertUserDetailsToMembersTable(lBean, createUserBean, resultSet);
		String response = null;
		
		String registerJsonResponse = null;
		String registerUserResponse = null;
		try {

			URL url = new URL("http://18.206.169.158:8081/stylopay_wallet/api/v1/CommonServices/CreateUser");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
//			conn.setRequestProperty("client_id", "swagger-client");
//			conn.setRequestProperty("client_secret", "swagger-secret");
			conn.setRequestProperty("Content-Type", "application/json");
			
//			BASE64Encoder enc = new sun.misc.BASE64Encoder();
//		      String userpassword = "swagger-client" + ":" + "swagger-secret";
//		      String encodedAuthorization = enc.encode( userpassword.getBytes() );
//			conn.setRequestProperty("Authorization", "Basic "+encodedAuthorization);
			
//			System.out.println(conn.getHeaderFields());
//			System.out.println(conn.getRequestProperties());
			
			String input = "{\"username\":\""+username+"\",\"email\":\""+ email + "\",\"password\":\"" + password + "\",\r\n"
					+ "\"firstname\":\"" + createUserBean.getFirstname() + "\", \"middlename\":\"" + createUserBean.getMiddlename() + "\", \"lastname\":\"" + createUserBean.getLastname() + "\",\r\n"
					+ "\"gender\":\"" + createUserBean.getGender() + "\", \"dob\":\"" + createUserBean.getDob() + "\", \"country\":\"" + createUserBean.getCountry() + "\",\r\n"
					+ "\"countryCodeISD\":\"" + "91" + "\", \"countryId\":\"" + "1" + "\", \"streetno\":\"" + createUserBean.getStreetno() + "\",\r\n"
					+ "\"street\":\"" + createUserBean.getStreet() + "\", \"city\":\"" + createUserBean.getCity() + "\", \"postcode\":\"" + createUserBean.getPostcode() + "\",\r\n"
					+ "\"phonenumber\":\"" + createUserBean.getPhonenumber()
					+ "\"}";
						
			
			System.out.println("CreateUser API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((registerJsonResponse = br.readLine())!= null) {	
				System.out.println(registerJsonResponse);
				
				response = registerJsonResponse;	
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		JSONObject json1 = new JSONObject(response);
		//System.out.println("CreateUserAPICalling Response is: " + json1.getString("Status"));
		String check=null;
	//	if();
		//check=json1.getString("Status");
		
		if(json1.toString().contains("error"))
		{
			ModelAndView mv = new ModelAndView("index");
			JSONObject json = new JSONObject(response);
			System.out.println("create user response"+json);
			String messageToDisplay = json.getString("msg");
			
			System.out.println("CreateUserAPICalling Response is: " + messageToDisplay);
		//	System.out.println("CreateUserAPICalling Response is: " + json.getString("Code"););
			
			mv.addObject("messageToDisplay", messageToDisplay);
			
			return mv;
			
		}
		else{
		check=json1.getString("Status");
		JSONObject json2 = new JSONObject(check);
		String status=null;
		status=json2.getString("Code");
		
	/*	if(response.contains("error")) {*/
		if(status.compareToIgnoreCase("0")!=0) {
			
			ModelAndView mv = new ModelAndView("index");
			JSONObject json = new JSONObject(response);
			System.out.println("create user response"+json);
			String messageToDisplay = json2.getString("Message");
			
			System.out.println("CreateUserAPICalling Response is: " + messageToDisplay);
		//	System.out.println("CreateUserAPICalling Response is: " + json.getString("Code"););
			
			mv.addObject("messageToDisplay", messageToDisplay);
			
			return mv;
			
		}else {
			

			String insertUserCredResponse = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(lBean.getEmail(), lBean.getPassword());
			
			JSONObject json = new JSONObject(insertUserCredResponse);
			JSONObject UserData = json.getJSONObject("UserData");
			
			username = UserData.getString("UserName_UserID");
			email = UserData.getString("Email_ID");		
			memberId = UserData.getString("MemberID");		
			kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
			
			password = UserData.getString("Password");			
			birthDate = UserData.getString("BirthDate");
			aptNo = UserData.getString("NumApt");
			streetNo = UserData.getString("NumStreet");
			street = UserData.getString("Street");
			city = UserData.getString("City");
			state = UserData.getString("State");
			postcode = UserData.getString("ZipCode");
			countryId = UserData.getString("CountryId");
			
			firstname = UserData.getString("User_FirstName");
			lastName = UserData.getString("User_LastName");
			
			name = firstname + " " + lastName;
			
			// get USD wallet balance and display it in front-end screen
			USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);
			accountInfo = getUserAccountsList.accountDetails(username);
			System.out.println("accountInfo is: " + accountInfo);
			
			request.setAttribute("email", email);
			request.setAttribute("memberId", memberId);
			request.setAttribute("kycUploadFlag", kycUploadFlag);

			//ModelAndView mv = new ModelAndView("index");
			ModelAndView mv = new ModelAndView("redirect:mfaVerification");
			mv.addObject("name", name);
			mv.addObject("USDAccountBalance", USDAccountBalance);
			mv.addObject("accountInfo", accountInfo);
			return mv;

			
		}	
		}
	}

	/*
	 * @RequestMapping(value = "/displayAccountBal", method = RequestMethod.POST)
	 * public ModelAndView displayUSDAccountBalance() throws JSONException,
	 * SQLException {
	 * 
	 * // get USD wallet balance and display it in front-end screen
	 * USDAccountBalance =
	 * viewUSDWalletBalance.getUSDWalletBalance(lBean.getUserName());
	 * 
	 * accountInfo = getUserAccountsList.accountDetails(lBean.getUserName());
	 * System.out.println("accountInfo is: " + accountInfo); //name =
	 * getUserCredentialsdao.userCredentialDetails(lBean.getUserName());
	 * 
	 * ModelAndView mv = new ModelAndView("index"); mv.addObject("name", name);
	 * mv.addObject("USDAccountBalance", USDAccountBalance);
	 * mv.addObject("accountInfo", accountInfo); return mv;
	 * 
	 * }
	 */

	@RequestMapping(value = "/moneyTransferOtherAccount", method = RequestMethod.POST)
	@ResponseBody
	public String transferMoneyToOtherAccount(@RequestParam("beneficiaryEmailId") String beneficiaryEmailid,
			@RequestParam("currencyType") String currencyType, HttpServletRequest request)
			throws JSONException, SQLException {	
		

		String beneficiaryEmail = beneficiaryEmailid;
		String accCurrenctType = currencyType;

		String beneficiaryAccToBeDisplayed = null;

		System.out.println("Beneficiary email is: " + beneficiaryEmail);
		System.out.println("Currency Type is: " + accCurrenctType);
		
		
		GetUserContactDetailsNewAPI getUserContactDetailsNewAPI = new GetUserContactDetailsNewAPI();
		String response = getUserContactDetailsNewAPI.getUserContactDetails(beneficiaryEmail);
		
		JSONObject jsonResponse = new JSONObject(response);
		if(((JSONObject)jsonResponse.getJSONObject("Status")).getString("Code").equals("401001") || ((JSONObject)jsonResponse.getJSONObject("Status")).getString("Code").equals("400035"))
		{
			return response;
		}
		JSONObject beneficiaryUserData = jsonResponse.getJSONObject("user_data");
		
		String beneficiaryUsername = beneficiaryUserData.getString("UserName_UserID");
		
		System.out.println("Beneficiary username is: " + beneficiaryUsername);

		List<String> beneficiaryAccountInfo = getUserAccountsList.accountDetails(beneficiaryUsername);

		for (int i = 0; i < beneficiaryAccountInfo.size(); i++) {

			System.out.println("Beneficiary accout is: " + beneficiaryAccountInfo.get(i));

			if (beneficiaryAccountInfo.get(i).contains(accCurrenctType)) {

				beneficiaryAccToBeDisplayed = beneficiaryAccountInfo.get(i);

				System.out.println("beneficiaryAccToBeDisplayed: " + beneficiaryAccToBeDisplayed);

				break;

			} else {

				beneficiaryAccToBeDisplayed = beneficiaryAccountInfo.get(0);

			}

		}

		return beneficiaryAccToBeDisplayed;
	}

	@RequestMapping(value = "/senderToReceiverTransfer", method = RequestMethod.POST)
	@ResponseBody
	public String senderToReceiverTransfer(@RequestParam("senderAcc") String senderAcc,
			@RequestParam("beneficiaryEmailId") String beneficiaryEmailid,
			@RequestParam("beneficiaryAccount") String beneficiaryAccount, @RequestParam("amount") String amount,
			@RequestParam("currencyType") String currencyType) throws JSONException {

		String senderAccount = senderAcc;
		String beneficiaryEmail = beneficiaryEmailid;

		String beneficiaryAccDetail = beneficiaryAccount;
		String beneficiaryAcc = beneficiaryAccDetail.substring(0, 8);

		System.out.println("Beneficiary account is: " + beneficiaryAcc);

		String transferAmount = amount;
		String currency = currencyType;

		MoneyTransferToAnotherAcc MoneyTransferToAnotherAcc = new MoneyTransferToAnotherAcc();
		String response = MoneyTransferToAnotherAcc.fundTransferAnotherUser(senderAccount, beneficiaryEmail,
				beneficiaryAcc, transferAmount, currency);

		return response;
	}

	@RequestMapping(value = "/getToAccInfo", method = RequestMethod.POST)

	public @ResponseBody String senderToReceiverTransfer(@RequestParam("fromAccNo") String fromAccNo,
			@RequestParam("userEmail") String userEmail) throws SQLException, JSONException {

		String fromAccNunber = fromAccNo;
		String email = userEmail;

		// JSONObject obj = new JSONObject();
		// JSONArray toAccList = new JSONArray();

		List<String> toAccList = new ArrayList<String>();

		System.out.println("FromAccNo: " + fromAccNunber + " and user email: " + email);

		//username = viewUserDetailsDao.getUserDetails(email);

		System.out.println("Now username is: " + username);

		List<String> accountList = getUserAccountsList.accountDetails(username);

		for (int i = 0; i < accountList.size(); i++) {

			if (accountList.get(i).equalsIgnoreCase(fromAccNunber)) {

				continue;
			}

			// System.out.println("toAccList: " + accountList.get(i));
			String toAccNo = accountList.get(i);

			// toAccList.put(toAccNo);
			toAccList.add(toAccNo);

		}

		for (int j = 0; j < toAccList.size(); j++) {

			System.out.println("toAccountList: " + toAccList.get(j));

		}

		// obj.put("account", toAccList);
		String jsonString = gson.toJson(toAccList);

		return jsonString;
	}

	@RequestMapping(value = "/moneyTransferOwnAccount", method = RequestMethod.POST)
	@ResponseBody
	public String senderToReceiverTransfer(@RequestParam("fromAccNo") String fromAccNo,
			@RequestParam("toAccNo") String toAccNo, @RequestParam("fromAccCurrency") String fromAccCurrency,
			@RequestParam("amount") String amount) throws JSONException {

		String fromAccountNo = fromAccNo;
		String toAccountNo = toAccNo;
		String currency = fromAccCurrency;
		String exchangedAmount = amount;

		System.out.println("fromAccountNo: " + fromAccountNo + " toAccountNo: " + toAccountNo);

		MoneyTransferToOwnAccount moneyTransferToOwnAccount = new MoneyTransferToOwnAccount();
		String response = moneyTransferToOwnAccount.fundTransferOwnAccountAPI(fromAccountNo, toAccountNo, currency,
				exchangedAmount);

		System.out.println("response: " + response);

		return response;
	}

	/*
	 * @RequestMapping(value = "/getCountryISDCode", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String getCountryISDCode(@RequestParam("countryId")
	 * String countryId) throws JSONException{
	 * 
	 * String countryIdValue = countryId;
	 * 
	 * GetCountryISDCode getCountryISDCode = new GetCountryISDCode(); String isdCode
	 * = getCountryISDCode.countryListAPI(countryIdValue);
	 * 
	 * return isdCode; }
	 */

	@RequestMapping(value = "/getCLXVirtualCard", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getCLXVirtualCard(@RequestParam("email") String emailid) throws SQLException, JSONException {

		System.out.println("hello clx virtual card");
		
		
		//String userEmail = email;
		
		System.out.println("userEmail is: " + email);

		String cardInfo = null;
		String cardNum = null;
		String securityCode = null;
		String cardCurrency = null;
		String expMonth = null;
		String expYear = null;
		//String expDate = null;
		
		List<String> cardInfoJsonData = new ArrayList<String>();
		GetUserContactDetailsNewAPI getUserContactDetailsNewAPI = new GetUserContactDetailsNewAPI();
		String responseDetail = getUserContactDetailsNewAPI.getUserContactDetails(email);
		String res = null;
		try {
			
			JSONObject jsonResDtl = new JSONObject(responseDetail);
			JSONObject userData = jsonResDtl.getJSONObject("user_data");
			
			memberId = userData.getString("MemberID");
			username = userData.getString("UserName_UserID");
			
			String userFirstName = userData.getString("User_FirstName");
			String userLastName = userData.getString("User_LastName");
			
			String userFullName = userFirstName + " " + userLastName;
			
			System.out.println("memberid is: " + memberId + " and username is: " + username);
			
			if(resultSet.getString("Card_Program").equalsIgnoreCase("CLX"))
			{
				CreateCLXVirtualCard createCLXVirtualCard = new CreateCLXVirtualCard(); 
//				String res = createCLXVirtualCard.registrationWithoutCardAPI(email, username);
				res = createCLXVirtualCard.registrationWithoutCardAPI(email, username,resultSet);
				  
				  
				  if (res.contains("OK")) {
				  
				  JSONObject jsonResponse = new JSONObject(res); 
				  
				  String userId = jsonResponse.getString("UserId"); 
				  String accountId = jsonResponse.getString("AccountId");
				  
				  System.out.println("userId and accountId are: " + userId + ", " + accountId);
				  
				  //username = viewUserDetailsDao.getUserDetails(userEmail);
				  
				  //name = getUserCredentialsdao.userCredentialDetails(username);
				  
				  GetCardInformation getCardInformation = new GetCardInformation(); 
				  cardInfo = getCardInformation.clxCardInformationAPI(userId, accountId);
				  
				  JSONObject cardInfoJson = new JSONObject(cardInfo);
				  
				  cardNum = cardInfoJson.getString("CardNum");
				  securityCode = cardInfoJson.getString("SecurityCode");
				  cardCurrency = cardInfoJson.getString("Currency");
				  expMonth = cardInfoJson.getString("Month");
				  expYear = cardInfoJson.getString("Year");
				  
				  //expDate = expMonth + "/" + expYear;	  
				  
				  res = "Card has been created successfully!";
				  
				  cardInfoJsonData.add(userFullName);
				  cardInfoJsonData.add(cardNum);
				  cardInfoJsonData.add(securityCode);
				  cardInfoJsonData.add(cardCurrency);
				  cardInfoJsonData.add(expMonth);
				  cardInfoJsonData.add(expYear);
				  cardInfoJsonData.add(res);
				  
				  } else {
				  
//				  res = "Some internal error occured there";
//				  
//				  cardInfoJsonData.add(res);
					  ModelAndView mv = new ModelAndView("redirect:mfaVerification");
						return mv;
					 // return res;
				  }
			}
			
			else if(resultSet.getString("Card_Program").equalsIgnoreCase("MM"))
			{
				registerAndCreateWalletInMM(username);;
			}
			
			else if(resultSet.getString("Card_Program").equalsIgnoreCase("Instrarem"))
			{
				registerAndCreateWalletInInstrarem(username);
			}
			
		}
		catch(Exception e)
		{	ModelAndView mv = new ModelAndView("redirect:mfaVerification");
			return mv;
		}
		
		  
		  System.out.println("cardInfoJsonData is: " + cardInfoJsonData.toString());
			ModelAndView mv = new ModelAndView("redirect:mfaVerification");
		//return cardInfo.toString();
		  return mv;

	}
	
	private void registerAndCreateWalletInMM(String userName) throws JSONException {
		String userJsonResponse = null;
		String userResponse = null;
		JSONObject jsonObject = null;
		
		
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/CommonServices/GetUser_Details");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"username\":\""+userName + "\"}";
						
			
			System.out.println("CreateUser API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((userJsonResponse = br.readLine())!= null) {	
				System.out.println(userJsonResponse);
				
				userResponse = userJsonResponse;	
				
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {
			e.printStackTrace();

		  } catch (IOException e) {
			e.printStackTrace();

		 }
		JSONObject json = null;
		try
		{
			json = new JSONObject(userResponse);
		}
		catch(Exception e)
		{
			
		}
		
		JSONObject object = (JSONObject) json.get("user_details");
		String dob = object.getString("date_Of_birth");
		String user_permission_group = object.getString("user_permission_group");
		String first_name = object.getString("first_name");
		String middle_name = object.getString("middle_name");
		String last_name = object.getString("last_name");
		String merchant_sponsor = object.getString("merchant_sponsor");
		String gender = object.getString("gender");
		String address = object.getString("address");
		String address_2 = object.getString("address_2");
		String city = object.getString("city");
		String state = object.getString("state");
		String postal_code = object.getString("postal_code");
		String country = object.getString("country");
//		String home_phone = object.getString("home_phone");
//		String work_phone = object.getString("work_phone");
		String cell_phone = object.getString("cell_phone");
		String email = object.getString("email");
		String billing_address = object.getString("billing_address");
		String billing_address_2 = object.getString("billing_address_2");
		String billing_city = object.getString("billing_city");
		String billing_state = object.getString("billing_state");
		String billing_postal_code = object.getString("billing_postal_code");
		String billing_country = object.getString("billing_country");
		
		GetCountryISDCode getCountryISDCode = new GetCountryISDCode();
		
		String registerJsonResponse = null;
		String registerUserResponse = null;
		try {

			URL url = new URL("http://3.1.184.189:8081/api-mm/api/v1/register");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
//			conn.setRequestProperty("client_id", "swagger-client");
//			conn.setRequestProperty("client_secret", "swagger-secret");
			conn.setRequestProperty("Content-Type", "application/json");
			
			BASE64Encoder enc = new sun.misc.BASE64Encoder();
		      String userpassword = "swagger-client" + ":" + "swagger-secret";
		      String encodedAuthorization = enc.encode( userpassword.getBytes() );
			conn.setRequestProperty("Authorization", "Basic "+encodedAuthorization);
			
//			System.out.println(conn.getHeaderFields());
//			System.out.println(conn.getRequestProperties());
			
			String input = "{\"firstName\":\""+first_name+"\",\"lastName\":\""+ last_name + "\",\"preferredName\":\"" + first_name + "\",\r\n"
					+ "\"mobileCountyCode\":\"" + "91" + "\", \"mobile\":\"" + cell_phone + "\", \"email\":\"" + email + "\",\r\n"
		+ "\"password\": \"" + password + "\"}";
						
			
			System.out.println("CreateUser API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((registerJsonResponse = br.readLine())!= null) {	
				System.out.println(registerJsonResponse);
				
				registerUserResponse = registerJsonResponse;	
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		String authJsonResponse = null;
		String authUserResponse = null;
		try {

			URL url = new URL("http://3.1.184.189:8081/oauth/token");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
//			conn.addRequestProperty("client_id", "swagger-client");
//			conn.addRequestProperty("client_secret", "swagger-secret");
			BASE64Encoder enc = new sun.misc.BASE64Encoder();
		      String userpassword = "swagger-client" + ":" + "swagger-secret";
		      String encodedAuthorization = enc.encode( userpassword.getBytes() );
			conn.setRequestProperty("Authorization", "Basic "+encodedAuthorization);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			String urlParameters  = "username="+email+"&password="+password+"&grant_type=password";
						
			
			

			OutputStream os = conn.getOutputStream();
			os.write(urlParameters.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((authJsonResponse = br.readLine())!= null) {	
				System.out.println(authJsonResponse);
				
				authUserResponse = authJsonResponse;	
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		JSONObject jsonUserResponse = new JSONObject(authUserResponse);
		String accessToken = jsonUserResponse.getString("access_token");
		
		String createCardJsonResponse = null;
		String createCardUserResponse = null;
		try {

			URL url = new URL("http://3.1.184.189:8081/api-mm/api/v1/users/wallet/createCard?cardTypeCode=stylopaymccard");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization",  "Bearer "+accessToken);
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((createCardJsonResponse = br.readLine())!= null) {	
				System.out.println(createCardJsonResponse);
				
				createCardUserResponse = createCardJsonResponse;	
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	private void registerAndCreateWalletInInstrarem(String userName) throws JSONException {
		String userJsonResponse = null;
		String userResponse = null;
		JSONObject jsonObject = null;
		
		
		
		try {

			URL url = new URL("http://35.180.75.185/StyloDemoWalletService/API/CommonServices/GetUser_Details");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "asdfghjklLKJHGFDSA");

			String input = "{\"Application_ID\":1,\"username\":\""+userName + "\"}";
						
			
			System.out.println("CreateUser API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((userJsonResponse = br.readLine())!= null) {	
				System.out.println(userJsonResponse);
				
				userResponse = userJsonResponse;	
				
			}
			

			//conn.disconnect();

		  } catch (MalformedURLException e) {
			e.printStackTrace();

		  } catch (IOException e) {
			e.printStackTrace();

		 }
		JSONObject json = null;
		try
		{
			json = new JSONObject(userResponse);
		}
		catch(Exception e)
		{
			
		}
		
		JSONObject object = (JSONObject) json.get("user_details");
		String dob = object.getString("date_Of_birth");
		String user_permission_group = object.getString("user_permission_group");
		String first_name = object.getString("first_name");
		String middle_name = object.getString("middle_name");
		String last_name = object.getString("last_name");
		String merchant_sponsor = object.getString("merchant_sponsor");
		String gender = object.getString("gender");
		String address = object.getString("address");
		String address_2 = object.getString("address_2");
		String city = object.getString("city");
		String state = object.getString("state");
		String postal_code = object.getString("postal_code");
		String country = object.getString("country");
//		String home_phone = object.getString("home_phone");
//		String work_phone = object.getString("work_phone");
		String cell_phone = object.getString("cell_phone");
		String email = object.getString("email");
		String billing_address = object.getString("billing_address");
		String billing_address_2 = object.getString("billing_address_2");
		String billing_city = object.getString("billing_city");
		String billing_state = object.getString("billing_state");
		String billing_postal_code = object.getString("billing_postal_code");
		String billing_country = object.getString("billing_country");
		
		GetCountryISDCode getCountryISDCode = new GetCountryISDCode();
		
		String authJsonResponse = null;
		String authUserResponse = null;
		try {

			URL url = new URL("http://18.206.169.158:8081/oauth/token");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
//			conn.addRequestProperty("client_id", "swagger-client");
//			conn.addRequestProperty("client_secret", "swagger-secret");
			BASE64Encoder enc = new sun.misc.BASE64Encoder();
		      String userpassword = "swagger-client" + ":" + "swagger-secret";
		      String encodedAuthorization = enc.encode( userpassword.getBytes() );
			conn.setRequestProperty("Authorization", "Basic "+encodedAuthorization);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			String urlParameters  = "username="+email+"&password="+password+"&grant_type=password";
						
			
			

			OutputStream os = conn.getOutputStream();
			os.write(urlParameters.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((authJsonResponse = br.readLine())!= null) {	
				System.out.println(authJsonResponse);
				
				authUserResponse = authJsonResponse;	
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		JSONObject jsonUserResponse = new JSONObject(authUserResponse);
		String accessToken = jsonUserResponse.getString("access_token");
		
		String addCustomerJsonResponse = null;
		String addCustomerUserResponse = null;
		try {

			URL url = new URL("http://18.206.169.158:8081/instrarem_proj/api/v1/addCustomer");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization",  "Bearer "+accessToken);
			
			String input = "{\"title\":\""+"Mr"+"\",\"firstName\":\""+ first_name + "\",\"middleName\":\"" + middle_name + "\",\r\n"
					+ "\"lastName\":\"" + last_name + "\", \"preferredName\":\"" + first_name + "\", \"dateOfBirth\":\"" + dob + "\",\r\n"
					+ "\"nationality\":\"" + "US" + "\", \"email\":\"" + email + "\", \"countryCode\":\"" + "US" + "\",\r\n"
					+ "\"mobile\":\"" + cell_phone + "\", \"deliveryAddress1\":\"" + billing_address + "\", \"deliveryAddress2\":\"" + billing_address_2 + "\",\r\n"
					+ "\"deliveryCity\":\"" + billing_city + "\", \"deliveryLandmark\":\"" + billing_city + "\", \"deliveryState\":\"" + billing_state + "\",\r\n"
					+ "\"deliveryZipCode\":\"" + billing_postal_code + "\", \"billingAddress1\":\"" + billing_address + "\",\r\n"
					+ "\"billingAddress2\":\"" +billing_address_2 + "\", \"billingCity\":\"" + billing_city + "\",\r\n"
					+ "\"billingLandmark\":\"" + billing_city + "\", \"billingState\":\"" + billing_state + "\",\r\n"
					+ "\"billingZipCode\":\"" + billing_postal_code + "\", \"correspondenceAddress1\":\"" + billing_address + "\",\r\n"
					+ "\"correspondenceAddress2\":\"" + billing_address_2 + "\", \"correspondenceCity\":\"" + billing_city + "\",\r\n"
					+ "\"correspondenceLandmark\":\"" + billing_city + "\", \"correspondenceState\":\"" + billing_state + "\",\r\n"
					+ "\"correspondenceZipCode\":\"" + billing_postal_code
					+ "\"}";
						
			
			System.out.println("Add Customer API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((addCustomerJsonResponse = br.readLine())!= null) {	
				System.out.println(addCustomerJsonResponse);
				
				addCustomerUserResponse = addCustomerJsonResponse;	
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		JSONObject jsonObj = null;
		try
		{
			jsonObj = new JSONObject(addCustomerUserResponse);
		}
		catch(Exception e)
		{
			
		}
		String customerId = jsonObj.getString("customerHashId");
		String walletId = jsonObj.getString("walletHashId");
		String addCardJsonResponse = null;
		String addCardUserResponse = null;
		try {

			URL url = new URL("http://18.206.169.158:8081/instrarem_proj/api/v1/addCard/"+customerId+"/"+walletId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization",  "Bearer "+accessToken);
			
			String input = "{\"cardExpiry\":\""+"0124"+"\",\"cardFeeCurrencyCode\":\""+ "SGD" + "\",\"cardIssuanceAction\":\"" + "NEW" + "\",\r\n"
					+ "\"cardType\":\"" + "GPR_VIR" + "\", \"embossingLine1\":\"" + "vedant" + "\", \"embossingLine2\":\"" + "vedant" + "\",\r\n"
					+ "\"issuanceMode\":\"" + "normal_delivery_local" + "\", \"logoId\":\"" + "100" + "\", \"memorableWord\":\"" + "vedant" + "\",\r\n"
					+ "\"plasticId\":\"" + "34"
					+ "\"}";
						
			
			System.out.println("Add Customer API Json input is: " + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			

			
			while ((addCardJsonResponse = br.readLine())!= null) {	
				System.out.println(addCardJsonResponse);
				
				addCardUserResponse = addCardJsonResponse;	
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	@RequestMapping(value = "/getCLXPhysicalCard", method = RequestMethod.POST)

	@ResponseBody
	public String getCLXPhysicalCard(@RequestParam("email") String email, @RequestParam("cardNumber") String cardNumber)
			throws SQLException, JSONException {

		String userEmail = email;
		String physicalCardNo = cardNumber;

		String cardNum = null;

		CreateCLXPhysicalCardDao createCLXPhysicalCardDao = new CreateCLXPhysicalCardDao();
		String res = createCLXPhysicalCardDao.getClxPhysicalCard(userEmail, cardNumber, resultSet );

		if (res.contains("OK")) {

			JSONObject jsonResponse = new JSONObject(res);

			String userId = jsonResponse.getString("UserId");
			String accountId = jsonResponse.getString("AccountId");

			System.out.println("userId and accountId are: " + userId + ", " + accountId);

			username = viewUserDetailsDao.getUserDetails(userEmail);
			name = getUserCredentialsdao.userCredentialDetails(username);

			GetCardInformation getCardInformation = new GetCardInformation();
			cardNum = getCardInformation.clxCardInformationAPI(userId, accountId);

			res = "Card has been created successfully!";

		} else {

			res = "Some internal error occured there";

		}

		String cardInformation = name + "," + cardNum + "," + res;

		return cardInformation;

	}

	@RequestMapping(value = "/getStatesName", method = RequestMethod.POST)

	@ResponseBody
	public String getStatesName(@RequestParam("countryid") String countryid) throws SQLException, JSONException {

		String countryId = countryid;

		GetStateList getStateList = new GetStateList();
		List<String> stateList = getStateList.getCountryWiseStateNames(countryId);

		String jsonString = gson.toJson(stateList);

		return jsonString;
	}

	
	  @RequestMapping(value = "/kycUpload", method = RequestMethod.POST)
	  
	  public ModelAndView uploadKycDocs(@ModelAttribute("uploadFiles") KYCFileUpload kycFileUpload, Model model, HttpServletRequest servletRequest) throws IOException, JSONException {
	  
	  System.out.println("Hello");
	  System.out.println("memberid required for KYC docs upload is: " + memberId);
	  
	  String docType = null;
	  
	  GetTribeMemberId getTribeMemberId = new GetTribeMemberId();
	  
	//Get the list of files
      List<MultipartFile> files = kycFileUpload.getFiles();
      List<String> fileNames = new ArrayList<String>();
      List<String> docUId = new ArrayList<String>();

      //Check whether the list is not null or empty
      if (files != null && !files.get(0).getOriginalFilename().isEmpty())
      {
          //Get the individual MultipartFile
          for (MultipartFile multipartFile : files)
          {
        	  
        	  System.out.println("File name is: " + multipartFile.getOriginalFilename());
				        	  
        	  byte[] bytes = multipartFile.getBytes();
        	  byte[] encoded = Base64.encodeBase64(bytes);
        	  String encodedString = new String(encoded);
        	  
        	  //System.out.println("encoded string is: " + encodedString);  
        	  
        	  if(multipartFile.getOriginalFilename().contains("jpeg")) {
        		  
        		  
        		  docType = "JPEG";
        		  
        	  }else if(multipartFile.getOriginalFilename().contains("png")) {
        		  
        		  docType = "PNG";
        		  
        	  }else if(multipartFile.getOriginalFilename().contains("pdf")) {
        		  
        		  docType = "PDF";
        		  
        	  }
        	  
        	  KYCDocumentUploadAPI kycDocumentUploadAPI = new KYCDocumentUploadAPI();
        	  String response = kycDocumentUploadAPI.uploadKYCFile(encodedString, memberId, docType);
        	  
        	 System.out.println("KYC Document Upload Response in java class is: " + response); 
        	 
        	 JSONObject jsonResponse = new JSONObject(response);
       	  	 String documentUid = jsonResponse.getString("DocumentUID");
       	  
       	  	 System.out.println("documentUid is: " + documentUid);
       	  	 
       	     docUId.add(documentUid);        	 
         
          }
          
      } 
      else
      {
          //model.addAttribute("message", "Please select atleast one file!!");
      }
      
      //Calling IDVCheck API
      
      	GetISOCountryCode getISOCountryCode = new GetISOCountryCode();
 	 	String isoCountryCode = getISOCountryCode.getISOCountryCodeAPI(countryId);
 	
 	 	IDVCheckAPI idvCheckAPI = new IDVCheckAPI();
 	 	String idvCheckResponse = idvCheckAPI.kycIdvChcking(docUId, memberId, isoCountryCode, birthDate, firstname, aptNo, streetNo, street, city, state, postcode, lastName);
 	 	
 	 	System.out.println("idvCheck response is: " + idvCheckResponse);
 	 	
 	 	String insertUserCredResponse = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
		
		JSONObject json = new JSONObject(insertUserCredResponse);
		JSONObject UserData = json.getJSONObject("UserData");				
		kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
 	 	
      	servletRequest.setAttribute("email", email);
      	servletRequest.setAttribute("memberId", memberId);
      	servletRequest.setAttribute("kycUploadFlag", kycUploadFlag);
      	
      	accountInfo = getUserAccountsList.accountDetails(username);
      	System.out.println("accountInfo is: " + accountInfo);      
      	
      	
		//ModelAndView mv = new ModelAndView("index");
      	ModelAndView mv = new ModelAndView("redirect:mfaVerification");
		mv.addObject("name", name);
		mv.addObject("USDAccountBalance", USDAccountBalance);
		mv.addObject("accountInfo", accountInfo);
		return mv;
	  
	  }
	 
	
	
@RequestMapping(value = "/getTransactionList", method = RequestMethod.POST)
	  
	  @ResponseBody public String getTransactionListDetails(@RequestParam("accountId") String accountId,
	  
	  @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String
	  toDate, HttpServletRequest request) throws JSONException {
	  
	  String jsontTransActvtyRes = null;  
		  
	  String accountDetail = accountId; String fromDateOfTrans = fromDate; String
	  toDateOfTrans = toDate;
	  
	  List<String> transactionList = new ArrayList<String>(); List<List>
	  transactionDetails = new ArrayList<List>();
	  
	  String accountNo = accountDetail.substring(0, 8);
	  
	  System.out.println("accountNo, fromDateOfTrans, toDateOfTrans -" + accountNo
	  + ", " + fromDateOfTrans + ", " + toDateOfTrans);
	  
	  GetAccountActivityAPI getAccountActivityAPI = new GetAccountActivityAPI();
	  String transactionActivityResponse = getAccountActivityAPI.getTransactionActivityDetails(accountNo,
	  fromDateOfTrans, toDateOfTrans); 
	  
	  jsontTransActvtyRes = transactionActivityResponse.substring(1, transactionActivityResponse.length()-1);
	  System.out.println("jsontTransActvtyRes is: " + jsontTransActvtyRes);	  
	  
	  return jsontTransActvtyRes;
	  
	  }
	  
	  
	  @RequestMapping(value = "/setClxCardPin", method = RequestMethod.POST)
		@ResponseBody
		public String setClxCardPin(@RequestParam("pin") String pin) throws SQLException, JSONException {
		  
		  String clxCardPin = pin;
		  
		  String msg = null;
		  String clxMemberId = null;
		  String accountNo = null;
		  String expMonth = null;
		  String expYear = null;
		  String validThru = null; 
		  String secCode = null;
		  
		  if(memberId == null) {
			  
			  msg = "Please complete your profile first!";
			  
		  }else {
			  
			  GetUserContactDetailsAPI getUserContactDetailsAPI = new GetUserContactDetailsAPI();
			  String response = getUserContactDetailsAPI.getUserContactDetails(memberId);
			  
			  if(response.contains("OK")) {
				  
				  
				  JSONObject jsonResponse = new JSONObject(response);
				  JSONObject userData = jsonResponse.getJSONObject("user_data");
				  
				  clxMemberId = userData.getString("CLX_MemberID");
				  
				  if(clxMemberId == null || clxMemberId.trim().length()==0) {
					  
					  msg = "Your card has not been created yet";
				  }else {
					  
					  GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
					  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
					  
					  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
					  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
					  
					  JSONObject account = clxAccDetails.getJSONObject(0);
					  accountNo = account.getString("AccountID");
					  
					  System.out.println("accountNo is: " + accountNo);
					  
					  GetCardInformation getCardInformation = new GetCardInformation();
					  String cardinfo = getCardInformation.clxCardInformationAPI(clxMemberId, accountNo);
					  
					  JSONObject cardInfoJson = new JSONObject(cardinfo);
					  
					  expMonth = cardInfoJson.getString("Month");
					  expYear = cardInfoJson.getString("Year");
					  
					  validThru = expMonth + expYear.substring(2);
					  secCode = cardInfoJson.getString("SecurityCode");
					  
					  SetCLXCardPINSetAPI setCLXCardPINSetAPI = new SetCLXCardPINSetAPI();
					  String cardPinSetRes = setCLXCardPINSetAPI.clxCardPinSet(clxMemberId, accountNo, validThru, secCode, clxCardPin);
					  
					  System.out.println("cardPinSetRes is: " + cardPinSetRes);
					  
					  JSONObject clxCardPinSetMsgJson = new JSONObject(cardPinSetRes);
					  JSONObject status = clxCardPinSetMsgJson.getJSONObject("Status");
					  
					  String clxCardPinSetMsg = status.getString("Message");
					  
					  msg = clxCardPinSetMsg;
					  
				  }
				  
				  
			  }else {
				  
				  
				  msg = "Some internal error occurs there!";
				  
			  }
			  
		  }
		  
		  
		  return msg;
	  }
	 
	

	  @RequestMapping(value = "/clxCardReplacement", method = RequestMethod.POST)
		@ResponseBody
		public String clxCardReplacement(@RequestParam("newCardNo") String newCardNo, @RequestParam("reason") String reason) throws SQLException, JSONException { 
		  
		 String cardNoForReplacement =  newCardNo;
		 String reasonForReplacement = reason;
		 
		 
		 String msg = null;
		  String clxMemberId = null;
		  String accountNo = null;
		  String expMonth = null;
		  String expYear = null;
		  String validThru = null; 
		  String secCode = null;
		  
		  String accNoForReplacement =  null;
		  
		  if(memberId == null) {
			  
			  msg = "Please complete your profile first!";
			  
		  }else {
			  
			  GetUserContactDetailsAPI getUserContactDetailsAPI = new GetUserContactDetailsAPI();
			  String response = getUserContactDetailsAPI.getUserContactDetails(memberId);
			  
			  if(response.contains("OK")) {
				  
				  
				  JSONObject jsonResponse = new JSONObject(response);
				  JSONObject userData = jsonResponse.getJSONObject("user_data");
				  
				  clxMemberId = userData.getString("CLX_MemberID");
				  
				  if(clxMemberId == null || clxMemberId.trim().length()==0) {
					  
					  msg = "Your card has not been created yet";
				  }else {
					  
					  GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
					  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
					  
					  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
					  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
					  
					  JSONObject account = clxAccDetails.getJSONObject(0);
					  accountNo = account.getString("AccountID");
					  
					  System.out.println("accountNo is: " + accountNo);
					  
					  GetAccountIdFromCardAPI getAccountIdFromCardAPI = new GetAccountIdFromCardAPI();
					  String getAccIdFromCardRes = getAccountIdFromCardAPI.getAccIdFromCard(cardNoForReplacement);
					  
					  if(getAccIdFromCardRes.contains("Found")) {
						  
						  JSONObject getAccIdFromCardResJson = new JSONObject(getAccIdFromCardRes);
						  accNoForReplacement = getAccIdFromCardResJson.getString("Account_Id");
						  
						  CLXCardReplaceAPI clxCardReplaceAPI = new CLXCardReplaceAPI();
						  String CLXCardReplaceAPIRes = clxCardReplaceAPI.clxCardReplac(clxMemberId, accountNo, accNoForReplacement);
						  
						  System.out.println("CLXCardReplaceAPIRes is: " + CLXCardReplaceAPIRes);
						  
						  if(CLXCardReplaceAPIRes.contains("OK")) {
							  
							  JSONObject CLXCardReplaceAPIResJson = new JSONObject(CLXCardReplaceAPIRes);
							  String newAccountId = CLXCardReplaceAPIResJson.getString("NewAccountId");
							  String newCardNumber = CLXCardReplaceAPIResJson.getString("NewCardNum");
							  
							  msg = "Card has been replaced successfully with new accountId: " + newAccountId + " and new card number: " + newCardNumber; 
							  
						  }else {
							  
							  msg = "Some error occured during card replacement!";
							  
						  }
						  
					  }else {
						  
						  msg = "Please enter a valid card number!";
						  
					  }
					  
				  }
				  
				  
			  }else {
				  
				  
				  msg = "Some internal error occurs there!";
				  
			  }
			  
		  }
		  
		  
		return msg;  
		  
	  }
	  
	  
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logoutPage(HttpSession session) throws SQLException {

		session.invalidate();
		accessToken =null;
		ModelAndView view = new ModelAndView("login");
			view.addObject("logo", resultSet.getString("logo_link"));
			view.addObject("dashBoardName", resultSet.getString("DashboardName"));
			return view;
	}
	 /* @RequestMapping(value = "/logout", method = RequestMethod.GET)
		public ModelAndView logoutPage(HttpSession session) {
			
			AWSCognitoIdentityProvider cognitoClient =  getAmazonCognitoIdentityClient();
			
			GlobalSignOutRequest globalSignOutRequest = new GlobalSignOutRequest()
					.withAccessToken(accessToken);
			cognitoClient.globalSignOut(globalSignOutRequest);

			session.invalidate();

			return new ModelAndView("login");
		}*/
	
	// Cognito user pool
	   
	   public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		      ClasspathPropertiesFileCredentialsProvider propertiesFileCredentialsProvider = 
		           new ClasspathPropertiesFileCredentialsProvider();
		 
		       return AWSCognitoIdentityProviderClientBuilder.standard()
		                      .withCredentials(propertiesFileCredentialsProvider)
		                             .withRegion(cognitoConfig.getRegion())
		                             .build();
		 
		   }
	  /*****************************old Version***************/
	  /* public UserType signUp(UserSignUpRequest signUpRequest, Model model){
		   UserType cognitoUser = null;
		   System.out.println("test");
		   try
		   {
		   AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
		   AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
		        .withUserPoolId(cognitoConfig.getUserPoolId())
		        .withUsername(signUpRequest.getEmail())
		        .withUserAttributes(
		              new AttributeType()
		              .withName("email")
		               .withValue(signUpRequest.getEmail()),
		               new AttributeType()
		               .withName("phone_number")
		               .withValue(signUpRequest.getPhoneNumber()))
		               .withTemporaryPassword(signUpRequest.getPasswprd())
		               .withMessageAction(MessageActionType.SUPPRESS)
		               .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
		               .withForceAliasCreation(Boolean.FALSE);
		        AdminCreateUserResult createUserResult =  cognitoClient.adminCreateUser(cognitoRequest);
		        		final Map<String, String> challengeResponses = new HashMap();
			       challengeResponses.put("USERNAME", signUpRequest.getUsername());
			       challengeResponses.put("PASSWORD", signUpRequest.getPasswprd());
			       //add the new password to the params map
			       challengeResponses.put("NEW_PASSWORD", signUpRequest.getPasswprd());
			       //populate the challenge response
//			        final RespondToAuthChallengeRequest request = new RespondToAuthChallengeRequest();
//			          request.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
//			          .withChallengeResponses(challengeResponses)
//			          .withClientId(cognitoConfig.getClientId());
//			          		
//		        cognitoClient.respondToAuthChallenge(request);
			       cognitoUser =  createUserResult.getUser();
			        cognitoClient.shutdown();
		   }
		        
		  
		   catch(Exception e)
		   {
			   model.addAttribute("error", "Opps! Something went wrong");
		   }
		   
		        return cognitoUser;
		  
		 }*************************OLD Vesion End*/
	   public ModelAndView signUp(UserSignUpRequest signUpRequest, Model model) {
			UserType cognitoUser = null;
			try {
				AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
				AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
						.withUserPoolId(cognitoConfig.getUserPoolId()).withUsername(signUpRequest.getEmail())
						.withUserAttributes(new AttributeType().withName("email").withValue(signUpRequest.getEmail()),
								new AttributeType().withName("phone_number")
										.withValue(signUpRequest.getPhoneNumber()))
						.withTemporaryPassword(signUpRequest.getPasswprd()).withMessageAction(MessageActionType.SUPPRESS)
						.withDesiredDeliveryMediums(DeliveryMediumType.EMAIL).withForceAliasCreation(Boolean.FALSE);

				AdminCreateUserResult createUserResult = cognitoClient.adminCreateUser(cognitoRequest);

				final Map<String, String> challengeResponses = new HashMap();
				challengeResponses.put("USERNAME", signUpRequest.getUsername());
				challengeResponses.put("PASSWORD", signUpRequest.getPasswprd());
				// add the new password to the params map
				challengeResponses.put("NEW_PASSWORD", signUpRequest.getPasswprd());
				// populate the challenge response
//				        final RespondToAuthChallengeRequest request = new RespondToAuthChallengeRequest();
//				          request.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
//				          .withChallengeResponses(challengeResponses)
//				          .withClientId(cognitoConfig.getClientId());
//				          		
//			        cognitoClient.respondToAuthChallenge(request);

//				       ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest();
//				       confirmSignUpRequest.wi
//				       cognitoClient.confirmSignUp(arg0)
				AuthenticationRequest authenticationRequest = new AuthenticationRequest();
				authenticationRequest.setUsername(signUpRequest.getUsername());
				authenticationRequest.setPassword(signUpRequest.getPasswprd());
				return signInWhileSignUp(authenticationRequest, model);

//				cognitoUser = createUserResult.getUser();
	//
//				cognitoClient.shutdown();
			}

			catch (Exception e) {
				model.addAttribute("error", "Opps! Something went wrong");
			}

			return new ModelAndView("login");

		}

	   
	   public void changePassword(PasswordRequest passwordRequest) {
           
		     AWSCognitoIdentityProvider cognitoClient= getAmazonCognitoIdentityClient();
		     
		     ChangePasswordRequest changePasswordRequest= new ChangePasswordRequest()
		              .withAccessToken(passwordRequest.getAccessToken())
		              .withPreviousPassword(passwordRequest.getOldPassword())
		              .withProposedPassword(passwordRequest.getPassword());
		     
		 
		      cognitoClient.changePassword(changePasswordRequest);
		      cognitoClient.shutdown();
		              
		}
	   
	   
	   public UserResponse getUserInfo(String username) {
		   
	       AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();             
	       AdminGetUserRequest userRequest = new AdminGetUserRequest()
	                      .withUsername(username)
	                      .withUserPoolId(cognitoConfig.getUserPoolId());
	 
	 
	       AdminGetUserResult userResult = cognitoClient.adminGetUser(userRequest);
	 
	       UserResponse userResponse = new UserResponse();
	       userResponse.setUsername(userResult.getUsername());
	       userResponse.setUserStatus(userResult.getUserStatus());
	       userResponse.setUserCreateDate(userResult.getUserCreateDate());
	       userResponse.setLastModifiedDate(userResult.getUserLastModifiedDate());
	 
	       List<AttributeType> userAttributes = userResult.getUserAttributes();
	       for(AttributeType attribute: userAttributes) {

//	              if(attribute.getName().equals("custom:companyName")) {
//	                 userResponse.setCompanyName(attribute.getValue());
//	}else if(attribute.getName().equals("custom:companyPosition")) {
//	                 userResponse.setCompanyPosition(attribute.getValue());
//	              }else
	    	   
	    	   if(attribute.getName().equals("email")) {
	                 userResponse.setEmail(attribute.getValue());
	              }
	       }
	 
	       cognitoClient.shutdown();
	       return userResponse;
	              
	}
	   
	   
	   // sign in
	   
	   public ModelAndView signIn(AuthenticationRequest authenticationRequest, Model model){
		    AuthenticationResultType authenticationResult = null;
		    cognitoClient = getAmazonCognitoIdentityClient();
		 
		    final Map<String, String>authParams = new HashMap();
		    authParams.put("USERNAME", authenticationRequest.getUsername());  
		    authParams.put("PASSWORD", authenticationRequest.getPassword());
		    //authParams.put("PHONE_NUMBER", "+919051623419");
		    //authParams.put("SECRET_HASH", "fbto0hp26njo2kahi7o4i0jv79o5idh0od3kgvicpbclgstdvjl");
		 
		   final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
		       authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
		       .withClientId(cognitoConfig.getClientId())
		       .withUserPoolId(cognitoConfig.getUserPoolId())
		       .withAuthParameters(authParams);
		   try
		   {
		   result = cognitoClient.adminInitiateAuth(authRequest);
		   
		   if(result.getChallengeName() != null) {
				//If the challenge is required new Password validates if it has the new password variable.
				   if("NEW_PASSWORD_REQUIRED".equals(result.getChallengeName())){
					   
					   
						
						       //we still need the username
						       final Map<String, String> challengeResponses = new HashMap();
						       challengeResponses.put("USERNAME", authenticationRequest.getUsername());
						       challengeResponses.put("PASSWORD", authenticationRequest.getPassword());
						       //add the new password to the params map
						       challengeResponses.put("NEW_PASSWORD", password);
						       //populate the challenge response
						        final AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest();
						          request.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
						          .withChallengeResponses(challengeResponses)
						          .withClientId(cognitoConfig.getClientId())
						          .withUserPoolId(cognitoConfig.getUserPoolId())
						          .withSession(result.getSession());
						 
						      resultChallenge =  
						               cognitoClient.adminRespondToAuthChallenge(request);
						      authenticationResult = resultChallenge.getAuthenticationResult();
						      ModelAndView modelAndView = new ModelAndView("mfaFromLogin");
						      modelAndView.addObject("logo", resultSet.getString("logo_link"));
								modelAndView.addObject("dashBoardName", resultSet.getString("DashboardName"));
						      return modelAndView;
						      }
				   
				   else if("SMS_MFA".equals(result.getChallengeName()))
						   {
					   			ModelAndView view = new ModelAndView("mfa");
					   			view.addObject("logo", resultSet.getString("logo_link"));
					   			view.addObject("dashBoardName", resultSet.getString("DashboardName"));
					   			return view;
						   }
						

						
						
				      
		   }
		   }
		   catch(Exception e)
		   {
			   model.addAttribute("error", "Invalid credentials");
		   }
		   
		   
		return new ModelAndView("login");
		 
		   
		
		   
	   }
	   
	   public AuthenticationResultType mfaAuthentication(MfaBean mfaBean, Model model)
	   {
		   AuthenticationResultType authenticationResult = null;
		   
		   
		   UserResponse response = getUserInfo(email);
		    
		 //Has a Challenge
		   if(result.getChallengeName() != null) {
		//If the challenge is required new Password validates if it has the new password variable.
		   
		   if ("SMS_MFA".equals(result.getChallengeName())) {
            if (null == mfaBean.getMfaCode()) {
                try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
         	   try {
         		   
         	   
                final Map<String, String> challengesResponse = new HashMap();
                challengesResponse.put("USERNAME", response.getUsername());
                challengesResponse.put("PASSWORD", password);
                challengesResponse.put("SMS_MFA_CODE", mfaBean.getMfaCode());
                final AdminRespondToAuthChallengeRequest requestChallenge = new AdminRespondToAuthChallengeRequest();
                requestChallenge
                        .withChallengeName(ChallengeNameType.SMS_MFA)
                        .withChallengeResponses(challengesResponse)
                        .withClientId(cognitoConfig.getClientId())
                        .withUserPoolId(cognitoConfig.getUserPoolId())
                        .withSession(result.getSession());
                

                AdminRespondToAuthChallengeResult requestChallengeResponse = cognitoClient.adminRespondToAuthChallenge(requestChallenge);
                authenticationResult = requestChallengeResponse.getAuthenticationResult();
                cognitoClient.shutdown();
         	   }
         	   catch(Exception e)
         	   {
         		   e.printStackTrace();
         		   model.addAttribute("error", "Wrong athentication code");
         	   }

            }


        } 
		   
		   else{
		    //has another challenge
		    try {
				throw new Exception(result.getChallengeName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		   }else{
		       //Doesn't have a challenge
		       authenticationResult = result.getAuthenticationResult();
		   }
		   
		   //return userAuthenticated;
		   return authenticationResult;
	   }
	   
	   public AuthenticationResultType mfaAuthenticationForFirstTime(MfaBean mfaBean, Model model)
	   {
		   AuthenticationResultType authenticationResult = null;
		   
		   
		   UserResponse response = getUserInfo(email);
		    
		 //Has a Challenge
		   if(resultChallenge.getChallengeName() != null) {
		//If the challenge is required new Password validates if it has the new password variable.
		   
		   if ("SMS_MFA".equals(resultChallenge.getChallengeName())) {
            if (null == mfaBean.getMfaCode()) {
                try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
         	   try {
         		   
         	   
                final Map<String, String> challengesResponse = new HashMap();
                challengesResponse.put("USERNAME", response.getUsername());
                challengesResponse.put("PASS_WORD", password);
                challengesResponse.put("SMS_MFA_CODE", mfaBean.getMfaCode());
                final AdminRespondToAuthChallengeRequest requestChallenge = new AdminRespondToAuthChallengeRequest();
                requestChallenge
                        .withChallengeName(ChallengeNameType.SMS_MFA)
                        .withChallengeResponses(challengesResponse)
                        .withClientId(cognitoConfig.getClientId())
                        .withUserPoolId(cognitoConfig.getUserPoolId())
                        .withSession(resultChallenge.getSession());
                

                AdminRespondToAuthChallengeResult requestChallengeResponse = cognitoClient.adminRespondToAuthChallenge(requestChallenge);
                authenticationResult = requestChallengeResponse.getAuthenticationResult();
                cognitoClient.shutdown();
         	   }
         	   catch(Exception e)
         	   {
         		   model.addAttribute("error", "Wrong authentication code");
         	   }

            }


        } 
		   
		   else{
		    //has another challenge
		    try {
				throw new Exception(result.getChallengeName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		   }else{
		       //Doesn't have a challenge
		       authenticationResult = result.getAuthenticationResult();
		   }
		   
		   //return userAuthenticated;
		   return authenticationResult;
	   }
	   
	   @RequestMapping(value = "/mfaVerification", method = RequestMethod.POST) 
		public ModelAndView userMfaAuthentication(@ModelAttribute("mfa") MfaBean mfaBean, HttpServletRequest request, Model model) throws SQLException, JSONException, ParseException 
		{ 
			
			LoginDao loginDao = new LoginDao();
			String clxMemberId="0";
			String cardbal1="0.00";
			String cardstatus="IN";
			String availablebalance="0.00";
			if(accessToken != null)
			{
	
				
				
				//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
				
				String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
//				String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex("", loginBean.getPassword());
				
				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				//JSONParser parser = new JSONParser();
				JSONObject jsonreponse = new JSONObject(response);
				
				
				if (((JSONObject)jsonreponse.get("Status")).get("Code").toString().equals("0")) {
					
					JSONObject json = new JSONObject(response);
					JSONObject UserData = json.getJSONObject("UserData");
				
					//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
					username = UserData.getString("UserName_UserID");
					
					firstname = UserData.getString("User_FirstName");
					lastName = UserData.getString("User_LastName");
					
					name = firstname + " " + lastName;
					email = UserData.getString("Email_ID");
					password = UserData.getString("Password");
					memberId = UserData.getString("MemberID");
					birthDate = UserData.getString("BirthDate");
					aptNo = UserData.getString("NumApt");
					streetNo = UserData.getString("NumStreet");
					street = UserData.getString("Street");
					city = UserData.getString("City");
					state = UserData.getString("State");
					postcode = UserData.getString("ZipCode");
					countryId = UserData.getString("CountryId");
					clxMemberId=UserData.getString("CLX_MemberID");
					kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
					/**************fetching card Balance*************************/
					System.out.println("size"+clxMemberId.length());
					if(clxMemberId.length()>2)
					{
						 GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
						  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
						  
						  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
						  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
						  JSONObject account = clxAccDetails.getJSONObject(0);
							 String accountNo = account.getString("AccountID");
						  
						 
						 /***************fetching card status**************/
                        CardStatus clxstatusresponse=new CardStatus();
						String cardstatusresponse=clxstatusresponse.clxCardStatusAPI(clxMemberId, accountNo);
						JSONObject cardstat1 = new JSONObject(cardstatusresponse);
						String cardstatus1=cardstat1.get("CardStatus").toString();
						JSONObject cardstat2 = new JSONObject(cardstatus1);
						cardstatus=cardstat2.get("Code").toString();
						System.out.println("Card Status"+cardstatus);
						
						 /***************fetching card balance**************/
						 CardBalance clxbalanceresponse=new CardBalance();
						 String cardbalance=clxbalanceresponse.clxCardBalanceAPI(clxMemberId, accountNo);
						 System.out.println("Card Balance is 4: " + cardbalance);
						 JSONObject cardbal = new JSONObject(cardbalance);
						  System.out.println("Card Balance is2: " + cardbal.toString());
						   cardbal1=cardbal.get("AccountBalance").toString();
						   JSONObject cardba2 = new JSONObject(cardbal1);
						   availablebalance=cardba2.get("AvailableBalance").toString();
						   System.out.println("Card Balance is3: " + cardba2.get("AvailableBalance").toString());
						   System.out.println("Card Balance is1: " + cardbal1);
						   System.out.println("Card Balance is2: " + cardbal.toString());
					}
					
					System.out.println("After login username is: " + username);

					USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);

					if (USDAccountBalance.contains("Some internal errors occured there!")) {

						return new ModelAndView("index");

					} else {

						accountInfo = getUserAccountsList.accountDetails(username);
						System.out.println("accountInfo is: " + accountInfo);
						
						//name = getUserCredentialsdao.userCredentialDetails(username);					
						
						request.setAttribute("email", email);
						//request.setAttribute("email", "");
						request.setAttribute("memberId", memberId);
						request.setAttribute("kycUploadFlag", kycUploadFlag);
						request.setAttribute("clxMemberID",  clxMemberId);
						request.setAttribute("cardbalance",  availablebalance);
						request.setAttribute("cardstatus",  cardstatus);
						
						ModelAndView mv = new ModelAndView("index");

						mv.addObject("name", name);

						mv.addObject("USDAccountBalance", USDAccountBalance);
						mv.addObject("accountInfo", accountInfo);
						return mv;

					}

				} else {

					return new ModelAndView("index");
				}
				  
				
				
			
			}
			AuthenticationResultType authenticationResultType = mfaAuthentication(mfaBean, model);
			accessToken = authenticationResultType.getAccessToken();
			
			//String response = loginDao.userLogin(loginBean);
			
			//System.out.println("LoginDao class Response is: " + response);
			
			if(authenticationResultType != null) {	
				
				
				//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
				
				String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
//				String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex("", loginBean.getPassword());
				
				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				//JSONParser parser = new JSONParser();
				JSONObject jsonreponse = new JSONObject(response);
				
				
				if (((JSONObject)jsonreponse.get("Status")).get("Code").toString().equals("0")) {
					
					JSONObject json = new JSONObject(response);
					JSONObject UserData = json.getJSONObject("UserData");
				
					//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
					username = UserData.getString("UserName_UserID");
					
					firstname = UserData.getString("User_FirstName");
					lastName = UserData.getString("User_LastName");
					
					name = firstname + " " + lastName;
					email = UserData.getString("Email_ID");
					password = UserData.getString("Password");
					memberId = UserData.getString("MemberID");
					birthDate = UserData.getString("BirthDate");
					aptNo = UserData.getString("NumApt");
					streetNo = UserData.getString("NumStreet");
					street = UserData.getString("Street");
					city = UserData.getString("City");
					state = UserData.getString("State");
					postcode = UserData.getString("ZipCode");
					countryId = UserData.getString("CountryId");
					clxMemberId=UserData.getString("CLX_MemberID");
					kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
					/**************fetching card Balance*************************/
					System.out.println("size"+clxMemberId.length());
					if(clxMemberId.length()>2)
					{
						 GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
						  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
						  
						  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
						  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
						  JSONObject account = clxAccDetails.getJSONObject(0);
							 String accountNo = account.getString("AccountID");
						  
						 
						 /***************fetching card status**************/
                        CardStatus clxstatusresponse=new CardStatus();
						String cardstatusresponse=clxstatusresponse.clxCardStatusAPI(clxMemberId, accountNo);
						JSONObject cardstat1 = new JSONObject(cardstatusresponse);
						String cardstatus1=cardstat1.get("CardStatus").toString();
						JSONObject cardstat2 = new JSONObject(cardstatus1);
						cardstatus=cardstat2.get("Code").toString();
						
						 /***************fetching card balance**************/
						 CardBalance clxbalanceresponse=new CardBalance();
						 String cardbalance=clxbalanceresponse.clxCardBalanceAPI(clxMemberId, accountNo);
						 System.out.println("Card Balance is 4: " + cardbalance);
						 JSONObject cardbal = new JSONObject(cardbalance);
						  System.out.println("Card Balance is2: " + cardbal.toString());
						   cardbal1=cardbal.get("AccountBalance").toString();
						   JSONObject cardba2 = new JSONObject(cardbal1);
						   availablebalance=cardba2.get("AvailableBalance").toString();
						   System.out.println("Card Balance is3: " + cardba2.get("AvailableBalance").toString());
						   System.out.println("Card Balance is1: " + cardbal1);
						   System.out.println("Card Balance is2: " + cardbal.toString());
					}
					
					System.out.println("After login username is: " + username);

					USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);

					if (USDAccountBalance.contains("Some internal errors occured there!")) {

						return new ModelAndView("index");

					} else {

						accountInfo = getUserAccountsList.accountDetails(username);
						System.out.println("accountInfo is: " + accountInfo);
						
						//name = getUserCredentialsdao.userCredentialDetails(username);					
						
						request.setAttribute("email", email);
						//request.setAttribute("email", "");
						request.setAttribute("memberId", memberId);
						request.setAttribute("kycUploadFlag", kycUploadFlag);
						request.setAttribute("clxMemberID",  clxMemberId);
						request.setAttribute("cardbalance",  availablebalance);
						request.setAttribute("cardstatus",  cardstatus);
						
						ModelAndView mv = new ModelAndView("index");

						mv.addObject("name", name);

						mv.addObject("USDAccountBalance", USDAccountBalance);
						mv.addObject("accountInfo", accountInfo);
						return mv;

					}

				} else {

					return new ModelAndView("index");
				}
				  
				
				
			}else {
				
				return new ModelAndView("login");
			} 
			  
		  }
		
		@RequestMapping(value = "/mfaVerificationFromLogin", method = RequestMethod.POST) 
		public ModelAndView userMfaAuthenticationFromLogin(@ModelAttribute("mfa") MfaBean mfaBean, HttpServletRequest request, Model model) throws SQLException, JSONException, ParseException 
		{ 
			
			LoginDao loginDao = new LoginDao();
			AuthenticationResultType authenticationResultType = mfaAuthenticationForFirstTime(mfaBean, model);
			//String response = loginDao.userLogin(loginBean);
			
			//System.out.println("LoginDao class Response is: " + response);
			
			if(authenticationResultType != null) {
				
	
				
				
				//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
				
				String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
//				String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex("", loginBean.getPassword());
				
				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				JSONObject jsonreponse = new JSONObject(response);
				

				if (((JSONObject)jsonreponse.get("Status")).get("Code").toString().equals("0")) {
					
					JSONObject json = new JSONObject(response);
					JSONObject UserData = json.getJSONObject("UserData");
				
					//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
					username = UserData.getString("UserName_UserID");
					
					firstname = UserData.getString("User_FirstName");
					lastName = UserData.getString("User_LastName");
					
					name = firstname + " " + lastName;
					email = UserData.getString("Email_ID");
					password = UserData.getString("Password");
					memberId = UserData.getString("MemberID");
					birthDate = UserData.getString("BirthDate");
					aptNo = UserData.getString("NumApt");
					streetNo = UserData.getString("NumStreet");
					street = UserData.getString("Street");
					city = UserData.getString("City");
					state = UserData.getString("State");
					postcode = UserData.getString("ZipCode");
					countryId = UserData.getString("CountryId");
					
					kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
					
					
					System.out.println("After login username is: " + username);

					USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);

					if (USDAccountBalance.contains("Some internal errors occured there!")) {

						return new ModelAndView("index");

					} else {

						accountInfo = getUserAccountsList.accountDetails(username);
						System.out.println("accountInfo is: " + accountInfo);
						
						name = getUserCredentialsdao.userCredentialDetails(username);					
						
						//request.setAttribute("email", loginBean.getEmail());
						request.setAttribute("email", "");
						request.setAttribute("memberId", memberId);
						request.setAttribute("kycUploadFlag", kycUploadFlag);
						
						ModelAndView mv = new ModelAndView("index");

						mv.addObject("name", name);

						mv.addObject("USDAccountBalance", USDAccountBalance);
						mv.addObject("accountInfo", accountInfo);
						return mv;

					}

				} else {

					return new ModelAndView("index");
				}
				  
				
				
			
			}else {
				
				return new ModelAndView("login");
			} 
			  
		  }
		/**
		 * @throws SQLException **************************/
		@RequestMapping(value = "/cryptoLoadInitate", method = RequestMethod.POST)
		@ResponseBody
		public String cryptoLoadInitate(
				@RequestParam("DepositCurrencyCode") String DepositCurrencyCode,
				@RequestParam("DepositAmount") String DepositAmount,
				@RequestParam("SellingCurrencyCode") String SellingCurrencyCode) throws JSONException, SQLException {
			Map<String,String> param=new HashMap<String,String>();
			System.out.println("DepositCurrencyCode account is: " + DepositCurrencyCode);
			JSONObject jsonResponse = new JSONObject();
			CryptoProcessing MoneyTransferToAnotherAcc = new CryptoProcessing();
			String response = MoneyTransferToAnotherAcc.cryptoLoadQuote(DepositAmount,DepositCurrencyCode,SellingCurrencyCode);
			if(response!=null)
			{
				CryptoProcessing CryptoLoadInitate = new CryptoProcessing();
				//JSONObject jsonResponse = new JSONObject();
				jsonResponse = CryptoLoadInitate.cryptoLoadInitate(DepositAmount,DepositCurrencyCode,SellingCurrencyCode,email, resultSet);
				jsonResponse.put("QuoteAmount",response);
				//param.put("QuoteAmount",response);
				/*param.put("DateCreate",jsonResponse.getString("DateCreate"));
				param.put("RequestedCurrencyCode",jsonResponse.getString("RequestedCurrencyCode"));
				param.put("SourceCurrencyCode",jsonResponse.getString("SourceCurrencyCode"));
				param.put("TransactionRequestID",jsonResponse.getString("TransactionRequestID"));
				param.put("TransactionRequestStatus",jsonResponse.getString("TransactionRequestStatus"));
				param.put("TransactionRequestType",jsonResponse.getString("TransactionRequestType"));
				System.out.println("Test"+param.toString());*/
			}
			return jsonResponse.toString();
		}
			/****************************/
			@RequestMapping(value = "/cryptoLoadStatus", method = RequestMethod.POST)
			@ResponseBody
			public String cryptoLoadStatus(
					@RequestParam("TransactionRequestID") String TransactionRequestID) throws JSONException {
				Map<String,String> param=new HashMap<String,String>();
				System.out.println("TransactionRequestID account is: " + TransactionRequestID);
				JSONObject jsonResponse = new JSONObject();
				CryptoProcessing cryptoLoadStatus = new CryptoProcessing();
				 jsonResponse = cryptoLoadStatus.cryptoLoadStatus(TransactionRequestID);
				if(jsonResponse!=null)
				{
					//CryptoProcessing CryptoLoadInitate = new CryptoProcessing();
					//JSONObject jsonResponse = new JSONObject();
				//	jsonResponse = CryptoLoadInitate.cryptoLoadInitate(DepositAmount,DepositCurrencyCode,SellingCurrencyCode);
				//	jsonResponse.put("QuoteAmount",response);
					//param.put("QuoteAmount",response);
					/*param.put("DateCreate",jsonResponse.getString("DateCreate"));
					param.put("RequestedCurrencyCode",jsonResponse.getString("RequestedCurrencyCode"));
					param.put("SourceCurrencyCode",jsonResponse.getString("SourceCurrencyCode"));
					param.put("TransactionRequestID",jsonResponse.getString("TransactionRequestID"));
					param.put("TransactionRequestStatus",jsonResponse.getString("TransactionRequestStatus"));
					param.put("TransactionRequestType",jsonResponse.getString("TransactionRequestType"));
					System.out.println("Test"+param.toString());*/
				}
				return jsonResponse.toString();
		}
	/*******************************************Rypto Transcation History**************************************/
			@RequestMapping(value = "/cryptoLoadHistroy", method = RequestMethod.POST)
			@ResponseBody
			public String cryptoLoadHistroy() throws JSONException {
				Map<String,String> param=new HashMap<String,String>();
				System.out.println("Email account is: " + email);
				JSONObject jsonResponse = new JSONObject();
				CryptoProcessing cryptoLoadHistory = new CryptoProcessing();
				 jsonResponse = cryptoLoadHistory.cryptoLoadHistory(email);
				 System.out.println("res"+jsonResponse.getString("TransactionList"));
				return jsonResponse.getString("TransactionList");
		}
			/*******************************************CLX Card info**************************************/
			 @RequestMapping(value = "/getCardInfo", method = RequestMethod.POST)
				@ResponseBody
				public String getCardInfo() throws SQLException, JSONException {
				  
				 // String clxCardPin = pin;
				  
				  String msg = null;
				  String clxMemberId = null;
				  String accountNo = null;
				  String expMonth = null;
				  String expYear = null;
				  String validThru = null; 
				  String secCode = null;
				  
				  if(memberId == null) {
					  
					  msg = "Please complete your profile first!";
					  
				  }else {
					  
					  GetUserContactDetailsAPI getUserContactDetailsAPI = new GetUserContactDetailsAPI();
					  String response = getUserContactDetailsAPI.getUserContactDetails(memberId);
					  
					  if(response.contains("OK")) {
						  
						  
						  JSONObject jsonResponse = new JSONObject(response);
						  JSONObject userData = jsonResponse.getJSONObject("user_data");
						  
						  clxMemberId = userData.getString("CLX_MemberID");
						  
						  if(clxMemberId == null || clxMemberId.trim().length()==0) {
							  
							  msg = "Your card has not been created yet";
						  }else {
							  
							  GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
							  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
							  
							  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
							  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
							  
							  JSONObject account = clxAccDetails.getJSONObject(0);
							  accountNo = account.getString("AccountID");
							  
							  System.out.println("accountNo is: " + accountNo);
							  
							  GetCardInformation getCardInformation = new GetCardInformation();
							  String cardinfo = getCardInformation.clxCardInformationAPI(clxMemberId, accountNo);
							  
							  JSONObject cardInfoJson = new JSONObject(cardinfo);
							  
							  expMonth = cardInfoJson.getString("Month");
							  expYear = cardInfoJson.getString("Year");
							  
							  validThru = expMonth + expYear.substring(2);
							  secCode = cardInfoJson.getString("SecurityCode");
							  
							  
							  
							  msg = cardInfoJson.toString();
							  
						  }
						  
						  
					  }else {
						  
						  
						  msg = "Some internal error occurs there!";
						  
					  }
					  
				  }
				  
				  
				  return msg;
			  }
			 /*******************************************CLX Card Load**************************************/
			 @RequestMapping(value = "/clxcardload", method = RequestMethod.POST)
				@ResponseBody
				public String ClxCardLoad(@RequestParam("Amount") String amount) throws SQLException, JSONException {
				  
				 // String clxCardPin = pin;
				  
				  String msg = null;
				  String clxMemberId = null;
				  String accountNo = null;
				 // String amount = null;
				  String expYear = null;
				  String validThru = null; 
				  String secCode = null;
				  
				  if(memberId == null) {
					  
					  msg = "Please complete your profile first!";
					  
				  }else {
					  
					  GetUserContactDetailsAPI getUserContactDetailsAPI = new GetUserContactDetailsAPI();
					  String response = getUserContactDetailsAPI.getUserContactDetails(memberId);
					  
					  if(response.contains("OK")) {
						  
						  
						  JSONObject jsonResponse = new JSONObject(response);
						  JSONObject userData = jsonResponse.getJSONObject("user_data");
						  
						  clxMemberId = userData.getString("CLX_MemberID");
						  
						  if(clxMemberId == null || clxMemberId.trim().length()==0) {
							  
							  msg = "Your card has not been created yet";
						  }else {
							  
							  GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
							  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
							  
							  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
							  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
							  
							  JSONObject account = clxAccDetails.getJSONObject(0);
							  accountNo = account.getString("AccountID");
							  
							  System.out.println("accountNo is: " + accountNo);
							  
							  LoadCard loadclxcard = new LoadCard();
							  String cardinfo = loadclxcard.clxCardLoad(clxMemberId, accountNo,email,amount);
							  
							  JSONObject cardload = new JSONObject(cardinfo);
							  
							  System.out.println("card load"+cardload);
							  
							  
							  msg = cardload.toString();
							  
						  }
						  
						  
					  }else {
						  
						  
						  msg = "Some internal error occurs there!";
						  
					  }
					  
				  }
				  
				  
				  return msg;
			  }
			 
			 /*******************************************signInWhileSignUp
			 * @throws SQLException ****************************/
			 public ModelAndView signInWhileSignUp(AuthenticationRequest authenticationRequest, Model model) throws SQLException {
					AuthenticationResultType authenticationResult = null;
					cognitoClient = getAmazonCognitoIdentityClient();

					final Map<String, String> authParams = new HashMap();
					authParams.put("USERNAME", authenticationRequest.getUsername());
					authParams.put("PASSWORD", authenticationRequest.getPassword());
					// authParams.put("PHONE_NUMBER", "+919051623419");
					// authParams.put("SECRET_HASH",
					// "fbto0hp26njo2kahi7o4i0jv79o5idh0od3kgvicpbclgstdvjl");
					getUserInfo(authenticationRequest.getUsername());

					final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
					authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withClientId(cognitoConfig.getClientId())
							.withUserPoolId(cognitoConfig.getUserPoolId()).withAuthParameters(authParams);
					try {
						result = cognitoClient.adminInitiateAuth(authRequest);
						
						if (result.getChallengeName() != null) {
							// If the challenge is required new Password validates if it has the new
							// password variable.
							if ("NEW_PASSWORD_REQUIRED".equals(result.getChallengeName())) {

								// we still need the username
								final Map<String, String> challengeResponses = new HashMap();
								challengeResponses.put("USERNAME", authenticationRequest.getUsername());
								challengeResponses.put("PASSWORD", authenticationRequest.getPassword());
								// add the new password to the params map
								challengeResponses.put("NEW_PASSWORD", password);
								// populate the challenge response
								final AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest();
								request.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
										.withChallengeResponses(challengeResponses).withClientId(cognitoConfig.getClientId())
										.withUserPoolId(cognitoConfig.getUserPoolId()).withSession(result.getSession());

								resultChallenge = cognitoClient.adminRespondToAuthChallenge(request);
								authenticationResult = resultChallenge.getAuthenticationResult();
								accessToken = authenticationResult.getAccessToken();
								GetUserAttributeVerificationCodeRequest codeRequestEmail = new GetUserAttributeVerificationCodeRequest();
								codeRequestEmail.withAccessToken(accessToken)
								.withAttributeName("email");
								
								cognitoClient.getUserAttributeVerificationCode(codeRequestEmail);
								
								GetUserAttributeVerificationCodeRequest codeRequestPhone = new GetUserAttributeVerificationCodeRequest();
								codeRequestPhone.withAccessToken(accessToken)
								.withAttributeName("email")
								.withAttributeName("phone_number");
								cognitoClient.getUserAttributeVerificationCode(codeRequestPhone);

								ModelAndView modelAndView = new ModelAndView("verificationEmailAndPhone");
							      modelAndView.addObject("logo", resultSet.getString("logo_link"));
									modelAndView.addObject("dashBoardName", resultSet.getString("DashboardName"));
							      return modelAndView;
								
							}

							else if ("SMS_MFA".equals(result.getChallengeName())) {
								ModelAndView modelAndView = new ModelAndView("verificationEmailAndPhone");
							      modelAndView.addObject("logo", resultSet.getString("logo_link"));
									modelAndView.addObject("dashBoardName", resultSet.getString("DashboardName"));
							      return modelAndView;
							}

						}

					} catch (Exception e) {
						model.addAttribute("error", "Invalid credentials");
					}

					ModelAndView modelAndView = new ModelAndView("login");
				      modelAndView.addObject("logo", resultSet.getString("logo_link"));
						modelAndView.addObject("dashBoardName", resultSet.getString("DashboardName"));
				      return modelAndView;

				}
			 /*************************************************END signInWhileSignUp****************************/
/*****************************Email and Phone Verification and Reset Password************/
				@RequestMapping(value = "/verificationEmailAndPhone", method = RequestMethod.POST)
				public ModelAndView userVerificationPhnoeAndEmail(@ModelAttribute("verification") VerificationEmailAndPhone emailAndPhone,
						HttpServletRequest request, Model model) throws SQLException, JSONException, ParseException
				{
					AWSCognitoIdentityProvider cognitoClient =  getAmazonCognitoIdentityClient();
					
					VerifyUserAttributeRequest attributeRequestEmail = new VerifyUserAttributeRequest()
							.withAccessToken(accessToken)
							.withAttributeName("email")
							.withCode(emailAndPhone.getEmail());
					cognitoClient.verifyUserAttribute(attributeRequestEmail);
					
					 VerifyUserAttributeRequest attributeRequestPhoneNo = new VerifyUserAttributeRequest()
							.withAccessToken(accessToken)
							.withAttributeName("phone_number")
							.withCode(emailAndPhone.getPhone());
					cognitoClient.verifyUserAttribute(attributeRequestPhoneNo); 
					 MFAOptionType mfaOptionType = new MFAOptionType();
				       mfaOptionType.withAttributeName("phone_number");
				       mfaOptionType.withDeliveryMedium(DeliveryMediumType.SMS);
				       
				       AdminSetUserSettingsRequest adminSetUserSettingsRequest = new AdminSetUserSettingsRequest()
				    		   .withMFAOptions(mfaOptionType)
				    		   .withUsername(username)
			                   .withUserPoolId(cognitoConfig.getUserPoolId());
				 
				       cognitoClient.adminSetUserSettings(adminSetUserSettingsRequest);
					String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
//					String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex("", loginBean.getPassword());

				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				JSONObject jsonreponse = new JSONObject(response);

				if (((JSONObject) jsonreponse.get("Status")).get("Code").toString().equals("0")) {

					JSONObject json = new JSONObject(response);
					JSONObject UserData = json.getJSONObject("UserData");

					// username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
					username = UserData.getString("UserName_UserID");

					firstname = UserData.getString("User_FirstName");
					lastName = UserData.getString("User_LastName");

					name = firstname + " " + lastName;
					email = UserData.getString("Email_ID");
					password = UserData.getString("Password");
					memberId = UserData.getString("MemberID");
					birthDate = UserData.getString("BirthDate");
					aptNo = UserData.getString("NumApt");
					streetNo = UserData.getString("NumStreet");
					street = UserData.getString("Street");
					city = UserData.getString("City");
					state = UserData.getString("State");
					postcode = UserData.getString("ZipCode");
					countryId = UserData.getString("CountryId");

					kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");

					System.out.println("After login username is: " + username);

					USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);

					if (USDAccountBalance.contains("Some internal errors occured there!")) {

						ModelAndView mv = new ModelAndView("redirect:mfaVerification");
						mv.addObject("name", name);
						mv.addObject("USDAccountBalance", USDAccountBalance);
						mv.addObject("accountInfo", accountInfo);
						return mv;

					} else {

						accountInfo = getUserAccountsList.accountDetails(username);
						System.out.println("accountInfo is: " + accountInfo);

						name = getUserCredentialsdao.userCredentialDetails(username);

						// request.setAttribute("email", loginBean.getEmail());
						request.setAttribute("email", "");
						request.setAttribute("memberId", memberId);
						request.setAttribute("kycUploadFlag", kycUploadFlag);

						ModelAndView mv = new ModelAndView("redirect:mfaVerification");
						mv.addObject("name", name);
						mv.addObject("USDAccountBalance", USDAccountBalance);
						mv.addObject("accountInfo", accountInfo);
						return mv;

					}

				} else {

					ModelAndView mv = new ModelAndView("redirect:mfaVerification");
					mv.addObject("name", name);
					mv.addObject("USDAccountBalance", USDAccountBalance);
					mv.addObject("accountInfo", accountInfo);
					return mv;
				}
				}
				
				@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
				public ModelAndView changePassword() {

					return new ModelAndView("changePassword");

				}
				
				@RequestMapping(value = "/setChangePassword", method = RequestMethod.POST)
				public ModelAndView setChangePassword(@ModelAttribute("changePassword") ChangePasswordBean changePasswordBean,
						HttpServletRequest request, Model model) throws Exception
				{
					AWSCognitoIdentityProvider cognitoClient =  getAmazonCognitoIdentityClient();
					
					ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest()
							.withAccessToken(accessToken)
							.withPreviousPassword(changePasswordBean.getOldPassword())
							.withProposedPassword(changePasswordBean.getNewPassword());
					
					cognitoClient.changePassword(changePasswordRequest);
					return new ModelAndView("index");
				}
 /**************************Forgot Password***************/

				@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
				public ModelAndView forgotPassword() {

					return new ModelAndView("forgotPassword");

				}

				@RequestMapping(value = "/forgotpasswordUsername", method = RequestMethod.POST)
				public ModelAndView setForgotpassword(
						@ModelAttribute("forgotpasswordUsername") ForgotPasswordUserNameBean forgotPasswordBean,
						HttpServletRequest request, Model model) {

					AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

					username = forgotPasswordBean.getUserName();
					ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
							.withClientId(cognitoConfig.getClientId())
							// .withSecretHash("8gslPs1sidr4nFEFnAbX19HZaxZJwncg6LLLUQ7j")
							.withUsername(forgotPasswordBean.getUserName());
					cognitoClient.forgotPassword(forgotPasswordRequest);

					return new ModelAndView("forgotPasswordSet");
				}

				@RequestMapping(value = "/setForgotpassword", method = RequestMethod.POST)
				public ModelAndView setForgotpassword(@ModelAttribute("forgotpasswordBean") ForgotPasswordBean forgotPasswordBean,
						HttpServletRequest request, Model model) {

//					       ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
//					    		   .withClientId(cognitoConfig.getClientId())
//					    		   //.withSecretHash("8gslPs1sidr4nFEFnAbX19HZaxZJwncg6LLLUQ7j")
//					    		   .withUsername(username);

					AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
					ConfirmForgotPasswordRequest confirmForgotPasswordRequest = new ConfirmForgotPasswordRequest()
							.withClientId(cognitoConfig.getClientId()).withConfirmationCode(forgotPasswordBean.getMfaCode())
							.withUsername(username).withPassword(forgotPasswordBean.getPassword());
					ConfirmForgotPasswordResult confirmForgotPasswordResult = cognitoClient
							.confirmForgotPassword(confirmForgotPasswordRequest);
					return new ModelAndView("login");

				}
				
				@ExceptionHandler(Exception.class)
				public ModelAndView handleSQLException(HttpServletRequest request, Exception ex){
					
					ModelAndView andView = new ModelAndView("404");
					andView.addObject("message", ex.getMessage());
					return andView;
				}
				
				@RequestMapping(value = "/mfaVerification", method = RequestMethod.GET) 
				public ModelAndView userMfaAuthenticationGet(@ModelAttribute("mfa") MfaBean mfaBean, HttpServletRequest request, Model model) throws SQLException, JSONException, ParseException 
				{ 
					
					LoginDao loginDao = new LoginDao();
					String clxMemberId="0";
					String cardbal1="0.00";
					String cardstatus="IN";
					String availablebalance="0.00";
					if(accessToken != null)
					{
			
						
						
						//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
						
						String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
//						String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex("", loginBean.getPassword());
						
						System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
						System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
						//JSONParser parser = new JSONParser();
						JSONObject jsonreponse = new JSONObject(response);
						
						
						if (((JSONObject)jsonreponse.get("Status")).get("Code").toString().equals("0")) {
							
							JSONObject json = new JSONObject(response);
							JSONObject UserData = json.getJSONObject("UserData");
						
							//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
							username = UserData.getString("UserName_UserID");
							
							firstname = UserData.getString("User_FirstName");
							lastName = UserData.getString("User_LastName");
							
							name = firstname + " " + lastName;
							email = UserData.getString("Email_ID");
							password = UserData.getString("Password");
							memberId = UserData.getString("MemberID");
							birthDate = UserData.getString("BirthDate");
							aptNo = UserData.getString("NumApt");
							streetNo = UserData.getString("NumStreet");
							street = UserData.getString("Street");
							city = UserData.getString("City");
							state = UserData.getString("State");
							postcode = UserData.getString("ZipCode");
							countryId = UserData.getString("CountryId");
							clxMemberId=UserData.getString("CLX_MemberID");
							kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
							/**************fetching card Balance*************************/
							System.out.println("size"+clxMemberId.length());
							if(clxMemberId.length()>2)
							{
								 GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
								  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
								  
								  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
								  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
								  JSONObject account = clxAccDetails.getJSONObject(0);
									 String accountNo = account.getString("AccountID");
								  
								 
								 /***************fetching card status**************/
		                        CardStatus clxstatusresponse=new CardStatus();
								String cardstatusresponse=clxstatusresponse.clxCardStatusAPI(clxMemberId, accountNo);
								JSONObject cardstat1 = new JSONObject(cardstatusresponse);
								String cardstatus1=cardstat1.get("CardStatus").toString();
								JSONObject cardstat2 = new JSONObject(cardstatus1);
								cardstatus=cardstat2.get("Code").toString();
								System.out.println("Card Status"+cardstatus);
								
								 /***************fetching card balance**************/
								 CardBalance clxbalanceresponse=new CardBalance();
								 String cardbalance=clxbalanceresponse.clxCardBalanceAPI(clxMemberId, accountNo);
								 System.out.println("Card Balance is 4: " + cardbalance);
								 JSONObject cardbal = new JSONObject(cardbalance);
								  System.out.println("Card Balance is2: " + cardbal.toString());
								   cardbal1=cardbal.get("AccountBalance").toString();
								   JSONObject cardba2 = new JSONObject(cardbal1);
								   availablebalance=cardba2.get("AvailableBalance").toString();
								   System.out.println("Card Balance is3: " + cardba2.get("AvailableBalance").toString());
								   System.out.println("Card Balance is1: " + cardbal1);
								   System.out.println("Card Balance is2: " + cardbal.toString());
							}
							
							System.out.println("After login username is: " + username);

							USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);

							if (USDAccountBalance.contains("Some internal errors occured there!")) {

								return new ModelAndView("index");

							} else {

								accountInfo = getUserAccountsList.accountDetails(username);
								System.out.println("accountInfo is: " + accountInfo);
								
								//name = getUserCredentialsdao.userCredentialDetails(username);					
								
								request.setAttribute("email", email);
								//request.setAttribute("email", "");
								request.setAttribute("memberId", memberId);
								request.setAttribute("kycUploadFlag", kycUploadFlag);
								request.setAttribute("clxMemberID",  clxMemberId);
								request.setAttribute("cardbalance",  availablebalance);
								request.setAttribute("cardstatus",  cardstatus);
								
								ModelAndView mv = new ModelAndView("index");

								mv.addObject("name", name);

								mv.addObject("USDAccountBalance", USDAccountBalance);
								mv.addObject("accountInfo", accountInfo);
								return mv;

							}

						} else {

							return new ModelAndView("index");
						}
						  
						
						
					
					}
					AuthenticationResultType authenticationResultType = mfaAuthentication(mfaBean, model);
					accessToken = authenticationResultType.getAccessToken();
					
					//String response = loginDao.userLogin(loginBean);
					
					//System.out.println("LoginDao class Response is: " + response);
					
					if(authenticationResultType != null) {	
						
						
						//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
						
						String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
//						String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex("", loginBean.getPassword());
						
						System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
						System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
						//JSONParser parser = new JSONParser();
						JSONObject jsonreponse = new JSONObject(response);
						
						
						if (((JSONObject)jsonreponse.get("Status")).get("Code").toString().equals("0")) {
							
							JSONObject json = new JSONObject(response);
							JSONObject UserData = json.getJSONObject("UserData");
						
							//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
							username = UserData.getString("UserName_UserID");
							
							firstname = UserData.getString("User_FirstName");
							lastName = UserData.getString("User_LastName");
							
							name = firstname + " " + lastName;
							email = UserData.getString("Email_ID");
							password = UserData.getString("Password");
							memberId = UserData.getString("MemberID");
							birthDate = UserData.getString("BirthDate");
							aptNo = UserData.getString("NumApt");
							streetNo = UserData.getString("NumStreet");
							street = UserData.getString("Street");
							city = UserData.getString("City");
							state = UserData.getString("State");
							postcode = UserData.getString("ZipCode");
							countryId = UserData.getString("CountryId");
							clxMemberId=UserData.getString("CLX_MemberID");
							kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
							/**************fetching card Balance*************************/
							System.out.println("size"+clxMemberId.length());
							if(clxMemberId.length()>2)
							{
								 GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
								  String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);
								  
								  JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
								  JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
								  JSONObject account = clxAccDetails.getJSONObject(0);
									 String accountNo = account.getString("AccountID");
								  
								 
								 /***************fetching card status**************/
		                        CardStatus clxstatusresponse=new CardStatus();
								String cardstatusresponse=clxstatusresponse.clxCardStatusAPI(clxMemberId, accountNo);
								JSONObject cardstat1 = new JSONObject(cardstatusresponse);
								String cardstatus1=cardstat1.get("CardStatus").toString();
								JSONObject cardstat2 = new JSONObject(cardstatus1);
								cardstatus=cardstat2.get("Code").toString();
								
								 /***************fetching card balance**************/
								 CardBalance clxbalanceresponse=new CardBalance();
								 String cardbalance=clxbalanceresponse.clxCardBalanceAPI(clxMemberId, accountNo);
								 System.out.println("Card Balance is 4: " + cardbalance);
								 JSONObject cardbal = new JSONObject(cardbalance);
								  System.out.println("Card Balance is2: " + cardbal.toString());
								   cardbal1=cardbal.get("AccountBalance").toString();
								   JSONObject cardba2 = new JSONObject(cardbal1);
								   availablebalance=cardba2.get("AvailableBalance").toString();
								   System.out.println("Card Balance is3: " + cardba2.get("AvailableBalance").toString());
								   System.out.println("Card Balance is1: " + cardbal1);
								   System.out.println("Card Balance is2: " + cardbal.toString());
							}
							
							System.out.println("After login username is: " + username);

							USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);

							if (USDAccountBalance.contains("Some internal errors occured there!")) {

								return new ModelAndView("index");

							} else {

								accountInfo = getUserAccountsList.accountDetails(username);
								System.out.println("accountInfo is: " + accountInfo);
								
								//name = getUserCredentialsdao.userCredentialDetails(username);					
								
								request.setAttribute("email", email);
								//request.setAttribute("email", "");
								request.setAttribute("memberId", memberId);
								request.setAttribute("kycUploadFlag", kycUploadFlag);
								request.setAttribute("clxMemberID",  clxMemberId);
								request.setAttribute("cardbalance",  availablebalance);
								request.setAttribute("cardstatus",  cardstatus);
								
								ModelAndView mv = new ModelAndView("index");

								mv.addObject("name", name);

								mv.addObject("USDAccountBalance", USDAccountBalance);
								mv.addObject("accountInfo", accountInfo);
								return mv;

							}

						} else {

							return new ModelAndView("index");
						}
						  
						
						
					}
					return new ModelAndView("login");
					  
				  }
				@RequestMapping(value = "/verificationEmailAndPhone", method = RequestMethod.GET) 
				public ModelAndView userVerificationRefresh(@ModelAttribute("mfa") MfaBean mfaBean, HttpServletRequest request, Model model) throws SQLException, JSONException, ParseException 
				{ 

				LoginDao loginDao = new LoginDao();
				String clxMemberId="0";
				String cardbal1="0.00";
				String cardstatus="IN";
				String availablebalance="0.00";
				if(accessToken != null)
				{



				//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());

				String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex(email, password);
//					String response = insertUsercredentialsToDatabase.insertLoginCredentialsToEmailIndex("", loginBean.getPassword());

				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				System.out.println("InsertUserCredentialToDatabase class Response is: " + response);
				//JSONParser parser = new JSONParser();
				JSONObject jsonreponse = new JSONObject(response);


				if (((JSONObject)jsonreponse.get("Status")).get("Code").toString().equals("0")) {

				JSONObject json = new JSONObject(response);
				JSONObject UserData = json.getJSONObject("UserData");

				//username = viewUserDetailsDao.getUserDetails(loginBean.getEmail());
				username = UserData.getString("UserName_UserID");

				firstname = UserData.getString("User_FirstName");
				lastName = UserData.getString("User_LastName");

				name = firstname + " " + lastName;
				email = UserData.getString("Email_ID");
				password = UserData.getString("Password");
				memberId = UserData.getString("MemberID");
				birthDate = UserData.getString("BirthDate");
				aptNo = UserData.getString("NumApt");
				streetNo = UserData.getString("NumStreet");
				street = UserData.getString("Street");
				city = UserData.getString("City");
				state = UserData.getString("State");
				postcode = UserData.getString("ZipCode");
				countryId = UserData.getString("CountryId");
				clxMemberId=UserData.getString("CLX_MemberID");
				kycUploadFlag = UserData.getString("KYC_Uploaded_Flag_Y");
				/**************fetching card Balance*************************/
				System.out.println("size"+clxMemberId.length());
				if(clxMemberId.length()>2)
				{
				GetCLXAccountDetailsAPI getCLXAccountDetailsAPI = new GetCLXAccountDetailsAPI();
				String accDetailsRes = getCLXAccountDetailsAPI.getCLXAccountDetails(email, clxMemberId);

				JSONObject jsonAccDetailsRes = new JSONObject(accDetailsRes);
				JSONArray clxAccDetails = jsonAccDetailsRes.getJSONArray("Account");
				JSONObject account = clxAccDetails.getJSONObject(0);
				String accountNo = account.getString("AccountID");


				/***************fetching card status**************/
				CardStatus clxstatusresponse=new CardStatus();
				String cardstatusresponse=clxstatusresponse.clxCardStatusAPI(clxMemberId, accountNo);
				JSONObject cardstat1 = new JSONObject(cardstatusresponse);
				String cardstatus1=cardstat1.get("CardStatus").toString();
				JSONObject cardstat2 = new JSONObject(cardstatus1);
				cardstatus=cardstat2.get("Code").toString();
				System.out.println("Card Status"+cardstatus);

				/***************fetching card balance**************/
				CardBalance clxbalanceresponse=new CardBalance();
				String cardbalance=clxbalanceresponse.clxCardBalanceAPI(clxMemberId, accountNo);
				System.out.println("Card Balance is 4: " + cardbalance);
				JSONObject cardbal = new JSONObject(cardbalance);
				System.out.println("Card Balance is2: " + cardbal.toString());
				cardbal1=cardbal.get("AccountBalance").toString();
				JSONObject cardba2 = new JSONObject(cardbal1);
				availablebalance=cardba2.get("AvailableBalance").toString();
				System.out.println("Card Balance is3: " + cardba2.get("AvailableBalance").toString());
				System.out.println("Card Balance is1: " + cardbal1);
				System.out.println("Card Balance is2: " + cardbal.toString());
				}

				System.out.println("After login username is: " + username);

				USDAccountBalance = viewUSDWalletBalance.getUSDWalletBalance(username);

				if (USDAccountBalance.contains("Some internal errors occured there!")) {

					ModelAndView mv = new ModelAndView("redirect:mfaVerification");
					mv.addObject("name", name);
					mv.addObject("USDAccountBalance", USDAccountBalance);
					mv.addObject("accountInfo", accountInfo);
					return mv;

				} else {

				accountInfo = getUserAccountsList.accountDetails(username);
				System.out.println("accountInfo is: " + accountInfo);

				//name = getUserCredentialsdao.userCredentialDetails(username);	

				request.setAttribute("email", email);
				//request.setAttribute("email", "");
				request.setAttribute("memberId", memberId);
				request.setAttribute("kycUploadFlag", kycUploadFlag);
				request.setAttribute("clxMemberID", clxMemberId);
				request.setAttribute("cardbalance", availablebalance);
				request.setAttribute("cardstatus", cardstatus);

				ModelAndView mv = new ModelAndView("redirect:mfaVerification");
				mv.addObject("name", name);
				mv.addObject("USDAccountBalance", USDAccountBalance);
				mv.addObject("accountInfo", accountInfo);
				return mv;

				}

				} else {

					ModelAndView mv = new ModelAndView("redirect:mfaVerification");
					mv.addObject("name", name);
					mv.addObject("USDAccountBalance", USDAccountBalance);
					mv.addObject("accountInfo", accountInfo);
					return mv;
				}




				}

				return new ModelAndView("login");

				}
				
				
				@RequestMapping(value = "/getUserDetails", method = RequestMethod.POST)
				@ResponseBody
				public String getUserDetails() throws JSONException {
					
					
					String authJsonResponse = null;
					String authUserResponse = null;
					try {

						URL url = new URL("http://18.206.169.158:8081/oauth/token");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoOutput(true);
						conn.setRequestMethod("POST");
//						conn.addRequestProperty("client_id", "swagger-client");
//						conn.addRequestProperty("client_secret", "swagger-secret");
						BASE64Encoder enc = new sun.misc.BASE64Encoder();
					      String userpassword = "swagger-client" + ":" + "swagger-secret";
					      String encodedAuthorization = enc.encode( userpassword.getBytes() );
						conn.setRequestProperty("Authorization", "Basic "+encodedAuthorization);
						conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						
						String urlParameters  = "username="+email+"&password="+password+"&grant_type=password";
									
						
						

						OutputStream os = conn.getOutputStream();
						os.write(urlParameters.getBytes());
						os.flush();
						

						BufferedReader br = new BufferedReader(new InputStreamReader(
								(conn.getInputStream())));
						

						
						while ((authJsonResponse = br.readLine())!= null) {	
							System.out.println(authJsonResponse);
							
							authUserResponse = authJsonResponse;	
							
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					JSONObject jsonUserResponse = new JSONObject(authUserResponse);
					String accessToken = jsonUserResponse.getString("access_token");
					
					String response = null;
					
					String registerJsonResponse = null;
					String registerUserResponse = null;
					try {

						URL url = new URL("http://18.206.169.158:8081/stylopay_wallet/api/v1/CommonServices/GetUser_Details");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoOutput(true);
						conn.setRequestMethod("GET");
//						conn.setRequestProperty("client_id", "swagger-client");
//						conn.setRequestProperty("client_secret", "swagger-secret");
						conn.setRequestProperty("Content-Type", "application/json");
						conn.setRequestProperty("Authorization",  "Bearer "+accessToken);
						
//						BASE64Encoder enc = new sun.misc.BASE64Encoder();
//					      String userpassword = "swagger-client" + ":" + "swagger-secret";
//					      String encodedAuthorization = enc.encode( userpassword.getBytes() );
//						conn.setRequestProperty("Authorization", "Basic "+encodedAuthorization);
						
//						System.out.println(conn.getHeaderFields());
//						System.out.println(conn.getRequestProperties());
						
						
									
						
						
						

						BufferedReader br = new BufferedReader(new InputStreamReader(
								(conn.getInputStream())));
						

						
						while ((registerJsonResponse = br.readLine())!= null) {	
							System.out.println(registerJsonResponse);
							
							response = registerJsonResponse;	
							
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					return response;
				}
				

				@RequestMapping(value = "/updatePersonalInfo", method = RequestMethod.POST)
								@ResponseBody
								public String updatePersonalInfo(@RequestParam("updatedEmail") String updatedEmail) throws SQLException, JSONException {
								  
								 String newEmail = updatedEmail;
								 
								 System.out.println("newEmail: " + newEmail);
								  
								 UpdatePersonalInfo updatePersonalInfo = new UpdatePersonalInfo();
								 String updatePersonalInfoRes = updatePersonalInfo.updatePersonalDetails(email, newEmail);
								 
								  return updatePersonalInfoRes;
							  }
							 
							 
							 @RequestMapping(value = "/updateAddressInfo", method = RequestMethod.POST)
								@ResponseBody
								public String updateAddressInfo(@RequestParam("updateAddress1") String updateAddress1, @RequestParam("updateAddress2") String updateAddress2, @RequestParam("updateCity") String updateCity, @RequestParam("updateZip") String updateZip, @RequestParam("updateState") String updateState, @RequestParam("updateCountry") String updateCountry, @RequestParam("updateBillingAddress1") String updateBillingAddress1, @RequestParam("updateBillingAddress2") String updateBillingAddress2, @RequestParam("updateBillingCity") String updateBillingCity, @RequestParam("updateBillingZip") String updateBillingZip, @RequestParam("updateBillingState") String updateBillingState, @RequestParam("updateBillingCountry") String updateBillingCountry) throws SQLException, JSONException {
								  
								 String newAddress1 = updateAddress1;
								 String newAddress2 = updateAddress2;
								 String newCity = updateCity;
								 String newZip = updateZip;
								 String newState = updateState;
								 String newCountryId = updateCountry;
								 
								 String newBillingAddress1 = updateBillingAddress1;
								 String newBillingAddress2 = updateBillingAddress2;
								 String newBillingCity = updateBillingCity;
								 String newBillingZip = updateBillingZip;
								 String newBillingState = updateBillingState;
								 String newBillingCountryId = updateBillingCountry;
								 
								 System.out.println("newAddress1: " + newAddress1);
								  
								 UpdateAddressInfo updateAddressInfo = new UpdateAddressInfo();
								 String updateAddressInfoRes = updateAddressInfo.updateAddressDetails(email, newAddress1, newAddress2, newCity, newZip, newState, newCountryId, newBillingAddress1, newBillingAddress2, newBillingCity, newBillingZip, newBillingState, newBillingCountryId);
								 
								  return updateAddressInfoRes;
							  }
 
}


