package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import exception.InputException;
import model.User;
import ws.remote.DefaultSocketClient;

/**
 * This class file is to handleuser login input.
 */
public class Login extends Activity {

    private Button login;
    private Button register;
    private EditText userName;
    private EditText pwd;
    private SharedPreferences sp;
    private User user;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_login);


        //Get the button modules
        initViews();
        //Add listeners
        addListeners();

        //At beginning, I need get the local user name and password.
        String localUserName = sp.getString("USER_NAME","");
        String localPwd = sp.getString("PWD","");

        if (!localUserName.equals("")  && !localPwd .equals("")) {
            userName.setText(sp.getString("USER_NAME",""));
            pwd.setText(sp.getString("PWD",""));
            SharedPreferences.Editor editor = sp.edit();
            String location = getLocation();
            editor.putString("LOCATION", location);
            editor.apply();
            if (loginPro2()) {
                //Start next activity
                user.setLocation(location);
                DefaultSocketClient defaultSocketClient = new DefaultSocketClient(4, user);
                defaultSocketClient.start();
                while (defaultSocketClient.isAlive()) {System.out.print("");}

                Intent intent = new Intent();
                Bundle data = new Bundle();
                data.putSerializable("USER", user);
                intent.putExtras(data);
                intent.setClass(Login.this, MainMenu.class);
                startActivity(intent);
                //Finish current activity.
                finish();
            } else {
                Toast.makeText(Login.this, "Incorrect username or password!!", Toast.LENGTH_LONG).show();
            }//If loginPro();
        }
    }


    /**
     * This method is to initialize all modules.
     */
    public void initViews() {
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        userName = (EditText) findViewById(R.id.username);
        pwd = (EditText) findViewById(R.id.pwd);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        pwd.setInputType(0x81);
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }


    /**
     * This method is to add listeners to all modules.
     */
    public void addListeners() {
        //Add listener to the login button
        //Handling user login case.
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //If local user name and pwd don't exist, get user input and do query to remote database
                        //to get user name and password.
                        //So at beginning, I need get the local user name and password.
                        String localUserName = sp.getString("USER_NAME", "");
                        String localPwd = sp.getString("PWD", "");

                        //If local user name doesn't exist, ask user input name and password.
                        if (localUserName.equals("") || localPwd.equals("")) {
                            //First validate the user input format.
                            if (validate()) {
                                //Then I need to check the accuracy of user name and password
                                if (loginPro1()) {
                                    //Store the user name and pwd automatically.
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("USER_NAME", user.getUserName());
                                    editor.putString("PWD", user.getPassWord());
                                    editor.putString("EMAIL", user.getEmail());
                                    editor.putString("DESCRIPTION", user.getDescription());
                                    editor.putString("IMAGE", user.getUrlForProfile());
                                    editor.putInt("USER_ID", user.getUserid());
                                    editor.putInt("RECIPE_NUMBER", user.getRecipenum());
                                    String location = getLocation();
                                    editor.putString("LOCATION", location);
                                    editor.apply();
                                    //Start next activity
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("USER", user);
                                    Intent intent = new Intent();
                                    intent.setClass(Login.this, MainMenu.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    //Finish current activity.
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Incorrect username or password!!", Toast.LENGTH_LONG).show();
                                }//If loginPro();
                            } else {
                                Toast.makeText(Login.this, "Invalid input!!", Toast.LENGTH_LONG).show();
                            }//If validate()
                        }
                    }
                }
        );
        //Add listener to the register page
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(Login.this, Register.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }


    /**
     * This method is to validate the input user name and password
     * @return boolean
     */
    public boolean validate() {
        String localUserName = userName.getText().toString().trim();
        String localPwd = pwd.getText().toString().trim();
        if (localUserName.equals("")) {
            Toast.makeText(Login.this, "User name is necessary", Toast.LENGTH_LONG).show();
            return false;
        }
        if (localPwd.equals("")) {
            Toast.makeText(Login.this, "Password is necessary", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /**
     * This method is to verify the input user name and pwd to the remote user name and pwd.
     * @return boolean
     */
    public boolean loginPro1() {
        String name = userName.getText().toString().trim();
        String password = pwd.getText().toString().trim();
        User loginUser = new User();
        loginUser.setPassWord(password);
        loginUser.setUserName(name);

        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(2, loginUser);
        defaultSocketClient.start();

        while(defaultSocketClient.isAlive()) {
            System.out.print("");
        }
        //Return success or not
        String result = defaultSocketClient.getResult();

        if (result.equals("Success")) {
            //If login successfully, get the user information.
            DefaultSocketClient ds = new DefaultSocketClient(10, name);
            ds.start();
            while(ds.isAlive()) {
                System.out.print("");
            }
            user = ds.getUser();
            Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_LONG).show();
            return true;
        }
        else {
            //new InputException().show(this, result);
            new InputException().fix(this, result);
            //Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
            return false;
        }

    }

    /**
     * This method is to check the local user name with the shared preferences user info.
     * @return boolean
     */
    public boolean loginPro2() {
        String localUserName = sp.getString("USER_NAME", "");
        String localPwd = sp.getString("PWD", "");
        String[] localInfo = getLocalInfo().split(",");
        String remoteUserName = localInfo[0];
        String remotePwd = localInfo[1];

        if (localUserName.equals(remoteUserName) && localPwd.equals(remotePwd)) {
            DefaultSocketClient defaultSocketClient = new DefaultSocketClient(10, localUserName);
            defaultSocketClient.start();
            while (defaultSocketClient.isAlive()) {System.out.print("");}
            user = defaultSocketClient.getUser();
            return true;
        }
        return false;
    }

    /**
     * This method is to return login position.
     * @return String
     */
    private String getLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String position = ";";
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            position = String.valueOf(longitude) + ";" + String.valueOf(latitude);
        }
        if (position.equals(";")) {
            position = "-79.94395695807664;40.45370917737972";
        }
        return position;
    }
//    /**
//     * This method is to get remote user name and pwd.
//     * @return
//     */
//    public String getRemoteInfo() {
//
//        String localUserName = sp.getString("USER_NAME","");
//        String localPwd = sp.getString("PWD","");
//        return localUserName + "," + localPwd;
//    }

    /**
     * This method is to get remote user name and pwd.
     * @return String
     */
    public String getLocalInfo() {
        //Get user info from shared preferences.
        String localUserName = sp.getString("USER_NAME","");
        String localPwd = sp.getString("PWD","");
        return localUserName + "," + localPwd;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
