/**
 * The main fucntions how to deal with server
 * Created by zhengyu on 15/11/12.
 */
package ws.remote;

import android.util.Log;

import model.Recipe;
import model.User;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class DefaultSocketClient extends Thread implements
		SocketClientInterface, SocketClientConstants, UserAction {
	private Socket sock;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream = null;
	private HashMap<Integer, User> Userlist;
	private HashMap<Integer, Recipe> recipeList;
	private String result = "";
	private int opt;
	private User user;
	private Recipe recipe;
	private int userid, followid, recipeid;
	private ArrayList<Integer> myfriendid;
	private ArrayList<Integer> mycollection;
	private String username;
	private ArrayList<Recipe> top10recipe;
	private ArrayList<User> top10users;

	
	
	public ArrayList<Recipe> getTop10recipelist() {
		return top10recipe;
	}

	public ArrayList<User> getTop10userslist() {
		return top10users;
	}

	public HashMap<Integer, User> getUserlist() {
		return Userlist;
	}

	public ArrayList<Integer> getMyfriendid() {
		return myfriendid;
	}

	public ArrayList<Integer> getMycollection() {
		return mycollection;
	}

	public HashMap<Integer, Recipe> getRecipeList() {
		return recipeList;
	}
	
	public DefaultSocketClient(int opt, String name) {
		this.opt = opt;
		this.username = name;
        objectInputStream = null;
    }
	
	public DefaultSocketClient(int opt, int userid) {
		this.opt = opt;
		this.userid = userid;
        objectInputStream = null;
    }
	
	public DefaultSocketClient(int opt, int userid, int followid, int recipeid) {
		this.opt = opt;
		this.userid = userid;
		this.followid = followid;
		this.recipeid = recipeid;
        objectInputStream = null;
    } // for follow function and add collection funcion.
	
	public DefaultSocketClient(int opt, User user) {
		this.opt = opt;
		this.user = user;
		this.userid = user.getUserid();
        objectInputStream = null;
    } // about user
	
	public DefaultSocketClient(int opt, Recipe recipe) {
		this.opt = opt;
		this.recipe = recipe;
        objectInputStream = null;
    }

	//public void setOpt(int opt) {this.opt = opt;}
	public DefaultSocketClient(int opt) {
		this.opt = opt;
        objectInputStream = null;
    }
	
	public String getResult() {
		return result;
	}
	
	public User getUser() {
		return user;
	}
	
	public Recipe getRecipe() {
		return recipe;
	}

	public void run() {
		if (openConnection()) {
			handleSession();
			closeSession();
		}
	}// run

	public boolean openConnection() {

		int iPort;
		try {
			iPort = iDAYTIME_PORT;
			sock = new Socket(host, iPort);

			objectInputStream = new ObjectInputStream(sock.getInputStream());
			objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
		} catch (Exception e) {
			Log.d("Here", e.toString());
			if (DEBUG)
				System.err.println("Unable to obtain stream to/from " + iPort);
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void handleSession() {
		String serverinput;

		//int iPort;
		if (DEBUG)
			System.out.println("Handing in " + iDAYTIME_PORT);

		// telling the user connection is successful
		try {
			serverinput = (String) objectInputStream.readObject();
			System.out.println("Server: " + serverinput);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		try {
			switch (opt) {
			// register
			case 1:
				serverinput = register();
				result = serverinput;
				break;
			// login
			case 2:
				serverinput = login();
				result = serverinput;
				break;
			// upload Recipe
			case 3:
				serverinput = uploadNewRecipe();
				result = serverinput;
				break;
			// update user
			case 4:
				serverinput = updateUser();
				result = serverinput;
				break;
			//get my recipe
			case 5:
				recipeList = getmyRecipe();
				break;
			// follow
			case 6:
				result = Follow(userid, followid);
				break;
			// add collection
			case 7:
				result = addCollection();
				break;
			// get Top 10 Recipe
			case 8:
				top10recipe = getTop10Recipes();
				break;
			// get Top 10 cook
			case 9:
				top10users = gettop10user();
				break;
			// get user information
			case 10:
				user = getUserInfo(username);
				break;
			// get All User from user Table
			case 11:
				Userlist = getAllUser();
				break;
			// get All recipe from recipe Table
			case 12:
				recipeList = getAllRecipes();
				break;
			// delete collection By id
			case 13:
				result = deteleMyRecipe(userid, recipeid);
				break;
			// get my friend id list
			case 14:
				myfriendid = getAllFriend(userid);
				break;
			// get my collection id list
			case 15:
				mycollection = getMyCollection(userid);
				break;
			// like
			case 16:
				result = updateRecipe();
				break;
            case 17:
                result = Follow(userid, recipeid);
                break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// close sesstion
	public void closeSession() {
		try {
			sock.close();
			objectOutputStream.close();
			objectInputStream.close();
			
		} catch (IOException e) {
			//String host;
			if (DEBUG)
				System.err.println("Error closing socket to " + host);
		}
	}

	@Override
	public String register() {
		String serverinput = "";
		try {
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(user);
			serverinput = (String) objectInputStream.readObject();
			result = serverinput;
			objectOutputStream.writeObject(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverinput;
	}
	
	@Override
	public String login() {
		String serverinput = "";
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(user);
			serverinput = (String) objectInputStream.readObject();
			result = serverinput;
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverinput;
	}

	@Override
	public String updateUser() {
		String serverinput = "";
		try {
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(user);
			serverinput = (String) objectInputStream.readObject();
			result = serverinput;
			objectOutputStream.writeObject(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return serverinput;	
	}
	
	@Override
	public String updateRecipe() {
		String serverinput = "";
		try {
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(recipe);
			serverinput = (String) objectInputStream.readObject();
			result = serverinput;
			objectOutputStream.writeObject(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return serverinput;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, Recipe> getmyRecipe() {
		String serverinput;
		try {
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(userid);
			recipeList = (HashMap<Integer, Recipe>)objectInputStream.readObject();
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return recipeList;
	}

	@Override
	public String addCollection() {
		String serverinput = "";
		try {
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(userid);
			objectOutputStream.writeObject(recipeid);
			serverinput = (String) objectInputStream.readObject();
			result = serverinput;
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return serverinput;
	}

	@Override
	public String uploadNewRecipe() {
		String serverinput = "";
		try {
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(recipe);
			serverinput = (String) objectInputStream.readObject();
			result = serverinput;
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverinput;		
	}


	@Override
	public String Follow(int userID, int followID) {
		String serverinput = "";
		try {
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(userID);
			objectOutputStream.writeObject(followID);
			serverinput = (String) objectInputStream.readObject();
			result = serverinput;
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return serverinput;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Integer> getAllFriend(int userid) {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(userid);
			myfriendid = (ArrayList<Integer>)objectInputStream.readObject(); 
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
			return myfriendid;
		}
		return myfriendid;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Integer> getMyCollection(int userid) {
		String serverinput;
		
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(userid);
			mycollection = (ArrayList<Integer>)objectInputStream.readObject(); 
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
			return mycollection;
		}
		return mycollection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, Recipe> getAllRecipes() {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			recipeList = (HashMap<Integer, Recipe>)objectInputStream.readObject();
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
			return recipeList;
		}
		return recipeList;
	}
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	public ArrayList<Recipe> getTop10Recipes() {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			top10recipe = (ArrayList<Recipe>)objectInputStream.readObject();
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
			return top10recipe;
		}
		return top10recipe;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, User> getAllUser() {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			Userlist = (HashMap<Integer, User>)objectInputStream.readObject(); 
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
			return Userlist;
		}
		return Userlist;
	} 
	
	@SuppressWarnings("unchecked")
	public ArrayList<User> gettop10user() {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			top10users = (ArrayList<User>)objectInputStream.readObject(); 
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
			return top10users;
		}
		return top10users;
	}
	
	@Override
	public User getUserInfo(String username) {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(username);
			user = (User)objectInputStream.readObject(); 
			objectOutputStream.writeObject(0);
			
		}catch (Exception e) {
			e.printStackTrace();
			return user;
		}
		return user;
	}

	@SuppressWarnings("unused unchecked")
	public HashMap<Integer, Recipe> getLocalRecipe(String coordinary) {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			recipeList = (HashMap<Integer, Recipe>)objectInputStream.readObject(); 
			objectOutputStream.writeObject(0);
		}catch (Exception e) {
			e.printStackTrace();
			return recipeList;
		}
		return recipeList;
	}

	@SuppressWarnings("unused unchecked")
	public HashMap<Integer, User> getLocalCook(String coordinary) {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			Userlist = (HashMap<Integer, User>)objectInputStream.readObject(); 
			objectOutputStream.writeObject(0);
		} catch (Exception e) {
			e.printStackTrace();
			return Userlist;
		}
		return Userlist;
	}
	
	@Override
	public String deteleMyRecipe(int userid, int recipeid) {
		String serverinput;
		try { 
			objectOutputStream.writeObject(opt);
			serverinput = (String) objectInputStream.readObject();
			System.out.println(serverinput);
			objectOutputStream.writeObject(userid);
			objectOutputStream.writeObject(recipeid);
			result = (String) objectInputStream.readObject();
			objectOutputStream.writeObject(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	// Below are all user action

}// class DefaultSocketClient
