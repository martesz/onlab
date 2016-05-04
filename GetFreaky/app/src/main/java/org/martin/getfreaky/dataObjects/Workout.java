package org.martin.getfreaky.dataObjects;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by martin on 2016. 04. 20..
 */
public class Workout extends RealmObject {

    private String name;

    private RealmList<Exercise> exercises;

    // GSON needs a no-arg constructor
    public Workout(){

    }

    public Workout(String name) {
        this.name = name;
        exercises = new RealmList<Exercise>();
    }

    public String getName() {
        return name;
    }

    public RealmList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(RealmList<Exercise> exercises) {
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
