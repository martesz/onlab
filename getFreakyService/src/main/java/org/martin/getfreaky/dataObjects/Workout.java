package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Created by martin on 2016. 04. 20..
 */
@Entity
public class Workout {

    private String id;

    private String name;

    private List<Exercise> exercises;

    // GSON needs a no-arg constructor
    public Workout() {
        id = UUID.randomUUID().toString();
    }

    public Workout(String name) {
        id = UUID.randomUUID().toString();
        this.name = name;
        exercises = new ArrayList<Exercise>();
    }

    @Column(name = "WORKOUT_NAME")
    public String getName() {
        return name;
    }

    @OneToMany(cascade = CascadeType.ALL)
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int countExercises() {
        return exercises.size();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final Workout other = (Workout) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
