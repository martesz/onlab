package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 2016. 04. 20..
 */
public class Workout {

    private String name;
    private List<Exercise> exercises;

    // GSON needs a no-arg constructor
    public Workout(){

    }

    public Workout(String name) {
        this.name = name;
        exercises = new ArrayList<Exercise>();
    }

    public String getName() {
        return name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public int countExercises() {
        return exercises.size();
    }
}
