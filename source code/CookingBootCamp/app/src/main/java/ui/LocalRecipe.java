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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qiuyi.cookingbootcamp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import model.Recipe;
import model.ReicipeComparator;
import ws.remote.DefaultSocketClient;

/**
 * This class file is just like recipeList class file, and its main idea is to
 * load the local recipes to the list view.
 * Theoretically when user login in different location, different recipes rank will be
 * displayed.
 */
public class LocalRecipe extends Activity {
    private String userName;
    private int userID;
    private String location;
    private LocationManager locationManager;

    private static final int CIRCLE = 6371;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_local_recipe);

}

    @Override
    public void onStart() {
        super.onStart();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SharedPreferences sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        userName = sp.getString("USER_NAME", "");
        location = sp.getString("LOCATION", "");
        userID = sp.getInt("USER_ID", 0);
        TextView textView = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        textView.setText(data.getString("title"));
        ArrayList<Recipe> localRecipes = getLocalRecipes();
        setListView(localRecipes);

    }

    /**
     * This method is to get local recipes from database.
     * @return ArrayList<Recipe>
     */
    public ArrayList<Recipe> getLocalRecipes() {
        //First get all recipe and based on locaton information to get local recipes.
        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<Recipe> result;
        //Here is to get local recipes
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(12, userName);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}

        HashMap<Integer, Recipe> recipeList = defaultSocketClient.getRecipeList();

        for (Object o : recipeList.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Recipe recipe = (Recipe) entry.getValue();
            if (recipe.getAuthorID() != userID) {
                recipes.add(recipe);
            }
        }

        //Now calculate the local recipes based on position.
        result = calculateLocationForRecipe(recipes);
        return result;
    }

    /**
     * This method is to sort all recipes according to their locations.
     * @param recipes "recipes"
     * @return ArrayList<Recipe>
     */
    public ArrayList<Recipe> calculateLocationForRecipe(ArrayList<Recipe> recipes) {
        if (location.equals(";")) {
            location = getLocation();
            Toast.makeText(LocalRecipe.this, "Get your current location..", Toast.LENGTH_LONG).show();
        }
        String[] position = location.split(";");
        double myLongitude = Double.parseDouble(position[0]);
        double myLatitude = Double.parseDouble(position[1]);
        //ArrayList<Double> distances = new ArrayList<>();
        for (Recipe recipe : recipes) {
            String[] recipePosition = recipe.getLocation().split(";");
            double longitude = Double.parseDouble(recipePosition[0]);
            double latitude = Double.parseDouble(recipePosition[1]);
            double temp = haversine(latitude-myLatitude) +
                    Math.cos(myLatitude)*Math.cos(latitude) * haversine(Math.log(Math.abs(longitude)) - Math.log(Math.abs(myLongitude)));
            recipe.setDistance(CIRCLE * inverseHaversine(temp));
        }
        Collections.sort(recipes, new ReicipeComparator());
        return recipes;
    }

    /**
     * This method is to return login position.
     * @return String
     */
    public String getLocation() {
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
    /**
     * This function is to calculate the distance.
     * @param a "a"
     * @return double
     */
    public double haversine(double a) {
        return ((1 - Math.cos(a))/2);
    }

    /**
     * This method is to calculate the inverse cos function.
     * @param a "a"
     * @return double
     */
    public double inverseHaversine(double a) {
        return Math.acos(1 - 2*a);
    }


    /**
     * This method is to convert the arraylist of recipes to arraylist of hashmap.
     * @param recipes recipes
     * @return ArrayList<>()
     */
    public ArrayList<HashMap<String, Object>> getRecipeList(ArrayList<Recipe> recipes) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (Recipe recipe : recipes) {
            HashMap<String, Object> current = new HashMap<>();
            String name = recipe.getRecipeName();
            //String material = recipe.getMaterial();
            //String description = recipe.getDescription();
            String imageString = recipe.getImageUri();
            //String sendTime = recipe.getSendTime();
            String distance = String.format("%.2f", recipe.getDistance());
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
            current.put("distance", distance);
            list.add(current);
        }
        return list;
    }

    /**
     * This method is to set list view of current page.
     * @param localRecipes "ArrayList<Recipe>"
     */
    public void setListView(final ArrayList<Recipe> localRecipes) {
        if (localRecipes != null) {
            ListView listView = (ListView) findViewById(R.id.recipeList);
            ArrayList<HashMap<String, Object>> list = getRecipeList(localRecipes);

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    list,
                    R.layout.ui_local_one_recipe,
                    new String[]{"iconimage", "recipeName", "distance", "likeicon", "likecount"},
                    new int[]{R.id.iconimage, R.id.recipeName, R.id.distance, R.id.likeicon, R.id.likecount}
            );

            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
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
                    //Recipe currentRecipe = localRecipes.get(position);
                    Bundle data = new Bundle();
                    data.putInt("position", position);
                    data.putSerializable("recipes",localRecipes);
                    //data.putSerializable("recipe", currentRecipe);
                    Toast.makeText(LocalRecipe.this, String.valueOf(position), Toast.LENGTH_LONG).show();
                    Intent recipeInfo = new Intent(LocalRecipe.this, RecipeDetail.class);
                    recipeInfo.putExtras(data);
                    startActivity(recipeInfo);
                }
            });
        } else {
            Toast.makeText(LocalRecipe.this, "No local recipes found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_recipe_page, menu);
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
