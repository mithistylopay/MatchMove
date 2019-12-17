function getFromAccountInfo(email){
								
								var x = document.getElementById("toAccountDropdown").options.length;
								var listOptions = document.getElementById("toAccountDropdown");
								
								
								if(x>1){									
									
									listOptions.options.length = 1;									
									
								}								
								
								
								/* var y = document.getElementById("toAccDropdown").options.length;
								
								for(var i = 0; i<y; i++){
									
									var opt = document.getElementById("toAccDropdown").options[i].text;
									alert("opt: " + opt);
									
								}
								alert("now option length is: " + y); */
							
								
								var fromAccInfo = document.getElementById("fundExchangeFromAccount").value;							
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
								
								var select = document.getElementById("toAccountDropdown");	
								document.getElementById("toAccountDropdown").value= "";
								
								for(var i = 0; i <toAccArray.length; i++){					
									
									var option = document.createElement("OPTION"),
									txt = document.createTextNode(toAccArray[i].replace("[", "").replace("]", ""));
									
									option.appendChild(txt);
									select.insertBefore(option,select.lastChild);
									
								}
								
							}
							
							
							function fundTransferOwnAccount(){
								
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
								
								var fromAccInfo = document.getElementById("fundExchangeFromAccount").value;
								var toAccInfo = document.getElementById("toAccountDropdown").value;
								
								var fromAccNoStr = String(fromAccInfo);
								var fromAccNo = fromAccNoStr.substring(0,8);
								var fromAccCurrency = fromAccNoStr.substring(10,13);
								
								var toAccNoStr = String(toAccInfo);
								var toAccNo = toAccNoStr.substring(0,8);
								
								var exchangedAmount = document.getElementById("amountForExchange").value;
								
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
							
			
							function getSenderAccountInfo(){				
								
								
								var userAccountInfo = document.getElementById("senderAccountInfo").value;
								var userAccInfo = String(userAccountInfo);
								var currencyType = userAccInfo.substring(10,13);
								console.log(userAccountInfo);						
								
								document.getElementById("senderAcccurrencyType").value = currencyType;
								
								document.getElementById("recipientEmail").value = "";
								document.getElementById("beneficiaryWalletAcc").value = "";
								
								//return currencyType;		
								
							}
							
							function getRecipientEmail(){
								
								var beneficiaryEmail = document.getElementById("recipientEmail").value;							
								var getCurrencyType = document.getElementById("senderAcccurrencyType").value;
								
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
								    		
								    		 
								    		 //alert(receiverAccount);
								    		 
								    		 document.getElementById("beneficiaryWalletAcc").value = receiverAccount;
								    		 //document.getElementById("recipientEmail").value = receiverAccount;
								        
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
							
							function fundTransferOtherUser(email){
								
								
								swal({
									  text: 'Please wait....',
									button : false
									});
								
								var userAccountInfo = document.getElementById("senderAccountInfo").value;
								
								var userAccInfo = String(userAccountInfo);
								var senderAccount = userAccInfo.substring(0,8);
								
								//alert("Sender account is: " + senderAccount);
								
								var beneficiaryEmail = document.getElementById("recipientEmail").value;							
								var receiverAccount = document.getElementById("beneficiaryWalletAcc").value;
								var amount = document.getElementById("amount").value;
								var getCurrencyType = document.getElementById("senderAcccurrencyType").value;
								
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
								    		 
								    		// alert(response);
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
								 
								 document.getElementById("senderAccountInfo").value = "";
								 document.getElementById("recipientEmail").value = "";
								 document.getElementById("beneficiaryWalletAcc").value = "";
								 document.getElementById("amount").value = "";
								 document.getElementById("senderAcccurrencyType").value = "";
								
							}
							
							/***************************CryptoLoad Initate********************/
							
							function cryptoLoadInitate(){
								
								document.getElementById('crypto_address').value = '';
								
								swal({
									  text: 'Please wait....',
										  button: false
									});
															var DepositAmount = document.getElementById("crypto_amount").value;
															var DepositCurrencyCode = document.getElementById("selling_currency").value;
															var SellingCurrencyCode="BTC";
														
															$.ajax({
																
															     type : "POST",
															     url : "/cryptoLoadInitate",
															     data : {DepositCurrencyCode:DepositCurrencyCode, DepositAmount:DepositAmount, SellingCurrencyCode:SellingCurrencyCode},
															     timeout : 100000,
															 
															     success : function(data) {							    	 
															   		 
															    	//	 alert(data);
															    		 var obj = JSON.parse(data);
															    		 //var obj = JSON.stringify(data);
															    	//	 alert(obj);
															    		// var quote=data;
															    		document.getElementById("cryptoquote").innerText =obj['QuoteAmount'];
															    		document.getElementById("DateCreate").innerText =obj['DateCreate'];
															    		document.getElementById("RequestedCurrencyCode").innerText =obj['RequestedCurrencyCode'];
															    		document.getElementById("SourceCurrencyCode").innerText =obj['SourceCurrencyCode'];
															    		document.getElementById("TransactionRequestID").innerText =obj['TransactionRequestID'];
															    		document.getElementById("TransactionRequestStatus").innerText =obj['TransactionRequestStatus'];
															    		document.getElementById("TransactionRequestIDNew").value =obj['TransactionRequestID'];
															    
															    		 swal.stopLoading();
															    		    swal.close();
															    		    var val = obj['QuoteAmount'];
															    		  	if(!val)	  
															    		  		 swal(
																		        		    'Oops!! Something Went Wrong '
																		        		    
																		        		  );
															    		 },
															     error : function(e) {
															         console.log("ERROR: ", e);
															         display(e);
															         swal(
															        		    'Good job!',
															        		    'You clicked the button!',
															        		    'error'
															        		  );
															        		 
															     },
															     done : function(e) {
															         console.log("DONE");
															         
															        		
															       // document.getElementById("LoadingImage").hide();
															     } 
															     
															 });
															
															
														}
														
					function cryptoLoadstatus(){
						
						/*if(!document.getElementById("TransactionRequestID").value)
							{
								return false;
							}*/
								swal({
									  text: 'Please wait....',
									  button : false
										
									});
														var TransactionRequestID = document.getElementById("TransactionRequestIDNew").value;
			
															$.ajax({
																
															     type : "POST",
															     url : "/cryptoLoadStatus",
															     data : {TransactionRequestID:TransactionRequestID},
															     timeout : 100000,
															 
															     success : function(data) {							    	 
															   		 
															    		// alert(data);
															    		 var obj = JSON.parse(data);
															    		 //var obj = JSON.stringify(data);
															    		// alert(obj['Account']['Address']);qr_address
															    		// var quote=data;
															    		document.getElementById("crypto_address").value =obj['Account']['Address'];
//															    		document.getElementById("crypto_address").value =Math.random();
															    		document.getElementById("qr_address").value =obj['Account']['Address'];
															    		document.getElementById("DateCreate").innerText =obj['DateCreate'];
															    		document.getElementById("RequestedCurrencyCode").innerText =obj['RequestedCurrencyCode'];
															    		document.getElementById("SourceCurrencyCode").innerText =obj['SourceCurrencyCode'];
															    		document.getElementById("TransactionRequestID").innerText =obj['TransactionRequestID'];
															    		document.getElementById("TransactionRequestStatus").innerText =obj['TransactionRequestStatus'];
															    
															    		 swal.stopLoading();
															    		    swal.close();
															    		    var val = obj['Account']['Address'];
															    		  	if(!val)	  
															    		  		 swal(
																		        		    'Oops!! Something Went Wrong '
																		        		    
																		        		  );
															    		 },
															     error : function(e) {
															         console.log("ERROR: ", e);
															         display(e);
															         swal(
															        		    'Good job!',
															        		    'You clicked the button!',
															        		    'error'
															        		  );
															        		 
															     },
															     done : function(e) {
															         console.log("DONE");
															         
															        		
															       // document.getElementById("LoadingImage").hide();
															     } 
															     
															 });
															
															
														}
							function CryptoTranscationHistory(){
								
															swal({
																  text: 'Please wait....',
																  button : false
																	
																});
																					//var Email_ID = document.getElementById("TransactionRequestIDNew").value;
										
																						$.ajax({
																							
																						     type : "POST",
																						     url : "/cryptoLoadHistroy",
																						   //  data : {Email:TransactionRequestID},
																						     timeout : 100000,
																						 
																						     success : function(data) {							    	 
																						   		 
																						    		// alert(data);
																						    	 
																						    		 var obj = JSON.parse(data);
																						    		
																						    		         var myBooks = obj;
																						    		         var table =  $('#myTable');

																						    					var max_size=myBooks.length;
																						    					var sta = 0;
																						    					var elements_per_page = 3;
																						    					var limit = elements_per_page;
																						    					goFun(sta,limit);
																						    					function goFun(sta,limit){
																						    						for(var i=sta;i<limit;i++){
																						    						var tab='<tr><td>'+myBooks[i].DepositAmountRequested+"\n"+'</td><td>'+myBooks[i].SourceCurrencyCode+"\n"+'</td><td>'+myBooks[i].DateCreate+"\n"+'</td><td>'
																						    						          +myBooks[i].TransactionRequestID+"\n"+'</td><td>'+myBooks[i].TransactionRequestStatus+"\n"+'</td></tr>';

																						    						 $('#myTable').append(tab)

																						    						}
																						    					}
																						    					$('#nextValue').click(function(){
																						    						var next = limit;
																						    						if(max_size>=next) {
																						    						limit = limit+elements_per_page;
																						    						table.empty();
																						    						goFun(next,limit);
																						    						}
																						    					});
																						    					  $('#PreValue').click(function(){
																						    						var pre = limit-(2*elements_per_page);
																						    						if(pre>=0) {
																						    						limit = limit-elements_per_page;
																						    						table.empty();
																						    						goFun(pre,limit); 
																						    						}
																						    					});

																						    		       // prompt("Copy to clipboard: Ctrl+C, Enter", myBooks);

																						    		         // EXTRACT VALUE FOR HTML HEADER. 
																						    		         // ('Book ID', 'Book Name', 'Category' and 'Price')
//																						    		         var col = [];
//																						    		         for (var i = 0; i <2; i++) {
//																						    		             for (var key in myBooks[i]) {
//																						    		                 if (col.indexOf(key) === -1) {
//																						    		                     col.push(key);
//																						    		                 }
//																						    		             }
//																						    		         }

																						    		         // CREATE DYNAMIC TABLE.
//																						    		         var table = document.createElement("table");
//
//																						    		         // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.
//
//																						    		         var tr = table.insertRow(-1);                   // TABLE ROW.
//
//																						    		         for (var i = 0; i < col.length; i++) {
//																						    		             var th = document.createElement("th");      // TABLE HEADER.
//																						    		             th.innerHTML = col[i];
//																						    		             tr.appendChild(th);
//																						    		         }
//
//																						    		         // ADD JSON DATA TO THE TABLE AS ROWS.
//																						    		         for (var i = 0; i < 5; i++) {
//
//																						    		             tr = table.insertRow(-1);
//
//																						    		             for (var j = 0; j < col.length; j++) {
//																						    		                 var tabCell = tr.insertCell(-1);
//																						    		                 tabCell.innerHTML = myBooks[i][col[j]];
//																						    		             }
//																						    		         }
//
//																						    		         // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
//																						    		         var divContainer = document.getElementById("showData");
//																						    		         divContainer.innerHTML = "";
//																						    		         divContainer.appendChild(table);
																						    		         swal.stopLoading();
																								    		 swal.close();
																						    		
																						    		 
																						    		 }
																						}
																						       // document.getElementById("LoadingImage").hide();
																						     
																						     
																						 );
																						
																						
																					}
														