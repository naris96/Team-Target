package assignment1.narispillai.com.assignment1;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_CONTACTS;


public class UserLoginActivity extends AppCompatActivity implements LoaderCallbacks<Object> {

    //Permission checking variable.
    private static final int REQUEST_READ_CONTACTS = 0;

    private final AppCompatActivity activity = UserLoginActivity.this;

    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;

    Button signin;
    Button register;

    EditText username;
    EditText password;

    private AdminDatabaseClass admindatabase;

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }

        ll = (LinearLayout) findViewById(R.id.linearlay);

        ImageView loginlogo = (ImageView) findViewById(R.id.imageViewloginlogo);

        signin = (Button) findViewById(R.id.sign_in_button);
        register = (Button) findViewById(R.id.email_register_button);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        username.setText("");
        password.setText("");

        admindatabase = new AdminDatabaseClass(this);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loginscreeniconanimation);
        loginlogo.setAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movefromrightanim);
        signin.setAnimation(animation2);

        Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movefromleftanim);
        register.setAnimation(animation4);


        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterInButton = (Button) findViewById(R.id.email_register_button);
        mRegisterInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(UserLoginActivity.this, RegisterActivity.class);
                startActivity(go);
            }
        });
    }


    //Check if the the user allows the Contact permission. If not then it will show to the user everytime the user launch the application.
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    //Asking for Contact access permission. Mostly for Android 6.0 Marshmallow
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

 void attemptLogin() {
        if (username.getText().toString().equals("") && password.getText().toString().equals("") && username.getText().toString().equals(" ") && password.getText().toString().equals(" ")) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        final String usernamechecker = mUsernameView.getText().toString();
        String passwordchecker = mPasswordView.getText().toString();

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (passwordchecker=="") {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (usernamechecker=="") {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            if (admindatabase.checkUser(username.trim(),password.trim())) {

                Toast.makeText(UserLoginActivity.this,"Logging In...", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent accountsIntent = new Intent(activity, UserNavigation.class);
                        accountsIntent.putExtra("USERNAME", usernamechecker);
                        startActivity(accountsIntent);
                        finish();
                    }
                }, 100);
            } else {

                Snackbar.make(ll, "Wrong username or password!", Snackbar.LENGTH_SHORT)
                        .setAction("Close", null)
                        .show();
            }

        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}



