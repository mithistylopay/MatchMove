

   function getTransactionDetails(){
	   
	   if (!$(this).valid()) {  //<<< I was missing this check
	        return false;
	    }
	   swal({
			  text: 'Please wait....',
			button : false
			});
	   
	   var accountId = document.getElementById("accountDetails").value;							
	   var fromDate = document.getElementById("fromDateOfTransaction").value;
	   var toDate = document.getElementById("toDateOfTransaction").value;
	   
	   var jsonData, jsonDataString, jsonParseData;
		
		//alert(getCurrencyType);
		
		var tempAccountId = accountId;
		var tempFromDate = fromDate;
		var tempToDate = toDate;
		
		
		 $.ajax({
		     type : "POST",
		     url : "/MatchMoveWebsite/getTransactionList",
		     data : {accountId:tempAccountId, fromDate:tempFromDate, toDate:tempToDate},
		     timeout : 100000,
		     async: false,
		     success : function(data) {				    	 
		   		 
		    		 //alert(data);
		    		 
		    		 jsonData = data.toString();	    		 
		    		
		    		 },
		     error : function(e) {
		         console.log("ERROR: ", e);
		         display(e);
		     },
		     done : function(e) {
		         console.log("DONE");
		     } 
		     
		 });		 
	 
		 
		 jsonParseData = JSON.parse(jsonData); 		 
		
		 var i, x=1;		 
		 
		 var tableBody = document.getElementById("transactionTBody");
		 var rowCount = jsonParseData.activity.length;		
		 
		 
		// alert("rowCount is: " + rowCount);
		 swal.stopLoading();
		    swal.close();
		 
		 for( var i = 0; i<rowCount; i++){
			 
			 var rowId = "transactionTableRow"+i;			 
			 
			 var row = document.createElement("TR");
			 row.setAttribute("id", rowId);
			 
			 tableBody.appendChild(row);
			 
			 var col1 = document.createElement("TD");
			 var col1Data = document.createTextNode(x);
			 col1.appendChild(col1Data);
			 
			 document.getElementById(rowId).appendChild(col1);
			 
			 var col2 = document.createElement("TD");
			 var col2Data = document.createTextNode(jsonParseData.activity[i].id);
			 col2.appendChild(col2Data);
			 
			 document.getElementById(rowId).appendChild(col2);
			 
			 var col3 = document.createElement("TD");
			 var col3Data = document.createTextNode("");
			 col3.appendChild(col3Data);
			 
			 document.getElementById(rowId).appendChild(col3);
			 
			 var col4 = document.createElement("TD");
			 var col4Data = document.createTextNode(jsonParseData.activity[i].description);
			 col4.appendChild(col4Data);
			 
			 document.getElementById(rowId).appendChild(col4);
			 
			 var col5 = document.createElement("TD");
			 var col5Data = document.createTextNode(jsonParseData.activity[i].date);
			 col5.appendChild(col5Data);
			 
			 document.getElementById(rowId).appendChild(col5);
			 
			 var col6 = document.createElement("TD");
			 var col6Data = document.createTextNode(jsonParseData.activity[i].sender);
			 col6.appendChild(col6Data);
			 
			 document.getElementById(rowId).appendChild(col6);
			 
			 var col7 = document.createElement("TD");
			 var col7Data = document.createTextNode(jsonParseData.activity[i].recipient);
			 col7.appendChild(col7Data);
			 
			 document.getElementById(rowId).appendChild(col7);
			 
			 var col8 = document.createElement("TD");
			 var col8Data = document.createTextNode(jsonParseData.activity[i].debit);
			 col8.appendChild(col8Data);
			 
			 document.getElementById(rowId).appendChild(col8);
			 
			 var col9 = document.createElement("TD");
			 var col9Data = document.createTextNode(jsonParseData.activity[i].credit);
			 col9.appendChild(col9Data);
			 
			 document.getElementById(rowId).appendChild(col9);
			 
			 var col10 = document.createElement("TD");
			 var col10Data = document.createTextNode(jsonParseData.activity[i].currency);
			 col10.appendChild(col10Data);
			 
			 document.getElementById(rowId).appendChild(col10);		 
			 
		 }	 
		
   }