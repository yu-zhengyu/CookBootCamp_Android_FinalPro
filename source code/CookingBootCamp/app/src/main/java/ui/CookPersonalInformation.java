package ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import java.util.ArrayList;

import model.User;
import ws.remote.DefaultSocketClient;

/**
 * This class file is mainly focuses on the detail of a specific cook
 * information. Also the page content is based on the user's operations.
 */
public class CookPersonalInformation extends Activity {

    private ImageView cookImage;
    private TextView cookName;
    private TextView cookEmail;
    private TextView cookDesc;
    private Button add;

    private User currentUser;
    //private SharedPreferences sp;
    //private String userName;
    private int userID;
    private int followID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_cook_personal_information);

        //Get data from last activity.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentUser = (User) bundle.getSerializable("cook");
        assert currentUser != null;
        followID = currentUser.getUserid();

        //Initialize all modules
        initViews();

        //Add listeners
        addListeners();


        //Set the user information page.
        getAndSet();

    }

    /**
     * This method is to register all listeners to all needed modules.
     */
    public void addListeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int followID = getFollowID();
                DefaultSocketClient defaultSocketClient = new DefaultSocketClient(6, userID, followID, 0);
                defaultSocketClient.start();
                while (defaultSocketClient.isAlive()) {System.out.print("");}

                String result = defaultSocketClient.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(CookPersonalInformation.this, "Following successfully", Toast.LENGTH_LONG).show();
                    add.setText("added");
                    add.setClickable(false);
                }
            }
        });
    }


    /**
     * Set basic cook information. And initialize the page content.
     */
    public void getAndSet() {

        cookName.setText(currentUser.getUserName());
        cookEmail.setText(currentUser.getEmail());
        cookDesc.setText(currentUser.getDescription());
        String imageString = currentUser.getUrlForProfile();
        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = null;

        if (bytes != null && bytes.length > 0) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        cookImage.setImageBitmap(bitmap);

    }
    /**
     * This method is to get follow id.
     * @return int
     */
    public int getFollowID() {
        int result;
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(10, currentUser.getUserName());
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}
        User user = defaultSocketClient.getUser();
        result = user.getUserid();
        return result;
    }

    /**
     * This method is to initialize all modules.
     */
    public void initViews() {
        SharedPreferences sp;
        //String userName;
        cookImage = (ImageView) findViewById(R.id.cookImage);
        cookName = (TextView) findViewById(R.id.editName);
        cookEmail = (TextView) findViewById(R.id.editcookemail);
        cookDesc = (TextView) findViewById(R.id.editcookdescription);
        add = (Button) findViewById(R.id.addfriend);

        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        userID = sp.getInt("USER_ID",0);

        if (isFollowing()) {
            add.setClickable(false);
            add.setText("added");
        }
    }

    /**
     * This method is to check whether this cook is alrady my friend,
     * @return boolean
     */
    public boolean isFollowing() {
        ArrayList<Integer> friendID;
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(14, userID);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {
            System.out.print("");
        }
        friendID = defaultSocketClient.getMyfriendid();

        return friendID.size() != 0 && friendID.contains(followID);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cook_personal_information_page, menu);
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
