package org.martin.getfreaky.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.Exercise;

import java.util.List;

/**
 * Created by martin on 2016. 05. 09..
 */
public class ExerciseAdapter extends BaseAdapter {

    private Context context;
    private List<Exercise> exercises;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void updatePlace(int index, Exercise exercise){
        exercises.set(index, exercise);
    }

    public void removeItem(int index){
        exercises.remove(index);
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Object getItem(int position) {
        return exercises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView tvName;
        TextView tvWorkingSetNo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_exercise, null);
            ViewHolder holder = new ViewHolder();
            holder.tvName = (TextView) v.findViewById(R.id.tvExerciseName);
            holder.tvWorkingSetNo = (TextView) v.findViewById(R.id.tvWorkingSetCount);
            v.setTag(holder);
        }

        final Exercise exercise = exercises.get(position);
        if (exercise != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.tvName.setText(exercise.getName());
            holder.tvWorkingSetNo.setText(String.valueOf(exercise.countSets()));
        }

        return v;
    }
}
