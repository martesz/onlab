/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author martin
 */
@Entity
@Table(name = "USERS")
public class User {

    private String id;

    // Social accounts
    private String googleId;
    private String facebookId;
    
    private String name;
    private String email;
    private String password;
    
    private List<DayLog> dayLogs;
    private List<Workout> workouts;

    public User() {
        
        dayLogs = new ArrayList<>();
        workouts = new ArrayList<>();
    }
    
    /**
     * 
     * @return The id set for the user 
     */
    public String generateUniqueId(){
        id = UUID.randomUUID().toString();
        return id;
    }

    @Column(name = "USER_NAME")
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

    @OneToMany(cascade = CascadeType.ALL)
    public List<DayLog> getDayLogs() {
        return dayLogs;
    }

    public void setDayLogs(List<DayLog> dayLogs) {
        this.dayLogs = dayLogs;
    }

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    
}
