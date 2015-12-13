package model;

/**
 * The information when user get other user information
 */

import java.util.ArrayList;


public class UserReturnInformation {
		private String userName;
		private String email;
		private String urlForProfile;
		private ArrayList<Recipe> myRecipes;
		
		
		public String getUserName() {
            return userName;
        }
		public String getEmail() {
            return email;   
        }
		public String getUrlForProfile() {
            return urlForProfile;
        }
		
		public ArrayList<Recipe> getUserRecipes() {
            return myRecipes;
        }
		public Recipe getRecipe(String userName, String recipeName) {
            return null;
        }
		
}
