package com.stylopay.bean;

public class ForgotPasswordBean {

	String mfaCode;
	//String userName;
	String password;
	public String getMfaCode() {
		return mfaCode;
	}
	public void setMfaCode(String mfaCode) {
		this.mfaCode = mfaCode;
	}
//	public String getUserName() {
//		return userName;
//	}
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
