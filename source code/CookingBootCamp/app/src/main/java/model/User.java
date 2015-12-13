package model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
/**
 * This class file is the User class, involving all the user information and user properties.
 * It supports several
 */
public class User implements Serializable{

	private static final long serialVersionUID = 0;
	private int userid;
	private String userName;
	private String passWord;
	private String description;
	private ArrayList<Recipe> myCollections = new ArrayList<>();
	private String email;
	private String urlForProfile;
	private String location;
	private int recipenum;
	private double distance;
	
	
	public User(int userid, String userName, String passWord,
			String description, String email, String urlForProfile,
			String location, int recipenum) {
		this.userid = userid;
		this.userName = userName;
		this.passWord = passWord;
		this.description = description;
		this.email = email;
		this.urlForProfile = urlForProfile;
		this.location = location;
		this.recipenum = recipenum;
	}

	public User(String userName, String passWord,
				String description, String email, String urlForProfile,
				String location, int recipenum) {
		this.userName = userName;
		this.passWord = passWord;
		this.description = description;
		this.email = email;
		this.urlForProfile = urlForProfile;
		this.location = location;
		this.recipenum = recipenum;
	}
	
	public User() {
		
	}

	public int getRecipenum() {return recipenum;}
	public double getDistance() {return distance;}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrlForProfile() {
		return urlForProfile;
	}
	public void setUrlForProfile(String urlForProfile) {
		this.urlForProfile = urlForProfile;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public int getUserid() {
		return userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public ArrayList<Recipe> getMyCollections() {
		return myCollections;
	}

	public void setRecipenum(int n) {this.recipenum = n;}
	public void setDistance(double distance) {this.distance = distance;}

	public void setUserid(int userid) {
		this.userid = userid;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}


