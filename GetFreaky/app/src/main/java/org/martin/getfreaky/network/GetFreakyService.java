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

    @PUT("signInOrRegister")
    Call<LoginResponse> signInOrRegisterUser(@Body User user);

    @GET("{email}/workouts")
    Call<List<Workout>> getWorkouts(@Path("email") String email);

    @PUT("{email}/workouts")
    Call<WorkoutResponse> putWorkout(@Body Workout workout, @Path("email") String email);

    @DELETE("{email}/workouts/{workoutId}")
    Call<WorkoutResponse> deleteWorkout(@Path("email") String email, @Path("workoutId") String workoutId);

    @GET("{email}/dayLogs")
    Call<List<DayLog>> getDayLogs(@Path("email") String email);

    @PUT("{email}/dayLogs")
    Call<DayLogResponse> putDayLog(@Body DayLog dayLog, @Path("email") String email);
}
