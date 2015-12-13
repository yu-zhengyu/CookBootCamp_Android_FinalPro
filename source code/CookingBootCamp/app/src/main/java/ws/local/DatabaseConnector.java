package ws.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import model.Recipe;

/**
 * This class file is to connect to local database and interact with local database.
 * All these design details refer to the given code by Professor in assignment2.
 */
@SuppressWarnings("unused")
public class DatabaseConnector {

    private static final int VERSION = 1;
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;

    /**
     * Constructor
     *
     * @param context context
     */
    public DatabaseConnector(Context context) {
        databaseOpenHelper =
                new DatabaseOpenHelper(context, SQLCmd.DB_NAME, null, VERSION);
    }

    /**
     * Open the database connection
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = databaseOpenHelper.getWritableDatabase();
    }

    /**
     * This method is to get the readable permission of the local database.
     * @throws SQLException
     */
    public void openForRead() throws SQLException {
        database = databaseOpenHelper.getReadableDatabase();
    }
    /**
     * Close the database connection
     */
    public void close() {
        if (database != null)
            database.close(); // close the database connection
    } // end method close

    /**
     * When user upload a new recipe, update local database.
     * Meantime, update the remote database, which is another part.
     * @param recipe recipe
     */
    public void insertRecipe(Recipe recipe) {
        //Get the to be inserted recipe information.
        String recipeName = recipe.getRecipeName();
        String recipeMaterial = recipe.getMaterial();
        String recipeStep = recipe.getDescription();
        String recipeImageString = recipe.getImageUri();
        String recipeLocation = recipe.getLocation();
        int recipeAuthorID =  recipe.getAuthorID();
        ContentValues content = new ContentValues();
        content.put(SQLCmd.RECIPE_NAME, recipeName);
        content.put(SQLCmd.AUTHOR_ID, recipeAuthorID);
        content.put(SQLCmd.RECIPE_LOCATION, recipeLocation);
        content.put(SQLCmd.RECIPE_DESC, recipeStep);
        content.put(SQLCmd.RECIPE_MATERIAL, recipeMaterial);
        content.put(SQLCmd.RECIPE_IMAGE, recipeImageString);
        open();
        databaseOpenHelper.onCreate(database);
        database.insertWithOnConflict(SQLCmd.CREATE_TB_RECIPE, null, content, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * This method is to update the user basic information when user edit his/her information
     * in Android device.
     */
    public ArrayList<Recipe> getUserRecipe() {
        openForRead();
        ArrayList<Recipe> ret = new ArrayList<>();
        String[] columns = {SQLCmd.RECIPE_NAME, SQLCmd.AUTHOR_ID, SQLCmd.RECIPE_DESC, SQLCmd.RECIPE_MATERIAL, SQLCmd.RECIPE_LOCATION, SQLCmd.RECIPE_IMAGE};
        Cursor cursor = database.query(SQLCmd.TB_RECIPE, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recipe = new Recipe();
            String recipeName = cursor.getString(cursor.getColumnIndexOrThrow(SQLCmd.RECIPE_NAME));
            int recipeAuthorID = cursor.getInt(cursor.getColumnIndexOrThrow(SQLCmd.AUTHOR_ID));
            String recipeDesc = cursor.getString(cursor.getColumnIndexOrThrow(SQLCmd.RECIPE_DESC));
            String recipeMaterial = cursor.getString(cursor.getColumnIndexOrThrow(SQLCmd.RECIPE_MATERIAL));
            String recipeLocation = cursor.getString(cursor.getColumnIndexOrThrow(SQLCmd.RECIPE_LOCATION));
            String recipeImageString = cursor.getString(cursor.getColumnIndexOrThrow(SQLCmd.RECIPE_IMAGE));
            recipe.setAuthorID(recipeAuthorID);
            recipe.setDistance(Double.parseDouble(recipeLocation));
            recipe.setImageUri(recipeImageString);
            recipe.setRecipeName(recipeName);
            recipe.setDescription(recipeDesc);
            recipe.setMaterial(recipeMaterial);
            ret.add(recipe);

        }
        return ret;
    }

    /**
     * Inner class: Database helper
     */
    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        /**
         * Constructor and create local database.
         *
         * @param context context
         * @param name name
         * @param factory factory
         * @param version version
         */
        public DatabaseOpenHelper(Context context, String name,
                                  CursorFactory factory, int version) {
            super(context, name, factory, version);
            Log.d("Database:","DB create successfully");
        }


        /**
         * Create local tables
         * @param db db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQLCmd.CREATE_TB_USER); // execute the query
            // query to create a new table named changes2
            db.execSQL(SQLCmd.CREATE_TB_RECIPE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
        }
    }
}
