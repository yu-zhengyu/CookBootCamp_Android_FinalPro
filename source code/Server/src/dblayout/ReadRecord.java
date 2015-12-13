package dblayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import model.Recipe;
import model.User;


/**
 * This class includes all action about read record into database
 * 
 * @author zhengyu
 * 
 */

public class ReadRecord {
	/**
	 * @param userid
	 * @return all user friend
	 */
	public String getUserInfo(String username, String password,
			Connection connection) {
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			String selectuserString = "SELECT * FROM COOKBOOTCAMP.USER where "
					+ "username='" + username + "';";
			result = statement.executeQuery(selectuserString);

			User user = new User();
			int count = 0;
			while (result.next()) {
				count++;
				user.setUserid(result.getInt(1));
				user.setRecipenum(result.getInt("recipenum"));
				user.setUserName(result.getString("username"));
				user.setEmail(result.getString("email"));
				user.setUrlForProfile(result.getString("image"));
				user.setDescription(result.getString("description"));
				user.setLocation(result.getString("location"));
				user.setPassWord(result.getString("password"));
			}
			System.out.println(user.getPassWord());
			if (count == 0) {
				return "Your Username is not correct";
			}
			if (!password.equals(user.getPassWord())) {
				return "Your password is not correct";
			}
			
			statement.close();
			connection.close();
			return "Success";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UnknowError";
		}
	}

	public HashMap<Integer, Recipe> getmyRecipes(int userid, Connection connection) {
		HashMap<Integer, Recipe> recipelist = new HashMap<Integer, Recipe>();
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String selectrecipeString = "SELECT * FROM COOKBOOTCAMP.RECIPE where "
					+ "authorid=" + userid + ";";
			result = statement.executeQuery(selectrecipeString);

			while (result.next()) {
				Recipe temp = new Recipe();
				temp.setRecipeID(result.getInt("recipeid"));
				temp.setAuthorID(result.getInt("authorid"));
				temp.setLikeNumber(result.getInt("likecount"));
				temp.setRecipeName(result.getString("name"));
				temp.setMaterial(result.getString("material"));
				temp.setDescription(result.getString("description"));
				temp.setSendTime(result.getString("uploadtime"));
				temp.setLocation(result.getString("location"));
				temp.setImageUri(result.getString("image"));

				recipelist.put(temp.getRecipeID(), temp);
			}
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return recipelist;
		}
		return recipelist;
	}

	/**
	 * 
	 * @param userid
	 * @return user's collection
	 */
	public ArrayList<Integer> getAllCollectionRecipes(int userid,
			Connection connection) {
		ArrayList<Integer> allCollectionid = new ArrayList<Integer>();
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String selectrecipeString = "SELECT * FROM COOKBOOTCAMP.COLLECTION where "
					+ "userid=" + userid + ";";
			result = statement.executeQuery(selectrecipeString);
			while (result.next()) {
				int recipeid = result.getInt("recipeid");
				allCollectionid.add(recipeid);
			}
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return allCollectionid;
		}
		return allCollectionid;
	}

	public ArrayList<Integer> getAllFriend(int userid, Connection connection) {
		ArrayList<Integer> allFriendid = new ArrayList<Integer>();
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String selectrecipeString = "SELECT * FROM COOKBOOTCAMP.FOLLOW where "
					+ "userid=" + userid + ";";
			result = statement.executeQuery(selectrecipeString);
			while (result.next()) {
				int recipeid = result.getInt("friendid");
				allFriendid.add(recipeid);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return allFriendid;
		}
		return allFriendid;
	}

	/**
	 * 
	 * @return get top ten recipe according to the count of like
	 */
	public  ArrayList<Recipe> getTopTenRecipe(Connection connection) {
		ArrayList<Recipe> recipelist = new  ArrayList<Recipe>();
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String selectrecipeString = "SELECT * from COOKBOOTCAMP.RECIPE order by likecount DESC limit 0,10;";
			result = statement.executeQuery(selectrecipeString);

			while (result.next()) {
				Recipe temp = new Recipe();
				temp.setRecipeID(result.getInt("recipeid"));
				temp.setAuthorID(result.getInt("authorid"));
				temp.setLikeNumber(result.getInt("likecount"));
				temp.setRecipeName(result.getString("name"));
				temp.setMaterial(result.getString("material"));
				temp.setDescription(result.getString("description"));
				temp.setSendTime(result.getString("uploadtime"));
				temp.setLocation(result.getString("location"));
				temp.setImageUri(result.getString("image"));

				recipelist.add(temp);
			}
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return recipelist;
		}
		return recipelist;
	}

	public ArrayList<User> getTopTenUser(Connection connection) {
		ArrayList<User> userlist = new ArrayList<User>();
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String selectrecipeString = "SELECT * from COOKBOOTCAMP.USER order by recipenum DESC limit 0,10;";
			result = statement.executeQuery(selectrecipeString);

			while (result.next()) {
				User user = new User();
				user.setUserid(result.getInt(1));
				user.setRecipenum(result.getInt("recipenum"));
				user.setUserName(result.getString("username"));
				user.setEmail(result.getString("email"));
				user.setUrlForProfile(result.getString("image"));
				user.setDescription(result.getString("description"));
				user.setLocation(result.getString("location"));
				user.setPassWord(result.getString("password"));
				userlist.add(user);
			}
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return userlist;
		}
		return userlist;
	}
	
	/**
	 * 
	 * @param userid
	 * @return get user own information
	 */
	public User getUserInfo(String username, Connection connection) {
		Statement statement = null;
		User user = new User();
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			String selectuserString = "SELECT * FROM COOKBOOTCAMP.USER where "
					+ "username='" + username + "';";
			result = statement.executeQuery(selectuserString);
			while (result.next()) {
				user.setUserid(result.getInt(1));
				user.setRecipenum(result.getInt("recipenum"));
				user.setUserName(result.getString("username"));
				user.setEmail(result.getString("email"));
				user.setUrlForProfile(result.getString("image"));
				user.setDescription(result.getString("description"));
				user.setLocation(result.getString("location"));
				user.setPassWord(result.getString("password"));
			}
			
			statement.close();
			connection.close();
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return user;
		}
	}

	public  HashMap<Integer, Recipe> getallRecipe(Connection connection) {
		HashMap<Integer, Recipe> recipelist = new  HashMap<Integer, Recipe>();
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String selectrecipeString = "SELECT * from COOKBOOTCAMP.RECIPE;";
			result = statement.executeQuery(selectrecipeString);

			while (result.next()) {
				Recipe temp = new Recipe();
				temp.setRecipeID(result.getInt("recipeid"));
				temp.setAuthorID(result.getInt("authorid"));
				temp.setLikeNumber(result.getInt("likecount"));
				temp.setRecipeName(result.getString("name"));
				temp.setMaterial(result.getString("material"));
				temp.setDescription(result.getString("description"));
				temp.setSendTime(result.getString("uploadtime"));
				temp.setLocation(result.getString("location"));
				temp.setImageUri(result.getString("image"));

				recipelist.put(temp.getRecipeID(), temp);
			}
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return recipelist;
		}
		return recipelist;
	}
	
	public HashMap<Integer, User> getallUser(Connection connection) {
		HashMap<Integer, User> userlist = new HashMap<Integer, User>();
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String selectrecipeString = "SELECT * from COOKBOOTCAMP.USER;";
			result = statement.executeQuery(selectrecipeString);

			while (result.next()) {
				User user = new User();
				user.setUserid(result.getInt(1));
				user.setRecipenum(result.getInt("recipenum"));
				user.setUserName(result.getString("username"));
				user.setEmail(result.getString("email"));
				user.setUrlForProfile(result.getString("image"));
				user.setDescription(result.getString("description"));
				user.setLocation(result.getString("location"));
				user.setPassWord(result.getString("password"));
				userlist.put(user.getUserid(), user);
			}
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return userlist;
		}
		return userlist;
	}

}
