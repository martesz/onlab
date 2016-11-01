package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Created by martin on 2016. 04. 20. This class represents the activity of the
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

    @OneToOne(cascade = CascadeType.ALL)
    private ProgressPicture progressPicture;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Workout> workoutResults;

    @OneToOne(cascade = CascadeType.ALL)
    private BodyLog bodylog;

    // Only for GSON
    public DayLog() {
        dayLogId = UUID.randomUUID().toString();
        workoutResults = new ArrayList<>();
        bodylog = new BodyLog();
    }

    public DayLog(Date date) {
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        workoutResults = new ArrayList<>();
    }

    public DayLog(Date date, ProgressPicture progressPictures, List<Workout> workouts, BodyLog bodylog) {
        dayLogId = UUID.randomUUID().toString();
        this.date = date;
        this.progressPicture = progressPictures;
        this.workoutResults = workouts;
        this.bodylog = bodylog;
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

    public void setProgressPicture(ProgressPicture progressPictures) {
        this.progressPicture = progressPictures;
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
