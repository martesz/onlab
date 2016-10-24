/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.dataObjects.Workout;
import org.martin.getfreaky.network.WorkoutResponse;
import org.martin.getfreaky.utils.ListUtils;

/**
 *
 * @author martin
 *
 * Provides access to the workouts in the database.
 *
 */
@Stateless
public class WorkoutDao {

    @PersistenceContext(unitName = "getfreaky")
    private EntityManager em;

    /**
     *
     * @param userId The id of the user
     * @return The workouts of the user
     */
    public List<Workout> getWorkouts(String userId) {
        User user = em.find(User.class, userId);
        if (user != null) {
            return user.getWorkouts();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param workout The workout to be uploaded
     * @param userId The user that owns the workout
     * @return The result of the upload or update
     */
    public WorkoutResponse insertOrUpdateWorkout(Workout workout, String userId) {
        Workout existing = em.find(Workout.class, workout.getId());
        if (existing == null) {
            User user = em.find(User.class, userId);
            user.getWorkouts().add(workout);
            return new WorkoutResponse(WorkoutResponse.ResponseMessage.WORKOUT_UPLOADED);
        } else {
            existing.setName(workout.getName());
            Iterator<Exercise> it = existing.getExercises().iterator();
            ListUtils.merge(existing.getExercises(), workout.getExercises());

            return new WorkoutResponse(WorkoutResponse.ResponseMessage.WORKOUT_UPDATED);
        }
    }

    /**
     *
     * @param workoutId The id of the workout to be deleted
     * @param userId The id of the owner user
     * @return The result of the deletion
     */
    public WorkoutResponse deleteWorkout(String workoutId, String userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            return new WorkoutResponse(WorkoutResponse.ResponseMessage.SOMETHING_WENT_WRONG);
        } else {
            Iterator<Workout> it = user.getWorkouts().iterator();
            while (it.hasNext()) {
                Workout workout = it.next();
                if (workout.getId().equals(workoutId)) {
                    it.remove();
                    em.remove(workout);
                    return new WorkoutResponse(WorkoutResponse.ResponseMessage.WORKOUT_DELETED);
                }
            }

        }
        return new WorkoutResponse(WorkoutResponse.ResponseMessage.SOMETHING_WENT_WRONG);
    }
}
