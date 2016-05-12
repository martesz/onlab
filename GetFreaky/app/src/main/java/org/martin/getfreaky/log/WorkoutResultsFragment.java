package org.martin.getfreaky.log;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.martin.getfreaky.MyPagerAdapter;
import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.DayLog;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class WorkoutResultsFragment extends Fragment {

    private ListView listView;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_results, null);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String daylogId = preferences.getString(MyPagerAdapter.SELECTED_DAYLOG_ID, "NO_KEY");

        RealmConfiguration config = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        DayLog dayLog = realm.where(DayLog.class).equalTo("dayLogId", daylogId).findFirst();

        listView = (ListView) rootView.findViewById(R.id.workoutResultList);
        WorkoutResultAdapter adapter = new WorkoutResultAdapter(getContext(), dayLog.getWorkoutResults());
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(R.id.emptyWorkoutResult));
        registerForContextMenu(listView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
