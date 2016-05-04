package org.martin.getfreaky.dataObjects;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents an exercise
 * that has a name and contains sets
 */
public class Exercise extends RealmObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    // One to many relationship
    // Would be better to use only an interface here,
    // but Realm only works with this kind of List
    private RealmList<WorkingSet> sets;


    public Exercise() {
        sets = new RealmList<WorkingSet>();
    }

    public Exercise(String name) {
        this.name = name;
        sets = new RealmList<WorkingSet>();
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
}
