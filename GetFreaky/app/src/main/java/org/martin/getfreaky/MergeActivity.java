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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
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

import org.martin.getfreaky.dataObjects.MergeData;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.network.GetFreakyService;
import org.martin.getfreaky.network.MergeResponse;
import org.martin.getfreaky.network.RetrofitClient;
import org.martin.getfreaky.utils.Password;
import org.martin.getfreaky.utils.Validator;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;

public class MergeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EmailMergeTask mAuthTask = null;
    private MergeResponse response;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 101;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String userId;
    private GlobalVariables application = (GlobalVariables) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_merge);
        callbackManager = CallbackManager.Factory.create();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_merge);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MergeActivity.this);
        userId = preferences.getString(LoginActivity.USER_ID_KEY, "DefaultUser");

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_merge);

        mPasswordView = (EditText) findViewById(R.id.password_merge);
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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button_merge);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form_merge);
        mProgressView = findViewById(R.id.merge_progress);

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

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button_google_merge);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Facebook login
        loginButton = (LoginButton) findViewById(R.id.login_button_facebook_merge);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new FacebookMergeTask(loginResult).execute((Void) null);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        application = (GlobalVariables) this.getApplication();
    }

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
            mAuthTask = new EmailMergeTask(email, password);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            new GoogleMergeTask(result).execute((Void) null);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class EmailMergeTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private Realm realm;

        public EmailMergeTask(String mEmail, String mPassword) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            realm = Realm.getDefaultInstance();
            User user = realm.where(User.class)
                    .equalTo("id", userId)
                    .findFirst();

            User userByEmail = realm.where(User.class)
                    .equalTo("email", mEmail)
                    .findFirst();

            MergeData data = new MergeData();
            data.setUserId(userId);
            data.setEmail(mEmail);
            data.setPassword(mPassword);
            RetrofitClient client = new RetrofitClient(application);
            GetFreakyService service = client.createService();
            Call<MergeResponse> call = service.mergeUsers(data);
            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                if (response.getMessage() == MergeResponse.Message.EMAIL_ASSOCIATED) {

                    if (userByEmail != null) {
                        realm.beginTransaction();
                        user.setEmail(userByEmail.getEmail());
                        user.setPassword(userByEmail.getPassword()); // Already checked the password on the server
                        realm.commitTransaction();
                        user.merge(userByEmail, realm);
                    } else {
                        realm.beginTransaction();
                        user.setEmail(mEmail);
                        user.setPassword(Password.getHash(mPassword));
                        realm.commitTransaction();
                    }
                }
            } else {
                response = new MergeResponse(MergeResponse.Message.COULD_NOT_CONNECT);
            }
            realm.close();
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            handleResult();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private class FacebookMergeTask extends AsyncTask<Void, Void, Boolean> {
        private LoginResult result;
        Realm realm;

        public FacebookMergeTask(LoginResult loginResult) {
            this.result = loginResult;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            realm = Realm.getDefaultInstance();
            AccessToken accessToken = result.getAccessToken();
            String token = accessToken.getToken();
            String facebookUserId = accessToken.getUserId();

            MergeData data = new MergeData();
            data.setUserId(userId);
            data.setFacebookAccessToken(token);
            RetrofitClient client = new RetrofitClient(application);
            GetFreakyService service = client.createService();
            Call<MergeResponse> call = service.mergeUsers(data);

            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                if (response.getMessage() == MergeResponse.Message.FACEBOOK_ACCOUNT_ASSOCIATED) {
                    User userByFacebookId = realm.where(User.class)
                            .equalTo("facebookId", facebookUserId)
                            .findFirst();
                    User user = realm.where(User.class)
                            .equalTo("id", userId)
                            .findFirst();
                    realm.beginTransaction();
                    user.setFacebookId(facebookUserId);
                    realm.commitTransaction();
                    if (userByFacebookId != null) {
                        user.merge(userByFacebookId, realm);
                    }
                }
            } else {
                response = new MergeResponse(MergeResponse.Message.COULD_NOT_CONNECT);
            }
            realm.close();
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            handleResult();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private class GoogleMergeTask extends AsyncTask<Void, Void, Boolean> {

        private GoogleSignInResult result;
        Realm realm;

        public GoogleMergeTask(GoogleSignInResult result) {
            this.result = result;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            realm = Realm.getDefaultInstance();
            GoogleSignInAccount acct = result.getSignInAccount();
            MergeData data = new MergeData();
            data.setUserId(userId);
            data.setGoogleIdToken(acct.getIdToken());
            RetrofitClient client = new RetrofitClient(application);
            GetFreakyService service = client.createService();
            Call<MergeResponse> call = service.mergeUsers(data);
            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                if (response.getMessage() == MergeResponse.Message.GOOGLE_ACCOUNT_ASSOCIATED) {
                    User googleUser = realm.where(User.class)
                            .equalTo("googleId", acct.getId())
                            .findFirst();
                    User user = realm.where(User.class)
                            .equalTo("id", userId)
                            .findFirst();
                    realm.beginTransaction();
                    user.setGoogleId(acct.getId());
                    realm.commitTransaction();
                    if (googleUser != null) {
                        user.merge(googleUser, realm);
                    }
                }
            } else {
                response = new MergeResponse(MergeResponse.Message.COULD_NOT_CONNECT);
            }
            realm.close();
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            handleResult();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void handleResult() {
        if (response != null) {
            Toast.makeText(MergeActivity.this, response.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
