/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.martin.getfreaky.network.DayLogResponse;
import org.martin.getfreaky.network.LoginResponse;
import org.martin.getfreaky.network.WorkoutResponse;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.ProgressPicture;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.dataObjects.Workout;
import org.martin.getfreaky.utils.Password;

@Stateless
public class QueryBean {

    @PersistenceContext(unitName = "getfreaky")
    private EntityManager em;

    public QueryBean() {

    }

    public void saveWorkingSet(WorkingSet workingSet) {
        em.persist(workingSet);

    }

    public void saveExercise(Exercise ex) {
        em.persist(ex);
    }

    public void saveDayLog(DayLog dl) {
        em.persist(dl);
    }

    // If the user exists and the password is wrong returns false, else returns true
    public LoginResponse signInOrRegisterUser(User user) {
        User existingUser = em.find(User.class, user.getEmail());
        if (existingUser != null) {
            if (Password.equals(user.getPassword(), existingUser.getPassword())) {
                return new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN);
            } else {
                return new LoginResponse(LoginResponse.ResponseMessage.WRONG_PASSWORD);
            }
        } else {
            user.setPassword(Password.getHash(user.getPassword()));
            em.persist(user);
            return new LoginResponse(LoginResponse.ResponseMessage.USER_REGISTERED);
        }
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> result = em.createNamedQuery("findAllExercises").getResultList();
        return result;
    }

    public List<DayLog> getAllDaylogs() {
        List<DayLog> result = em.createNamedQuery("findAllDayLogs").getResultList();
        return result;
    }

    public Exercise getExercise(String id) {
        return em.find(Exercise.class, id);
    }

    public WorkingSet getWorkingSet(long id) {
        return em.find(WorkingSet.class, id);
    }

    public List<Workout> getWorkouts(String email) {
        User user = em.find(User.class, email);
        if (user != null) {
            return user.getWorkouts();
        } else {
            return new ArrayList<>();
        }
    }

    public WorkoutResponse insertOrUpdateWorkout(Workout workout, String email) {
        Workout existing = em.find(Workout.class, workout.getId());
        if (existing == null) {
            User user = em.find(User.class, email);
            user.getWorkouts().add(workout);
            return new WorkoutResponse(WorkoutResponse.ResponseMessage.WORKOUT_UPLOADED);
        } else {
            existing.setName(workout.getName());
            Iterator<Exercise> it = existing.getExercises().iterator();
            merge(existing.getExercises(), workout.getExercises());

            return new WorkoutResponse(WorkoutResponse.ResponseMessage.WORKOUT_UPDATED);
        }
    }

    public List<DayLog> getDayLogs(String email) {
        User user = em.find(User.class, email);
        if (user != null) {
            return user.getDayLogs();
        } else {
            return new ArrayList<>();
        }
    }

    public DayLogResponse insertOrUpdateDayLog(DayLog dayLog, String email) {
        DayLog existing = em.find(DayLog.class, dayLog.getDayLogId());
        if (existing == null) {
            User user = em.find(User.class, email);
            if (user != null) {
                user.getDayLogs().add(dayLog);
                return new DayLogResponse(DayLogResponse.ResponseMessage.DAYLOG_UPLOADED);
            } else {
                return new DayLogResponse(DayLogResponse.ResponseMessage.SOMETHING_WENT_WRONG);
            }
        } else {
            existing.setDate(dayLog.getDate());
            merge(existing.getProgressPictures(), dayLog.getProgressPictures());
            merge(existing.getWorkoutResults(), dayLog.getWorkoutResults());
            existing.updateBodyLog(dayLog.getBodylog());
            return new DayLogResponse(DayLogResponse.ResponseMessage.DAYLOG_UPDATED);
        }
    }

    public <T> void merge(List<T> existing, List<T> uploaded) {
        Iterator<T> it = existing.iterator();
        while (it.hasNext()) {
            if (!uploaded.contains(it.next())) {
                it.remove();
            }
        }
        for (T t : uploaded) {
            if (!existing.contains(t)) {
                existing.add(t);
            }
        }
    }

    public WorkoutResponse deleteWorkout(String workoutId, String email) {
        User user = em.find(User.class, email);
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

    /**
     *
     * @param email User email
     * @param date DayLog date
     * @return If there is a DayLog of that date and user return it, if there is
     * not match return empty DayLog
     */
    public DayLog getDayLog(String email, String date) {
        User user = em.find(User.class, email);
        if (user != null) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            for (DayLog dl : user.getDayLogs()) {
                String actDate = fmt.format(dl.getDate());
                if (date.equals(actDate)) {
                    return dl;
                }
            }
            return new DayLog();
        } else {
            return new DayLog();
        }
    }

}
