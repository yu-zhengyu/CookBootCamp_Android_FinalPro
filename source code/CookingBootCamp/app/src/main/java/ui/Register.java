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
import android.widget.EditText;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.User;
import ws.remote.DefaultSocketClient;

public class Register extends Activity {

    private Button register;
    private Button cancel;
    private EditText userName;
    private EditText pwd;
    private EditText confirmPwd;
    private EditText email;
    private EditText description;

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_register);

        //Get the buttons
        initViews();
        //Add listeners
        addListeners();

    }

    /**
     * This method is to initialize all modules
     */
    public void initViews() {
        register = (Button) findViewById(R.id.rregister);
        cancel = (Button) findViewById(R.id.cancel);
        userName = (EditText) findViewById(R.id.rusername);
        pwd = (EditText) findViewById(R.id.rpwd);
        confirmPwd = (EditText) findViewById(R.id.rcpwd);
        email = (EditText) findViewById(R.id.email);
        description = (EditText) findViewById(R.id.description);

        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);

        pwd.setInputType(0x81);
        confirmPwd.setInputType(0x81);
    }

    /**
     * This method is to add listeners to all modules.
     */
    public void addListeners() {
        //Add listener to register
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validate() && isOccupied(userName.getText().toString().trim())) {
                            String name = userName.getText().toString().trim();
                            String password = pwd.getText().toString().trim();
                            String userEmail = email.getText().toString().trim();
                            String desc = description.getText().toString().trim();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", name);
                            editor.putString("PWD", password);
                            editor.putString("EMAIL", userEmail);
                            editor.putString("DESCRIPTION", desc);
                            editor.apply();
                            //Add new user
                            User user = new User();
                            user.setUserName(name);
                            user.setPassWord(password);
                            user.setEmail(userEmail);
                            user.setDescription(desc);

                            Bundle data = new Bundle();
                            data.putSerializable("USER", user);
                            Toast.makeText(Register.this, "Basic information added", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(Register.this, UploadProfilePhoto.class);
                            intent.putExtras(data);
                            startActivity(intent);
                            //Finish current activity.
                            finish();
                        }

                    }
                }
        );

        //Add listener to cancel
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(Register.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }


    /**
     * This function is to check whether user name has already been registered.
     * @param name name
     * @return boolean
     */
    public boolean isOccupied(String name) {
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(10, name);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}

        User user = defaultSocketClient.getUser();

        if (user.getUserName() == null || user.getUserName().equals("")) {
            return true;
        } else {
            Toast.makeText(Register.this, "User name has been registerd!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * This method is to check whether user registration information matches format.
     * @return boolean
     */
    public boolean validate() {
        String name = userName.getText().toString().trim();
        String password = pwd.getText().toString().trim();
        String cPassword = confirmPwd.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String desc = description.getText().toString().trim();

        if (name.equals("")) {
            Toast.makeText(Register.this, "Invalid user name!!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.equals("")) {
            Toast.makeText(Register.this, "Invalid password", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(cPassword)) {
            Toast.makeText(Register.this, "Password don't match!!",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!checkEmail(userEmail)) {
            Toast.makeText(Register.this, "Invalid email!!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (desc.equals("")) {
            Toast.makeText(Register.this, "Invalid description!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * This method is to check email format.
     * @param email email
     * @return boolean
     */
    public boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*" + "@"
                + "\\w+([-.]\\w+)*" + "\\." + "\\w+([-.]\\w+)*");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_page, menu);
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
