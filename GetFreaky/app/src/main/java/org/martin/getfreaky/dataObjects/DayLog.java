package org.martin.getfreaky.dataObjects;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents the activity of the
 * user on one day
 */
public class DayLog extends RealmObject {

    private Date date;
    private RealmList<ProgressPicture> progressPictures;
    private RealmList<Workout> workouts;
    private BodyLog bodylog;

    // Only for GSON
    public DayLog(){

    }

    public DayLog(Date date){
        this.date = date;
        progressPictures = new RealmList<ProgressPicture>();
        workouts = new RealmList<Workout>();
    }

    public DayLog(Date date, RealmList<ProgressPicture> progressPictures, RealmList<Workout> workouts, BodyLog bodylog) {
        this.date = date;
        this.progressPictures = progressPictures;
        this.workouts = workouts;
        this.bodylog = bodylog;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<ProgressPicture> getProgressPictures() {
        return progressPictures;
    }

    public void setProgressPictures(RealmList<ProgressPicture> progressPictures) {
        this.progressPictures = progressPictures;
    }

    public RealmList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(RealmList<Workout> workouts) {
        this.workouts = workouts;
    }

    public BodyLog getBodylog() {
        return bodylog;
    }

    public void setBodylog(BodyLog bodylog) {
        this.bodylog = bodylog;
    }
}
