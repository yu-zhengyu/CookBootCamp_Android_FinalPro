package dblayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Recipe;
import model.User;
/**
 * This Class help user interact with Database
 * @author zhengyu
 *
 */


public class DatabaseIO{
	private static final String URL_STRING = "jdbc:mysql://localhost:3306";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "zhengyu19910808";
	private Connection connection = null;
	private ReadRecord readRecord;
	private UpdateRecord updaterocord;
	private DeleteRecord deleteRecord;
	private CrateRecord crateaction;
	
	public DatabaseIO() {
		crateaction = new CrateRecord();
		readRecord = new ReadRecord();
		updaterocord = new UpdateRecord();
		deleteRecord = new DeleteRecord();
		
	}
	
	public void connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL_STRING, USER_NAME, PASSWORD);
			System.out.println("Success connect Mysql server!");

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// add data into collection table
	public String addCollection(int userid, int recipeid) {
		connectDB();
		return crateaction.insertNewCollection(userid, recipeid, connection);
	}
	
	// add data into Like table
	public String addLike(int userid, int recipeid) {
		connectDB();
		return crateaction.insertNewLike(userid, recipeid, connection);
	}
	
	public String addFriend(int userid, int friendid) {
		connectDB();
		return crateaction.insertNewRalation(userid, friendid, connection);
	}
	
	public HashMap<Integer, Recipe> getMyRecipe(int userid) {
		connectDB();
		return readRecord.getmyRecipes(userid, connection);
	}
	
	public ArrayList<Integer> getMyFriend(int userid) {
		connectDB();
		return readRecord.getAllFriend(userid, connection);
	}
	
	public ArrayList<Integer> getMyCollection(int userid) {
		connectDB();
		return readRecord.getAllCollectionRecipes(userid, connection);
	}
	
	public String updateUser(User user) {
		connectDB();
		return updaterocord.updateUser(user, connection);
	}
	
	public String updateRecipe(Recipe recipe) {
		connectDB();
		return updaterocord.updateRecipe(recipe, connection);
	}
	
	public String insertNewUser(User user) {
		connectDB();
		String check = readRecord.getUserInfo(user.getUserName(), "0", connection);
		if (check == "Your Username is not correct")
			return crateaction.insertNewUser(user, connection);
		else
			return "The Username is already exist";
	}
	
	public String insertNewRecipe(Recipe recipe) {
		connectDB();
		return crateaction.uploadNewRecipe(recipe, connection);
	}
	
	public String login(User user) {
		connectDB();
		return readRecord.getUserInfo(user.getUserName(), 
				user.getPassWord(), connection);
	}
	
	public ArrayList<Recipe> getTop10Recipe() {
		connectDB();
		return readRecord.getTopTenRecipe(connection);
	}
	
	public ArrayList<User> getTop10User() {
		connectDB();
		return readRecord.getTopTenUser(connection);
	}
	
	public HashMap<Integer, Recipe> getallRecipe() {
		connectDB();
		return readRecord.getallRecipe(connection);
	}
	
	public HashMap<Integer, User> getallUser() {
		connectDB();
		return readRecord.getallUser(connection);
	}
	
	public User getuserinfo(String username) {
		connectDB();
		return readRecord.getUserInfo(username, connection);
	}
	
	
	public String deleteRecipeByID(int userid, int recipeid) {
		connectDB();
		return deleteRecord.deleteMyRecipe(userid, recipeid, connection);
	}
	
}
