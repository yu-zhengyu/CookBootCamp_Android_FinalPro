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

import model.Recipe;
import ws.remote.DefaultSocketClient;

public class RecipeDetail extends Activity {

    private ImageView recipeImage;
    private TextView recipeID;
    private TextView author;
    private ImageView like;
    private TextView likeNumber;
    private TextView material;
    private TextView steps;
    private Button addCollections;
    private TextView recipeName;

    private int userID;
    private Recipe currentRecipe;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_recipe_detail);

        //Receive data from upper activity.
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        int position = data.getInt("position");
        ArrayList<Recipe> recipes;
        recipes = (ArrayList<Recipe>) data.getSerializable("recipes");
        assert recipes != null;
        currentRecipe = recipes.get(position);
        //currentRecipe = (Recipe) data.getSerializable("recipe");
        //Initialize all modules
        initViews();
        //Add listeners
        addListeners();
        //Get recipe information and set layout.
        getAndSet(currentRecipe);

        //Set recipe information view.
    }

    /**
     * This method is to set recipe information.
     * @param recipe recipe
     */
    public void getAndSet(Recipe recipe) {
        recipeID.setText(String.valueOf(recipe.getRecipeID()));
        author.setText(String.valueOf(recipe.getAuthorID()));
        likeNumber.setText(String.valueOf(recipe.getLikeNumber()));
        material.setText(recipe.getMaterial());
        steps.setText(recipe.getDescription());
        recipeName.setText(recipe.getRecipeName());
        like.setImageResource(R.drawable.iconfont_like);
        String imageString = recipe.getImageUri();
        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);

        Bitmap bitmap = null;
        if (bytes != null && bytes.length > 0) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        recipeImage.setImageBitmap(bitmap);
    }

    /**
     * This method is to add listeners to all modules.
     */
    public void addListeners() {
        addCollections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultSocketClient defaultSocketClient = new DefaultSocketClient(7, userID, 0, currentRecipe.getRecipeID());
                defaultSocketClient.start();
                while (defaultSocketClient.isAlive()) {
                    System.out.print("");
                }
                String result = defaultSocketClient.getResult();

                if (result.equals("Success")) {
                    Toast.makeText(RecipeDetail.this, "Add to collections successfully", Toast.LENGTH_LONG).show();
                    addCollections.setText("Added to collections");
                    addCollections.setClickable(false);
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAlreadyLiked(userID, currentRecipe.getRecipeID())) {
                    //When click like icon. Like number increases.
                    int count = Integer.parseInt(likeNumber.getText().toString().trim());
                    count++;
                    likeNumber.setText(String.valueOf(count));
                    currentRecipe.setLikeNumber(count);
                    DefaultSocketClient defaultSocketClient = new DefaultSocketClient(16, currentRecipe);
                    defaultSocketClient.start();
                    while (defaultSocketClient.isAlive()) {
                        System.out.print("");
                    }
                    String result = defaultSocketClient.getResult();
                    if (result.equals("Success")) {
                        Toast.makeText(RecipeDetail.this, "Like this recipe successfully", Toast.LENGTH_LONG).show();
                        System.out.print("");
                    }
                } else {
                    Toast.makeText(RecipeDetail.this, "Already like this recipe", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * This method is to check whether user like this recipe or not.
     * @param userID userID
     * @param recipeID recipeID
     * @return boolean
     */
    public boolean isAlreadyLiked(int userID, int recipeID) {
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(17, userID, 0, recipeID);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {
            System.out.print("");
        }
        String result = defaultSocketClient.getResult();
        return !result.equals("Success");
    }
    /**
     * This method is to initialize all modules
     */
    public void initViews() {
        recipeImage = (ImageView) findViewById(R.id.recipepicture);
        recipeID = (TextView) findViewById(R.id.recipeID2);
        author = (TextView) findViewById(R.id.author);
        like = (ImageView) findViewById(R.id.like);
        likeNumber = (TextView) findViewById(R.id.likeNumber2);
        material = (TextView) findViewById(R.id.material);
        steps = (TextView) findViewById(R.id.steps);
        addCollections = (Button) findViewById(R.id.addtocollection);
        recipeName = (TextView) findViewById(R.id.recipeinformation);
        SharedPreferences sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        userID = sp.getInt("USER_ID", 0);
        if (isAlreadyCollections()) {
            addCollections.setClickable(false);
            addCollections.setText("Added to collections");
        }
        //String userName = sp.getString("USER_NAME", "")
    }

    public boolean isAlreadyCollections() {
        ArrayList<Integer> collectionsID;
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(15, userID);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {
            System.out.print("");
        }
        collectionsID = defaultSocketClient.getMycollection();

        return collectionsID.size() != 0 && collectionsID.contains(currentRecipe.getRecipeID());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_detail_page, menu);
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

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}
