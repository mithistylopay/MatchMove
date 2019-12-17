function createClxVirCard(email){
//	 swal({
//			text:'Please wait....',
//			button: false
//	 });
	
	
					var userEmail = email;
					var cardInfoArray = new Array();
					
					var tempEmail = userEmail;
					

					$.ajax({
					     type : "POST",
					     url : "/getCLXVirtualCard",
					     data : {email: tempEmail},
					     timeout : 100000,
					     async: false,
					     success : function(data) {	

//					    	 swal({
//					   		  text: 'XXVTU....',
//					   			  button: false
//					   		});
					    		 //alert(data);
//					    	 if(data.includes('Card has been created successfully!'))
//					    		 {
//					    		 swal({
//									   text: "Card has been created successfully", button: true
//									}).then(function() {
//										//if(button)
//										document.location.reload();
//									});
//					    		 }
//					    	 else
//					    		 {
//					    		 swal({
//									  text: 'Test',
//										  button: true
//									});
//				    			 
//				    			 swal.stopLoading();
//					    		    swal.close();
//					    		 }
					    		 var obj=JSON.parse(data);
					    		
//					    		 swal.stopLoading();
//					    		    swal.close();
					    		  //alert(obj);
					    		 var cardInfo = String(data);
					    		 var trimCardInfo = cardInfo.replace(/[&\/\\#+$~%.'":*?<>{}]/g, '');
					    		 
					    		 cardInfoArray = trimCardInfo.split(",");
					    		 //var val=JSON.parse(obj.Status);
					    		 if(obj.Status.Code == '0')
				    			 {
				    			 swal({
									   text: "Card has been created successfully", button: true
									}).then(function() {
										//if(button)
										document.location.reload();
									});
				    			 
//				    			 swal({
//				    				 title:"Success Alert",  text: "Card has been created successfully", type: "success"},
//				    					   function(){ 
//				    				 		document.location.reload();
//				    					   }
//				    					);
				    			 
//				    			 swal({
//				    		            title: "Wow!",
//				    		            text: "Message!",
//				    		            type: "success"
//				    		        }, function() {
//				    		        	document.location.reload();
//				    		        });
			    		
				    			 }
				    		 else
				    			 {
				    			 var str = new String(obj.Status.Message);
				    			 swal({
									  text: str,
										  button: true
									}).then(function() {
										//if(button)
										document.location.reload();
									});
				    			 
//				    			 swal.stopLoading();
//					    		    swal.close();
				    			 }
					    		 
//					    		 
					    		 
					    		/* if(cardInfoArray.length == 1){
					    			 
					    		//	 alert(cardInfoArray[0].replace("[", "").replace("]", ""));
					    			 
					    		 }else{					    			 
					    			 
					    		//	alert(cardInfoArray[6].replace("[", "").replace("]", "")); 
					    			 
					    		 }*/
					    		 
					    		 },
					     error : function(e) {
					         console.log("ERROR: ", e);
					         display(e);
					     },
					     done : function(e) {
					         console.log("DONE");
					     } 
					     
					 });
					
					var cardDetailArray = cardInfoArray;
//					if(cardDetailArray.length == 1)
//						{
//						var str = new String(cardDetailArray);
//						swal({
//							  text: 'Test',
//								  button: true
//							}).then(function() {
//								document.location.reload();
//							});
//						
//						}
					var cardNo = String(cardDetailArray[1]);
					var cardNoSubStrng =  "******" + cardNo.substring(11);
					var cardNoToView =  cardNo.substring(0,5) + "********" + cardNo.substring(13);
					
					var expDate = cardDetailArray[4] + "/" + cardDetailArray[5].substring(3);
					
					document.getElementById("clxCardNum").innerHTML = cardNoSubStrng;
					document.getElementById("clxCardName").innerHTML = cardDetailArray[0].replace("[", "").replace("]", "");
					
					document.getElementById("cardNumberToView").value = cardNoToView;
					document.getElementById("clxCardSecCode").value = cardDetailArray[2];
					document.getElementById("clxCardCurrency").value = cardDetailArray[3];
					document.getElementById("clxCardExpDate").value = expDate;
					
				}
				
				
				function createClxPhyCard(email){
					
					var userEmail = email;
					var clxPhyCardNo = document.getElementById("cuallixPhysicalCardNumber").value;
					
					var tempEmail = userEmail;
					var tempCardNo = clxPhyCardNo;
					
					$.ajax({
					     type : "POST",
					     url : "/getCLXPhysicalCard",
					     data : {email: tempEmail, cardNumber: tempCardNo},
					     timeout : 100000,
					     success : function(data) {				    	 
					   		 
					    	// alert(data);
					    	 
					    		 },
					     error : function(e) {
					         console.log("ERROR: ", e);
					         display(e);
					     },
					     done : function(e) {
					         console.log("DONE");
					     } 
					     
					 });	
					
					
					var cardDetailArray = cardInfoArray;
					
					var cardNo = String(cardDetailArray[1]);
					var cardNoSubStrng =  "******" + cardNo.substring(11);
					var cardNoToView =  cardNo.substring(0,5) + "********" + cardNo.substring(13);
					
					var expDate = cardDetailArray[4] + "/" + cardDetailArray[5].substring(3);
					
					document.getElementById("clxCardNum").innerHTML = cardNoSubStrng;
					document.getElementById("clxCardName").innerHTML = cardDetailArray[0].replace("[", "").replace("]", "");
					
					document.getElementById("cardNumberToView").value = cardNoToView;
					document.getElementById("clxCardSecCode").value = cardDetailArray[2];
					document.getElementById("clxCardCurrency").value = cardDetailArray[3];
					document.getElementById("clxCardExpDate").value = expDate;
					
				}
				
				
				function setClxCardPin(){
					
					var pin = document.getElementById("clxCardPin").value;
					var tempPin = pin;
					
					
					$.ajax({
					     type : "POST",
					     url : "/setClxCardPin",
					     data : {pin: tempPin},
					     timeout : 100000,
					     success : function(data) {						    	 
					    		 alert(data);
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
				
				
				function replaceCLXCard(){
					
					var newCardNum = document.getElementById("cardNumForReplacement").value;
					var reason = document.getElementById("cardReplacementReason").value;
						
					var tempNewCardNum = newCardNum;
					var tempReason = reason;
					
					$.ajax({
					     type : "POST",
					     url : "/clxCardReplacement",
					     data : {newCardNo: tempNewCardNum, reason:tempReason},
					     timeout : 100000,
					     success : function(data) {							    	 
					   		 
					    		 alert(data);
					    		 
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
				
				
				function activateCLXCard(){				
					
					
					$.ajax({
					     type : "POST",
					     url : "/activateCLXCard",					    
					     timeout : 100000,
					     success : function(data) {							    	 
					   		 
					    		 alert(data);
					    		 
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
				
function getCardInfo(){
					
					//var pin = document.getElementById("clxCardPin").value;
					//var tempPin = pin;
					
					
					$.ajax({
					     type : "POST",
					     url : "/getCardInfo",
					     //data : {pin: tempPin},
					     timeout : 100000,
					     success : function(data) {						    	 
					    		// alert(data);
					    		 var obj = JSON.parse(data);
									//alert(obj);
								//	alert(obj['CardNum']);
									
									//var cardDetailArray = cardInfoArray;
									
									//var cardNo = String(cardDetailArray[1]);
									//var cardNoSubStrng =  "******" + obj['CardNum'];
									//var cardNoToView =  cardNo.substring(0,5) + "********" + cardNo.substring(13);
									
									var expDate = obj['Month'] + "/" + obj['Year'];
									
									document.getElementById("clxCardNum").innerHTML = obj['CardNum'];
									//document.getElementById("clxCardName").innerHTML = cardDetailArray[0].replace("[", "").replace("]", "");
									
									document.getElementById("cardNumberToView").value = obj['CardNum'];
									document.getElementById("clxCardSecCode").value = "SecurityCode:"+obj['SecurityCode'];
									document.getElementById("clxCardCurrency").value = obj['Currency'];
									document.getElementById("clxCardExpDate").value = expDate;
									
								
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
/**************************************************CLX Card Load**********************************/
function ClxCardLoad(){
	if (!$(this).valid()) {  
        return false;
    }
	swal({
		  text: 'Please wait....',
		button : false
		});
	var Amount = document.getElementById("amountToBeLoaded").value;
	//var tempPin = pin;
	
	
	$.ajax({
	     type : "POST",
	     url : "/clxcardload",
	     data : {Amount: Amount},
	     timeout : 100000,
	     success : function(data) {						    	 
	    		// alert(data);
	    		 var obj = JSON.parse(data);
				//	alert(obj["Fund Transfer Detail"]["description"]);
					 swal(
							 obj["Fund Transfer Detail"]["description"]
			        		    
			        		  );
				//	alert(obj['CardNum']);
					
					//var cardDetailArray = cardInfoArray;
					/*
					//var cardNo = String(cardDetailArray[1]);
					//var cardNoSubStrng =  "******" + obj['CardNum'];
					//var cardNoToView =  cardNo.substring(0,5) + "********" + cardNo.substring(13);
					
					var expDate = obj['Month'] + "/" + obj['Year'];
					
					document.getElementById("clxCardNum").innerHTML = obj['CardNum'];
					//document.getElementById("clxCardName").innerHTML = cardDetailArray[0].replace("[", "").replace("]", "");
					
					document.getElementById("cardNumberToView").value = obj['CardNum'];
					document.getElementById("clxCardSecCode").value = obj['SecurityCode'];
					document.getElementById("clxCardCurrency").value = obj['Currency'];
					document.getElementById("clxCardExpDate").value = expDate;
					
				*/
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