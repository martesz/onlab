package org.martin.getfreaky.dataObjects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Created by martin on 2016. 04. 20..
 */
@Entity
public class BodyLog {
    
    private float weight;
    private int bodyFatPercentage;
    
    private Measurements measurements;    
    
    private Long id;

    // GSON need a no-arg constructor
    public BodyLog() {

    }

    public BodyLog(float weight, int bodyFat, Measurements measurements) {
        this.weight = weight;
        this.bodyFatPercentage = bodyFat;
        this.measurements = measurements;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        if (weight < 300 && weight > 0) {
            this.weight = weight;
        }
    }

    public int getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(int bodyFatPercentage) {
        if (bodyFatPercentage > 0 && bodyFatPercentage < 100) {
            this.bodyFatPercentage = bodyFatPercentage;
        }
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Measurements getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Measurements measurements) {
        this.measurements = measurements;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setValues(BodyLog bodylog){
        this.weight = bodylog.getWeight();
        this.bodyFatPercentage = bodylog.bodyFatPercentage;
        this.measurements.setValues(bodylog.getMeasurements());
    }
}
