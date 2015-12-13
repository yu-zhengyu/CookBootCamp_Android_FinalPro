package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import model.User;
import ws.remote.DefaultSocketClient;

@SuppressWarnings("unused")
/**
 * This method is mainly focuses on the conformation of user's photo.
 * This happens when user take his or her photo.
 * Next activity can be retake or confirm again.
 */
public class ConfirmPhoto extends Activity {

    private Button confirmPhoto;
    private Button retake;
    private ImageView image;
    private byte[] bitmapByte;
    private User user;

    private SharedPreferences sp;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_confirm_photo);

        //Initialize all modules
        initViews();
        //Add listeners
        addListeners();

        //Get picture data.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.get("USER");


        bitmapByte = intent.getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        image.setImageBitmap(bitmap);


    }

    /**
     * This method is to initialize modules
     */
    public void initViews() {
        confirmPhoto = (Button) findViewById(R.id.confirmphoto);
        retake = (Button) findViewById(R.id.retake);
        image = (ImageView) findViewById(R.id.image);
        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        locationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);
    }


    /**
     * Add listeners to all modules
     */
    public void addListeners() {
        //Add listener to both buttons
        confirmPhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Save image into local shared preference as string.
                        saveImageBitmap();
                        //Save user location
                        saveLocation();
                        //Then register this user information into remote database.
                        if (write2RemoteDB()) {
                            Bundle bundle = new Bundle();
                            //For new register user, his/her recipe number is 0.
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("RECIPE_NUMBER",0);
                            editor.apply();
                            bundle.putSerializable("USER", user);
                            Intent intent = new Intent();
                            intent.setClass(ConfirmPhoto.this, MainMenu.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
        );

        retake.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("USER", user);
                        Intent intent = new Intent();
                        intent.setClass(ConfirmPhoto.this, UploadProfilePhoto.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    /**
     * This method is to write new registered user information into remote database.
     */
    public boolean write2RemoteDB() {
        //Register to remote database.
        user.setRecipenum(0);
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(1, user);
        defaultSocketClient.start();

        while (defaultSocketClient.isAlive()) {System.out.print("");}

        String result = defaultSocketClient.getResult();

        if (result.equals("Success")) {
            Toast.makeText(ConfirmPhoto.this, "Register successfully", Toast.LENGTH_LONG).show();
            DefaultSocketClient ds = new DefaultSocketClient(10, user.getUserName());
            ds.start();
            while (ds.isAlive()) {System.out.print("");}
            User user = ds.getUser();
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("USER_ID", user.getUserid());
            editor.apply();
            return true;
        } else {
            Toast.makeText(ConfirmPhoto.this, result, Toast.LENGTH_LONG).show();
            return false;
        }

    }
    /**
     * This method is to add image string in local shared preference.
     */
    public void saveImageBitmap() {
        SharedPreferences.Editor editor = sp.edit();
        String imageString = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
        user.setUrlForProfile(imageString);
        editor.putString("IMAGE", imageString);
        editor.apply();
    }

    /**
     * This method is to save the user location information.
     */
    public void saveLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String position = String.valueOf(longitude) + ";" + String.valueOf(latitude);
            user.setLocation(position);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("LOCATION", position);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_photo_page, menu);
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
