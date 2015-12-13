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

import java.util.ArrayList;

import model.Recipe;
import ws.remote.DefaultSocketClient;

/**
 * This class file is to delete a collection from user's collection recipes
 * based on the recipe ID. If the user input recipe ID is not in the collection
 * list, an error will occur so that the user can check user's input.
 */
public class Delete extends Activity {
    private EditText recipeID;
    private Button delete;

    //private SharedPreferences sp;
    //private String userName;
    private int userID;

    protected ArrayList<Recipe> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_delete);
        initViews();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteRecipe()) {
                    //recipes = getMyRecipes();
                    Intent intent = new Intent(Delete.this, RecipeList.class);
                    Bundle data = new Bundle();
                    //data.putSerializable("recipes", recipes);
                    data.putString("option", "myCollection");
                    data.putString("title", "My Collections");
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    /**
     * This method is to delete a recipe.
     * @return boolean
     */
    public boolean deleteRecipe() {
        int id = Integer.parseInt(recipeID.getText().toString().trim());
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(13, userID, 0, id);

        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}

        String result = defaultSocketClient.getResult();
        if (result.equals("Success")) {
            Toast.makeText(Delete.this, "Delete successfully", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(Delete.this, result, Toast.LENGTH_LONG).show();
            return false;
        }
    }
    /**
     * This method is to initialize all modules.
     */
    public void initViews() {
        SharedPreferences sp;
        //String userName;
        recipeID = (EditText) findViewById(R.id.deleteRecipeId);
        delete = (Button) findViewById(R.id.deleteRecipeButton);

        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        //userName = sp.getString("USER_NAME", "");
        userID = sp.getInt("USER_ID", 0);

        recipes = new ArrayList<>();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
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
