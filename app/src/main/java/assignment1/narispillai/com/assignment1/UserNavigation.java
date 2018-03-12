package assignment1.narispillai.com.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UserNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Shared preference and shared preference content declaration
    SharedPreferences getPrefs;
    private static final String PREF_NAME = "nextage_quiz";
    private static final int PRIVATE_MODE = 0;

    //SQL database class declaration
    private AdminDatabaseClass addatabase;
    private AdminUserClass adminuser;

    //Relative layout declaration for button click events
    RelativeLayout relay;

    //Search bar edit text declaration
    EditText inputSearch;

    //Clear floating icon
    FloatingActionButton clearbutton;

    //boolean values to check of the app need refresh
    static boolean needRefreshContactList=false;
    static boolean needRefreshActivity=false;

    private final AppCompatActivity activity = UserNavigation.this;

    //Permission request checking interger variables
    final private int REQUEST_READ_CONTACTS = 123;
    final private int REQUEST_CALL_CONTACTS = 456;
    final private int REQUEST_MESSAGE_CONTACTS = 789;

    //List view, arraylist and adapter declaration for the contact list.
    ListView contactsname;
    ArrayList<Contact> contactList = new ArrayList<>();
    ContactArrayAdapter adapter;

    //String value which will contain username
    String loginusername;

    //Textview which will display the username
    TextView usernamenavibar;

    private static final String KEY_TEXT_REPLY = "key_text_reply";

    AdminDatabaseClass adc = new AdminDatabaseClass(activity);

    List<AdminUserClass> adminList = new ArrayList<AdminUserClass>();

    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String currentdatetime = new SimpleDateFormat("d MMM").format(new Date());

        NavigationView navigationView = (NavigationView) UserNavigation.this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final RelativeLayout backyUN = (RelativeLayout) findViewById(R.id.relativeback);

        View headerView = navigationView.getHeaderView(0);
        usernamenavibar = (TextView) headerView.findViewById(R.id.usernametextView);

        Intent intent = getIntent();
        loginusername = intent.getExtras().getString("USERNAME");
        usernamenavibar.setText("Hello " + loginusername + "!");


        name = adc.checkBirthday(currentdatetime);

        ImageButton addcontact = (ImageButton) findViewById(R.id.AddContactButton);

        if(name != null){
            addNotification(name);
        }
        else{
            //Do nothing
        }

        getPrefs = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        final ImageView backgroundd = (ImageView) findViewById(R.id.background);

        if (getPrefs.getInt("id", 0) == 1) {
            backyUN.setBackgroundResource(R.drawable.bg1);
        } else if (getPrefs.getInt("id", 0) == 2) {
            backyUN.setBackgroundResource(R.drawable.bg2);
        } else if (getPrefs.getInt("id", 0) == 3) {
            backyUN.setBackgroundResource(R.drawable.bg3);
        } else if (getPrefs.getInt("id", 0) == 4) {
            backyUN.setBackgroundResource(R.drawable.bg4);
        }
        else{
            if (!getPrefs.getString("path", "").equalsIgnoreCase("")) {

                Picasso.with(UserNavigation.this)
                        .load("file:" + getPrefs.getString("path", ""))
                        .fit()
                        .centerCrop()
                        .into(backgroundd, new Callback() {
                            @Override
                            public void onSuccess() {

                                backyUN.setBackgroundDrawable(backgroundd.getDrawable());
                            }

                            @Override
                            public void onError() {

                                Log.i("Background Photo","Missing user photo");

                            }
                        });
            }
            else{
                BitmapDrawable ob = new BitmapDrawable(getPrefs.getString("path", ""));
                backyUN.setBackgroundDrawable(ob);
            }
        }


        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_INSERT,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
                needRefreshContactList=true;
            }
        });

        contactsname = (ListView) findViewById(R.id.list);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {

            try {
                loadContact();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
        //dO NOTHING
        }
        else{
            Toast.makeText(getApplicationContext(), "Refresh the application", Toast.LENGTH_LONG).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void callcaller(Context context,String phonenumber, String contactname) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_CONTACTS);

            Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Click 'ALLOW' then try again.", Snackbar.LENGTH_LONG)
                    .setAction("Close", null)
                    .show();
        }
        else{
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_CONTACTS);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+phonenumber));
            context.startActivity(intent);
            Toast.makeText(context,"Calling " + contactname + " now...", Toast.LENGTH_LONG).show();
        }
    }


    public void messagemessengger(final Context mcon, final String contactphnum, final String contactnam){


        if (ActivityCompat.checkSelfPermission(mcon, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mcon, new String[]{Manifest.permission.SEND_SMS}, REQUEST_MESSAGE_CONTACTS);
            Snackbar.make(((Activity) mcon).findViewById(android.R.id.content), "Click 'ALLOW' then try again.", Snackbar.LENGTH_LONG)
                    .setAction("Close", null)
                    .show();
        }
        else{
            final Dialog alert;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                alert= new Dialog(mcon, android.R.style.Theme_Material_Light_Dialog);
            }
            else{
                alert = new Dialog(mcon);
            }

            alert.setContentView(R.layout.alertdialogmessagebox);
            final SmsManager smsManager = SmsManager.getDefault();

            final EditText message = (EditText) alert.findViewById(R.id.editTextmessagetextview);
            Button sendbut = (Button) alert.findViewById(R.id.sendbutton);
            Button cancel = (Button) alert.findViewById(R.id.cancelbutton);

            alert.setTitle("Send Message to " + contactnam);

            sendbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String messagetext = message.getText().toString();
                    if(messagetext.isEmpty()) {
                        Snackbar.make(((Activity) mcon).findViewById(android.R.id.content), "Field is empty. Message not sent.", Snackbar.LENGTH_LONG)
                                .setAction("Close", null)
                                .show();
                        alert.dismiss();
                    }

                    else{
                        smsManager.sendTextMessage(contactphnum, null, messagetext, null, null);
                        Snackbar.make(((Activity) mcon).findViewById(android.R.id.content), "Message successfully send.", Snackbar.LENGTH_LONG)
                                .setAction("Close", null)
                                .show();
                        alert.dismiss();
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(((Activity) mcon).findViewById(android.R.id.content), "Message cancelled.", Snackbar.LENGTH_LONG)
                            .setAction("Close", null)
                            .show();
                    alert.dismiss();
                }
            });

            alert.show();

        }

    }



    private void loadContact() throws IOException {

        //Email related uri and string declaration
        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        //Contact phone number related uri and string declaration
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        int WORKNUM = ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE;


        String email = "";
        String phonenumber = "";

        CursorLoader contactsCursorLoader = new CursorLoader(this,
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        Cursor contactsCursor = contactsCursorLoader.loadInBackground();

        Context context = null;

        //contact list
        ContentResolver cr = getContentResolver();

        ImageView profile  = (ImageView)findViewById(R.id.contactimage);

        //load all the name from contacts cursor
        while (contactsCursor.moveToNext()) {

            //get the id
            long contact_id = contactsCursor.getLong(
                    contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));



            String cid = contactsCursor.getString(
                    contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));

            //get the name
            String name = contactsCursor.getString(
                    contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));


            //Get contact photo
            String photoUri = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI));
            Bitmap my_btmp = BitmapFactory.decodeResource(getResources(), R.drawable.user);

            if (photoUri != null) {
                Uri my_contact_Uri = Uri.parse(photoUri);
                try {

                    my_btmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), my_contact_Uri);


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            //Get email
            Cursor emailCursor = cr.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{cid}, null);
            int emailnum = emailCursor.getCount();

            if (emailnum > 0) {
                emailCursor.moveToNext();
                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
            } else {
                email = "NA";
            }
            emailCursor.close();

            //Get phone number
            int hasPhoneNumber = Integer.parseInt(contactsCursor.getString(contactsCursor.getColumnIndex(HAS_PHONE_NUMBER)));
            Cursor phoneCursor = cr.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{cid}, null);

            if (hasPhoneNumber > 0) {
                // Query and loop for every phone number of the contact
                phoneCursor.moveToNext();
                int type = phoneCursor.getInt(phoneCursor.getColumnIndex(Phone.TYPE));
                if(type==Phone.TYPE_MOBILE){
                    //Log.i("Type work","working");
                    phonenumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    phoneCursor.close();
                    contactList.add(new Contact(contact_id, name, my_btmp, email, phonenumber));
                }
                else{
                    //Log.i("Not work","working");
                }
            } else {
                phonenumber = "NA";
            }
            phoneCursor.close();

            Collections.sort(contactList);
            adapter = new ContactArrayAdapter(this, contactList);
            contactsname.setAdapter(adapter);

        }

        contactsCursor.close();

        inputSearch = (EditText) findViewById(R.id.searchText);

        clearbutton = (FloatingActionButton) findViewById(R.id.clearbutton);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });

        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.clearFocus();

                //Input method declaration(For hidding)
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                contactsname.smoothScrollToPosition(0);
            }
        });

    }



    //Executes itself when the smartphone physical back button is clicked.
    @Override
    public void onBackPressed() {
        hideSoftKeyboard(UserNavigation.this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //Input method declaration(For hidding)
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "The app has been refreshed...", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawer.isDrawerOpen(GravityCompat.START)) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        Intent intent = getIntent();
        final String usernamechecker = intent.getExtras().getString("USERNAME");

        int id = item.getItemId();

        if (id == R.id.viewStaff) {
            drawer.closeDrawer(GravityCompat.START);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent ad = new Intent(UserNavigation.this, AdminList.class);
                    ad.putExtra("ADMINAME",usernamechecker);
                    startActivity(ad);
                }
            }, 200);
        }

        else if (id == R.id.addWorker) {
            drawer.closeDrawer(GravityCompat.START);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent ad = new Intent(UserNavigation.this, RegisterActivity.class);
                    startActivity(ad);
                }
            }, 200);
        }

        else if (id == R.id.removeWorker) {
            drawer.closeDrawer(GravityCompat.START);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    deleto();
                }
            }, 200);
        }

        else if (id == R.id.howTo) {
            drawer.closeDrawer(GravityCompat.START);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent ad = new Intent(UserNavigation.this, ToolsActivity.class);
                    startActivity(ad);
                }
            }, 200);
        }

        else if (id == R.id.logoutApp) {
            logouto();
            drawer.closeDrawer(GravityCompat.START);
        }

        else if (id == R.id.exitApp) {
            alerto();
            drawer.closeDrawer(GravityCompat.START);
        }

        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        relay = (RelativeLayout) findViewById(R.id.relativeback);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            if(drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            alerto();
        }
        return false;
    }

    public void deleto() {

        relay = (RelativeLayout) findViewById(R.id.relativeback);



        AlertDialog.Builder alert = new AlertDialog.Builder(UserNavigation.this);
        alert.setIcon(R.drawable.adminremove);
        alert.setMessage("Do you really want to remove yourself from the database?");
        alert.setTitle("Help");
        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = getIntent();
                final String nama = intent.getExtras().getString("USERNAME");

                addatabase = new AdminDatabaseClass(activity);
                adminuser = new AdminUserClass();
                adminuser.setUsername(nama);
                addatabase.deleteAdmin(adminuser);

                Toast.makeText(getApplicationContext(), "Goodbye " + nama, Toast.LENGTH_LONG).show();
                finish();
                Intent i = new Intent(UserNavigation.this, UserLoginActivity.class);
                startActivity(i);
            }
        });
        alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            Intent intent = getIntent();
            final String nama = intent.getExtras().getString("USERNAME");

            public void onClick(DialogInterface dialog, int id) {
                Snackbar.make(relay, "Welcome back " + nama, Snackbar.LENGTH_SHORT)
                        .setAction("Close", null)
                        .show();
            }
        });
        alert.show();

    }

    public void logouto() {

        relay = (RelativeLayout) findViewById(R.id.relativeback);



        AlertDialog.Builder alert = new AlertDialog.Builder(UserNavigation.this);
        alert.setIcon(R.drawable.logout);
        alert.setMessage("Do you really want to logout from the app?");
        alert.setTitle(" ");
        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = getIntent();
                String nama = intent.getExtras().getString("USERNAME");
                Toast.makeText(getApplicationContext(), "See you soon " + nama, Toast.LENGTH_LONG).show();
                finish();
                Intent i = new Intent(UserNavigation.this, UserLoginActivity.class);
                startActivity(i);
            }
        });

        alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Snackbar.make(relay, "Continue using the app...", Snackbar.LENGTH_SHORT)
                        .setAction("Close", null)
                        .show();
            }
        });

        alert.show();
    }

    public void alerto() {

        relay = (RelativeLayout) findViewById(R.id.relativeback);

        AlertDialog.Builder alert = new AlertDialog.Builder(UserNavigation.this);
        alert.setIcon(R.drawable.exitpopup);
        alert.setMessage("Do you really want to exit?");
        alert.setTitle(" ");
        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "See you soon..", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Snackbar.make(relay, "Back to the app...", Snackbar.LENGTH_SHORT)
                        .setAction("Close", null)
                        .show();
            }
        });
        alert.show();

    }


    public void refreshContactList(){
        contactList.clear();
        try {

            loadContact();
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        needRefreshContactList=false;
    }

    public void refreshActivity(){

        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        needRefreshActivity=false;
    }


    private void addNotification(String name) {

        String replyLabel = getResources().getString(R.string.app_name);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        adminuser = new AdminUserClass();
        adminList = adc.returnAdminInfo(name);

        String phone = adminList.get(0).getPhoneNumber();

        int icon = R.drawable.cake;
        int mNotificationId = 001;

        String wholesen = "It's " + name +"'s birthday!" + "\n" + "Call and wish " + name + " now!!";

        Intent phoneCall = new Intent(Intent.ACTION_CALL);
        phoneCall.setData(Uri.parse("tel:"+phone));
        PendingIntent phoneCallIntent = PendingIntent.getActivity(this, 0, phoneCall, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        Notification notification = mBuilder.setSmallIcon(icon).setTicker("Team Target Birthday Alert").setWhen(0)
                .setAutoCancel(true)
                .setContentTitle("Team Target Birthday Alert")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(wholesen))
                .setContentIntent(phoneCallIntent)
                .setVibrate (new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.GREEN, 3000, 3000)
                .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.arpeggio))
                .addAction(R.drawable.ic_menu_send, "Call " + name, phoneCallIntent)
                .setContentText(wholesen).build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);
    }

    protected void onResume() {
        super.onResume();

        if (needRefreshContactList==true) {
            refreshContactList();
        }

        if(needRefreshActivity==true){
            refreshActivity();
        }

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }
}



class ContactArrayAdapter extends ArrayAdapter<Contact> {

    RoundIconMaker rim;


    Context mContext;
    LayoutInflater inflater;
    private List<Contact> worldpopulationlist = null;
    private ArrayList<Contact> arraylist;

    public ContactArrayAdapter(Context context, ArrayList<Contact> objects) {
        super(context, 0, objects);
        mContext=context;
        this.worldpopulationlist=objects;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Contact>();
        this.arraylist.addAll(worldpopulationlist);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.content_user_navigation, parent, false);

        Context con = null;

        final Contact contact = getItem(position);

        //Contact name setting
        TextView nameTextView = (TextView) view.findViewById(R.id.textViewCName);
        nameTextView.setText(contact.getName());


        ImageView imgv = (ImageView) view.findViewById(R.id.contactimage);
        rim = new RoundIconMaker(contact.getImage());
        imgv.setImageDrawable(rim);


        ImageView callimageicon = (ImageView) view.findViewById(R.id.callimageView);
        callimageicon.setImageResource(R.drawable.calllogo);
        callimageicon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserNavigation cun = new UserNavigation();
                cun.callcaller(v.getContext(),contact.getPhoneNumber(), contact.getName());
            }
        });

        ImageView messageimageicon = (ImageView) view.findViewById(R.id.messageImageView);
        messageimageicon.setImageResource(R.drawable.messagelogo);
        messageimageicon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserNavigation mun = new UserNavigation();
                mun.messagemessengger(v.getContext(),contact.getPhoneNumber(), contact.getName());
            }
        });

        TextView emailtextview = (TextView) view.findViewById(R.id.emailtectview);
        emailtextview.setText(contact.getEmail());

        TextView pnum = (TextView) view.findViewById(R.id.phonenumberTextView);
        pnum.setText(contact.getPhoneNumber());

        //when the contact clicked

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ContactDetailActivity.class);
                    intent.putExtra("id", getItem(position).getId());
                    getContext().startActivity(intent);
                }
            });

        return view;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        } else {
            for (Contact wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}


class Contact implements Comparable<Contact> {
    private long id;
    private String name;
    private String email;
    Bitmap image;
    private String cphonenumber;

    public Contact(long id, String name, Bitmap image, String email, String cphonenumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.cphonenumber = cphonenumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return cphonenumber;
    }

    public void setPhoneNumber(String cphonenumber) {
        this.cphonenumber = cphonenumber;
    }

    @Override
    public int compareTo(Contact o) {
        return (this.name).compareTo(o.name);
    }
}


