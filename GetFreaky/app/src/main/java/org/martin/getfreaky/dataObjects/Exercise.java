package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents an exercise
 * that has a name and contains sets
 */
public class Exercise {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private List<Set> sets;

    // GSON needs a no-arg constructor
    public Exercise(){

    }

    public Exercise(String name) {
        this.name = name;
        sets = new ArrayList<Set>();
    }

    public void addSet(Set set) {
        sets.add(set);
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

    public List<Set> getSets() {
        return sets;
    }

    public void removeSet(Set set) {
        sets.remove(set);
    }

    public int countSets() {
        return sets.size();
    }
}
