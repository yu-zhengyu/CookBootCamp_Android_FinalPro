package ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiuyi.cookingbootcamp.R;

import java.io.ByteArrayInputStream;


/**
 * This class file mainly focuses on the user information and several operations
 * connected with user information. User can check user created recipes, user collections,
 * user friends user delete collections and user edition.
 */
public class UserInformation extends Activity {

    private Button myRecipe;
    private Button myCollection;
    private Button myFriend;
    private Button edit;
    private Button delete;
    private Button logout;

    private ImageView weixin;
    private ImageView contact;
    private ImageView find;
    private ImageView me;

    private ImageView image;
    private TextView userName;
    private TextView email;
    private TextView userDesc;


    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_user_information);

        //first initialize all modules.
        initViews();
        //Add listeners to all modules
        addListeners();
        //Set tab color
        setTabColor();
        //Set photo and basic information.
        getInfoAndSet();

    }


    /**
     * This method is to get all the activity modules.
     */
    public void initViews() {
        //Get all buttons
        myRecipe = (Button) findViewById(R.id.myrecipesbutton);
        myCollection = (Button) findViewById(R.id.myCollections);
        myFriend = (Button) findViewById(R.id.myFriendButton);
        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.deleteRecipe);
        logout = (Button) findViewById(R.id.logout);

        weixin = (ImageView) findViewById(R.id.weixinImageView);
        contact = (ImageView) findViewById(R.id.contractImageView);
        find = (ImageView) findViewById(R.id.findImageView);
        me = (ImageView) findViewById(R.id.meImageView);

        image = (ImageView) findViewById(R.id.icon);
        userName = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        userDesc = (TextView) findViewById(R.id.userDesc);

        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);

    }

    /**
     * This method is to add listeners to all modules
     */
    public void addListeners() {
        //Add listeners to all buttons
        myRecipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInformation.this, RecipeList.class);
                        Bundle data = new Bundle();
                        data.putString("option","myRecipes");
                        data.putString("title", "My recipes");
                        intent.putExtras(data);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.d("Here", e.toString());
                        }
                    }
                }
        );
        myCollection.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInformation.this, RecipeList.class);
                        Bundle data = new Bundle();
                        data.putString("option","myCollection");
                        data.putString("title","My Collections");
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                }
        );
        myFriend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInformation.this, Cook.class);
                        Bundle data = new Bundle();
                        data.putString("option","myFriends");
                        data.putString("title","My cook friends");
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                }
        );

        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInformation.this, EditUser.class);
                        startActivity(intent);
                    }
                }
        );

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInformation.this, Delete.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(UserInformation.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        weixin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInformation.this, MainMenu.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        contact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInformation.this, UploadRecipe.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        find.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInformation.this, Community.class);
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
        find.setImageResource(R.drawable.unselected_community);
        me.setImageResource(R.drawable.selected_me);
    }

    public void getInfoAndSet() {
        String name = sp.getString("USER_NAME","");
        String userEmail = sp.getString("EMAIL","");
        String imageString = sp.getString("IMAGE", "");
        String description = sp.getString("DESCRIPTION","");
        //int number = sp.getInt("RECIPE_NUMBER",0);

        byte[] bitmapByte = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(bitmapByte);
        Bitmap bitmap = BitmapFactory.decodeStream(bais);

        userName.setText(name);
        email.setText(userEmail);
        image.setImageBitmap(bitmap);
        userDesc.setText(description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_information_page, menu);
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
