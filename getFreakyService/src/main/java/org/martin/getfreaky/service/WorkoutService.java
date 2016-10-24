/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.service;

import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.martin.getfreaky.database.WorkoutDao;
import org.martin.getfreaky.network.Secured;
import org.martin.getfreaky.network.WorkoutResponse;

/**
 *
 * @author martin
 * 
 * REST service for accessing the user's workouts
 * 
 */
@Path("workout")
public class WorkoutService {

    @EJB
    private WorkoutDao workoutDao;

    @Context
    private SecurityContext securityContext;

    private final Gson gson;

    public WorkoutService() {
        super();
        gson = new Gson();
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
            List<org.martin.getfreaky.dataObjects.Workout> workouts = workoutDao.getWorkouts(userId);
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
        org.martin.getfreaky.dataObjects.Workout workout = gson.fromJson(content, org.martin.getfreaky.dataObjects.Workout.class);
        WorkoutResponse response = workoutDao.insertOrUpdateWorkout(workout, userId);
        return gson.toJson(response);
    }

    @DELETE
    @Secured
    @Path("{userId}/workouts/{workoutId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteWorkout(@PathParam("userId") String userId, @PathParam("workoutId") String workoutId) {
        WorkoutResponse response = workoutDao.deleteWorkout(workoutId, userId);
        return gson.toJson(response);
    }

}
