
    document.getElementById("black").addEventListener("click",focusBlack);
    function focusBlack() {
     document.getElementById("black").style.zIndex = "25";
     document.getElementById("blue").style.zIndex ="20";

    }


    document.getElementById("blue").addEventListener("click",focusBlue);
    function focusBlue(){
          document.getElementById('black').style.zIndex="20";
         document.getElementById('blue').style.zIndex="25";


  }

    function showDiv() {	
        document.getElementById('add__acc').style.display = "block";
        var objDiv = document.getElementById("add__acc");
   	  objDiv.scrollIntoView();
     }
  function hideDiv() {
     document.getElementById('add__acc').style.display = "none";
  }

  function replaceIndex()
  { document.getElementById("index").style.display="none";
   document.getElementById("trans1").style.display="block";
   document.getElementById("profile").style.display="none";
   document.getElementById("load").style.display="none";
   document.getElementById("complete").style.display="none";
   document.getElementById("ExchangeMain").style.display="none";
   document.getElementById("SendMain").style.display="none";
   document.getElementById("kyc").style.display="none";



   }
   function replaceTrans()
   { document.getElementById("index").style.display="block";
    document.getElementById("trans1").style.display="none";
    document.getElementById("profile").style.display="none";
    document.getElementById("load").style.display="none";
    document.getElementById("complete").style.display="none";
    document.getElementById("ExchangeMain").style.display="none";
    document.getElementById("SendMain").style.display="none";
    document.getElementById("kyc").style.display="none";


    }
    function showProfile()
    { document.getElementById("profile").style.display="block";
     document.getElementById("trans1").style.display="none";
     document.getElementById("index").style.display="none";
     document.getElementById("load").style.display="none";
     document.getElementById("complete").style.display="none";
     document.getElementById("ExchangeMain").style.display="none";
     document.getElementById("SendMain").style.display="none";
     document.getElementById("kyc").style.display="none";




     }
     function showLoad()
     { document.getElementById("load").style.display="block";
      document.getElementById("trans1").style.display="none";
      document.getElementById("index").style.display="none";
      document.getElementById("profile").style.display="none";
      document.getElementById("complete").style.display="none";
      document.getElementById("ExchangeMain").style.display="none";
      document.getElementById("SendMain").style.display="none";
      document.getElementById("kyc").style.display="none";



      }

      function showComplete(memberId)
      {
    	if(memberId==null)
    		{
    		document.getElementById("complete").style.display="block";
    	       document.getElementById("trans1").style.display="none";
    	       document.getElementById("index").style.display="none";
    	       document.getElementById("profile").style.display="none";
    	       document.getElementById("load").style.display="none";
    	       document.getElementById("ExchangeMain").style.display="none";
    	       document.getElementById("SendMain").style.display="none";
    	       document.getElementById("kyc").style.display="none";
    		}
       
    	else{
    		showLoad();
    	}

       }
      
      function showComplete1()
      {
    	//if(memberId==null)
    		{
    		document.getElementById("complete").style.display="block";
    	       document.getElementById("trans1").style.display="none";
    	       document.getElementById("index").style.display="none";
    	       document.getElementById("profile").style.display="none";
    	       document.getElementById("load").style.display="none";
    	       document.getElementById("ExchangeMain").style.display="none";
    	       document.getElementById("SendMain").style.display="none";
    	       document.getElementById("kyc").style.display="none";
    		}
       
//    	else{
//    		showLoad();
//    	}

       }

       function showExchangeMain()
       {
        document.getElementById("ExchangeMain").style.display="block";
        document.getElementById("complete").style.display="none";
        document.getElementById("trans1").style.display="none";
        document.getElementById("index").style.display="none";
        document.getElementById("profile").style.display="none";
        document.getElementById("load").style.display="none";
        document.getElementById("SendMain").style.display="none";
        document.getElementById("kyc").style.display="none";


        }
        function showKyc()
        {
          document.getElementById("kyc").style.display="block";
         document.getElementById("ExchangeMain").style.display="none";
         document.getElementById("complete").style.display="none";
         document.getElementById("trans1").style.display="none";
         document.getElementById("index").style.display="none";
         document.getElementById("profile").style.display="none";
         document.getElementById("load").style.display="none";
         document.getElementById("SendMain").style.display="none";
         }
        function showSendMain()
        {
         document.getElementById("SendMain").style.display="block";
         document.getElementById("complete").style.display="none";
         document.getElementById("trans1").style.display="none";
         document.getElementById("index").style.display="none";
         document.getElementById("profile").style.display="none";
         document.getElementById("load").style.display="none";
         document.getElementById("ExchangeMain").style.display="none";
         document.getElementById("kyc").style.display="none";
         

         }

       function showCDBlue()
       {
        document.getElementById("track1_bl").style.display="block";
        document.getElementById("track2_bl").style.display="block";
        document.getElementById("track3_bl").style.display="block";
        document.getElementById("track0_bl").style.display="none";


        }
        function showCDBlack()
        {
         document.getElementById("track1_bk").style.display="block";
         document.getElementById("track2_bk").style.display="block";
         document.getElementById("track3_bk").style.display="block";
         document.getElementById("track0_bk").style.display="none";

         }

         function showForm1()
         {
          document.getElementById("frm1").style.display="block";

          }
          function showForm2()
          {
           document.getElementById("frm2").style.display="block";

           }



     // Main div replacement

    function replaceSend()
    { document.getElementById("exchange").style.display="block";
     document.getElementById("send").style.display="none";



     }

     function replaceExchange()
     { document.getElementById("send").style.display="block";
      document.getElementById("exchange").style.display="none";


      }

      function cryptoLoad(){
        document.getElementById("cryptoLoad").style.display="block";
           document.getElementById("frm2").style.display="block";

      }

            // Money Movement Collapse
            var coll = document.getElementsByClassName("collapsible");
            var i;

            for (i = 0; i < coll.length; i++) {
              coll[i].addEventListener("click", function() {
                this.classList.toggle("active");
                var content = this.nextElementSibling;
                if (content.style.maxHeight){
                  content.style.maxHeight = null;
                } else {
                  content.style.maxHeight = content.scrollHeight + "px";
                }
              });
            }


            //
            // // Modal JS------------------------
            // // Get the modal
            // var card_modal = document.getElementById("add_card_Modal");
            //
            // // Get the button that opens the modal
            // var btn = document.getElementById("myBtn");
            //
            // // Get the <span> element that closes the modal
            // var span = document.getElementsByClassName("close")[0];
            //
            //
            // // When the user clicks on the button, open the modal
            // btn.onclick = function() {
            //   card_modal.style.display = "block";
            // }
            //
            // // When the user clicks on <span> (x), close the modal
            // span.onclick = function() {
            //   card_modal.style.display = "none";
            // }
            //
            // // When the user clicks anywhere outside of the modal, close it
            // window.onclick = function(event) {
            //   if (event.target == card_modal) {
            //     card_modal.style.display = "none";
            //   }
            // }
            // // Modal JS End----------------------------------
            // // Get the modal
            // var card_modal = document.getElementById("add_card_Modal");
            //
            // // Get the button that opens the modal
            // var btn = document.getElementById("myBtn1");
            //
            // // Get the <span> element that closes the modal
            // var span = document.getElementsByClassName("close")[0];
            //
            //
            // // When the user clicks on the button, open the modal
            // btn.onclick = function() {
            //   card_modal.style.display = "block";
            // }
            //
            // // When the user clicks on <span> (x), close the modal
            // span.onclick = function() {
            //   card_modal.style.display = "none";
            // }
            //
            // // When the user clicks anywhere outside of the modal, close it
            // window.onclick = function(event) {
            //   if (event.target == card_modal) {
            //     card_modal.style.display = "none";
            //   }
            // }
            // // Modal JS End----------------------------------
            // // Get the modal
            // var p_info_modal = document.getElementById("p_info_Modal");
            //
            // // Get the button that opens the modal
            // var btn = document.getElementById("p_info");
            //
            // // Get the <span> element that closes the modal
            // var span = document.getElementsByClassName("close1")[0];
            //
            //
            // // When the user clicks on the button, open the modal
            // btn.onclick = function() {
            //   p_info_modal.style.display = "block";
            // }
            //
            // // When the user clicks on <span> (x), close the modal
            // span.onclick = function() {
            //   p_info_modal.style.display = "none";
            // }
            //
            // // When the user clicks anywhere outside of the modal, close it
            // window.onclick = function(event) {
            //   if (event.target == p_info_modal) {
            //     p_info_modal.style.display = "none";
            //   }
            // }
            // // Modal JS End----------------------------------
            //
            //
            //
            // // Get the modal
            // var add_info_modal = document.getElementById("add_info_Modal");
            //
            // // Get the button that opens the modal
            // var btn = document.getElementById("add_info");
            //
            // // Get the <span> element that closes the modal
            // var span = document.getElementsByClassName("close2")[0];
            //
            //
            // // When the user clicks on the button, open the modal
            // btn.onclick = function() {
            //   add_info_modal.style.display = "block";
            // }
            //
            // // When the user clicks on <span> (x), close the modal
            // span.onclick = function() {
            //   add_info_modal.style.display = "none";
            // }
            //
            // // When the user clicks anywhere outside of the modal, close it
            // window.onclick = function(event) {
            //   if (event.target == add_info_modal) {
            //     add_info_modal.style.display = "none";
            //   }
            // }

 // modal test
            $(".modButton").on("click", function() {
              var modal = $(this).data("modal");
              $(modal).show();
            });

            $(".modal").on("click", function(e) {
              var className = e.target.className;
              if(className === "modal" || className === "close"){
                $(this).closest(".modal").hide();
              }
            });


//copy function

   var cryptotext=document.querySelector(".cryptotext");
    document.querySelector(".copy1").addEventListener(
      "click",copyfunction);

function copyfunction(){
  // alert("hi");
           cryptotext.select();
           document.execCommand("copy");
           // alert(cryptotext.value);
        }

//qr code

// var qrcode = new QRCode("qrcode");

// function makeCode () {    
//   var elText = document.getElementById("text");
  
//   if (!elText.value) {
//     alert("Input a text");
//     elText.focus();
//     return;
//   }
  
//   qrcode.makeCode(elText.value);
// }

// makeCode();

// $("#text").
//   on("blur", function () {
//     makeCode();
//   }).
//   on("keydown", function (e) {
//     if (e.keyCode == 13) {
//       makeCode();
//     }
//   });
//      




/*$('#buttonCnf').on('click', function(){
	window.swal({
		title: "Checking...",
		text: "Please wait",
		imageUrl: "resources/images/logonew.png",
		showConfirmButton: false,
		allowOutsideClick: false
		});
	});*/

function checkValidation(id)
{
	var value = document.getElementById(id).value;
//	for(var i= 0; i<value.length; i++)
//		{
//		 if(value.charAt(i) == " ")
//			 {
//			 swal(
//	        		    'Space between characters are not acceptable'
//	        		    
//	        		  );
//			 return false; 
//			 }
//		}
	
	var letterNumber = /^[0-9a-zA-Z ]+$/;
	//var letterNumber = /[A-z\u00C0-\u00ff]+/g;
	 if(value.match(letterNumber)) 
	  {
	   return true;
	  }
	else
	  { 
		 swal(
     		    'Only Latin characters and numbers are allowed'
     		    
     		  );
		 document.getElementById(id).value = '';
	   return false; 
	  }
}

//function validate_fileupload(fileName)
//{
//    var allowed_extensions = new Array("jpeg","png","pdf");
//    var file_extension = fileName.split('.').pop().toLowerCase(); // split function will split the filename by dot(.), and pop function will pop the last element from the array which will give you the extension as well. If there will be no extension then it will return the filename.
//
//    for(var i = 0; i <= allowed_extensions.length; i++)
//    {
//        if(allowed_extensions[i]==file_extension)
//        {
//            return true; // valid file extension
//        }
//    }
//    
//    swal('Only JPEG, PNG and PDF file are accepted');
//    return false;
//}
function validate_fileupload(fileName, id)
{
var allowed_extensions = new Array("jpeg","png","pdf");
var file_extension = fileName.split('.').pop().toLowerCase(); // split function will split the filename by dot(.), and pop function will pop the last element from the array which will give you the extension as well. If there will be no extension then it will return the filename.

var isValid = false;
for(var i = 0; i <= allowed_extensions.length; i++)
{
if(allowed_extensions[i] ==file_extension)
{
	isValid = true; // valid file extension
	break;
}
}
if(!isValid)
	{
	swal('Only JPEG, PNG and PDF file are accepted');
	document.getElementById(id).value = '';
	return false;
	}

var f = document.getElementById(id);
//alert(f.files[0].size);
if(f.files[0].size > 2048*1024)
	{
	swal('2 MB size is permitted');
	document.getElementById(id).value = '';
	return false;
	}

}

function validateMyForm()
{
	var fileupload1 = document.getElementById("fileupload1").value
	var isTruefileupload1 = validate_fileupload(fileupload1);
	
	var fileupload2 = document.getElementById("fileupload2").value
	var isTruefileupload2 = validate_fileupload(fileupload2);
	
	var fileupload3 = document.getElementById("fileupload3").value
	var isTruefileupload3 = validate_fileupload(fileupload3);
	
	if(!isTruefileupload1 || !isTruefileupload2 || !isTruefileupload3)
		{
		swal('Only JPEG, PNG and PDF file are accepted');
		return false;
		}
	
	return true;
}

function checkNumber(value)
{
//	var num = Number(value);
if(value.includes(".") || Number(value) <50 || Number(value) >5000)
{
$('#buttonCnf').attr("disabled", true);
}

else
{
$('#buttonCnf').attr("disabled", false);
}
}

function showIBANDiv()
{
	
	if(document.getElementById("addIBan").style.display=="none")
		{
		$.ajax({
		     type : "POST",
		     url : "/getUserDetails",
		     data : {},
		     timeout : 100000,
		     async: false,
		     success : function(data) {			    	 
	 	    	 
		    	 var obj = JSON.parse(data);
		    	 var user_details = obj['user_details'];
		    	 document.getElementById("title").value=(user_details['gender'] =='M')? 'Mr': 'Ms/Mrs';
		    	 document.getElementById("firstName").value= user_details['first_name'];
		    	 document.getElementById("lastName").value= user_details['last_name'];
		    	 document.getElementById("addressLine1").value= user_details['address'];
		    	 document.getElementById("addressLine2").value= user_details['address_2'];
		    	 document.getElementById("addressLine3").value= user_details['billing_address'];
		    	 document.getElementById("addressLine4").value= user_details['billing_address_2'];
		    	 document.getElementById("postcode").value= user_details['postal_code'];
		    	 document.getElementById("city").value= user_details['billing_city'];
		    	 document.getElementById("state").value= user_details['billing_state'];
		    	 document.getElementById("countryIsoCode").value= '91';
		    	 document.getElementById("phoneNo").value= user_details['cell_phone'];
		    		 
		    	},
		     error : function(e) {
		         console.log("ERROR: ", e);
		         display(e);
		     },
		     done : function(e) {
		         console.log("DONE");
		     } 
		     
		 });
		document.getElementById("addIBan").style.display="block";
		}
	
	else if(document.getElementById("addIBan").style.display=="block")
		document.getElementById("addIBan").style.display="none";

 }


function createIBAN()
{
	document.getElementById("addIBan").style.display="none";
	document.getElementById("addIBanSuccessPopUp").style.display="block";
}


