package ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.qiuyi.cookingbootcamp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import model.Recipe;
import model.User;
import model.UserComparator;
import ws.remote.DefaultSocketClient;

@SuppressWarnings("unused")
/**
 * This class file focuses on the Community activity of this project.
 * In mainly includes two next two activities, local cooks activities and local recipes activities.
 */
public class Community extends Activity {

    private static final int CIRCLE = 6371;
    private Button recipe;
    private Button cook;
    private ImageView weixin;
    private ImageView contact;
    private ImageView me;
    private ImageView find;

    private ArrayList<Recipe> localRecipe;
    private ArrayList<User> localUser;

    //private SharedPreferences sp;
    //private String userName;
    private int userID;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_community);

        //First initialize modules
        initViews();
        //Add listeners to modules
        addListeners();
        //Set tab colors
        setTabColor();


    }

    /**
     * This method is to get all the activity modules.
     */
    public void initViews() {
        //Get all buttons
        String userName;
        SharedPreferences sp;
        recipe = (Button) findViewById(R.id.localrecipesbutton);
        cook = (Button) findViewById(R.id.localcookbutton);
        weixin = (ImageView) findViewById(R.id.weixinImageView);
        contact = (ImageView) findViewById(R.id.contractImageView);
        me = (ImageView) findViewById(R.id.meImageView);
        find = (ImageView) findViewById(R.id.findImageView);

        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        //userName = sp.getString("USER_NAME", "");
        userID = sp.getInt("USER_ID",0);
        location = sp.getString("LOCATION","");
    }

    /**
     * This mehthod is to add listeners to all modules
     */
    public void addListeners() {
        //Add listeners to both buttons
        recipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Community.this, LocalRecipe.class);
                        Bundle data = new Bundle();
                        data.putString("title","Local Recipes");
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                }
        );

        cook.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localUser = getLocalUsers();
                        Intent intent = new Intent(Community.this, SearchLocalCooks.class);
                        Bundle data = new Bundle();
                        data.putSerializable("cooks", localUser);
                        data.putString("title", "Local Cooks");
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                }
        );

        weixin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Community.this, MainMenu.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        contact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Community.this, UploadRecipe.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        me.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Community.this, UserInformation.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    /**
     * This method is to set tab colors to indicate which page user in.
     */
    public void setTabColor() {
        weixin.setImageResource(R.drawable.unselected_main);
        contact.setImageResource(R.drawable.unselected_upload);
        find.setImageResource(R.drawable.selected_community);
        me.setImageResource(R.drawable.unselected_me);
    }

    /**
     * This method is to sort all recipes according to their locations.
     * @param users "users"
     * @return ArrayList<User>
     */
    public ArrayList<User> calculateLocationForUser(ArrayList<User> users) {
        String[] position = location.split(";");
        double myLongitude = Double.parseDouble(position[0]);
        double myLatitude = Double.parseDouble(position[1]);
        for (User user : users) {
            String[] recipePosition = user.getLocation().split(";");
            double longitude = Double.parseDouble(recipePosition[0]);
            double latitude = Double.parseDouble(recipePosition[1]);
            double temp = haversine(latitude-myLatitude) +
                    Math.cos(myLatitude)*Math.cos(latitude) * haversine(Math.log(Math.abs(longitude)) - Math.log(Math.abs(myLongitude)));
            user.setDistance(CIRCLE * inverseHaversine(temp));
        }
        Collections.sort(users, new UserComparator());
        return users;
    }

    /**
     * This function is to calculate the distance.
     * @param a "para"
     * @return double
     */
    public double haversine(double a) {
        return ((1 - Math.cos(a))/2);
    }

    /**
     * This method is to calculate the inverse cos function.
     * @param a "para"
     * @return double
     */
    public double inverseHaversine(double a) {
        return Math.acos(1 - 2*a);
    }
    /**
     * This method is to get local users from database.
     * @return ArrayList<User>
     */
    public ArrayList<User> getLocalUsers() {
        ArrayList<User> result;
        ArrayList<User> users = new ArrayList<>();
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(11);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}

        HashMap<Integer, User> userList = defaultSocketClient.getUserlist();

        for (Object o : userList.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            User user = (User) entry.getValue();
            if (userID != user.getUserid()) users.add(user);
        }

        result = calculateLocationForUser(users);

        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_community_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
