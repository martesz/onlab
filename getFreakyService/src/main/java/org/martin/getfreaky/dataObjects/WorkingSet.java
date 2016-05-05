package org.martin.getfreaky.dataObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by martin on 2016. 04. 20.. This class represents one Set of an
 * exercise
 */
@Entity
public class WorkingSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long workingSetId;

    @JoinColumn(name = "EXERCISEID")
    @ManyToOne
    private Exercise exercise;

    private int weight;
    private int repetition;

    // GSON needs a no-arg constructor
    public WorkingSet() {

    }

    public WorkingSet(int repetition, int weight) {
        this.repetition = repetition;
        this.weight = weight;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getWeight() {
        return weight;
    }

    public int getRepetition() {
        return repetition;
    }

    public Long getWorkingSetId() {
        return workingSetId;
    }

    public void setWorkingSetId(Long workingSetId) {
        this.workingSetId = workingSetId;
    }
}
