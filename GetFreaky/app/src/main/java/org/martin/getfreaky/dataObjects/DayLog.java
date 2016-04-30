package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents the activity of the
 * user on one day
 */
public class DayLog {

    private Date date;
    private List<ProgressPicture> progressPictures;
    private List<Workout> workouts;
    private BodyLog bodylog;

    // Only for GSON
    public DayLog(){

    }

    public DayLog(Date date){
        this.date = date;
        progressPictures = new ArrayList<ProgressPicture>();
        workouts = new ArrayList<Workout>();
    }

    public DayLog(Date date, List<ProgressPicture> progressPictures, List<Workout> workouts, BodyLog bodylog) {
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

    public List<ProgressPicture> getProgressPictures() {
        return progressPictures;
    }

    public void setProgressPictures(List<ProgressPicture> progressPictures) {
        this.progressPictures = progressPictures;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public BodyLog getBodylog() {
        return bodylog;
    }

    public void setBodylog(BodyLog bodylog) {
        this.bodylog = bodylog;
    }
}
