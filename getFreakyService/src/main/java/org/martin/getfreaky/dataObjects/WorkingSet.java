package org.martin.getfreaky.dataObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    public int getWeight() {
        return weight;
    }

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
}
