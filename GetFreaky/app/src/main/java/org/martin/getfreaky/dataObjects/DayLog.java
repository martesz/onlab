package org.martin.getfreaky.dataObjects;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents the activity of the
 * user on one day
 */
public class DayLog extends RealmObject {

    @PrimaryKey
    private String dayLogId;

    private Date date;
    private RealmList<ProgressPicture> progressPictures;
    private RealmList<Workout> workoutResults;
    private BodyLog bodylog;

    // Only for GSON
    public DayLog(){
        dayLogId = UUID.randomUUID().toString();
        progressPictures = new RealmList<ProgressPicture>();
        workoutResults = new RealmList<Workout>();
        bodylog = new BodyLog();
    }

    public DayLog(Date date){
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        progressPictures = new RealmList<ProgressPicture>();
        workoutResults = new RealmList<Workout>();
        bodylog = new BodyLog();
    }

    public DayLog(Date date, RealmList<ProgressPicture> progressPictures, RealmList<Workout> workoutResults, BodyLog bodylog) {
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        this.progressPictures = progressPictures;
        this.workoutResults = workoutResults;
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

    public RealmList<Workout> getWorkoutResults() {
        return workoutResults;
    }

    public void setWorkoutResults(RealmList<Workout> workoutResults) {
        this.workoutResults = workoutResults;
    }

    public BodyLog getBodylog() {
        return bodylog;
    }

    public void setBodylog(BodyLog bodylog) {
        this.bodylog = bodylog;
    }

    public String getDayLogId() {
        return dayLogId;
    }

    public void setDayLogId(String dayLogId) {
        this.dayLogId = dayLogId;
    }
}
