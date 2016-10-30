/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.dataObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author martin
 */
@Entity
@Table(name = "USERS")
public class User implements Serializable {

    private String id;

    // Social accounts
    private String googleId;
    private String facebookId;

    private String name;
    private String email;
    private String password;

    private List<DayLog> dayLogs;
    private List<Workout> workouts;

    private transient List<User> friends;
    private transient List<User> friendsOf;

    public User() {
        friendsOf = new ArrayList<>();
        friends = new ArrayList<>();
        dayLogs = new ArrayList<>();
        workouts = new ArrayList<>();
    }

    /**
     *
     * @return The id set for the user
     */
    public String generateUniqueId() {
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

    @ManyToMany
    @JoinTable(name = "friendships",
            joinColumns = @JoinColumn(name = "personId"),
            inverseJoinColumns = @JoinColumn(name = "friendId")
    )
    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @ManyToMany
    @JoinTable(name = "friendships",
            joinColumns = @JoinColumn(name = "friendId"),
            inverseJoinColumns = @JoinColumn(name = "personId")
    )
    public List<User> getFriendsOf() {
        return friendsOf;
    }

    public void setFriendsOf(List<User> friendsOf) {
        this.friendsOf = friendsOf;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
