package org.martin.getfreaky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.network.GetFreakyService;
import org.martin.getfreaky.preferences.URLManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by martin on 2016. 05. 07..
 */
public class WorkoutFragment extends Fragment {

    private Retrofit retrofit;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

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
        final TextView tv = (TextView) rootView.findViewById(R.id.exercise);
        Button refresh = (Button) rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFreakyService gfs = retrofit.create(GetFreakyService.class);
                Call<Exercise> call = gfs.getExercise();
                call.enqueue(new GetExercise(tv));
            }
        });

        Button create = (Button) rootView.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFreakyService gfs = retrofit.create(GetFreakyService.class);
                Exercise exercise = new Exercise("Legs");
                exercise.addSet(new WorkingSet(32, 44));
                exercise.addSet(new WorkingSet(62, 84));
                Call<Exercise> call = gfs.createExercise(exercise);
                call.enqueue(new SetExercise(tv));
            }
        });

        Button settings = (Button) rootView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(it);
            }
        });

        Button dropDb = (Button) rootView.findViewById(R.id.dropDB);
        dropDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                RealmConfiguration config = realm.getConfiguration();
                realm.close();
                boolean success = Realm.deleteRealm(config);
                if (success) {
                    Toast.makeText(v.getContext(), "DB succesfully dropped", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }
}

class GetExercise implements Callback<Exercise> {

    TextView tv;

    public GetExercise(TextView tv) {
        this.tv = tv;
    }

    @Override
    public void onResponse(Call<Exercise> call, Response<Exercise> response) {
        if (response != null) {
            String exercise = response.body().getName() + "\n"
                    + response.body().getSets().get(0).getRepetition()
                    + "\n"
                    + response.body().getSets().get(1).getWeight();

            tv.setText(exercise);
        }
    }

    @Override
    public void onFailure(Call<Exercise> call, Throwable t) {
        Log.d("a", t.getMessage());
    }
}

class SetExercise implements Callback<Exercise> {

    TextView tv;

    public SetExercise(TextView tv) {
        this.tv = tv;
    }

    @Override
    public void onResponse(Call<Exercise> call, Response<Exercise> response) {
        if (response != null) {
            String exercise = response.body().getName() + "\n"
                    + response.body().getSets().get(0).getRepetition()
                    + "\n"
                    + response.body().getSets().get(1).getWeight();

            tv.setText(exercise);
        }
    }

    @Override
    public void onFailure(Call<Exercise> call, Throwable t) {
        Log.d("a", t.getMessage());
    }
}
