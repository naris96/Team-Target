package assignment1.narispillai.com.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminList extends AppCompatActivity {

    private AppCompatActivity activity = AdminList.this;
    private AppCompatTextView adminemail;
    private RecyclerView adminRecycleView;
    private List<AdminUserClass> listAd;
    private AdminRecycleAdapterClass AdminRecyclerAdapter;
    private AdminDatabaseClass admindata;

    public static String nama;

    public static Activity context = null;

    private static final String PREF_NAME = "nextage_quiz";
    private static final int PRIVATE_MODE = 0;

    SharedPreferences getPrefs;

    int checkingnumber;

    static boolean needRefreshAdminList=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RelativeLayout backyAL = (RelativeLayout) findViewById(R.id.relayBackAL);

        final ImageView background = (ImageView) findViewById(R.id.background);

        getPrefs = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        if (getPrefs.getInt("id", 0) == 1) {
            backyAL.setBackgroundResource(R.drawable.bg1);
        } else if (getPrefs.getInt("id", 0) == 2) {
            backyAL.setBackgroundResource(R.drawable.bg2);
        } else if (getPrefs.getInt("id", 0) == 3) {
            backyAL.setBackgroundResource(R.drawable.bg3);
        } else if (getPrefs.getInt("id", 0) == 4) {
            backyAL.setBackgroundResource(R.drawable.bg4);
        }
        else{
            if (!getPrefs.getString("path", "").equalsIgnoreCase("")) {

                Picasso.with(AdminList.this)
                        .load("file:" + getPrefs.getString("path", ""))
                        .fit()
                        .centerCrop()
                        .into(background, new Callback() {
                            @Override
                            public void onSuccess() {

                                backyAL.setBackgroundDrawable(background.getDrawable());
                            }

                            @Override
                            public void onError() {

                                Log.i("Background Photo","Missing user photo");

                            }
                        });
            }
        }

        Intent intent = getIntent();
        String loginusername = intent.getExtras().getString("ADMINAME");

        getSupportActionBar().setTitle("Admin List");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        adminviewinitializer();
        initializeadmins();

        nama=loginusername;

    }

    public void refreshAdminList(){
        adminviewinitializer();
        initializeadmins();
        needRefreshAdminList=false;
    }

    public void adminviewinitializer(){

        adminemail = (AppCompatTextView) findViewById(R.id.textViewUsername);
        adminRecycleView = (RecyclerView) findViewById(R.id.adminrecycle);

    }

    protected void onResume() {
        super.onResume();
        refreshAdminList();
    }

    public void initializeadmins(){

        listAd = new ArrayList<>();
        AdminRecyclerAdapter = new AdminRecycleAdapterClass(listAd);

        RecyclerView.LayoutManager adminlayoutmanager = new LinearLayoutManager(getApplicationContext());
        adminRecycleView.setLayoutManager(adminlayoutmanager);
        adminRecycleView.setItemAnimator(new DefaultItemAnimator());
        adminRecycleView.setHasFixedSize(true);
        adminRecycleView.setAdapter(AdminRecyclerAdapter);
        admindata = new AdminDatabaseClass(activity);

        getDataFromSQLite();

    }

    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listAd.clear();
                listAd.addAll(admindata.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                AdminRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
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
