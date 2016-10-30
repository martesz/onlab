package org.martin.getfreaky.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.martin.getfreaky.GlobalVariables;
import org.martin.getfreaky.R;
import org.martin.getfreaky.network.FriendResponse;
import org.martin.getfreaky.network.GetFreakyService;
import org.martin.getfreaky.network.RetrofitClient;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by martin on 2016. 10. 25..
 */

public class AddFriendFragment extends DialogFragment {

    public static final String TAG = "AddFriendFragment";

    private EditText email;
    private IAddFriendListener listener;
    private CallbackManager callbackManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getTargetFragment() != null) {
            try {
                listener = (IAddFriendListener) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        } else {
            try {
                listener = (IAddFriendListener) activity;
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Parent Activity does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_add_friend, container, false);

        FacebookSdk.sdkInitialize(this.getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                addFacebookFriends(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        getDialog().setTitle("Add new friend");

        email = (EditText) root.findViewById(R.id.add_friend_email);

        final Button addEmailButton = (Button) root.findViewById(R.id.add_friend_email_button);
        addEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserByEmail();
                dismiss();
            }
        });

        final Button addFacebookButton = (Button) root.findViewById(R.id.add_friend_facebook_button);
        addFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    addFacebookFriends(accessToken.getToken());
                } else {
                    // User have to login first
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(),
                            Arrays.asList("public_profile", "user_friends"));
                }
                dismiss();
            }
        });

        Button cancelButton = (Button) root.findViewById(R.id.button_cancel_add_friend);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return root;
    }

    private void addUserByEmail() {
        RetrofitClient client = new RetrofitClient((GlobalVariables) this.getActivity().getApplication());
        GetFreakyService service = client.createService();
        Call<FriendResponse> call = service.putFriendByEmail(email.getText().toString());

        call.enqueue(new Callback<FriendResponse>() {
            @Override
            public void onResponse(Call<FriendResponse> call, Response<FriendResponse> response) {
                listener.onFriendAdded(response.body());
            }

            @Override
            public void onFailure(Call<FriendResponse> call, Throwable t) {

            }
        });
    }

    private void addFacebookFriends(String facebookAccessToken) {
        RetrofitClient client = new RetrofitClient((GlobalVariables) this.getActivity().getApplication());
        GetFreakyService service = client.createService();
        Call<FriendResponse> call = service.putFacebookFriends(facebookAccessToken);

        call.enqueue(new Callback<FriendResponse>() {
            @Override
            public void onResponse(Call<FriendResponse> call, Response<FriendResponse> response) {
                listener.onFriendAdded(response.body());
            }

            @Override
            public void onFailure(Call<FriendResponse> call, Throwable t) {

            }
        });
    }

    public interface IAddFriendListener {
        void onFriendAdded(FriendResponse friendResponse);
    }
}
