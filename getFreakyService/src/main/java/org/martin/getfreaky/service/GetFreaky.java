/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.martin.getfreaky.service;

import com.google.gson.Gson;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.WorkingSet;

/**
 * Root resource (exposed at "getFreakyServiceworld" path)
 */
@Stateless
@Path("getFreakyService")
public class GetFreaky {
    
    Exercise exercise;
    
    @Context
    private UriInfo context;

    /** Creates a new instance of HelloWorld */
    public GetFreaky() {
        exercise = new Exercise("back");
        exercise.addSet(new WorkingSet(10, 10));
        exercise.addSet(new WorkingSet(9,9));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(){
       Gson gson = new Gson();
       String json = gson.toJson(exercise);
       return json;
    }

    /**
     * PUT method for updating or creating an instance of HelloWorld
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
    public String postJsonEcho(String content){
        Gson gson = new Gson();
        exercise = gson.fromJson(content, Exercise.class);
        return gson.toJson(exercise);
    }
}
