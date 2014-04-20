package com.fsq.homework251_chrisesp;

// Singleton in-memory "data model"
// just used to share data between login and summary screens
public class SharedUserData 
{

	private String userEmailAddress;
	private String userPassword;
	
	//construct shared instance
	private static SharedUserData instance = new SharedUserData();
	
	// provide shared instance
	public static SharedUserData getInstance()
	{
		return instance;
	}
	
	// hide constructor to prevent arbitrary use
	private SharedUserData() 
	{
		// TODO Auto-generated constructor stub
		userEmailAddress = "no_email";
		userPassword = "no_password";
	}

	// setters & getters for data members
	public String getUserEmailAddress() 
	{
		return userEmailAddress;
	}

	public void setUserEmailAddress(String userEmailAddress) 
	{
		this.userEmailAddress = userEmailAddress;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	

}
