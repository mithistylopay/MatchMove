
	function updateProfileInfo(){
		
		
		var updatedEmail = document.getElementById("updatedEmailId").value;
		var tempUpdatedEmail = updatedEmail;	
		
		alert("updatedEmail is: " + updatedEmail);
						
						$.ajax({
						     type : "POST",
						     url : "/updatePersonalInfo",
						     data : {updatedEmail: tempUpdatedEmail},
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