package org.martin.getfreaky.workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.WorkingSet;

/**
 * Created by martin on 2016. 05. 10..
 */
public class CreateExerciseFragment extends DialogFragment {

    public static final String TAG = "CreateExerciseFragment";

    private EditText etExerciseName;
    private NumberPicker numberPicker;

    private ICreateExerciseFragment listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getTargetFragment() != null) {
            try {
                listener = (ICreateExerciseFragment) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        } else {
            try {
                listener = (ICreateExerciseFragment) activity;
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Parent Activity does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_create_exercise, container, false);

        getDialog().setTitle("Create new exercise");

        etExerciseName = (EditText) root.findViewById(R.id.etExerciseName);
        numberPicker = (NumberPicker) root.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);

        Button btnOk = (Button) root.findViewById(R.id.btnCreateExercise);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    Exercise exercise = new Exercise();
                    exercise.setName(etExerciseName.getText().toString());
                    for(int i = 0; i < numberPicker.getValue(); i++){
                        exercise.addSet(new WorkingSet());
                    }
                    listener.onExerciseCreated(exercise);
                    dismiss();
                }
            }
        });

        Button btnCancel = (Button) root.findViewById(R.id.btnCancelCreateExercise);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface ICreateExerciseFragment {
        public void onExerciseCreated(Exercise exercise);
    }
}
