package org.martin.getfreaky.dataObjects;

/**
 * Created by martin on 2016. 04. 20..
 */
public class BodyLog {

    private float weight;
    private int bodyFatPercentage;
    private Measurements measurements;

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

    public Measurements getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Measurements measurements) {
        this.measurements = measurements;
    }
}
