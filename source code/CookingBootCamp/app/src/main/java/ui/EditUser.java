package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InputException;
import model.User;
import ws.remote.DefaultSocketClient;

public class EditUser extends Activity {

    private EditText oldPwd;
    private EditText newPwd;
    private EditText newConfirmPwd;
    private EditText newEmail;
    private ImageView newPhoto;
    private Button submit;
    private Button camera;
    private Button folder;

    private SharedPreferences sp;
    private Uri uri;
    private byte[] bitmapByte;

    private static final int TAKE_PHOTO = 1;
    private static final int CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_edit_user);

        //First initialize all modules
        initViews();
        //Add listeners
        addListeners();

    }

    /**
     * This method is to initialize all modules
     */
    public void initViews() {
        oldPwd = (EditText) findViewById(R.id.oldpasswordedit);
        newPwd = (EditText) findViewById(R.id.newpasswordedit);
        newConfirmPwd = (EditText) findViewById(R.id.comfirmpasswordedit);
        newEmail = (EditText) findViewById(R.id.emailedit);
        newPhoto = (ImageView) findViewById(R.id.icon);
        camera = (Button) findViewById(R.id.changebutton);
        folder = (Button) findViewById(R.id.fromFolderEdit);
        submit = (Button) findViewById(R.id.submit);

        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);

        //First set the original photo.
        setOriginalPhoto();
    }

    /**
     * This method is to add listeners to all modules.
     */
    public void addListeners() {

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create file object to store the picture.
                File outPutImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                //IF already exitsed, delete the local picture.
                try {
                    if (outPutImage.exists()) {
                        outPutImage.delete();
                    }
                    outPutImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uri = Uri.fromFile(outPutImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CROP);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("PWD", newPwd.getText().toString().trim());
                    editor.putString("EMAIL", newEmail.getText().toString().trim());
                    editor.apply();
                    //Update user information into remote database.
                    updateRemoteDB();
                    Intent intent = new Intent(EditUser.this, UserInformation.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    public void updateRemoteDB() {
        String name = sp.getString("USER_NAME", "");
        String pwd = sp.getString("PWD","");
        String email = sp.getString("EMAIL","");
        String desc = sp.getString("DESCRIPTION","");
        String imageString = sp.getString("IMAGE","");
        int recipe_number = sp.getInt("RECIPE_NUMBER", 0);
        String location = sp.getString("LOCATION", "");

        User user = new User(name, pwd, desc, email, imageString, location, recipe_number);

        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(4, user);
        defaultSocketClient.start();

        while (defaultSocketClient.isAlive()) {System.out.print("");}

        String result = defaultSocketClient.getResult();
        if (result.equals("Success")) {
            Toast.makeText(EditUser.this, "Update successfully", Toast.LENGTH_LONG).show();
        } else {
            new InputException().show(this, result);
            new InputException().fix(this, result);
            Toast.makeText(EditUser.this, result, Toast.LENGTH_LONG).show();
        }

    }
    /**
     * This method is to validate whether the new information match format.
     * @return boolean
     */
    public boolean validate() {
        String oldPassword = oldPwd.getText().toString().trim();
        String newPassword = newPwd.getText().toString().trim();
        String newConfirmWord = newConfirmPwd.getText().toString().trim();
        String email = newEmail.getText().toString().trim();
        String SPOldPwd = sp.getString("PWD","");

        if (oldPassword.equals("") || !oldPassword.equals(SPOldPwd)) {
            //Toast.makeText(EditUser.this, "Wrong old password", Toast.LENGTH_LONG).show();
            //new InputException().show(this, "Wrong email format");
            new InputException().fix(this, "Wrong email format");
            return false;
        }

        if (newPassword.equals("") || newConfirmWord.equals("") || !newConfirmWord.equals(newPassword)) {
            new InputException().show(this, "Wrong email format");
            //new InputException().fix(this, "Wrong email format");
            //Toast.makeText(EditUser.this, "New password don't match",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!checkEmailFormat(email)) {
            //new InputException().show(this, "Wrong email format");
            new InputException().fix(this, "Wrong email format");
            //Toast.makeText(EditUser.this, "Wrong email format", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * This method is to check whether email match email format.
     * @param email "email"
     * @return boolean
     */
    public boolean checkEmailFormat(String email) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*" + "@"
                + "\\w+([-.]\\w+)*" + "\\." + "\\w+([-.]\\w+)*");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(uri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, CROP);
                }
                break;
            case CROP:
                if (resultCode == RESULT_OK) {
                    try {
                        uri = data.getData();
                        //Transfer picture to next page.
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(uri));
                        bitmap = scaleDownBitmap(bitmap, 100, this);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, baos);
                        bitmapByte = baos.toByteArray();
                        saveImageBitmap();
                        newPhoto.setImageBitmap(bitmap);
                    }catch (Exception e) {
                        Log.d("Take", "Error");
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * This method is to add image string in local shared preference.
     */
    public void saveImageBitmap() {
        SharedPreferences.Editor editor = sp.edit();
        String imageString = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
        editor.putString("IMAGE", imageString);
        editor.apply();
    }

    /**
     * This method is to scale down bitmap in case of big image.
     * @param photo "photo"
     * @param newHeight "newHeight"
     * @param context "Context"
     * @return bitmap
     */
    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    /**
     * This method is to set original photo.
     */
    public void setOriginalPhoto() {
        String imageString = sp.getString("IMAGE", "");
        byte[] bitmapByte = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(bitmapByte);
        Bitmap bitmap = BitmapFactory.decodeStream(bais);
        newPhoto.setImageBitmap(bitmap);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_user_page, menu);
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
