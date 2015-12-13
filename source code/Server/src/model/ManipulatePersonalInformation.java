package model;

/**
 * All action about user
 * @author zhengyu
 *
 */

interface ManipulatePersonalInformation {
	public String getName(); 
	public String getLocation();
	public String getEmail();
	public String getURLForProfile();
	
	public void setName(String name); 
	public void setLocation(String location);
	public void setEmail(String email);
	public void setURLForProfile(String URLForProfile);
}
