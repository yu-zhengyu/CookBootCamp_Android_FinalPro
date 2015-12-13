package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import exception.InputException;
import model.Recipe;
import ws.remote.DefaultSocketClient;

import static android.util.Base64.*;


public class UploadRecipe extends Activity {

    private ImageView weixin;
    private ImageView find;
    private ImageView me;
    private ImageView contact;


    private Button upload;
    private Button cancel;
    private Button takePhoto;

    private EditText name;
    private ImageView image;
    private EditText materials;
    private EditText description;

    private LocationManager locationManager;
    private final static int TAKE_PHOTO = 0;
    private final static int CROP = 2;
    private Uri uri;
    private Bitmap bitmap;

    private SharedPreferences sp;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_upload_recipe);

        //First I need to initialize modules
        initViews();

        //Then I need add listeners to all these modules.
        addListeners();

        //Set tab color.
        setTabColor();

    }

    @Override
    public void onStart() {
        super.onStart();
        name.setText("");
        image.setImageBitmap(null);
        materials.setText("");
        description.setText("");
    }

    /**
     * This method is to get all the activity modules.
     */
    public void initViews() {
        weixin = (ImageView) findViewById(R.id.weixinImageView);
        find = (ImageView) findViewById(R.id.findImageView);
        me = (ImageView) findViewById(R.id.meImageView);
        contact = (ImageView) findViewById(R.id.contractImageView);

        upload = (Button) findViewById(R.id.uploadButton);
        cancel = (Button) findViewById(R.id.cancelButton);
        takePhoto = (Button) findViewById(R.id.upimagebutton);

        name = (EditText) findViewById(R.id.Name);
        image = (ImageView) findViewById(R.id.RecipeImage);
        materials = (EditText) findViewById(R.id.materialsEdit);
        description = (EditText) findViewById(R.id.descriptionEdit);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //ArrayList<Recipe> recipes = new ArrayList<>();

        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        //String userName = sp.getString("USER_NAME", "");
        userID = sp.getInt("USER_ID", 0);
    }

    /**
     * This mehthod is to add listeners to all modules
     */
    public void addListeners() {
        weixin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UploadRecipe.this, MainMenu.class);
                        startActivity(intent);
                    }
                }
        );

        me.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UploadRecipe.this, UserInformation.class);
                        startActivity(intent);
                    }
                }
        );

        find.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UploadRecipe.this, Community.class);
                        startActivity(intent);
                    }
                }
        );

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Take photo.
                File image = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                try {
                    if (image.exists()) {
                        image.delete();
                    }
                    image.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uri = Uri.fromFile(image);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (addNewRecipe()) {
                                //recipes = getUserRecipes();
                                Intent intent = new Intent(UploadRecipe.this, RecipeList.class);
                                Bundle data = new Bundle();
                                data.putString("option", "myRecipes");
                                data.putString("title", "My recipes");
                                //data.putSerializable("recipes", recipes);
                                Toast.makeText(UploadRecipe.this, "Upload recipe successfully", Toast.LENGTH_LONG).show();
                                intent.putExtras(data);
                                startActivity(intent);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(UploadRecipe.this, "Invalid recipe", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                image.setImageBitmap(null);
                materials.setText("");
                description.setText("");
            }
        });
    }


    /**
     * This method is to set tab colors to indicate which page user in.
     */
    public void setTabColor() {
        weixin.setImageResource(R.drawable.unselected_main);
        contact.setImageResource(R.drawable.selected_upload);
        find.setImageResource(R.drawable.unselected_community);
        me.setImageResource(R.drawable.unselected_me);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    uri = data.getData();
                    try {
                        //Transfer picture to next page.
                        bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(uri));
                        bitmap = scaleDownBitmap(bitmap, 100, this);
                        image.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    /**
     * This method is to scale down the photo.
     * @param photo  photo
     * @param newHeight newHeight
     * @param context context
     * @return Bitmap
     */
    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {
        try {
            if (photo == null) {
                throw new Exception("Invalid photo");
            }
            final float densityMultiplier = context.getResources().getDisplayMetrics().density;

            int h= (int) (newHeight*densityMultiplier);
            int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

            photo=Bitmap.createScaledBitmap(photo, w, h, true);

        } catch (Exception e){
            e.printStackTrace();
        }
        return photo;
    }

    /**
     * This method is to add new recipe to user uploaded recipes and update database.
     * @return boolean
     * @throws Exception
     */
    public boolean addNewRecipe() throws Exception{
        Recipe newRecipe = new Recipe();
        String recipeName = name.getText().toString().trim();
        String desc = description.getText().toString().trim();
        String mat = materials.getText().toString().trim();
        String imageString;
        String recipeLocation = saveRecipeLocation();

            if (bitmap == null) {
                throw new Exception("Invalid photo");
            }
            imageString = bitmapToString(bitmap);

            if (recipeName.equals("")) {
                throw new Exception("Invalid recipe name");
            }
            if (desc.equals("")) {
                throw new Exception("Invalid description");
            }
            if (mat.equals("")) {
                throw new Exception("Invalid materials");
            }
            if (imageString == null || imageString.equals("")) {
                throw new Exception("Invalid photo");
            }

        newRecipe.setRecipeName(recipeName);
        newRecipe.setDescription(desc);
        newRecipe.setLikeNumber(0);
        newRecipe.setMaterial(mat);
        newRecipe.setImageUri(imageString);
        newRecipe.setLocation(recipeLocation);
        newRecipe.setAuthorID(userID);
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(3, newRecipe);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}

        String result = defaultSocketClient.getResult();

        if (result.equals("Success")) {
            Toast.makeText(UploadRecipe.this, "Upload recipe successfully", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("RECIPE_NUMBER",0);
            editor.apply();
            return true;
        } else {
            new InputException().fix(this, result);
            return false;
        }
    }

    /**
     * This method is to save recipe location.
     * @return String
     */
    public String saveRecipeLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            return Double.toString(location.getLongitude()) + ";" + Double.toString(location.getLatitude());
        }else {
            return "-79.94395695807664;40.45370917737972";
        }
    }

    /**
     * This method is to store image a imagestring.
     */
    public String bitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        return encodeToString(bitmapByte, DEFAULT);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_recipe_page, menu);
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
