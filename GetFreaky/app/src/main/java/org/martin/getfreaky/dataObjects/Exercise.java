package org.martin.getfreaky.dataObjects;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents an exercise
 * that has a name and contains sets
 */
public class Exercise extends RealmObject {

    @PrimaryKey
    private String exerciseId;

    private String name;

    // One to many relationship
    // Would be better to use only an interface here,
    // but Realm only works with this kind of List
    private RealmList<WorkingSet> sets;

    public Exercise() {
        exerciseId = UUID.randomUUID().toString();
        sets = new RealmList<WorkingSet>();
    }

    public Exercise(String name) {
        exerciseId = UUID.randomUUID().toString();
        this.name = name;
        sets = new RealmList<WorkingSet>();
    }


    public Exercise(Exercise exercise) {
        this.name = exercise.getName();
        this.exerciseId = exercise.getExerciseId();
        this.sets = new RealmList<>();
        for(WorkingSet ws : exercise.getSets()){
            this.sets.add(new WorkingSet(ws));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSet(WorkingSet set) {
        sets.add(set);
    }

    public void setSets(RealmList<WorkingSet> sets) {
        this.sets = sets;
    }

    public RealmList<WorkingSet> getSets() {
        return sets;
    }

    public void removeSet(WorkingSet set) {
        sets.remove(set);
    }

    public int countSets() {
        return sets.size();
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }
}
