package org.martin.getfreaky.dataObjects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martin on 2016. 05. 07..
 */
public class User extends RealmObject {

    @PrimaryKey
    private String id;

    // Social accounts
    private String googleId;
    private String facebookId;

    private String name;
    private String email;
    private String password;
    private RealmList<DayLog> dayLogs;
    private RealmList<Workout> workouts;

    public User() {
        dayLogs = new RealmList<DayLog>();
        workouts = new RealmList<Workout>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RealmList<DayLog> getDayLogs() {
        return dayLogs;
    }

    public void setDayLogs(RealmList<DayLog> dayLogs) {
        this.dayLogs = dayLogs;
    }

    public RealmList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(RealmList<Workout> workouts) {
        this.workouts = workouts;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
