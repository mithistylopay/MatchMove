<!DOCTYPE html>
 <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Paycent Dashboard</title>

	<link rel="stylesheet" href="resources/css/bootstrap.css">
	<link rel="stylesheet" href="resources/css/stylesheet.css">
	<link rel="stylesheet" href="resources/css/normalize.css">
	<link rel="stylesheet" href="resources/css/style.css">

</head>
<body>
<%
    String redirectURL = request.getContextPath()+"/login";
    response.sendRedirect(redirectURL);
%>

	<div class="login">
		<div class="container-fluid p-0">
			<div class="row p-0">
				<div class="col-lg-6 p-0">
					<div class="login-left text-center d-flex flex-column align-items-center justify-content-center">
							<div class="logo-title">
								<img width="200px" src="resources/images/styplogo.png" alt="">
								<p class="login__title">Welcome</p>
								<p class="ligin__subtitle">to Paycent</p>
							</div>
					</div>
				</div>
				<div class="col-lg-6 p-0 d-flex flex-column justify-content-center">
					<div class="login-right">
						<div class="authform login-authform">
							
								<p class="authform__title">
									Sign In
								</p>
								
								<form method = POST action = "/dashboard">
									<input required type="email" class="mt-4 inp-name" placeholder="Email" name="email">
									<input required type="password" placeholder="Password" class="mt-4 inp-pass" name="password">
									<button type="submit" class="mt-4 mb-3">LOG IN <img src="resources/images/arrow.svg" alt="" class="ml-3"></button>
							   </form>
							<c:if test="${not empty error}">
							 <span>${error}</span>  
							</c:if>
							
							<form method = POST action = "/forgotPassword" name="newUserRegistration1">
									<a href="#" class="authform__link__reset mt-4" onClick="document.newUserRegistration1.submit();">Forgot password or username?</a>
						    </form>
							
							<form method = POST action = "/logup" name="newUserRegistration">
									<a href="#" class="authform__link__create mt-3" onClick="document.newUserRegistration.submit();">Create New Account</a>
						    </form>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>



	<!-- скрипты -->
	<script src="resources/js/jquery-3.2.1.min.js"></script>
	<script src="resources/js/less.min.js"></script>
	<script src="resources/js/main.js"></script>

</body>
</html>
