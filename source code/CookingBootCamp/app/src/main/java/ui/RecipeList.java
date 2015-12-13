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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Recipe;
import model.User;
import ws.remote.DefaultSocketClient;

/**
 * This class file is just like LocalRecipe class file, which means this page is a
 * container for displaying recipes list based on user's operations.
 * All in all, there are three kinds of contents will be added that are top 10 recipes, my
 * recipes and my collections.
 */
public class RecipeList extends Activity {
    private ArrayList<Recipe> recipes;
    private String userName;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_recipe);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) findViewById(R.id.title);
        final Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String title = data.getString("title");
        textView.setText(title);
        SharedPreferences sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        userName = sp.getString("USER_NAME", "");
        userID = sp.getInt("USER_ID", 0);
        String option = data.getString("option");
        assert option != null;
        switch (option) {
            case "top10":
                recipes = getTopRecipes();
                break;
            case "myCollection":
                recipes = getMyCollections();
                break;
            case "myRecipes":
                recipes = getMyRecipes();
                break;
            default:
                break;
        }
        setListView(recipes);
    }

    /**
     * This method is to get my recipes from database.
     * @return ArrayList<Recipe>
     */
    public ArrayList<Recipe> getMyRecipes() {
        ArrayList<Recipe> result = new ArrayList<>();
        User user = new User();
        user.setUserName(userName);
        user.setUserid(userID);
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(5, user);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}
        HashMap<Integer, Recipe> recipeList = defaultSocketClient.getRecipeList();

        for (Object o : recipeList.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            result.add((Recipe) entry.getValue());
        }
        return result;
    }
    /**
     * This method is to get my collections from database.
     * @return ArrayList<Recipe>
     */
    public ArrayList<Recipe> getMyCollections() {
        ArrayList<Integer> collectionsID;
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(15, userID);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}
        collectionsID = defaultSocketClient.getMycollection();

        HashMap<Integer, Recipe> allRecipes;
        DefaultSocketClient ds = new DefaultSocketClient(12);
        ds.start();
        while (ds.isAlive()) {System.out.print("");}
        allRecipes = ds.getRecipeList();

        ArrayList<Recipe> result = new ArrayList<>();

        for (Integer index : collectionsID) {
            result.add(allRecipes.get(index));
        }
        return result;
    }
    /**
     * This method is to get top 10 recipes.
     * @return ArrayList<Recipe>
     */
    public ArrayList<Recipe> getTopRecipes() {
        DefaultSocketClient ds = new DefaultSocketClient(8, userName);
        ds.start();
        while (ds.isAlive()) {System.out.print("");}
        return ds.getTop10recipelist();
    }
    /**
     * This method is to set list view.
     * @param recipes "recipes"
     */
    public void setListView(final ArrayList<Recipe> recipes) {
        if (recipes != null) {

            ListView listView = (ListView) findViewById(R.id.recipeList);
            ArrayList<HashMap<String, Object>> list = getRecipeList(recipes);

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    list,
                    R.layout.ui_onerecipe,
                    new String[]{"iconimage", "recipeName", "likeicon", "likecount"},
                    new int[]{R.id.iconimage, R.id.recipeName, R.id.likeicon, R.id.likecount}
            );

            //Dynamically set the recipe image from imageString.
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if ((view instanceof ImageView) && (data instanceof Bitmap)) {
                        ImageView imageView = (ImageView) view;
                        Bitmap bitmap = (Bitmap) data;
                        imageView.setImageBitmap(bitmap);
                        return true;
                    }
                    return false;
                }
            });

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //According to different clicked item, show different information.
                    //Recipe currentRecipe = recipes.get(position);
                    Bundle data = new Bundle();
                    data.putInt("position",position);
                    data.putSerializable("recipes",recipes);
                    //data.putSerializable("recipe", currentRecipe);
                    Toast.makeText(RecipeList.this, String.valueOf(position), Toast.LENGTH_LONG).show();
                    Intent recipeInfo = new Intent(RecipeList.this, RecipeDetail.class);
                    recipeInfo.putExtras(data);
                    startActivity(recipeInfo);
                }
            });
        } else {
            Toast.makeText(RecipeList.this, "No recipes have been created", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * This method is to get recipe list object with list as input.
     * @param recipes "recipes"
     * @return ArrayList<>()
     */
    public ArrayList<HashMap<String, Object>> getRecipeList(ArrayList<Recipe> recipes) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (Recipe recipe : recipes) {
            HashMap<String, Object> current = new HashMap<>();
            String name = recipe.getRecipeName();
            String imageString = recipe.getImageUri();
            int likeNumber = recipe.getLikeNumber();
            byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap = null;
            if (bytes != null && bytes.length > 0) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            current.put("iconimage", bitmap);
            current.put("recipeName", name);
            current.put("likeicon", R.drawable.iconfont_like);
            current.put("likecount", likeNumber);
            list.add(current);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_page, menu);
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
