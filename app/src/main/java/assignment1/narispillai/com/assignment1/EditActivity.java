package assignment1.narispillai.com.assignment1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    String username;
    String position;
    CharSequence email;
    String password;
    String gender;
    String address;
    String phone;
    String datechosen;

    private final AppCompatActivity activity = EditActivity.this;
    private AdminDatabaseClass editatabase;
    private AdminUserClass adminuser;

    List<AdminUserClass> adminList = new ArrayList<AdminUserClass>();

    private TextView date;

    DateFormat formatDateTime = new SimpleDateFormat("d MMM yyyy");
    Calendar dateTime = Calendar.getInstance();

    DateFormat DMofBirth = new SimpleDateFormat("d MMM");

    String currentdatetime;

    String DMOB;

    private static final int PLACE_PICKER_REQUEST = 1;

    //Permission request checking interger variables
    final private int REQUEST_LOCATION = 123;

    EditText addresstxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText usernametxt = (EditText)findViewById(R.id.editTextUsername);
        final Spinner positionspinner = (Spinner) findViewById(R.id.positionspinner);
        final EditText emailtxt = (EditText)findViewById(R.id.editTextemail);
        final RadioButton male = (RadioButton) findViewById(R.id.radioButtonMale);
        final RadioButton female = (RadioButton) findViewById(R.id.radioButtonFemale);
        final EditText passwordtxt = (EditText) findViewById(R.id.editTextPassword);
        addresstxt = (EditText) findViewById(R.id.editTextaddress);
        final EditText phonetxt = (EditText) findViewById(R.id.editTextphonenumber);
        final TextView dpked = (TextView) findViewById(R.id.DatePickerDOB);

        Intent intent = getIntent();
        final String adminname = intent.getExtras().getString("Name");

        editatabase = new AdminDatabaseClass(activity);
        adminuser = new AdminUserClass();

        adminList = editatabase.returnAdminInfo(adminname);

        usernametxt.setText(adminList.get(0).getUsername());
        positionspinner.setSelection(getIndex(positionspinner, adminList.get(0).getPosition()));
        emailtxt.setText(adminList.get(0).getEmail());

        String getgender = adminList.get(0).getGender();

        if(getgender.equals("Male")){
            male.setChecked(true);
            gender="Male";
        }
        if (getgender.equals("Female")){
            female.setChecked(true);
            gender="Female";
        }

        passwordtxt.setText(adminList.get(0).getPassword());
        addresstxt.setText(adminList.get(0).getAddress());
        phonetxt.setText(adminList.get(0).getPhoneNumber());
        dpked.setText(adminList.get(0).getBirthday());
        DMOB=adminList.get(0).getDMofbirth();

        ImageView savebutton = (ImageView) findViewById(R.id.imageViewSave);
        ImageView clearbutton = (ImageView) findViewById(R.id.imageViewClear);

        final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        date = (TextView) findViewById(R.id.DatePickerDOB);
        date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateDate();
            }
        });

        addresstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                } else {
                    try {
                        startActivityForResult(builder.build(EditActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        currentdatetime = new SimpleDateFormat("d MMM yyyy").format(new Date());

        savebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                username = usernametxt.getText().toString();
                position = positionspinner.getSelectedItem().toString();
                email = emailtxt.getText().toString().trim();
                password = passwordtxt.getText().toString();
                address = addresstxt.getText().toString();
                phone = phonetxt.getText().toString();
                datechosen = date.getText().toString();

                int usernamelength = username.length();
                int passwordlength = password.length();

                if(username.equals("")){
                    usernametxt.setBackgroundColor(Color.YELLOW);
                }
                else{
                    usernametxt.setBackgroundColor(Color.WHITE);
                }
                if(username.length()<5 && username.length()>0 || username.contains(" ")){
                    usernametxt.setBackgroundColor(Color.RED);
                    Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    usernametxt.setText("");
                }
                if(position.equals("Your position")){
                    positionspinner.setBackgroundColor(Color.YELLOW);
                }
                else{
                    positionspinner.setBackgroundColor(Color.WHITE);
                }
                if(address.equals("") || (address.equals(" "))){
                    addresstxt.setBackgroundColor(Color.YELLOW);
                    addresstxt.setText("");
                }
                else{
                    if(!address.contains(" ") || address.length()<10){
                        addresstxt.setText("");
                        addresstxt.setBackgroundColor(Color.RED);
                        Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                    else{
                        addresstxt.setBackgroundColor(Color.WHITE);
                    }
                }

                if(datechosen.equals(currentdatetime)){
                    date.setBackgroundColor(Color.RED);
                    Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    dpked.setText("");
                }
                else{
                    dpked.setBackgroundColor(Color.WHITE);
                }
                if(datechosen.equals(" ") || (datechosen.equals(""))){
                    date.setBackgroundColor(Color.YELLOW);
                    dpked.setBackgroundColor(Color.YELLOW);
                    Snackbar.make(view, "Empty Birthday field", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    dpked.setText("");
                }
                else{
                    dpked.setBackgroundColor(Color.WHITE);
                }

                if(phone.equals("")){
                    phonetxt.setBackgroundColor(Color.YELLOW);
                    phonetxt.setText("");
                }
                else{
                    if(phone.length()<7){
                        phonetxt.setText("");
                        phonetxt.setBackgroundColor(Color.RED);
                        Snackbar.make(view, "Invalid Phone Number!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                    else{
                        phonetxt.setBackgroundColor(Color.WHITE);
                    }
                }

                if(email.equals("")){
                    emailtxt.setBackgroundColor(Color.YELLOW);
                }
                else{
                    emailtxt.setBackgroundColor(Color.WHITE);
                }

                if(!male.isChecked() && !female.isChecked()){
                    male.setBackgroundColor(Color.YELLOW);
                    female.setBackgroundColor(Color.YELLOW);
                }
                else{

                    if (male.isChecked()){
                        gender="Male";
                    }
                    if(female.isChecked()){
                        gender="Female";
                    }
                    male.setBackgroundColor(Color.WHITE);
                    female.setBackgroundColor(Color.WHITE);
                }
                if(password.equals("")){
                    passwordtxt.setBackgroundColor(Color.YELLOW);
                }
                else{
                    passwordtxt.setBackgroundColor(Color.WHITE);
                }
                if(password.length()<5 && password.length()>0 || password.contains(" ")){
                    passwordtxt.setBackgroundColor(Color.RED);
                    Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    passwordtxt.setText("");
                }

                if(username.equals("") || phone.equals("") || datechosen.equals("") || datechosen.equals(" ") || address.equals("") || address.equals(" ") || email.equals("") || username.equals("") || password.equals("") || position.equals("Your position") || !male.isChecked() && !female.isChecked() || email.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(),"The yellow highlighted fill is empty \n Complete the form.",Toast.LENGTH_SHORT).show();
                }

                else if(!username.equals("") && !address.equals("") && !address.equals(" ") && (address.contains(" ") && address.length()<10) && !datechosen.equals(currentdatetime) && !datechosen.equals("") && !datechosen.equals(" ") && !phone.equals("") && !username.contains(" ") && username.length()>=5 && !password.contains(" ") && password.length()>=5 && !position.equals("Your position") && male.isChecked() && female.isChecked() && !email.equals("") || !password.equals("")){

                    if(!isValidEmail(email)){
                        emailtxt.setBackgroundColor(Color.RED);
                        Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        emailtxt.setText("");
                    }
                    else {

                        if(username.length()<5 && username.length()>0 || username.contains(" ") || datechosen.equals(currentdatetime) || datechosen.equals("") || datechosen.equals(" ") || password.length()<5 && password.length()>0 || password.contains(" ") || phone.length()<7 || !address.contains(" ") || address.length()<10){

                            if(username.length()<5 && username.length()>0 || username.contains(" ")){
                                usernametxt.setBackgroundColor(Color.RED);
                                Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                usernametxt.setText("");
                            }

                            if(password.length()<5 && password.length()>0 || password.contains(" ")){
                                passwordtxt.setBackgroundColor(Color.RED);
                                Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                passwordtxt.setText("");
                            }

                            if(datechosen.equals(currentdatetime) || datechosen.equals(" ")){

                                dpked.setBackgroundColor(Color.RED);
                                Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                dpked.setText("");

                            }

                            if(phone.length()<7){
                                phonetxt.setText("");
                                phonetxt.setBackgroundColor(Color.RED);
                                Snackbar.make(view, "Invalid Phone Number!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }

                            if(!address.contains(" ") || address.length()<10){
                                addresstxt.setText("");
                                addresstxt.setBackgroundColor(Color.RED);
                                Snackbar.make(view, "Invalid value in the red highlighted text box", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }

                        }
                        else{

                            editatabase = new AdminDatabaseClass(activity);
                            adminuser = new AdminUserClass();

                                if(editatabase.checkUserEmailEdit(usernametxt.getText().toString().trim(),emailtxt.getText().toString().trim())){

                                    if(editatabase.checkUserPositionEdit(usernametxt.getText().toString().trim(),positionspinner.getSelectedItem().toString())) {

                                        adminuser.setUsername(usernametxt.getText().toString().trim());
                                        adminuser.setPassword(passwordtxt.getText().toString().trim());
                                        adminuser.setBirthday(date.getText().toString().trim());
                                        adminuser.setGender(gender);
                                        adminuser.setEmail(emailtxt.getText().toString().trim());
                                        adminuser.setPosition(positionspinner.getSelectedItem().toString());
                                        adminuser.setAddress(addresstxt.getText().toString().trim());
                                        adminuser.setPhonenumber(phonetxt.getText().toString().trim());
                                        adminuser.setDMofbirth(DMOB.trim());


                                        editatabase.updateAdmin(adminuser);

                                        new android.support.v7.app.AlertDialog.Builder(EditActivity.this)
                                                .setTitle("Message")
                                                .setMessage("Your form was successfully updated!")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .setIcon(R.drawable.checked)
                                                .show();

                                        usernametxt.setText("");
                                        usernametxt.setBackgroundColor(Color.WHITE);


                                        positionspinner.setSelection(0);
                                        positionspinner.setBackgroundColor(Color.WHITE);


                                        male.setChecked(false);
                                        female.setChecked(false);
                                        male.setBackgroundColor(Color.WHITE);
                                        female.setBackgroundColor(Color.WHITE);


                                        emailtxt.setText("");
                                        emailtxt.setBackgroundColor(Color.WHITE);

                                        date.setText("");

                                        passwordtxt.setText("");
                                        passwordtxt.setBackgroundColor(Color.WHITE);

                                        addresstxt.setText("");
                                        addresstxt.setBackgroundColor(Color.WHITE);

                                        phonetxt.setText("");
                                        phonetxt.setBackgroundColor(Color.WHITE);

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Position already filled", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Duplicate Email", Toast.LENGTH_LONG).show();
                                }
                        }
                    }
                }
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                female.setChecked(false);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                male.setChecked(false);
            }
        });

        clearbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                positionspinner.setSelection(0);
                positionspinner.setBackgroundColor(Color.WHITE);


                male.setChecked(false);
                female.setChecked(false);
                male.setBackgroundColor(Color.WHITE);
                female.setBackgroundColor(Color.WHITE);

                gender="";

                emailtxt.setText("");
                emailtxt.setBackgroundColor(Color.WHITE);


                passwordtxt.setText("");
                passwordtxt.setBackgroundColor(Color.WHITE);

                date.setText("");
                date.setBackgroundColor(Color.WHITE);

                addresstxt.setText("");
                addresstxt.setBackgroundColor(Color.WHITE);

                phonetxt.setText("");
                phonetxt.setBackgroundColor(Color.WHITE);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                address = (String) place.getAddress();
                addresstxt.setText(address);
            }
        }
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTextLabel();
            updateTextLabelDMofBirth();
        }
    };

    private void updateDate(){
        Calendar c = Calendar.getInstance();
        c.set(1999, 12, 31);
        DatePickerDialog sel = new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH));
        sel.getDatePicker().setMaxDate(c.getTimeInMillis());
        //new Date().getTime()
        sel.show();
    }

    private void updateTextLabel(){
        date.setText(formatDateTime.format(dateTime.getTime()));
    }

    private void updateTextLabelDMofBirth(){
        DMOB =(DMofBirth.format(dateTime.getTime()));
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void alerto(){
        AlertDialog.Builder alert = new AlertDialog.Builder(EditActivity.this);
        alert.setIcon(R.drawable.warning);
        alert.setMessage("Discard your changes and exit from the screen?");
        alert.setTitle(" ");
        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Do nothing
            }
        });
        alert.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            alerto();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                alerto();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
