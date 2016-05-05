package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Created by martin on 2016. 04. 20.. This class represents the activity of the
 * user on one day
 */
@Entity
@NamedQuery(
    name="findAllDayLogs",
    query="SELECT dl FROM DayLog dl "
)
public class DayLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dayLogId;

    private Date date;

    @OneToMany(mappedBy = "dayLog", cascade = CascadeType.ALL)
    private List<ProgressPicture> progressPictures;

    @OneToMany(mappedBy = "dayLog", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Workout> workouts;

    @OneToOne(mappedBy = "dayLog", cascade = CascadeType.ALL)
    private BodyLog bodylog;

    // Only for GSON
    public DayLog() {

    }

    public DayLog(Date date) {
        this.date = date;
        progressPictures = new ArrayList<>();
        workouts = new ArrayList<>();
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

    public Long getDayLogId() {
        return dayLogId;
    }

    public void setDayLogId(Long dayLogId) {
        this.dayLogId = dayLogId;
    }
    
    public void addWorkout(Workout w){
        workouts.add(w);
    }

}
