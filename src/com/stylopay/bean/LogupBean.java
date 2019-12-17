package com.stylopay.bean;

public class LogupBean {
	
	private String userName;	
	private String email;
	private String phoneNo;
	private String full_number;
	public String getFull_number() {
		return full_number;
	}
	public void setFull_number(String full_number) {
		this.full_number = full_number;
	}
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	
}
