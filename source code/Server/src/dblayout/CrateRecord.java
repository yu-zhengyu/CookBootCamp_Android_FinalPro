package dblayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Recipe;
import model.User;

/**
 * This class includes all action about create record into database
 * @author zhengyu
 *
 */

public class CrateRecord {
	/**
	 * User register
	 * @param user
	 */
	private static final String ADD_SENTENCE = "INSERT INTO COOKBOOTCAMP.USER VALUES (?,?,?,?,?,?,?,?);";
	private static final String ADD_INTORECIPE = "INSERT INTO COOKBOOTCAMP.RECIPE VALUES (?,?,?,?,?,?,?,?,?);";
	private static final String ADD_FOLLOW = "INSERT INTO COOKBOOTCAMP.FOLLOW VALUES (?,?,?);";
	private static final String ADD_COLLECTION = "INSERT INTO COOKBOOTCAMP.COLLECTION VALUES (?,?,?);";
	private static final String ADD_LIKE = "INSERT INTO COOKBOOTCAMP.LIKE VALUES (?,?,?);";
	
	public String insertNewUser(User user, Connection connection) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(ADD_SENTENCE);
			preparedStatement.setInt(1, 0);
			preparedStatement.setString(2, user.getUserName());
			preparedStatement.setString(3, user.getPassWord());
			preparedStatement.setString(4, user.getEmail());
			preparedStatement.setString(5, user.getDescription());
			preparedStatement.setString(6, user.getLocation());
			preparedStatement.setString(7, user.getUrlForProfile());
			preparedStatement.setInt(8, user.getRecipenum());
			
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
	 * user upload a new recipe into database
	 * @param recipe
	 */
	public String uploadNewRecipe(Recipe recipe, Connection connection){
		try {
			String selectuser = "SELECT recipenum FROM COOKBOOTCAMP.USER where userid="
					+ recipe.getAuthorID() + ";";
			Statement statement = null;
			statement = connection.createStatement();
			ResultSet result = null;
			result = statement.executeQuery(selectuser);
			int temprecipenum = 0;
			while (result.next()) {
				temprecipenum = result.getInt("recipenum");
			}
			
			temprecipenum++;
			String updateUserRecipeNum = "UPDATE COOKBOOTCAMP.USER"
					+ " SET recipenum=? WHERE userid=? ;";
			PreparedStatement updateusernum = connection.prepareStatement(updateUserRecipeNum);
			updateusernum.setInt(1, temprecipenum);
			updateusernum.setInt(2, recipe.getAuthorID());
			updateusernum.executeUpdate();
			updateusernum.close();
			
			PreparedStatement preparedStatement = connection.prepareStatement(ADD_INTORECIPE);
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, recipe.getAuthorID());
			preparedStatement.setLong(3, recipe.getLikeNumber());
			preparedStatement.setString(4, recipe.getRecipeName());
			preparedStatement.setString(5, recipe.getMaterial());
			preparedStatement.setString(6, recipe.getDescription());
			preparedStatement.setString(7, recipe.getSendTime());
			preparedStatement.setString(8, recipe.getLocation());
			System.out.println(recipe.getLocation());
			preparedStatement.setString(9, recipe.getImageUri());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
			return "Success";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Unknown Error";
		}
	}
	
	/**
	 * user follow a new friend
	 * @param userid
	 * @param followid
	 */
	public String insertNewRalation(int userid, int followid, Connection connection) {
		try {
			
			Statement statement = connection.createStatement();
			String selectrecipeString = "SELECT * FROM COOKBOOTCAMP.FOLLOW where "
					+ "userid=" + userid + " and friendid=" + followid + ";";
			ResultSet result = statement.executeQuery(selectrecipeString);
			int count = 0;
			while (result.next()) {
				count++;
			}
			if (count > 0) {
				return "You have already added this friend";
			}
			PreparedStatement preparedStatement = connection.prepareStatement(ADD_FOLLOW);
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, userid);
			preparedStatement.setInt(3, followid);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
			return "Success";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UnKnow Error";
		}
	}
	
	/**
	 * user collection a recipe
	 * @param userid
	 * @param recipeid
	 */
	public String insertNewCollection(int userid, int recipeid, Connection connection) {
		try {
			
			Statement statement = connection.createStatement();
			String selectrecipeString = "SELECT * FROM COOKBOOTCAMP.COLLECTION where "
					+ "userid=" + userid + " and recipeid=" + recipeid + ";";
			ResultSet result = statement.executeQuery(selectrecipeString);
			int count = 0;
			while (result.next()) {
				count++;
			}
			if (count > 0) {
				return "You have already added this Recipe";
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(ADD_COLLECTION);
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, userid);
			preparedStatement.setInt(3, recipeid);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
			return "Success";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UnKnow Error";
		}
	}
	
	public String insertNewLike(int userid, int recipeid, Connection connection) {
		try {
			
			Statement statement = connection.createStatement();
			String selectrecipeString = "SELECT * FROM COOKBOOTCAMP.LIKE where "
					+ "userid=" + userid + " and recipeid=" + recipeid + ";";
			ResultSet result = statement.executeQuery(selectrecipeString);
			int count = 0;
			while (result.next()) {
				count++;
			}
			if (count > 0) {
				return "You have already Like this Recipe";
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(ADD_LIKE);
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, userid);
			preparedStatement.setInt(3, recipeid);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
			return "Success";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UnKnow Error";
		}
	}
	
}
