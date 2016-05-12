package org.martin.getfreaky.workout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.martin.getfreaky.LoginActivity;
import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.dataObjects.Workout;
import org.martin.getfreaky.preferences.URLManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by martin on 2016. 05. 07..
 */
public class WorkoutFragment extends Fragment {

    public static final int REQUEST_NEW_WORKOUT_CODE = 100;
    public static final int REQUEST_EDIT_WORKOUT_CODE = 101;
    private static final int REQUEST_DO_WORKOUT_CODE = 102;
    public static final int CONTEXT_ACTION_DELETE = 10;
    public static final int CONTEXT_ACTION_EDIT = 11;
    public static final int CONTEXT_ACTION_DO_WORKOUT = 12;

    private Retrofit retrofit;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private ListView listView;
    private WorkoutAdapter adapter;
    private FloatingActionButton fab;
    private Realm realm;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout, null);

        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        String baseKey = getResources().getString(R.string.BASE_URL_KEY);
        URLManager.BASE_URL = myPrefs.getString((baseKey)
                , "http://192.168.1.2:8080/getFreakyService/");
        listener = new URLManager(baseKey);
        myPrefs.registerOnSharedPreferenceChangeListener(listener);

        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLManager.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        RealmConfiguration config = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(config);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userEmail = preferences.getString(LoginActivity.USER_EMAIL_KEY, "DefaultUser");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<Workout> workouts = new ArrayList<Workout>();
        user = realm.where(User.class)
                .equalTo("email", userEmail)
                .findFirst();
        RealmList<Workout> workoutRealmResults = user.getWorkouts();
        workouts.addAll(workoutRealmResults);
        realm.commitTransaction();

        listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new WorkoutAdapter(rootView.getContext(), workouts);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(R.id.empty));
        registerForContextMenu(listView);

        fab =
                (FloatingActionButton) rootView.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewWorkoutDialog();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        realm.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                if (requestCode == REQUEST_NEW_WORKOUT_CODE) {
                    String id = data.getStringExtra(CreateWorkoutActivity.KEY_EDIT_ID);

                    Workout workout = getWorkoutFromRealm(id);

                    realm.beginTransaction();
                    user.getWorkouts().add(workout);
                    realm.commitTransaction();
                    adapter.addWorkout(workout);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Workout added to the list!", Toast.LENGTH_LONG).show();

                } else if (requestCode == REQUEST_EDIT_WORKOUT_CODE) {
                    String id = data.getStringExtra(CreateWorkoutActivity.KEY_EDIT_ID);
                    int position = data.getIntExtra(CreateWorkoutActivity.KEY_EDIT_INDEX, -1);

                    Workout edited = getWorkoutFromRealm(id);

                    adapter.updateWorkout(position, edited);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Workout edited in the list!", Toast.LENGTH_LONG).show();

                }
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void showNewWorkoutDialog() {
        Intent i = new Intent();
        i.setClass(getContext(), CreateWorkoutActivity.class);
        startActivityForResult(i, REQUEST_NEW_WORKOUT_CODE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Menu");
        menu.add(0, CONTEXT_ACTION_DELETE, 0, "Delete");
        menu.add(0, CONTEXT_ACTION_EDIT, 0, "Edit");
        menu.add(0, CONTEXT_ACTION_DO_WORKOUT, 0, "Do it!");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CONTEXT_ACTION_DELETE) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Workout workout = (Workout) adapter.getItem(info.position);

            // Delete workout from DB
            deleteWorkout(workout);

            adapter.removeItem(info.position);
            adapter.notifyDataSetChanged();
        } else if (item.getItemId() == CONTEXT_ACTION_EDIT) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Workout selectedWorkout = (Workout) adapter.getItem(info.position);
            Intent i = new Intent();
            i.setClass(getContext(), CreateWorkoutActivity.class);
            i.putExtra(CreateWorkoutActivity.KEY_EDIT_INDEX, info.position);
            i.putExtra(CreateWorkoutActivity.KEY_EDIT_ID, selectedWorkout.getId());
            startActivityForResult(i, REQUEST_EDIT_WORKOUT_CODE);
        } else if (item.getItemId() == CONTEXT_ACTION_DO_WORKOUT) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Workout selectedWorkout = (Workout) adapter.getItem(info.position);
            Intent i = new Intent();
            i.setClass(getContext(), DoWorkoutActivity.class);
            i.putExtra(CreateWorkoutActivity.KEY_EDIT_INDEX, info.position);
            i.putExtra(CreateWorkoutActivity.KEY_EDIT_ID, selectedWorkout.getId());
            startActivity(i);
        } else {
            return false;
        }
        return true;
    }

    private void insertWorkout(Workout workout) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(workout);
        realm.commitTransaction();
        realm.close();
    }

    private void updateWorkout(Workout workout, int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Workout workoutInDB = realm.where(Workout.class)
                .equalTo("id", id)
                .findFirst();
        workoutInDB.setExercises(workout.getExercises());
        workoutInDB.setName(workout.getName());
        realm.commitTransaction();
        realm.close();
    }

    private void deleteWorkout(Workout workout) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Workout workoutInDB = realm.where(Workout.class)
                .equalTo("id", workout.getId())
                .findFirst();
        workoutInDB.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    private Workout getWorkoutFromRealm(String id) {
        Realm realm = Realm.getDefaultInstance();
        Workout workout = realm.where(Workout.class).equalTo("id", id).findFirst();
        realm.close();
        return workout;
    }
}

