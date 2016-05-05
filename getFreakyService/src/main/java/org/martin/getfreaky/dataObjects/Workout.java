package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Created by martin on 2016. 04. 20..
 */
@Entity
public class Workout {

    private Long workoutId;
    
    private String name;
    
    private List<Exercise> exercises;
    
    private DayLog dayLog;

    // GSON needs a no-arg constructor
    public Workout(){

    }

    public Workout(String name) {
        this.name = name;
        exercises = new ArrayList<Exercise>();
    }

    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    @JoinColumn(name = "DAYLOGID")
    @ManyToOne
    public DayLog getDayLog() {
        return dayLog;
    }

    public void setDayLog(DayLog dayLog) {
        this.dayLog = dayLog;
    }

    public int countExercises() {
        return exercises.size();
    }
}
