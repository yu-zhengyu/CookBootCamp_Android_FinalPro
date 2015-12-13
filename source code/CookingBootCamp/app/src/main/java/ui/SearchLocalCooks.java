package ui;

import android.app.Activity;
import android.content.Intent;
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

/**
 * This class file is just like Cook class file, which means this page can load
 * different contents based on user's operations. Also sort the cook based on their distances.
 */
@SuppressWarnings("unchecked")
public class SearchLocalCooks extends Activity {
    private ArrayList<User> localCooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_search_local_cooks);


        final TextView textView = (TextView) findViewById(R.id.title);
        final Intent intent = getIntent();
        Bundle data = intent.getExtras();
        localCooks = (ArrayList<User>)data.getSerializable("cooks");
        textView.setText(data.getString("title"));

        if (localCooks != null) {
            ListView listview = (ListView) findViewById(R.id
                    .ListView);
            ArrayList<HashMap<String, Object>> meumList = getLocalCooks(localCooks);
            SimpleAdapter saItem = new SimpleAdapter(this,
                    meumList, // data Source
                    R.layout.ui_local_one_cook, //xml implement
                    new String[]{"imageicon", "cookname", "distance"}, // Key value pair
                    new int[]{R.id.iconimage, R.id.friendName, R.id.distance});  // R id


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
                    User currentUser = localCooks.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cook", currentUser);
                    Intent intent = new Intent(SearchLocalCooks.this, CookPersonalInformation.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(SearchLocalCooks.this, "No cooks found", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * This method is to convert arraylist into hashmap arraylist.
     * @param localCooks localcooks
     * @return ArrayList<>()
     */
    public ArrayList<HashMap<String, Object>> getLocalCooks(ArrayList<User> localCooks) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        for (User cook : localCooks) {
            HashMap<String, Object> current = new HashMap<>();
            current.put("cookname", cook.getUserName());
            String imageString = cook.getUrlForProfile();
            byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);

            Bitmap bitmap = null;
            if (bytes != null && bytes.length > 0) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            current.put("imageicon", bitmap);
            current.put("distance", String.format("%.2f",cook.getDistance()));
            result.add(current);
        }
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_local_cooks_page, menu);
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
