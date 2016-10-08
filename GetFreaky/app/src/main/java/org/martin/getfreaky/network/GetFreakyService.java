package org.martin.getfreaky.network;

import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.dataObjects.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by martin on 2016. 04. 20..
 * This interface is used to communicate
 * with the JEE service
 */
public interface GetFreakyService {

    @GET("getFreakyService")
    Call<Exercise> getExercise();

    @PUT("signInOrRegisterEmail")
    Call<LoginResponse> signInOrRegisterUser(@Body User user);

    @GET("{userId}/workouts")
    Call<List<Workout>> getWorkouts(@Path("userId") String userId);

    @PUT("{userId}/workouts")
    Call<WorkoutResponse> putWorkout(@Body Workout workout, @Path("userId") String userId);

    @DELETE("{userId}/workouts/{workoutId}")
    Call<WorkoutResponse> deleteWorkout(@Path("userId") String userId, @Path("workoutId") String workoutId);

    @GET("{userId}/dayLogs")
    Call<List<DayLog>> getDayLogs(@Path("userId") String userId);

    @PUT("{userId}/dayLogs")
    Call<DayLogResponse> putDayLog(@Body DayLog dayLog, @Path("userId") String userId);
}
