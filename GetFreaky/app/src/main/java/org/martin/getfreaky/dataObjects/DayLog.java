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
    private ProgressPicture progressPicture;
    private RealmList<Workout> workoutResults;
    private BodyLog bodylog;

    // Only for GSON
    public DayLog() {
        dayLogId = UUID.randomUUID().toString();
        workoutResults = new RealmList<Workout>();
        bodylog = new BodyLog();
        progressPicture = new ProgressPicture();
    }

    public DayLog(Date date) {
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        workoutResults = new RealmList<Workout>();
        bodylog = new BodyLog();
        progressPicture = new ProgressPicture();
    }

    public DayLog(Date date, ProgressPicture progressPicture, RealmList<Workout> workoutResults, BodyLog bodylog) {
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        this.workoutResults = workoutResults;
        this.bodylog = bodylog;
        this.progressPicture = progressPicture;
    }

    public DayLog(DayLog dayLog) {
        this.dayLogId = dayLog.getDayLogId();
        this.date = dayLog.getDate();
        this.workoutResults = new RealmList<>();
        for (Workout workout : dayLog.getWorkoutResults()) {
            this.workoutResults.add(new Workout(workout));
        }
        this.progressPicture = new ProgressPicture(dayLog.getProgressPicture());
        this.bodylog = new BodyLog(dayLog.getBodylog());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ProgressPicture getProgressPicture() {
        return progressPicture;
    }

    public void setProgressPicture(ProgressPicture progressPicture) {
        this.progressPicture = progressPicture;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayLog)) return false;

        DayLog dayLog = (DayLog) o;

        return dayLogId.equals(dayLog.dayLogId);

    }

    @Override
    public int hashCode() {
        return dayLogId.hashCode();
    }
}
