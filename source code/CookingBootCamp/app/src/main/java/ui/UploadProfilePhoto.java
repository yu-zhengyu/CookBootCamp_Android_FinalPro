package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.qiuyi.cookingbootcamp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import model.User;

public class UploadProfilePhoto extends Activity {

    private Button takePhoto;
    private Button fromFolder;
    private static final int TAKE_PHOTO = 1;
    private static final int CROP = 2;
    private Uri uri;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_upload_profile_photo);

        //Get the button modules
        initViews();
        //Then add listeners to all modules
        addListeners();

        //Get data from last activity.
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        user = (User) data.get("USER");

    }

    /**
     * This method is to initialize all the modules.
     */
    public void initViews() {
        takePhoto = (Button) findViewById(R.id.takephoto);
        fromFolder = (Button) findViewById(R.id.fromimagefolder);
    }


    /**
     * This method is to add listeners to all modules.
     */
    public void addListeners() {
        //Add listener to both buttons
        takePhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Create file object to store the picture.
                        File outPutImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                        //IF already exitsed, delete the local picture.
                        try {
                            if(outPutImage.exists()) {
                                outPutImage.delete();
                            }
                            outPutImage.createNewFile();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        uri = Uri.fromFile(outPutImage);
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                }
        );

        fromFolder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, CROP);
                    }
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    try {
                        //uri = data.getData();
                        //Transfer picture to next page.
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(uri));
                        bitmap = scaleDownBitmap(bitmap, 100, this);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("USER", user);
                        Intent intent = new Intent(UploadProfilePhoto.this, ConfirmPhoto.class);
                        intent.putExtras(bundle);
                        intent.putExtra("bitmap", bitmapByte);
                        startActivity(intent);
                        finish();
                    }catch (Exception e) {
                        Log.d("Take","Error");
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * This method is to scale down bitmap in case of big image.
     * @param photo photo
     * @param newHeight newHeight
     * @param context context
     * @return bitmap
     */
    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_profile_photo_page, menu);
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
