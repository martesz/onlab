package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Created by martin on 2016. 04. 20.. This class represents the activity of the
 * user on one day
 */
@Entity
@NamedQuery(
        name = "findAllDayLogs",
        query = "SELECT dl FROM DayLog dl "
)
public class DayLog {

    @Id
    private String dayLogId;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProgressPicture> progressPictures;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Workout> workoutResults;

    @OneToOne(cascade = CascadeType.ALL)
    private BodyLog bodylog;

    // Only for GSON
    public DayLog() {
        dayLogId = UUID.randomUUID().toString();
    }

    public DayLog(Date date) {
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        progressPictures = new ArrayList<>();
        workoutResults = new ArrayList<>();
    }

    public DayLog(Date date, List<ProgressPicture> progressPictures, List<Workout> workouts, BodyLog bodylog) {
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        this.progressPictures = progressPictures;
        this.workoutResults = workouts;
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

    public List<Workout> getWorkoutResults() {
        return workoutResults;
    }

    public void setWorkoutResults(List<Workout> workoutResults) {
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

    public void addWorkout(Workout w) {
        workoutResults.add(w);
    }

    public void updateBodyLog(BodyLog bodylog) {
        if (this.bodylog != null) {
            this.bodylog.setValues(bodylog);
        } else {
            this.bodylog = bodylog;
        }
    }

}
