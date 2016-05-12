package org.martin.getfreaky.dataObjects;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by martin on 2016. 05. 07..
 */
public class User extends RealmObject {

    private String name;
    private String email;
    // TODO later should store password encrypted
    private String password;
    private RealmList<DayLog> dayLogs;
    private RealmList<Workout> workouts;

    public User(){
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
}
