package org.martin.getfreaky.log;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LogsPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public LogsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return new WorkoutResultsFragment();
            case 1:
                return new BodyLogFragment();
            default:
                return new WorkoutResultsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Workout logs";
            case 1:
                return "Body logs";
            default:
                return "unknown";
        }
    }
}