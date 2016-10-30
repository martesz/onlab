package org.martin.getfreaky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.friends.FriendRecyclerAdapter;
import org.martin.getfreaky.network.GetFreakyService;
import org.martin.getfreaky.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFriendActivity extends AppCompatActivity {

    public static final String KEY_SELECTED_FRIEND_ID = "SELECTED_FRIEND_ID";

    private RecyclerView recyclerView;
    private FriendRecyclerAdapter friendRecyclerAdapter;
    private FloatingActionButton fab;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        userId = preferences.getString(LoginActivity.USER_ID_KEY, "DefaultUser");

        recyclerView = (RecyclerView) findViewById(R.id.friend_recyclerview_share);
        friendRecyclerAdapter = new FriendRecyclerAdapter(getApplication());
        recyclerView.setAdapter(friendRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_share);
        setSupportActionBar(toolbar);

        fab =
                (FloatingActionButton) findViewById(R.id.select_friend_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFriendId = friendRecyclerAdapter.getSelectedUserId();
                if (selectedFriendId != null) {
                    selectFriend(selectedFriendId);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a friend", Toast.LENGTH_LONG).show();
                }
            }
        });

        getFriendsFromService();
    }

    // Put the selected user id in the result and finish the activity
    private void selectFriend(String friendId) {
        Intent intentResult = new Intent();
        intentResult.putExtra(KEY_SELECTED_FRIEND_ID, friendId);

        setResult(RESULT_OK, intentResult);
        finish();
    }

    private void getFriendsFromService() {
        RetrofitClient client = new RetrofitClient((GlobalVariables) this.getApplication());
        GetFreakyService service = client.createService();
        Call<List<User>> call = service.getFriends(userId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> friendsFromServer = response.body();
                friendRecyclerAdapter.clear();
                friendRecyclerAdapter.addAll(friendsFromServer);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}
