package org.martin.getfreaky.log;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.martin.getfreaky.MyPagerAdapter;
import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.DayLog;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EditLogsActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_logs);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String daylogId = preferences.getString(MyPagerAdapter.SELECTED_DAYLOG_ID, "NOT_FOUND");
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        DayLog dayLog = realm.where(DayLog.class).equalTo("dayLogId", daylogId).findFirst();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = fmt.format(dayLog.getDate());

        LogsPagerAdapter adapter = new LogsPagerAdapter(
                getSupportFragmentManager(), getApplicationContext());

        ViewPager pager = (ViewPager) findViewById(R.id.pagerLogs);
        pager.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLogs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(selectedDate);
    }
}
