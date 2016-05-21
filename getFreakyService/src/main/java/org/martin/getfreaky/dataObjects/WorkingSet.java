package org.martin.getfreaky.dataObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by martin on 2016. 04. 20.. This class represents one Set of an
 * exercise
 */
@Entity
public class WorkingSet {

    private Long Id;

    private int weight;

    private int repetition;

    // GSON needs a no-arg constructor
    public WorkingSet() {

    }

    public WorkingSet(int repetition, int weight) {
        this.repetition = repetition;
        this.weight = weight;
    }

    @Column(name = "weight")
    public int getWeight() {
        return weight;
    }

    @Column(name = "repetition")
    public int getRepetition() {
        return repetition;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }
}
