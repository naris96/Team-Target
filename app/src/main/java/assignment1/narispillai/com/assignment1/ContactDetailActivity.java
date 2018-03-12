package assignment1.narispillai.com.assignment1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ContactDetailActivity extends AppCompatActivity {

    TextView cntext;
    TextView cnphonenumber;
    TextView cemail;

    TextView contactname;
    TextView contactphonenumber;
    ImageView callbutton;
    ImageView messagebutton;
    ListView contactallemail;

    ImageView contactimage;

    final Context context = this;

    final private int REQUEST_CALL_CONTACTS = 456;
    final private int REQUEST_MESSAGE_CONTACTS = 789;

    private static final String PREF_NAME = "nextage_quiz";
    private static final int PRIVATE_MODE = 0;

    SharedPreferences getPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RelativeLayout backyCD = (RelativeLayout) findViewById(R.id.activity_contact_detail);
        final ImageView background = (ImageView) findViewById(R.id.background);

        getPrefs = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        if (getPrefs.getInt("id", 0) == 1) {
            backyCD.setBackgroundResource(R.drawable.bg1);
        } else if (getPrefs.getInt("id", 0) == 2) {
            backyCD.setBackgroundResource(R.drawable.bg2);
        } else if (getPrefs.getInt("id", 0) == 3) {
            backyCD.setBackgroundResource(R.drawable.bg3);
        } else if (getPrefs.getInt("id", 0) == 4) {
            backyCD.setBackgroundResource(R.drawable.bg4);
        }
        else{
            if (!getPrefs.getString("path", "").equalsIgnoreCase("")) {

                Picasso.with(ContactDetailActivity.this)
                        .load("file:" + getPrefs.getString("path", ""))
                        .fit()
                        .centerCrop()
                        .into(background, new Callback() {
                            @Override
                            public void onSuccess() {

                                backyCD.setBackgroundDrawable(background.getDrawable());
                            }

                            @Override
                            public void onError() {

                                Log.i("Background Photo","Missing user photo");

                            }
                        });
            }
        }

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollviewacontactdetail);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        long id = getIntent().getExtras().getLong("id");

        cntext = (TextView) findViewById(R.id.contactnametextview);
        cnphonenumber = (TextView) findViewById(R.id.contactpntextview);
        cemail = (TextView) findViewById(R.id.contactemailtextview);
        callbutton = (ImageView) findViewById(R.id.callbutton);
        messagebutton = (ImageView) findViewById(R.id.messagebutton);

        contactname = (TextView) findViewById(R.id.contactname);
        contactphonenumber = (TextView) findViewById(R.id.contactphonenumber);
        contactallemail = (ListView) findViewById(R.id.contactemail);

        cntext.setText("Name ");
        cnphonenumber.setText("Phone Number ");
        cemail.setText("Email ");
        callbutton.setImageResource(R.drawable.calllogo);
        messagebutton.setImageResource(R.drawable.messagelogo);


        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contactno = contactphonenumber.getText().toString();
                String conname = contactname.getText().toString();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_CONTACTS);

                    Snackbar.make(view, "Click 'ALLOW' then try again.", Snackbar.LENGTH_LONG)
                            .setAction("Close", null)
                            .show();
                }
                else{
                    ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_CONTACTS);
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+contactno));
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Calling " + conname + " now...", Toast.LENGTH_LONG).show();
                }
            }
        });

        messagebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(final View view){

                final String contactno = contactphonenumber.getText().toString();
                String conname = contactname.getText().toString();

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_MESSAGE_CONTACTS);
                    Snackbar.make(view.findViewById(android.R.id.content), "Click 'ALLOW' then try again.", Snackbar.LENGTH_LONG)
                            .setAction("Close", null)
                            .show();
                }
                else {

                    final Dialog alert;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        alert= new Dialog(ContactDetailActivity.this, android.R.style.Theme_Material_Light_Dialog);
                    }
                    else{
                        alert = new Dialog(ContactDetailActivity.this);
                    }

                    alert.setContentView(R.layout.alertdialogmessagebox);
                    final SmsManager smsManager = SmsManager.getDefault();

                    final EditText message = (EditText) alert.findViewById(R.id.editTextmessagetextview);
                    Button sendbut = (Button) alert.findViewById(R.id.sendbutton);
                    Button cancel = (Button) alert.findViewById(R.id.cancelbutton);

                    alert.setTitle("Send Message to " + conname);



                    sendbut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String messagetext = message.getText().toString();
                            if(messagetext.isEmpty()) {
                                Snackbar.make(ContactDetailActivity.this.findViewById(android.R.id.content), "Field is empty. Message not sent.", Snackbar.LENGTH_LONG)
                                        .setAction("Close", null)
                                        .show();
                                alert.dismiss();
                            }

                            else{
                                smsManager.sendTextMessage(contactno, null, messagetext, null, null);
                                Snackbar.make(ContactDetailActivity.this.findViewById(android.R.id.content), "Message successfully send.", Snackbar.LENGTH_LONG)
                                        .setAction("Close", null)
                                        .show();
                                alert.dismiss();
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar.make(ContactDetailActivity.this.findViewById(android.R.id.content), "Message cancelled.", Snackbar.LENGTH_LONG)
                                    .setAction("Close", null)
                                    .show();
                            alert.dismiss();
                        }
                    });

                    alert.show();

                    }
                }
        });

        //load the detail of the selected contact
        CursorLoader detailsCursorLoader = new CursorLoader(this,
                ContactsContract.Contacts.CONTENT_URI,
                null,
                ContactsContract.Contacts._ID + "= ?",
                new String[]{String.valueOf(id)},
                null);

        RoundIconMaker rim;

        Cursor detailsCursor = detailsCursorLoader.loadInBackground();
        detailsCursor.moveToFirst();

        //Getting contact name
        String name = detailsCursor.getString(
                detailsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        contactname.setText(name);

        loadEmail(id);
        loadPhone(String.valueOf(id));

        //Getting contact photo
        String photoUri = detailsCursor.getString(detailsCursor.getColumnIndex(ContactsContract.Data.PHOTO_URI));
        Bitmap my_btmp = BitmapFactory.decodeResource(getResources(), R.drawable.userbig);
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

        contactimage = (ImageView) findViewById(R.id.contactimageview);
        rim = new RoundIconMaker(my_btmp);
        contactimage.setImageDrawable(rim);
    }

    //Load email method to list out all the contact email.
    private void loadEmail(long contact_id) {
        CursorLoader emailCursorLoader = new CursorLoader(this,
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",
                new String[]{String.valueOf(contact_id)},
                null);

        Cursor emailCursor = emailCursorLoader.loadInBackground();

        //create a email list
        ArrayList<String> emailList = new ArrayList<>();

        //loop all emails
        while (emailCursor.moveToNext()) {
            //get email address
            String email = emailCursor.getString(
                    emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            //add email to email list
            emailList.add(email);
        }

        contactallemail.setAdapter(new EmailArrayAdapter(this, emailList));
    }

    private void loadPhone(String contact_id)  {

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        String phonenumber = "";
        ContentResolver cr = getContentResolver();

        CursorLoader contactsCursorLoader = new CursorLoader(this,
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        Cursor phoneCursor = cr.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

        phoneCursor.moveToNext();

        int hasPhoneNumber = Integer.parseInt(phoneCursor.getString(phoneCursor.getColumnIndex(HAS_PHONE_NUMBER)));

        if (hasPhoneNumber > 0) {
            int type = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                phonenumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                phoneCursor.close();
                contactphonenumber.setText(phonenumber);
        } else {
            phonenumber = "NA";
        }
    }

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

class EmailArrayAdapter extends ArrayAdapter<String> {

    public EmailArrayAdapter(Context context, ArrayList<String> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.emailonlylist, parent, false);

        final TextView emailTextView = (TextView) view.findViewById(R.id.contactemailtx);
        emailTextView.setText(getItem(position));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pemail = emailTextView.getText().toString();
                //Toast.makeText(getContext(), pemail, Toast.LENGTH_SHORT).show();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                //emailIntent.setData(Uri.parse("mailto:"+ pemail));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{pemail});
                emailIntent.putExtra(Intent.EXTRA_CC, pemail);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Team Target");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent from Team Target");

                try {
                    getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


}
