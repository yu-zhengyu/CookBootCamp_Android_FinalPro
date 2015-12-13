package ws.local;

/**
 * Created by qiuyi on 11/13/15.
 * This class file is to supply the cmmand that SQLite needs
 */
public class SQLCmd {

    public static final String DB_NAME = "CookingBootCampDB";

    public static final String TB_USER = "tb_user";
    public static final String TB_RECIPE = "tb_recipe";


    public static final String USER_NAME = "name";
    public static final String USER_PWD = "password";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";


    public static final String RECIPE_NAME = "recipe_name";
    public static final String AUTHOR_ID = "author_id";
    public static final String RECIPE_LOCATION = "recipe_location";
    public static final String RECIPE_DESC = "recipe_desc";
    public static final String RECIPE_MATERIAL = "recipe_material";
    public static final String RECIPE_IMAGE = "recipe_image";

    public static final String CREATE_TB_USER = "create table IF NOT EXISTS " + TB_USER +
            " ( " + USER_ID + " INTEGER PRIMARY KEY," + USER_NAME + " String," +
            USER_PWD + " String," + USER_EMAIL + " String);";

    public static final String CREATE_TB_RECIPE = "craete table IF NOT EXISTS " + TB_RECIPE +
            " ( " + RECIPE_NAME + " String PRIMARY KEY," + AUTHOR_ID + " INTEGER," + RECIPE_DESC + " String,"
            + RECIPE_MATERIAL + "String," + RECIPE_LOCATION + "String," + RECIPE_IMAGE + "String);";

}
