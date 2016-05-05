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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * Created by martin on 2016. 04. 20.. This class represents an exercise that
 * has a name and contains sets
 */
@Entity
@NamedQuery(
    name="findAllExercises",
    query="SELECT e FROM Exercise e "
)
public class Exercise {

    private Long exerciseId;

    private String name;
    private List<WorkingSet> sets;
    private Workout workout;

    // GSON needs a no-arg constructor
    public Exercise() {
    }

    public Exercise(String name) {
        this.name = name;
        sets = new ArrayList<WorkingSet>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSet(WorkingSet set) {
        sets.add(set);
    }

    public void setSets(List<WorkingSet> sets) {
        this.sets = sets;
    }

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.PERSIST)
    public List<WorkingSet> getSets() {
        return sets;
    }

    @JoinColumn(name = "WORKOUTID")
    @ManyToOne
    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public void removeSet(WorkingSet set) {
        sets.remove(set);
    }

    public int countSets() {
        return sets.size();
    }
}
