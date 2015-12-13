package model;


/**
 * User class file, get and set all information about user
 */
import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable{

	private static final long serialVersionUID = 0;
	private int userid;
	private String userName;
	private String passWord;
	private String description;
	private ArrayList<Recipe> myCollections;
	private ArrayList<Recipe> myRecipes;
	private ArrayList<UserReturnInformation> Follow;
	private String email;
	private String urlForProfile;
	private String location;
	private int recipenum;
	
	
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
	
	public int getRecipenum() {
		return recipenum;
	}

	public void setRecipenum(int recipenum) {
		this.recipenum = recipenum;
	}

	public User() {
		
	}
	
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
	public void setMyCollections(ArrayList<Recipe> myCollections) {
		this.myCollections = myCollections;
	}
	public ArrayList<Recipe> getMyRecipes() {
		return myRecipes;
	}
	public void setMyRecipes(ArrayList<Recipe> myRecipes) {
		this.myRecipes = myRecipes;
	}
	public ArrayList<UserReturnInformation> getFollow() {
		return Follow;
	}
	public void setFollow(ArrayList<UserReturnInformation> follow) {
		Follow = follow;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	public Recipe getRecipeFromCollections(int recipeID) {
		for(Recipe rec : myCollections){
			if(rec.getRecipeID() == recipeID) {
				return rec;
			}
		}
		return null;
    }
	public Recipe getRecipeFromMyRecipes(int recipeID) {
		for(Recipe rec : myCollections){
			if(rec.getRecipeID() == recipeID) {
				return rec;
			}
		}
		return null;
    }

}


