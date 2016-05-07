package org.martin.getfreaky;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter  extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return new WorkoutFragment();
            case 1:
                return new LogsFragment();
            default:
                return new WorkoutFragment();
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
                return "Workout";
            case 1:
                return "Logs";
            default:
                return "unknown";
        }
    }
}