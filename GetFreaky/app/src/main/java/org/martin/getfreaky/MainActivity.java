package org.martin.getfreaky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.martin.getfreaky.utils.Sequence;

public class MainActivity extends AppCompatActivity {

    public static final String SEQUENCE_KEY = "sequence key";

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sequenceStart = preferences.getInt(SEQUENCE_KEY, Integer.MAX_VALUE / 2);
        Sequence.setStart(sequenceStart);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt(SEQUENCE_KEY, Sequence.getCurrentValue());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.menu_settings, Menu.NONE, R.string.menu_settings);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent it = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

