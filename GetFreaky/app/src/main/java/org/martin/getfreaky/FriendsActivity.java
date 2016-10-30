package org.martin.getfreaky;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.friends.AddFriendFragment;
import org.martin.getfreaky.friends.FriendRecyclerAdapter;
import org.martin.getfreaky.network.FriendResponse;
import org.martin.getfreaky.network.GetFreakyService;
import org.martin.getfreaky.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsActivity extends AppCompatActivity implements AddFriendFragment.IAddFriendListener {

    private RecyclerView recyclerView;
    private FriendRecyclerAdapter friendRecyclerAdapter;
    private FloatingActionButton fab;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        userId = preferences.getString(LoginActivity.USER_ID_KEY, "DefaultUser");

        recyclerView = (RecyclerView) findViewById(R.id.friend_recyclerview);
        friendRecyclerAdapter = new FriendRecyclerAdapter(getApplication());
        recyclerView.setAdapter(friendRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friends);
        setSupportActionBar(toolbar);

        fab =
                (FloatingActionButton) findViewById(R.id.add_friend_email_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewFriendDialog();
            }
        });

        getFriendsFromService();
    }

    private void showNewFriendDialog() {
        AddFriendFragment addFriendFragment = new AddFriendFragment();
        FragmentManager fm = getSupportFragmentManager();
        addFriendFragment.show(fm, AddFriendFragment.TAG);
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

    @Override
    public void onFriendAdded(FriendResponse friendResponse) {
        Toast.makeText(this.getApplicationContext(), friendResponse.getMessage().toString(), Toast.LENGTH_LONG).show();
        getFriendsFromService();
    }
}
