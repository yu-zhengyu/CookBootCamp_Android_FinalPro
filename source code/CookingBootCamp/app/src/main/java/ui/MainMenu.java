package ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.qiuyi.cookingbootcamp.R;

/**
 * This class file is the main page of this app, which means when user login successfully,
 * he/she will see this page firstly. In the page, there are two next pages are involve that are
 * top 10 cooks and top 10 recipes.
 */
public class MainMenu extends Activity {

    private Button weeklytop10;
    private Button cookstar;
    private ImageView contact;
    private ImageView find;
    private ImageView me;
    private ImageView weixin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_main_menu);

        //First I need to initialize all the modules
        initViews();

        //Add listener to both buttons
        addListeners();

        //Set tab color.
        setTabColor();
    }

    /**
     * This method is to get all the activity modules.
     */
    public void initViews() {
        weeklytop10 = (Button) findViewById(R.id.weeklytop10);
        cookstar = (Button) findViewById(R.id.cookstar);
        contact = (ImageView) findViewById(R.id.contractImageView);
        find = (ImageView) findViewById(R.id.findImageView);
        me = (ImageView) findViewById(R.id.meImageView);
        weixin = (ImageView) findViewById(R.id.weixinImageView);

    }

    /**
     * This mehthod is to add listeners to all modules
     */
    public void addListeners() {

        weeklytop10.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(MainMenu.this, RecipeList.class);
                        Bundle data = new Bundle();
                        data.putString("option","top10");
                        data.putString("title","Weekly Top 10 Recipes");
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                }
        );

        cookstar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(MainMenu.this, Cook.class);
                        Bundle data = new Bundle();
                        data.putString("option", "top10");
                        data.putString("title", "Top 10 Cook Stars");
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                }
        );


        contact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainMenu.this, UploadRecipe.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        find.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainMenu.this, Community.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        me.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainMenu.this, UserInformation.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

    }


    /**
     * This method is to set tab colors to indicate which page user in.
     */
    public void setTabColor() {
        weixin.setImageResource(R.drawable.selected_main);
        contact.setImageResource(R.drawable.unselected_upload);
        find.setImageResource(R.drawable.unselected_community);
        me.setImageResource(R.drawable.unselected_me);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu_page, menu);
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
