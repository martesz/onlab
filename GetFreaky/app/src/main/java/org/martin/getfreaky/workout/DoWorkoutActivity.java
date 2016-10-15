package org.martin.getfreaky.workout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.martin.getfreaky.GlobalVariables;
import org.martin.getfreaky.LoginActivity;
import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.dataObjects.Workout;
import org.martin.getfreaky.network.DayLogResponse;
import org.martin.getfreaky.network.GetFreakyService;
import org.martin.getfreaky.network.RetrofitClient;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by martin on 2016. 05. 08..
 */
public class DoWorkoutActivity extends AppCompatActivity {

    private NumberPicker weightPicker;
    private NumberPicker repsPicker;
    private Toolbar toolbar;
    private TextView currentExerciseName;
    private TextView workingSetNumber;
    private Button save;
    private Button exit;
    private Button saveWorkoutResults;

    private Workout workoutToDo;
    private Workout workoutResult;
    private String workoutToDoId;
    private int positionInList;
    private Iterator<Exercise> exerciseIterator;
    private Iterator<WorkingSet> workingSetIterator;
    private Exercise currentExercise;
    private WorkingSet currentWorkingSet;
    private Exercise exerciseResult;
    private int currentSetNo;

    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_do_workout);
        setSupportActionBar(toolbar);

        weightPicker = (NumberPicker) findViewById(R.id.weightPicker);
        weightPicker.setMinValue(1);
        weightPicker.setMaxValue(500);

        repsPicker = (NumberPicker) findViewById(R.id.repsPicker);
        repsPicker.setMinValue(1);
        repsPicker.setMaxValue(100);

        workingSetNumber = (TextView) findViewById(R.id.workingSetNo);
        currentExerciseName = (TextView) findViewById(R.id.tvCurrentExerciseName);
        save = (Button) findViewById(R.id.btnSaveWorkingSet);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkingSet wsResult = new WorkingSet(repsPicker.getValue(), weightPicker.getValue());
                exerciseResult.addSet(wsResult);
                if (workingSetIterator.hasNext()) {
                    currentWorkingSet = workingSetIterator.next();
                    workingSetNumber.setText(" " + ++currentSetNo + " / " + currentExercise.getSets().size());
                } else if (exerciseIterator.hasNext()) {
                    currentExercise = exerciseIterator.next();
                    workoutResult.addExercise(exerciseResult);
                    exerciseResult = new Exercise(currentExercise.getName());
                    currentExerciseName.setText(currentExercise.getName());
                    workingSetIterator = currentExercise.getSets().iterator();
                    currentWorkingSet = workingSetIterator.next();
                    currentSetNo = 1;
                    workingSetNumber.setText(" " + currentSetNo + " / " + currentExercise.getSets().size());
                } else {
                    workoutResult.addExercise(exerciseResult);
                    Toast.makeText(v.getContext(), "Workout finished", Toast.LENGTH_LONG).show();
                    saveWorkoutResults.setEnabled(true);
                }
            }
        });

        exit = (Button) findViewById(R.id.btnExitWorkout);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveWorkoutResults = (Button) findViewById(R.id.btnSaveWorkoutResult);
        saveWorkoutResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                String daylogId = preferences.getString(LoginActivity.CURRENT_DAYLOG_ID_KEY, "NOT_FOUND");
                if (!daylogId.equals("NOT_FOUND")) {
                    DayLog dayLog = realm.where(DayLog.class).equalTo("dayLogId", daylogId).findFirst();
                    if (dayLog != null) {
                        realm.beginTransaction();
                        dayLog.getWorkoutResults().add(workoutResult);
                        realm.commitTransaction();
                        Toast.makeText(v.getContext(), "Workout results saved", Toast.LENGTH_LONG).show();
                        putDayLog(dayLog);
                        finish();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Could not save results", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(CreateWorkoutActivity.KEY_EDIT_ID)) {

            workoutToDoId = getIntent().getStringExtra(CreateWorkoutActivity.KEY_EDIT_ID);
            positionInList = getIntent().getIntExtra(CreateWorkoutActivity.KEY_EDIT_INDEX, -1);
            realm.beginTransaction();
            workoutToDo = realm.where(Workout.class).equalTo("id", workoutToDoId).findFirst();
            realm.commitTransaction();
            workoutResult = new Workout(workoutToDo.getName());
            if (workoutToDo.getExercises() != null) {
                exerciseIterator = workoutToDo.getExercises().iterator();
                if (exerciseIterator.hasNext()) {
                    currentExercise = exerciseIterator.next();
                    exerciseResult = new Exercise(currentExercise.getName());
                    currentExerciseName.setText(currentExercise.getName());
                    workingSetIterator = currentExercise.getSets().iterator();
                    currentWorkingSet = workingSetIterator.next();
                    currentSetNo = 1;
                    workingSetNumber.setText(" " + currentSetNo + " / " + currentExercise.getSets().size());
                } else {
                    Toast.makeText(this, "No exercises to do", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                finish();
            }

            getSupportActionBar().setTitle(workoutToDo.getName());
        } else {
            finish();
        }

    }

    private void putDayLog(DayLog dayLog) {
        DayLog copy = new DayLog(dayLog);
        RetrofitClient client = new RetrofitClient((GlobalVariables) this.getApplication());
        GetFreakyService service = client.createService();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DoWorkoutActivity.this);
        String userId = preferences.getString(LoginActivity.USER_ID_KEY, "DefaultUser");
        Call<DayLogResponse> call = service.putDayLog(copy, userId);
        call.enqueue(new Callback<DayLogResponse>() {
            @Override
            public void onResponse(Call<DayLogResponse> call, Response<DayLogResponse> response) {
                if (response.body() != null) {
                    Toast.makeText(DoWorkoutActivity.this, response.body().getMessage().toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DoWorkoutActivity.this, "Could not upload results", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DayLogResponse> call, Throwable t) {
                Toast.makeText(DoWorkoutActivity.this, "Could not upload results", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
