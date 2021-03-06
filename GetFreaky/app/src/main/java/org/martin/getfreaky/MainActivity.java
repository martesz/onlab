package org.martin.getfreaky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPagerAdapter adapter = new MyPagerAdapter(
                getSupportFragmentManager(), getApplicationContext());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.menu_settings, Menu.NONE, R.string.menu_settings);
        menu.add(Menu.NONE, R.id.merge_accounts, Menu.NONE, R.string.menu_merge);
        menu.add(Menu.NONE, R.id.friends, Menu.NONE, R.string.menu_friends);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent it = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(it);
                return true;
            case R.id.merge_accounts:
                Intent intent = new Intent(MainActivity.this, MergeActivity.class);
                startActivity(intent);
                return true;
            case R.id.friends:
                Intent friendsIntent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(friendsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

