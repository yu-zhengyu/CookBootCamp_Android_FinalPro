package dblayout;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.Recipe;
import model.User;

/**
 * This class includes all action about update record into database
 * @author zhengyu
 *
 */

public class UpdateRecord {
	
	private static final String UPDATE_USER = "UPDATE COOKBOOTCAMP.USER"
			+ " SET password = ?, email= ?, description= ?, location = ?, image = ?"
			+ ", recipenum = ? WHERE username = ? ;";
	
	private static final String UPDATE_RECIPE = "UPDATE COOKBOOTCAMP.RECIPE"
			+ " SET likecount=? WHERE recipeid=?;";
	/**
	 * 
	 * @param user
	 * @param connection
	 * @return
	 */
	public String updateUser(User user, Connection connection) {
		try {
			// change value
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
			preparedStatement.setString(1, user.getPassWord());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getDescription());
			preparedStatement.setString(4, user.getLocation());
			preparedStatement.setString(5, user.getUrlForProfile());
			preparedStatement.setInt(6, user.getRecipenum());
			
			// where value
			preparedStatement.setString(7, user.getUserName());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		
		} catch (Exception e) {
			System.out.println(e.toString());
			return "Unknown Error";
		}
		
		return "Success";
	}
	
	
	/**
	 * update recipe information
	 * @param recipeid
	 * @param descripe
	 */
	public String updateRecipe(Recipe recipe, Connection connection) {
		try {
			// change value
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RECIPE);
			preparedStatement.setInt(1, recipe.getLikeNumber());
			// where value
			preparedStatement.setInt(2, recipe.getRecipeID());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		
		} catch (Exception e) {
			System.out.println(e.toString());
			return "Unknown Error";
		}
		
		return "Success";
	}

}
