package org.martin.getfreaky.dataObjects;

import io.realm.RealmObject;

/**
 * Created by martin on 2016. 04. 20..
 */
public class Measurements extends RealmObject {

    private int Chest;
    private int Waist;
    private int Arms;
    private int Shoulders;
    private int foreArms;
    private int Neck;
    private int Hips;
    private int Thighs;
    private int Calves;

    // GSON needs a no argument constructor
    public Measurements() {
    }

    public Measurements(
            int chest, int waist, int arms, int shoulders,
            int foreArms, int neck, int hips, int thighs,
            int calves) {

        Chest = chest;
        Waist = waist;
        Arms = arms;
        Shoulders = shoulders;
        this.foreArms = foreArms;
        Neck = neck;
        Hips = hips;
        Thighs = thighs;
        Calves = calves;
    }


    public int getChest() {
        return Chest;
    }

    public void setChest(int chest) {
        Chest = chest;
    }

    public int getWaist() {
        return Waist;
    }

    public void setWaist(int waist) {
        Waist = waist;
    }

    public int getArms() {
        return Arms;
    }

    public void setArms(int arms) {
        Arms = arms;
    }

    public int getShoulders() {
        return Shoulders;
    }

    public void setShoulders(int shoulders) {
        Shoulders = shoulders;
    }

    public int getForeArms() {
        return foreArms;
    }

    public void setForeArms(int foreArms) {
        this.foreArms = foreArms;
    }

    public int getNeck() {
        return Neck;
    }

    public void setNeck(int neck) {
        Neck = neck;
    }

    public int getHips() {
        return Hips;
    }

    public void setHips(int hips) {
        Hips = hips;
    }

    public int getThighs() {
        return Thighs;
    }

    public void setThighs(int thighs) {
        Thighs = thighs;
    }

    public int getCalves() {
        return Calves;
    }

    public void setCalves(int calves) {
        Calves = calves;
    }
}
