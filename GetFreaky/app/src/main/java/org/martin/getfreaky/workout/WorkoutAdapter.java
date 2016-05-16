package org.martin.getfreaky.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.Workout;

import java.util.List;

/**
 * Created by martin on 2016. 05. 08..
 */
public class WorkoutAdapter extends BaseAdapter {

    private Context context;
    private List<Workout> workouts;

    public WorkoutAdapter(Context context, List<Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    public void updateWorkout(int index, Workout workout) {
        workouts.set(index, workout);
    }

    public void removeItem(int index) {
        workouts.remove(index);
    }

    public boolean contains(Workout workout) {
        return workouts.contains(workout);
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Workout getItem(int position) {
        return workouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvExerciseCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_workout, null);
            ViewHolder holder = new ViewHolder();
            holder.tvName = (TextView) v.findViewById(R.id.tvWorkoutName);
            holder.tvExerciseCount = (TextView) v.findViewById(R.id.tvExerciseCount);
            v.setTag(holder);
        }

        final Workout workout = workouts.get(position);
        if (workout != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.tvName.setText(workout.getName());
            holder.tvExerciseCount.setText("Exercises: " + String.valueOf(workout.countExercises()));
        }

        return v;
    }
}
