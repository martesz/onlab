package org.martin.getfreaky.dataObjects;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents one Set of an exercise
 */
public class WorkingSet {

    private int weight;
    private int repetition;

    // GSON needs a no-arg constructor
    public WorkingSet(){

    }

    public WorkingSet(int repetition, int weight) {
        this.repetition = repetition;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getRepetition() {
        return repetition;
    }
}
