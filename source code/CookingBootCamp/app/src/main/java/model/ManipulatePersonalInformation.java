package model;

@SuppressWarnings("unused")
/**
 * This interface mainly focuses on the operation on user information.
 * It mostly is concerned with edit operation.
 */
interface ManipulatePersonalInformation {
    String getName();
	String getLocation();
    String getEmail();
    String getURLForProfile();
	
    void setName(String name);
    void setLocation(String location);
    void setEmail(String email);
    void setURLForProfile(String URLForProfile);
}
