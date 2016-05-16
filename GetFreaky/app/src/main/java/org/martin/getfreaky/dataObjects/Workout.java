package org.martin.getfreaky.dataObjects;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martin on 2016. 04. 20..
 */
public class Workout extends RealmObject implements Serializable {


    @PrimaryKey
    private String id;

    private String name;

    private RealmList<Exercise> exercises;

    // GSON needs a no-arg constructor
    public Workout() {
        exercises = new RealmList<Exercise>();
        this.id = UUID.randomUUID().toString();
    }

    public Workout(String name) {
        this.name = name;
        exercises = new RealmList<Exercise>();
        this.id = UUID.randomUUID().toString();
    }

    public Workout(Workout workout){
        this.id = workout.getId();
        this.name = workout.getName();
        this.exercises = new RealmList<>();
        for(Exercise exercise : workout.getExercises()){
            this.exercises.add(new Exercise(exercise));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workout)) return false;

        Workout workout = (Workout) o;

        return id.equals(workout.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
