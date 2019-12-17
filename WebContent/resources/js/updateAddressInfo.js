	
	function updateAddressInfo(){
		
		
		var updateAddress1 = document.getElementById("updateAddress1").value;
		var updateAddress2 = document.getElementById("updateAddress2").value;
		var updateCity = document.getElementById("updateCity").value;
		var updateZip = document.getElementById("updateZip").value;
		var updateState = document.getElementById("updateState").value;
		var updateCountry = document.getElementById("updateCountry").value;
		
		var updateBillingAddress1 = document.getElementById("updateBillingAddress1").value;
		var updateBillingAddress2 = document.getElementById("updateBillingAddress2").value;
		var updateBillingCity = document.getElementById("updateBillingCity").value;
		var updateBillingZip = document.getElementById("updateBillingZip").value;
		var updateBillingState = document.getElementById("updateBillingState").value;
		var updateBillingCountry = document.getElementById("updateBillingCountry").value;
		
		var tempUpdateAddress1 = updateAddress1;	
		var tempUpdateAddress2 = updateAddress2;
		var tempUpdateCity = updateCity;
		var tempUpdateZip = updateZip;
		var tempUpdateState = updateState;
		var tempUpdateCountry = updateCountry;
		
		var tempUpdateBillingAddress1 = updateBillingAddress1;	
		var tempUpdateBillingAddress2 = updateBillingAddress2;
		var tempUpdateBillingCity = updateBillingCity;
		var tempUpdateBillingZip = updateBillingZip;
		var tempUpdateBillingState = updateBillingState;
		var tempUpdateBillingCountry = updateBillingCountry;
		
		alert("updateAddress1 is: " + updateAddress1);
						
						$.ajax({
						     type : "POST",
						     url : "/updateAddressInfo",
						     data : {updateAddress1:tempUpdateAddress1, updateAddress2:tempUpdateAddress2, updateCity:tempUpdateCity, updateZip:tempUpdateZip, updateState:tempUpdateState, updateCountry:tempUpdateCountry, updateBillingAddress1:tempUpdateBillingAddress1, updateBillingAddress2:tempUpdateBillingAddress2, updateBillingCity:tempUpdateBillingCity, updateBillingZip:tempUpdateBillingZip, updateBillingState:tempUpdateBillingState, updateBillingCountry:tempUpdateBillingCountry},
						     timeout : 100000,
						     async: false,
						     success : function(data) {							    	 
						   		 
						    		 console.log(data); 
						    		
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