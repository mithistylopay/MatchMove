function getFromAccInfo(email){
								
								var x = document.getElementById("toAccDropdown").options.length;
								var listOptions = document.getElementById("toAccDropdown");
								
								
								if(x>1){									
									
									listOptions.options.length = 1;									
									
								}								
								
								
								/* var y = document.getElementById("toAccDropdown").options.length;
								
								for(var i = 0; i<y; i++){
									
									var opt = document.getElementById("toAccDropdown").options[i].text;
									alert("opt: " + opt);
									
								}
								alert("now option length is: " + y); */
							
								
								var fromAccInfo = document.getElementById("fundExchangeFromAcc").value;							
								var userEmail = email;
								var array = new Array();
								
								var tempFromAccNo = fromAccInfo;
								var tempUserEmail = userEmail;
								
								$.ajax({
								     type : "POST",
								     url : "/getToAccInfo",
								     data : {fromAccNo:tempFromAccNo, userEmail:tempUserEmail},
								     timeout : 100000,
								     async: false,
								     success : function(data) {			    	 
							  	    	 
								    		 //alert(data);
								    		 var toAcc = String(data);
								    		 var trimToAssString = toAcc.replace(/[&\/\\#+$~%.'":*?<>{}]/g, '');
								    		 //alert(trimToAssString);
								    		 
								    		 array = trimToAssString.split(",");
								    		 
								    		 //alert(array.length);
								    		 //alert("array within ajax: " + array);
								    		 
								    		 //var select = document.getElementById("toAccDropdown");
								    		 //var dropdown = document.getElementById("toAccDropdown");							    		â€‹â€‹â€‹
								    		 
								    	},
								     error : function(e) {
								         console.log("ERROR: ", e);
								         display(e);
								     },
								     done : function(e) {
								         console.log("DONE");
								     } 
								     
								 });
								
								var toAccArray = array;								
								
								var select = document.getElementById("toAccDropdown");	
								document.getElementById("toAccDropdown").value= "";
								
								for(var i = 0; i <toAccArray.length; i++){					
									
									var option = document.createElement("OPTION"),
									txt = document.createTextNode(toAccArray[i].replace("[", "").replace("]", ""));
									
									option.appendChild(txt);
									select.insertBefore(option,select.lastChild);
									
								}
								
							}
							
							
							function fundTransferOwnAcc(){
								
								/* if(fromAccInfo == "" || fromAccInfo == null){
									
									alert("Please choose from account");
									
								}else if(toAccInfo == "" || toAccInfo == null){
									
									alert("Please choose to account");
									
								}else if(exchangedAmount == "" || exchangedAmount == null){
									
									alert("Please enter amount");
									
								}
								 */
								
								/* if((fromAccInfo!=null && fromAccInfo!="") && (toAccInfo!=null && toAccInfo!="") && (exchangedAmount!=null && exchangedAmount!="")){
									
									var confirmBtn = document.getElementById("ownFundTransferBtn");
									alert("hello1");
									confirmBtn.disabled = false;
									
								}else{
									
									alert("hello2");
									confirmBtn.disabled = true;
									
								} */
								
								var fromAccInfo = document.getElementById("fundExchangeFromAcc").value;
								var toAccInfo = document.getElementById("toAccDropdown").value;
								
								var fromAccNoStr = String(fromAccInfo);
								var fromAccNo = fromAccNoStr.substring(0,8);
								var fromAccCurrency = fromAccNoStr.substring(10,13);
								
								var toAccNoStr = String(toAccInfo);
								var toAccNo = toAccNoStr.substring(0,8);
								
								var exchangedAmount = document.getElementById("amountToBeExchange").value;
								
								var tempFromAccNo = fromAccNo;
								var tempToAccNo = toAccNo;
								var tempFromAccCurrency = fromAccCurrency;
								var tempAmount = exchangedAmount;
								
								
								$.ajax({
								     type : "POST",
								     url : "/moneyTransferOwnAccount",
								     data : {fromAccNo:tempFromAccNo, toAccNo:tempToAccNo, fromAccCurrency:fromAccCurrency, amount:tempAmount},
								     timeout : 100000,
								     success : function(data) {							    	 
								   		 
								    		// alert(data);	
								    	 swal(
								        		    'Oops! Something not correct'
								        		    
								        		  );
								    		 
								    		 },
								     error : function(e) {
								         console.log("ERROR: ", e);
								         display(e);
								     },
								     done : function(e) {
								         console.log("DONE");
								     } 
								     
								 });
								
							}
							
			
							function getAccountInfo(){				
								
								
								var userAccountInfo = document.getElementById("userAccountInfo").value;
								var userAccInfo = String(userAccountInfo);
								var currencyType = userAccInfo.substring(10,13);
								console.log(userAccountInfo);						
								
								document.getElementById("currencyType").value = currencyType;
								
								document.getElementById("beneficiaryEmail").value = "";
								document.getElementById("recepientWalletAcc").value = "";
								
								//return currencyType;		
								
							}
							
							function getBeneficiaryEmail(){
								
								var beneficiaryEmail = document.getElementById("beneficiaryEmail").value;							
								var getCurrencyType = document.getElementById("currencyType").value;
								
								//alert(getCurrencyType);
								
								var tempBeneficiaryEmail = beneficiaryEmail;
								var tempCurrencyType = getCurrencyType;								
								
								
								 $.ajax({
								     type : "POST",
								     url : "/moneyTransferOtherAccount",
								     data : {beneficiaryEmailId:tempBeneficiaryEmail, currencyType:tempCurrencyType},
								     timeout : 100000,
								     success : function(data) {							    	 
								   		 
								    		 console.log(beneficiaryEmail); 
								    		 console.log(getCurrencyType);
								    		 
								    		 //alert(data);
								    		 
								    		 var receiverAccount = data;
								    		 
								    		 if(receiverAccount.includes('Invalid Email_ID') || receiverAccount.includes('Email_ID is required'))
							    			 {
							    			 swal(
							    					 'Oops! Account not found! Enter valid account.'
									        		    
									        		  );
							    			 
							    			 return;
							    			 }
								    		 
								    		 document.getElementById("recepientWalletAcc").value = receiverAccount;								    		 
								        
								    		 },
								     error : function(e) {
								         console.log("ERROR: ", e);
								         display(e);
								     },
								     done : function(e) {
								         console.log("DONE");
								     } 
								     
								 });
								
							}
							
							function fundTransferAnotherUser(email){
								
								swal({
									  text: 'Please wait....',
									button : false
									});
								var userAccountInfo = document.getElementById("userAccountInfo").value;
								
								var userAccInfo = String(userAccountInfo);
								var senderAccount = userAccInfo.substring(0,8);
								
								//alert("Sender account is: " + senderAccount);
								
								var beneficiaryEmail = document.getElementById("beneficiaryEmail").value;							
								var receiverAccount = document.getElementById("recepientWalletAcc").value;
								var amount = document.getElementById("transferAmount").value;
								var getCurrencyType = document.getElementById("currencyType").value;
								
								var tempSenderAccount = senderAccount;
								var tempBeneficiaryEmail = beneficiaryEmail;
								var tempReceiverAcc = receiverAccount;
								var tempAmount = amount;
								var tempCurrencyType = getCurrencyType;	
								
								
								if(userAccountInfo==null || userAccountInfo ==""){
									
									swal(
						        		    'Please select sender account'
						        		    
						        		  );									
									
								}else if(beneficiaryEmail==null || beneficiaryEmail==""){
									
									swal(
						        		    'Please enter beneficiary email'
						        		    
						        		  );
																		
								}else if(receiverAccount==null || receiverAccount==""){
									
									swal(
						        		    'Please wait for receiver account to be displayed'
						        		    
						        		  );
									
								}else if(amount==null || amount==""){
									
									swal(
						        		    'Please enter amount'
						        		    
						        		  );
									
								}else if(beneficiaryEmail==email){
									
									swal(
						        		    'Sender email and receiver email should be different!'
						        		    
						        		  );
									
								}
								
								if((userAccountInfo!=null && userAccountInfo!="") && (beneficiaryEmail!=null && beneficiaryEmail!="") && (receiverAccount!=null && receiverAccount!="") && (amount!=null && amount!="") && beneficiaryEmail!=email){
									
									var confirmBtn = document.getElementById("btn");
									confirmBtn.disabled = false;
									
								}else{
									
									confirmBtn.disabled = true;
									
								}
								
								 $.ajax({
								     type : "POST",
								     url : "/senderToReceiverTransfer",
								     data : {senderAcc: tempSenderAccount, beneficiaryEmailId:tempBeneficiaryEmail, beneficiaryAccount:tempReceiverAcc, amount: tempAmount, currencyType:tempCurrencyType},
								     timeout : 100000,
								     success : function(data) {							    	 
								   		 
								    		 console.log(senderAccount); 
								    		 var response = data;
								    		 
								    		 swal.stopLoading();
								    		    swal.close();
								    		 if(response=='success')
								    			 	  
								    		  		 swal(
											        		    'Transfer Successfull'
											        		    
											        		  );
								    		 else
								    			 swal(
										        		    'Oops!! Something Went Wrong '
										        		    
										        		  );						    		 
								    		 
								    		 },
								     error : function(e) {
								         console.log("ERROR: ", e);
								         display(e);
								     },
								     done : function(e) {
								         console.log("DONE");
								     } 
								     
								 });
								 
								 document.getElementById("userAccountInfo").value = "";
								 document.getElementById("beneficiaryEmail").value = "";
								 document.getElementById("recepientWalletAcc").value = "";
								 document.getElementById("transferAmount").value = "";
								 document.getElementById("currencyType").value = "";
								
							}