package ws.remote;

import model.User;
import model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhengyu on 15/11/12.
 * This interface includes all user action
 */
public interface UserAction {
    String register();
    String login();
    String updateUser();
    String updateRecipe();
    String uploadNewRecipe();
    String addCollection();

    // get user all recipe they create
    HashMap<Integer, Recipe> getmyRecipe();

    // follow user, add relation into relation table
    String Follow(int userID, int followID);

    // get all friend user followed
    ArrayList<Integer> getAllFriend(int userid);
    ArrayList<Integer> getMyCollection(int userid);

    // get all recipe in database
    HashMap<Integer, Recipe> getAllRecipes();

    // get all user information in database
    HashMap<Integer, User> getAllUser();

    // get the detail information about user according username
    User getUserInfo(String username);

    // get local recipe and local cook according the location information
    //HashMap<Integer, Recipe> getLocalRecipe(String coordinary);
    //HashMap<Integer, User> getLocalCook(String coordinary);
    String deteleMyRecipe(int userid, int recipeid);
}
