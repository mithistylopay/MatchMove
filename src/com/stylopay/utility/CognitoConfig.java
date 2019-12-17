package com.stylopay.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:cognito.properties")
public class CognitoConfig {
	
	@Value("${clientId}")
	String clientId;
	
	@Value("${userPoolId}")
	String userPoolId;
	
	@Value("${endpoint}")
	String endpoint;
	
	@Value("${region}")
	String region;
	
	@Value("${identityPoolId}")
	String identityPoolId;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUserPoolId() {
		return userPoolId;
	}

	public void setUserPoolId(String userPoolId) {
		this.userPoolId = userPoolId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getIdentityPoolId() {
		return identityPoolId;
	}

	public void setIdentityPoolId(String identityPoolId) {
		this.identityPoolId = identityPoolId;
	}
}