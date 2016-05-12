package org.martin.getfreaky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.log.EditLogsActivity;
import org.martin.getfreaky.workout.WorkoutFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyPagerAdapter  extends FragmentPagerAdapter {

    public static final String SELECTED_DAYLOG_ID = "SELECTED_DAYLOG_ID";

    private Context context;

    public MyPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return new WorkoutFragment();
            case 1:
                CaldroidFragment caldroidFragment = new CaldroidFragment();
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                caldroidFragment.setArguments(args);
                markDayLogs(caldroidFragment);
                addListener(caldroidFragment);
                return caldroidFragment;
            default:
                return new WorkoutFragment();
        }
    }

    private void addListener(CaldroidFragment caldroidFragment) {
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                RealmConfiguration config = new RealmConfiguration.Builder(context).build();
                Realm.setDefaultConfiguration(config);
                Realm realm = Realm.getDefaultInstance();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String email = preferences.getString(LoginActivity.USER_EMAIL_KEY, "NO_USER");
                realm.beginTransaction();
                User user = realm.where(User.class)
                        .equalTo("email", email)
                        .findFirst();

                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                String selectedDate = fmt.format(date);
                for(DayLog dl : user.getDayLogs()){
                    if(selectedDate.equals(fmt.format(dl.getDate()))){
                        Intent intent = new Intent();
                        intent.setClass(context, EditLogsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        preferences.edit().putString(SELECTED_DAYLOG_ID, dl.getDayLogId()).apply();
                        context.startActivity(intent);
                    }
                }
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    private void markDayLogs(CaldroidFragment caldroidFragment) {
        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String email = preferences.getString(LoginActivity.USER_EMAIL_KEY, "NO_USER");
        realm.beginTransaction();
        User user = realm.where(User.class)
                .equalTo("email", email)
                .findFirst();

        for(DayLog dl : user.getDayLogs()){
            caldroidFragment.setTextColorForDate(R.color.primary_dark, dl.getDate());
        }
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Workout";
            case 1:
                return "Logs";
            default:
                return "unknown";
        }
    }
}