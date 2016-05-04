package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents an exercise
 * that has a name and contains sets
 */
public class Exercise {

    private String name;
    private List<WorkingSet> sets;

    // GSON needs a no-arg constructor
    public Exercise(){

    }

    public Exercise(String name) {
        this.name = name;
        sets = new ArrayList<WorkingSet>();
    }

    public void addSet(WorkingSet set) {
        sets.add(set);
    }

    public void setSets(List<WorkingSet> sets) {
        this.sets = sets;
    }

    public List<WorkingSet> getSets() {
        return sets;
    }

    public void removeSet(WorkingSet set) {
        sets.remove(set);
    }

    public int countSets() {
        return sets.size();
    }
}
