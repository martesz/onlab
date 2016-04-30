package org.martin.getfreaky.dataObjects;

import com.orm.SugarRecord;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents one Set of an exercise
 */
public class Set extends SugarRecord {

    private int weight;
    private int repetition;

    // GSON needs a no-arg constructor
    public Set(){

    }

    public Set(int repetition, int weight) {
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
