/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.martin.getfreaky.service;

import com.google.gson.Gson;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.martin.getfreaky.network.DayLogResponse;
import org.martin.getfreaky.network.LoginResponse;
import org.martin.getfreaky.network.WorkoutResponse;
import org.martin.getfreaky.dataObjects.BodyLog;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.Measurements;
import org.martin.getfreaky.dataObjects.MergeData;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.dataObjects.Workout;
import org.martin.getfreaky.database.TestDao;
import org.martin.getfreaky.network.MergeResponse;
import org.martin.getfreaky.network.Secured;
import org.martin.getfreaky.utils.FacebookServices;
import org.martin.getfreaky.utils.GoogleSignIn;

/**
 * Root resource (exposed at "getFreakyServiceworld" path)
 */
@RequestScoped
@Path("getFreakyService")
public class TestService {

    @EJB
    TestDao queryBean;
    
    @EJB
    FacebookServices facebook;

    @Context
    SecurityContext securityContext;

    Exercise exercise;

    public TestService() {
        exercise = new Exercise("incline bench press");
        exercise.addSet(new WorkingSet(10, 10));
        exercise.addSet(new WorkingSet(9, 9));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        Exercise ex = new Exercise("Cable row");
        WorkingSet ws = new WorkingSet(11, 12);
        ex.addSet(ws);
        ws = new WorkingSet(13, 14);
        ex.addSet(ws);
        queryBean.saveExercise(ex);
        List<Exercise> exercises = queryBean.getAllExercises();

        Gson gson = new Gson();
        String json = gson.toJson(exercises.get(exercises.size() - 1));
        return Response.ok(json).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") String id) {
        Exercise ex = queryBean.getExercise(id);

        Gson gson = new Gson();
        String json = gson.toJson(ex);
        return json;
    }

    @GET
    @Path("exampleWorkout")
    @Produces(MediaType.APPLICATION_JSON)
    public String getExampleUser() {
        Workout workout = new Workout("chest");
        workout.addExercise(new Exercise("bench press"));

        Gson gson = new Gson();
        String json = gson.toJson(workout);
        return json;
    }

    @GET
    @Path("dbTest")
    @Produces(MediaType.APPLICATION_JSON)
    public String dbTest() {
        Exercise chest = new Exercise("bench press");
        WorkingSet ws = new WorkingSet(43, 56);
        chest.addSet(ws);
        ws = new WorkingSet(43, 56);
        chest.addSet(ws);
        Measurements ms = new Measurements(10, 11, 12, 13, 14, 15, 16, 17, 18);
        BodyLog bl = new BodyLog(70, 8, ms);
        DayLog dl = new DayLog(new Date(2015, 11, 22));
        Workout w = new Workout("chest");
        w.addExercise(exercise);
        w.addExercise(chest);
        dl.addWorkout(w);
        dl.setBodylog(bl);
        queryBean.saveDayLog(dl);
        List<DayLog> saved = queryBean.getAllDaylogs();
        Gson gson = new Gson();
        String json = gson.toJson(saved);
        return json;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJsonEcho(String content) {
        Gson gson = new Gson();
        exercise = gson.fromJson(content, Exercise.class);
        return gson.toJson(exercise);
    }

    @PUT
    @Path("signInOrRegisterEmail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithEmail(String content) {
        Gson gson = new Gson();
        User user = gson.fromJson(content, User.class);
        LoginResponse response;
        if (user.getEmail() != null) {
            response = queryBean.signInOrRegisterUser(user);
        } else {
            response = new LoginResponse(LoginResponse.ResponseMessage.EMAIL_NULL);
        }
        return gson.toJson(response);
    }

    @PUT
    @Path("signInOrRegisterGoogle")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithGoogle(String googleIdToken) {
        Gson gson = new Gson();
        User user = GoogleSignIn.authenticateWeb(googleIdToken);
        LoginResponse response = queryBean.signInOrRegisterGoogle(user);
        return gson.toJson(response);
    }

    @PUT
    @Path("signInOrRegisterGoogleAndroid")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithGoogleAndroid(String googleIdToken) {
        Gson gson = new Gson();
        User user = GoogleSignIn.authenticateAndroid(googleIdToken);
        LoginResponse response = queryBean.signInOrRegisterGoogle(user);
        return gson.toJson(response);
    }

    @PUT
    @Path("signInOrRegisterFacebook")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithFacebook(String facebookAccessToken) {
        Gson gson = new Gson();
        User user = facebook.login(facebookAccessToken);
        LoginResponse response = queryBean.signInOrRegisterFacebook(user);
        return gson.toJson(response);
    }

    @PUT
    @Secured
    @Path("mergeAccountsAndroid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String mergeAccountsAndroid(String content) {
        Gson gson = new Gson();
        MergeData mergeData = gson.fromJson(content, MergeData.class);
        MergeResponse response = queryBean.mergeAccounts(mergeData);
        return gson.toJson(response);
    }

    /**
     *
     * @param userId The user's email
     * @return The user's workouts
     */
    @GET
    @Secured
    @Path("{userId}/workouts")
    @Produces(MediaType.APPLICATION_JSON)
    public String getWorkouts(@PathParam("userId") String userId) {
        if (userId.equals(securityContext.getUserPrincipal().getName())) {
            List<Workout> workouts = queryBean.getWorkouts(userId);
            Gson gson = new Gson();
            return gson.toJson(workouts);
        } else {
            throw new NotAuthorizedException("Wrong user");
        }
    }

    @PUT
    @Secured
    @Path("{userId}/workouts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadOrUpdateWorkout(String content, @PathParam("userId") String userId) {
        Gson gson = new Gson();
        Workout workout = gson.fromJson(content, Workout.class);
        WorkoutResponse response = queryBean.insertOrUpdateWorkout(workout, userId);
        return gson.toJson(response);
    }

    @DELETE
    @Secured
    @Path("{userId}/workouts/{workoutId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteWorkout(@PathParam("userId") String userId, @PathParam("workoutId") String workoutId) {
        Gson gson = new Gson();
        WorkoutResponse response = queryBean.deleteWorkout(workoutId, userId);
        return gson.toJson(response);
    }

    @GET
    @Secured
    @Path("{userId}/dayLogs")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDayLogs(@PathParam("userId") String userId) {
        List<DayLog> dayLogs = queryBean.getDayLogs(userId);
        Gson gson = new Gson();
        return gson.toJson(dayLogs);
    }

    /**
     *
     * @param userId The user's email
     * @param date The date of the DayLog
     * @return The DayLog adherent to the user and the date
     */
    @GET
    @Secured
    @Path("{userId}/dayLogs/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDayLog(@PathParam("userId") String userId,
            @PathParam("date") String date) {

        DayLog dayLog = queryBean.getDayLog(userId, date);
        Gson gson = new Gson();
        return gson.toJson(dayLog);
    }

    @PUT
    @Secured
    @Path("{userId}/dayLogs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadOrUpdateDaylog(String content, @PathParam("userId") String userId) {
        System.out.println(content);
        Gson gson = new Gson();
        DayLog dayLog = gson.fromJson(content, DayLog.class);
        DayLogResponse response = queryBean.insertOrUpdateDayLog(dayLog, userId);
        return gson.toJson(response);
    }

}
