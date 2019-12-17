

		

		function getUpdatedBillingStateNames(){	
	
	var x = document.getElementById("updateBillingState").options.length;
	var listOptions = document.getElementById("updateBillingState");
	
	
	if(x>1){									
		
		listOptions.options.length = 1;									
		
	}		
					
	var countryId = document.getElementById("updateBillingCountry").value;
	var tempcountryId = countryId;
	var array = new Array();
					
					$.ajax({
					     type : "POST",
					     url : "/getStatesName",
					     data : {countryid: tempcountryId},
					     timeout : 100000,
					     async: false,
					     success : function(data) {							    	 
					   		 
					    		 console.log(data); 
					    		
					    		 //alert(data);
					    		 
					    		 var statelist = String(data);
					    		 var trimStateList = statelist.replace(/[&\/\\#+$~%.'":*?<>{}]/g, '');
					    		 //alert("trimStateList: " + trimStateList);
					    		 
					    		 array = trimStateList.split(",");
					    		 //alert("array is: " + array);
					    		 
					    		 },
					     error : function(e) {
					         console.log("ERROR: ", e);
					         display(e);
					     },
					     done : function(e) {
					         console.log("DONE");
					     } 
					     
					 });
					
					
					
					var stateListArray = array;								
					
					var select = document.getElementById("updateBillingState");	
					document.getElementById("updateBillingState").value= "";
					
					for(var i = 0; i <stateListArray.length; i++){					
						
						var option = document.createElement("OPTION"),
						txt = document.createTextNode(stateListArray[i].replace("[", "").replace("]", ""));
						
						option.appendChild(txt);
						select.insertBefore(option,select.lastChild);
						
					}					
					
				}