package assignment1.narispillai.com.assignment1;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ScrollingView;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import static assignment1.narispillai.com.assignment1.AdminList.context;

public class ToolsActivity extends AppCompatActivity {

    private static final String PREF_NAME = "nextage_quiz";
    private static final int PRIVATE_MODE = 0;

    private SettingsManager setManager;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;

    private Uri mCapturedImageURI;

    SharedPreferences getPrefs;

    Uri photoURI;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            getPrefs.edit().putString("path", picturePath).apply();
            cursor.close();
            getPrefs.edit().putInt("id", 0).apply();

            Toast.makeText(this, "User picture selected.", Toast.LENGTH_LONG).show();

            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setManager = new SettingsManager(this);

        getPrefs = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        final NestedScrollView backy = (NestedScrollView) findViewById(R.id.nestedback);
        final ImageView backgroundd = (ImageView) findViewById(R.id.background);
        backy.setSmoothScrollingEnabled(true);
        if (getPrefs.getInt("id", 0) == 1) {
            backy.setBackgroundResource(R.drawable.bg1);
        } else if (getPrefs.getInt("id", 0) == 2) {
            backy.setBackgroundResource(R.drawable.bg2);
        } else if (getPrefs.getInt("id", 0) == 3) {
            backy.setBackgroundResource(R.drawable.bg3);
        } else if (getPrefs.getInt("id", 0) == 4) {
            backy.setBackgroundResource(R.drawable.bg4);
        }
        else{
            if (!getPrefs.getString("path", "").equalsIgnoreCase("")) {

                Picasso.with(ToolsActivity.this)
                        .load("file:" + getPrefs.getString("path", ""))
                        .fit()
                        .centerCrop()
                        .into(backgroundd, new Callback() {
                            @Override
                            public void onSuccess() {
                                backy.setBackgroundDrawable(backgroundd.getDrawable());
                            }

                            public void onError() {
                                Log.i("Background","User photo not found");
                            }
                        });
            }
            else{
                BitmapDrawable ob = new BitmapDrawable(getPrefs.getString("path", ""));
                backgroundd.setBackgroundDrawable(ob);
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        ImageView ImageViewBackground1 = (ImageView) findViewById(R.id.bg1imageview);
        ImageView ImageViewBackground2 = (ImageView) findViewById(R.id.bg2imageview);
        ImageView ImageViewBackground3 = (ImageView) findViewById(R.id.bg3imageview);
        ImageView ImageViewBackground4 = (ImageView) findViewById(R.id.bg4imageview);
        ImageView ImageViewBackground5 = (ImageView) findViewById(R.id.cameraimageview);
        ImageView ImageViewBackground6 = (ImageView) findViewById(R.id.galleryimageview);

        ImageViewBackground1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                backy.setBackgroundResource(R.drawable.bg1);
                getPrefs.edit().putInt("id", 1).apply();
                Toast.makeText(v.getContext(),"Wallpaper 1 selected.",Toast.LENGTH_SHORT).show();
                finish();
                UserNavigation un = new UserNavigation();
                un.needRefreshActivity=true;
            }
        });

        ImageViewBackground2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                backy.setBackgroundResource(R.drawable.bg2);
                getPrefs.edit().putInt("id", 2).apply();
                Toast.makeText(v.getContext(),"Wallpaper 2 selected.",Toast.LENGTH_SHORT).show();
                finish();
                UserNavigation un = new UserNavigation();
                un.needRefreshActivity=true;
            }
        });
        ImageViewBackground3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                backy.setBackgroundResource(R.drawable.bg3);
                getPrefs.edit().putInt("id", 3).apply();
                Toast.makeText(v.getContext(),"Wallpaper 3 selected.",Toast.LENGTH_SHORT).show();
                finish();
                UserNavigation un = new UserNavigation();
                un.needRefreshActivity=true;
            }
        });

        ImageViewBackground4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                backy.setBackgroundResource(R.drawable.bg4);
                getPrefs.edit().putInt("id", 4).apply();
                Toast.makeText(v.getContext(),"Wallpaper 4 selected.",Toast.LENGTH_SHORT).show();
                finish();
                UserNavigation un = new UserNavigation();
                un.needRefreshActivity=true;
            }
        });

        ImageViewBackground5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                haveCameraPermissions();
                haveInternalStoragePermissions();

                if(haveCameraPermissions()){
                    if(haveInternalStoragePermissions()){
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                        activeTakePhoto();
                    }
                    else{
                        getSDCardPermissions();
                    }
                }
                else{
                    getCameraPermissions();
                    getSDCardPermissions();
                    getWritePermissions();
                }
                UserNavigation un = new UserNavigation();
                un.needRefreshActivity=true;
            }
        });

        ImageViewBackground6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(haveInternalStoragePermissions()) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Toast.makeText(ToolsActivity.this, "Note:High resolution photos may cause the app to load slower.", Toast.LENGTH_SHORT).show();
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
                else{
                    getSDCardPermissions();
                    getWritePermissions();
                }
                UserNavigation un = new UserNavigation();
                un.needRefreshActivity=true;
            }
        });


    }


    public boolean haveInternalStoragePermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadSDCard = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(permissionReadSDCard == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    public boolean haveCameraPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if(permissionCamera == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    public boolean haveWritePermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionExternal == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    private void takePhotoByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/Team Target");
                if (!dir.exists()) dir.mkdirs();
                photoFile = File.createTempFile("temp", ".jpg", dir);

                photoURI = Uri.parse("file:" + photoFile.getAbsolutePath());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile));
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }

                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException ex) {
                //Toast.makeText(this, R.string.camera_failed, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void activeTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "teamtarget";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            if(haveWritePermissions()==true){
               mCapturedImageURI = getContentResolver()
                       .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                takePictureIntent
                        .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            else{
                getWritePermissions();
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void getWritePermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(ToolsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void getSDCardPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(ToolsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void getCameraPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(ToolsActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == android.view.KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return false;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
