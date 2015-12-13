package server;

/**
 * @version 1.0
 * @author YuZheng
 * @Date 11/12/2015
 * These code is referenced from professor's ppt, it implement all action which running in a server.
 * For this class, the server would handle all information from user's Android phone, 
 * and send the corresponding information to user's Android phone
 */

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

import dblayout.DatabaseIO;
import model.Recipe;
import model.User;

public class DefaultSocketClient extends Thread implements
		SocketClientInterface, SocketClientConstants {

	private BufferedWriter writer;
	private Socket sock;
	private String strHost;
	private int iPort;
	private ObjectInputStream objectInputStream = null;
	private ObjectOutputStream objectOutputStream = null;
	private DatabaseIO db;
	private String result;
	
	public DefaultSocketClient(Socket s) {
		this.sock = s;
		this.iPort = iDAYTIME_PORT;
	}// constructor
	
	public DefaultSocketClient(Socket s, DatabaseIO db) {
		this.sock = s;
		this.iPort = iDAYTIME_PORT;
		this.db = db;
	}
	

	public void run() {
		if (openConnection()) {
			handleSession();
			closeSession();
		}
	}// run

	public boolean openConnection() {
		try {
			objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
			objectInputStream = new ObjectInputStream(sock.getInputStream());
		} catch (Exception e) {
			if (DEBUG)
				System.err.println("Unable to obtain stream to/from " + iPort);
			return false;
		}
		return true;
	}

	public void handleSession() {
		String strOutput = "";
		if (DEBUG)
			System.out.println("Handing in " + iPort);

		// telling the user connection is successful
		strOutput = "Have connected to " + iPort;
		try {
			objectOutputStream.writeObject(strOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {
			int comment;
			try {
				comment = (int) objectInputStream.readObject();

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			if (comment == 0) {
				System.out.println("One user has gone~~");
				break;
			}

			try {
				switch (comment) {
				// Register
				case 1:
					strOutput = "Start Register";
					sendgoningaction(strOutput);
					User regisetuser = null;
					regisetuser = (User) objectInputStream.readObject();
					result = db.insertNewUser(regisetuser);
					objectOutputStream.writeObject(result);
					break;
					
				case 2:
				// login
					strOutput = "Start Login";
					sendgoningaction(strOutput);
					User login;
					login = (User) objectInputStream.readObject();
					result = db.login(login);
					objectOutputStream.writeObject(result);
					break;
				case 3:
				// upload recipe
					strOutput = "Start Upload Recipe";
					sendgoningaction(strOutput);
					Recipe recipe;
					recipe = (Recipe) objectInputStream.readObject();
					result = db.insertNewRecipe(recipe);
					objectOutputStream.writeObject(result);
					break;
					
				//Update user
				case 4:
					strOutput = "Start Update User";
					sendgoningaction(strOutput);
					User newuser;
					newuser = (User) objectInputStream.readObject();
					result = db.updateUser(newuser);
					objectOutputStream.writeObject(result);
					break;
				
				// get my recipe
				case 5:
					strOutput = "Start get my recipe";
					sendgoningaction(strOutput);
					int userid;
					userid = (int) objectInputStream.readObject();
					HashMap<Integer, Recipe> resultrecipeList;
					resultrecipeList = db.getMyRecipe(userid);
					objectOutputStream.writeObject(resultrecipeList);
					break;
				case 6:
					strOutput = "Start add friend";
					sendgoningaction(strOutput);
					int addid;
					addid = (int) objectInputStream.readObject();
					int friendid;
					friendid = (int) objectInputStream.readObject();
					result = db.addFriend(addid, friendid);
					objectOutputStream.writeObject(result);
					break;
				case 7:
					strOutput = "Start add Collection";
					sendgoningaction(strOutput);
					int menid;
					menid = (int) objectInputStream.readObject();
					int recipeid;
					recipeid = (int) objectInputStream.readObject();
					result = db.addCollection(menid, recipeid);
					objectOutputStream.writeObject(result);
					break;
				case 8:
					strOutput = "Start get top 10 recipe";
					sendgoningaction(strOutput);
					ArrayList<Recipe> top10recipe = null;
					top10recipe = db.getTop10Recipe();
					objectOutputStream.writeObject(top10recipe);
					break;
				case 9:
					strOutput = "Start get top 10 cook";
					sendgoningaction(strOutput);
					ArrayList<User> top10user = null;
					top10user = db.getTop10User();
					objectOutputStream.writeObject(top10user);
					break;
				case 10:
					strOutput = "Start get user information";
					sendgoningaction(strOutput);
					String infoname;
					infoname = (String) objectInputStream.readObject();
					User infouser = null;
					infouser = db.getuserinfo(infoname);
					objectOutputStream.writeObject(infouser);
					break;
				case 11:
					strOutput = "Start get all user";
					sendgoningaction(strOutput);
					HashMap<Integer, User> alluser = null;
					alluser = db.getallUser();
					objectOutputStream.writeObject(alluser);
					break;
				case 12:
					strOutput = "Start get all recipe";
					sendgoningaction(strOutput);
					HashMap<Integer, Recipe> allrecipe = null;
					allrecipe = db.getallRecipe();
					objectOutputStream.writeObject(allrecipe);
					break;
				case 13:
					strOutput = "Start delete Collection";
					sendgoningaction(strOutput);
					int deleterecipeid = 0;
					int deleteuserid = 0;
					deleteuserid = (int)objectInputStream.readObject();
					deleterecipeid = (int)objectInputStream.readObject();
					strOutput = db.deleteRecipeByID(deleteuserid, deleterecipeid);
					objectOutputStream.writeObject(strOutput);
					break;
				case 14:
					strOutput = "Start get my friend id list";
					sendgoningaction(strOutput);
					int user_frinedid = (int)objectInputStream.readObject();
					ArrayList<Integer> frindidList = db.getMyFriend(user_frinedid);
					objectOutputStream.writeObject(frindidList);
					break;
				case 15:
					strOutput = "Start get my collection id list";
					sendgoningaction(strOutput);
					int user_collectionid = (int)objectInputStream.readObject();
					ArrayList<Integer> collectionidList = db.getMyCollection(user_collectionid);
					objectOutputStream.writeObject(collectionidList);
					break;
				case 16:
					strOutput = "Start update my recipe";
					sendgoningaction(strOutput);
					Recipe updaterecipe = null;
					updaterecipe = (Recipe) objectInputStream.readObject();
					result = db.updateRecipe(updaterecipe);
					objectOutputStream.writeObject(result);
					break;
				case 17:
					strOutput = "Start like a recipe";
					sendgoningaction(strOutput);
					int likeuserid = (int) objectInputStream.readObject();
					int likerecipeid = (int) objectInputStream.readObject();
					result = db.addLike(likeuserid, likerecipeid);
					objectOutputStream.writeObject(result);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public void sendOutput(String strOutput) {
		try {
			writer.write(strOutput, 0, strOutput.length());
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("Error writing to " + strHost);
		}
	}
	
	public void sendgoningaction(String action) {
		try {
			objectOutputStream.writeObject(action);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(action);
	}

	public void handleInput(String strInput) {
		System.out.println(strInput);
	}

	public void closeSession() {
		try {
			objectInputStream.close();
			objectInputStream.close();
			writer = null;
			sock.close();
		} catch (IOException e) {
			if (DEBUG)
				System.err.println("Error closing socket to " + strHost);
		}
	}

	public void setHost(String strHost) {
		this.strHost = strHost;
	}

	public void setPort(int iPort) {
		this.iPort = iPort;
	}

}// class DefaultSocketClient
