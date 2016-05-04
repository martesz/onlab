package org.martin.getfreaky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.network.GetFreakyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.1.4:8080/getFreakyService/";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final TextView tv = (TextView) findViewById(R.id.exercise);
        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFreakyService gfs = retrofit.create(GetFreakyService.class);
                Call<Exercise> call = gfs.getExercise();
                call.enqueue(new GetExercise(tv));
            }
        });

        Button create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFreakyService gfs = retrofit.create(GetFreakyService.class);
                Exercise exercise = new Exercise("Legs");
                exercise.addSet(new WorkingSet(32,44));
                exercise.addSet(new WorkingSet(62,84));
                Call<Exercise> call = gfs.createExercise(exercise);
                call.enqueue(new SetExercise(tv));
            }
        });
    }
}

class GetExercise implements Callback<Exercise> {

    TextView tv;

    public GetExercise(TextView tv){
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

    public SetExercise(TextView tv){
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
