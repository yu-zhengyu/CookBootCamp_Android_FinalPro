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

import model.User;
import ws.remote.DefaultSocketClient;

/**
 * This method is mainly focuses on the cook list.
 * This page display different content based on user's operations.
 * All in all, there are two kind of contents will be added, including
 * top 10 cooks and my friends.
 */
public class Cook extends Activity {

    private ArrayList<User> cooks;
    //private SharedPreferences sp;
    private String userName;
    private int userID;
    //private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_cook);
    }
    @Override
    public void onStart() {
        super.onStart();
        TextView textView;
        SharedPreferences sp;
        textView = (TextView) findViewById(R.id.title);
        final Intent intent = getIntent();
        Bundle data = intent.getExtras();
        textView.setText(data.getString("title"));
        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        userName = sp.getString("USER_NAME", "");
        userID = sp.getInt("USER_ID", 0);
        String option = data.getString("option");
        assert option != null;
        switch (option) {
            case "top10":
                cooks = getTopUsers();
                break;
            case "myFriends":
                cooks = getMyFriends();
                break;
            default:
                break;
        }
        setListView(cooks);
    }

    /**
     * This method is to get my friends from database.
     * @return ArrayList<User>
     */
    public ArrayList<User> getMyFriends() {
        ArrayList<Integer> friendID;
        DefaultSocketClient defaultSocketClient = new DefaultSocketClient(14, userID);
        defaultSocketClient.start();
        while (defaultSocketClient.isAlive()) {System.out.print("");}
        friendID = defaultSocketClient.getMyfriendid();

        HashMap<Integer, User> allUsers;
        DefaultSocketClient ds = new DefaultSocketClient(11);
        ds.start();
        while (ds.isAlive()) {System.out.print("");}
        allUsers = ds.getUserlist();

        ArrayList<User> result = new ArrayList<>();
        for (Integer index : friendID) {
            result.add(allUsers.get(index));
        }
        return result;
    }


    /**
     * This method is to get top 10 users from database.
     * @return ArrayList<User>
     */
    public ArrayList<User> getTopUsers() {
        DefaultSocketClient ds = new DefaultSocketClient(9, userName);
        ds.start();
        while (ds.isAlive()) {System.out.print("");}
        return ds.getTop10userslist();
    }

    /**
     * This method is to set list view.
     * @param cooks "cooks"
     */
    public void setListView(final ArrayList<User> cooks) {
        if (cooks != null) {
            ListView listview = (ListView) findViewById(R.id
                    .ListView);
            ArrayList<HashMap<String, Object>> meumList = getCookMap(cooks);

            SimpleAdapter saItem = new SimpleAdapter(this,
                    meumList, // data Source
                    R.layout.ui_frienditem, //xml implement
                    new String[]{"ItemImage", "ItemText"}, // Key value pair
                    new int[]{R.id.ItemImage, R.id.ItemText});  // R id

            saItem.setViewBinder(new SimpleAdapter.ViewBinder() {
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
            listview.setAdapter(saItem);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User currentUser = cooks.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cook", currentUser);
                    Intent intent = new Intent(Cook.this, CookPersonalInformation.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(Cook.this, "No top 10 cooks found", Toast.LENGTH_LONG).show();
        }

    }
    /**
     * This method is to get all cooks as map.
     * @param cooks "cooks"
     * @return "ArrayList<>"
     */
    public ArrayList<HashMap<String, Object>> getCookMap(ArrayList<User> cooks) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        for (User cook : cooks) {
            HashMap<String, Object> current = new HashMap<>();
            current.put("ItemText", cook.getUserName());
            String imageString = cook.getUrlForProfile();
            byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);

            Bitmap bitmap = null;
            if (bytes != null && bytes.length > 0) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            current.put("ItemImage", bitmap);
            result.add(current);
        }
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cook_page, menu);
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
