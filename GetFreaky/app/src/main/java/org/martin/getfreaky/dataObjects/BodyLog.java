package org.martin.getfreaky.dataObjects;

import io.realm.RealmObject;

/**
 * Created by martin on 2016. 04. 20..
 */
public class BodyLog extends RealmObject{

    private float weight;
    private int bodyFatPercentage;
    private Measurements measurements;

    // GSON needs a no-arg constructor
    public BodyLog() {
        measurements = new Measurements();
    }

    public BodyLog(float weight, int bodyFat, Measurements measurements) {
        this.weight = weight;
        this.bodyFatPercentage = bodyFat;
        this.measurements = measurements;
    }

    public BodyLog(BodyLog bodyLog) {
        this.weight = bodyLog.getWeight();
        this.bodyFatPercentage = bodyLog.getBodyFatPercentage();
        this.measurements = new Measurements(bodyLog.getMeasurements());
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
