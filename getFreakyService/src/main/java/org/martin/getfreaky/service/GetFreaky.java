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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.martin.getfreaky.dataObjects.BodyLog;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.Measurements;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.dataObjects.Workout;
import org.martin.getfreaky.database.QueryBean;

/**
 * Root resource (exposed at "getFreakyServiceworld" path)
 */
@RequestScoped
@Path("getFreakyService")
public class GetFreaky {

    @EJB
    QueryBean queryBean;

    Exercise exercise;

    public GetFreaky() {
        exercise = new Exercise("incline bench press");
        exercise.addSet(new WorkingSet(10, 10));
        exercise.addSet(new WorkingSet(9, 9));

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        WorkingSet ws = queryBean.getWorkingSet(409);
        Gson gson = new Gson();
        String json = gson.toJson(ws);
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

    /**
     * PUT method for updating or creating an instance of HelloWorld
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJsonEcho(String content) {
        Gson gson = new Gson();
        exercise = gson.fromJson(content, Exercise.class);
        return gson.toJson(exercise);
    }
}
