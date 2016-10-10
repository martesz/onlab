package org.martin.getfreaky;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.network.DayLogResponse;
import org.martin.getfreaky.network.GetFreakyService;
import org.martin.getfreaky.network.LoginResponse;
import org.martin.getfreaky.network.RetrofitClient;
import org.martin.getfreaky.utils.Password;
import org.martin.getfreaky.utils.Validator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.BaseRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import retrofit2.Call;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String CURRENT_DAYLOG_ID_KEY = "DAYLOGIDKEY";

    public static String USER_ID_KEY = "user";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private LoginResponse response;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 101;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        // Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_server_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button_google);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Facebook login
        loginButton = (LoginButton) findViewById(R.id.login_button_facebook);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new FacebookSignInTask(loginResult).execute((Void) null);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            new GoogleSignInTask(result).execute((Void) null);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !Validator.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Validator.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void updateUser(User user, LoginResponse response) {

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        Realm realm;

        @Override
        protected Boolean doInBackground(Void... params) {

            realm = Realm.getDefaultInstance();
            User user = realm.where(User.class)
                    .equalTo("email", mEmail)
                    .findFirst();

            User userToSend = new User();
            userToSend.setEmail(mEmail);
            userToSend.setPassword(mPassword);

            RetrofitClient client = new RetrofitClient();
            GetFreakyService service = client.createService();
            Call<LoginResponse> call = service.signInOrRegisterUser(userToSend);
            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                if (response.getMessage() == LoginResponse.ResponseMessage.USER_REGISTERED ||
                        response.getMessage() == LoginResponse.ResponseMessage.USER_SIGNED_IN) {
                    // User not yet exists in our database, we have to store it
                    if (user == null) {
                        String hashedPassword = Password.getHash(mPassword);
                        userToSend.setPassword(hashedPassword);
                        storeUser(userToSend, response, realm);
                    } else {
                        // User already exists in our database, only update it
                        user.update(response.getUser(), realm);
                        createDaylog(user, realm);
                        saveUserInPreferences(user.getId());
                    }
                    realm.close();
                    return true;
                } else {
                    realm.close();
                    return false;
                }
            } else {
                // If the user exists on local database, sign in offline
                if (user != null) {
                    // Check the password
                    if (Password.equals(mPassword, user.getPassword())) {
                        createDaylog(user, realm);
                        saveUserInPreferences(user.getId());
                        realm.close();
                        response = new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN);
                        return true;
                    } else {
                        response = new LoginResponse(LoginResponse.ResponseMessage.WRONG_PASSWORD);
                        realm.close();
                        return false;
                    }
                } else {
                    realm.close();
                    return false;
                }
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            handleResult(success);

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class GoogleSignInTask extends AsyncTask<Void, Void, Boolean> {

        private GoogleSignInResult result;

        Realm realm;

        public GoogleSignInTask(GoogleSignInResult result) {
            this.result = result;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            GoogleSignInAccount acct = result.getSignInAccount();

            realm = Realm.getDefaultInstance();
            User user = realm.where(User.class)
                    .equalTo("googleId", acct.getId())
                    .findFirst();

            // If the user does not exist create it
            if (user == null) {
                user = new User();
                user.setGoogleId(acct.getId());
                user.setName(acct.getDisplayName());

                RetrofitClient client = new RetrofitClient();
                GetFreakyService service = client.createService();
                Call<LoginResponse> call = service.signInOrRegisterGoogle(acct.getIdToken());
                try {
                    response = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    if (response.getMessage() == LoginResponse.ResponseMessage.USER_REGISTERED ||
                            response.getMessage() == LoginResponse.ResponseMessage.USER_SIGNED_IN) {
                        storeUser(user, response, realm);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    response = new LoginResponse(LoginResponse.ResponseMessage.COULD_NOT_CONNECT);
                    return false;
                }
            } else {
                createDaylog(user, realm);
                saveUserInPreferences(user.getId());
                realm.close();
                response = new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN);
                return true;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            handleResult(success);

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class FacebookSignInTask extends AsyncTask<Void, Void, Boolean> {

        private LoginResult result;
        Realm realm;

        public FacebookSignInTask(LoginResult result) {
            this.result = result;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AccessToken accessToken = result.getAccessToken();
            String token = accessToken.getToken();
            String facebookUserId = accessToken.getUserId();

            realm = Realm.getDefaultInstance();
            User user = realm.where(User.class)
                    .equalTo("facebookId", facebookUserId)
                    .findFirst();

            // If the user does not exist create it
            if (user == null) {
                user = new User();
                user.setFacebookId(facebookUserId);

                RetrofitClient client = new RetrofitClient();
                GetFreakyService service = client.createService();
                Call<LoginResponse> call = service.signInOrRegisterFacebook(token);
                try {
                    response = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    if (response.getMessage() == LoginResponse.ResponseMessage.USER_REGISTERED ||
                            response.getMessage() == LoginResponse.ResponseMessage.USER_SIGNED_IN) {
                        storeUser(user, response, realm);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    response = new LoginResponse(LoginResponse.ResponseMessage.COULD_NOT_CONNECT);
                    return false;
                }
            } else {
                createDaylog(user, realm);
                saveUserInPreferences(user.getId());
                realm.close();
                response = new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN);
                return true;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            handleResult(success);

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    private void storeUser(User user, LoginResponse response, Realm realm) {
        user.setId(response.getAssignedUserId());
        createDaylog(user, realm);
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
        saveUserInPreferences(user.getId());
        realm.close();
    }

    private void handleResult(Boolean success) {
        if (success) {
            finish();
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
        } else {
            if (response != null) {
                if (response.getMessage() == LoginResponse.ResponseMessage.WRONG_PASSWORD) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                } else {
                    Toast.makeText(LoginActivity.this, response.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void saveUserInPreferences(String userId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        preferences.edit().putString(USER_ID_KEY, userId).apply();
    }

    private void createDaylog(User user, BaseRealm realm) {
        RealmList<DayLog> dayLogs = user.getDayLogs();

        RetrofitClient client = new RetrofitClient();
        GetFreakyService service = client.createService();
        Call<List<DayLog>> call = service.getDayLogs(user.getId());

        try {
            List<DayLog> dayLogsOnServer = call.execute().body();
            for (DayLog dayLog : dayLogsOnServer) {
                if (!dayLogs.contains(dayLog)) {
                    realm.beginTransaction();
                    dayLogs.add(dayLog);
                    realm.commitTransaction();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get or create a daylog for today
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String currentDay = fmt.format(calendar.getTime());

        DayLog dayLog = null;

        for (DayLog dl : dayLogs) {
            if (fmt.format(dl.getDate()).equals(currentDay)) {
                dayLog = dl;
            }
        }
        if (dayLog == null) {
            dayLog = new DayLog(calendar.getTime());
            realm.beginTransaction();
            user.getDayLogs().add(dayLog);
            realm.commitTransaction();
            Call<DayLogResponse> dayLogCall = service.putDayLog(dayLog, user.getId());
            try {
                DayLogResponse dlr = dayLogCall.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        preferences.edit().putString(CURRENT_DAYLOG_ID_KEY, dayLog.getDayLogId()).apply();
    }
}

