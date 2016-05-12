package org.martin.getfreaky.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.dataObjects.Workout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 2016. 05. 12..
 */
public class WorkoutResultAdapter extends BaseAdapter {

    private Context context;
    private List<Workout> workouts;
    private List<Exercise> exercises;

    public WorkoutResultAdapter(Context context, List<Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
        exercises = new ArrayList<>();
        for(Workout workout : workouts) {
            exercises.addAll(workout.getExercises());
        }
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Exercise getItem(int position) {
        return exercises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvWorkingSets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_workout_result, null);
            ViewHolder holder = new ViewHolder();
            holder.tvName = (TextView) v.findViewById(R.id.tvExerciseResultName);
            holder.tvWorkingSets = (TextView) v.findViewById(R.id.tvWorkingSetResults);
            v.setTag(holder);
        }

        final Exercise exercise = exercises.get(position);
        if (exercise != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.tvName.setText(exercise.getName());
            StringBuffer sb = new StringBuffer();
            int i = 1;
            for(WorkingSet ws : exercise.getSets()){
                sb.append("Set " + i++ + ": " + ws.getWeight()+  " x " + ws.getRepetition() + "\n");
            }
            holder.tvWorkingSets.setText(sb.toString());
        }

        return v;
    }
}