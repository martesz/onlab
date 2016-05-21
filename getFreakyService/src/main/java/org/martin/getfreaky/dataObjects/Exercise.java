package org.martin.getfreaky.dataObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
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

    private String exerciseId;

    private String name;
    private List<WorkingSet> sets;

    // GSON needs a no-arg constructor
    public Exercise() {
        exerciseId = UUID.randomUUID().toString();
    }

    public Exercise(String name) {        
        exerciseId = UUID.randomUUID().toString();
        this.name = name;
        sets = new ArrayList<WorkingSet>();
    }

    @Id
    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
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

    @OneToMany(cascade = CascadeType.ALL)
    public List<WorkingSet> getSets() {
        return sets;
    }

    public void removeSet(WorkingSet set) {
        sets.remove(set);
    }

    public int countSets() {
        return sets.size();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.exerciseId);
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
        final Exercise other = (Exercise) obj;
        if (!Objects.equals(this.exerciseId, other.exerciseId)) {
            return false;
        }
        return true;
    }
}
