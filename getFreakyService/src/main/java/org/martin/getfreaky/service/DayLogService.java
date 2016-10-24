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
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.database.DayLogDao;
import org.martin.getfreaky.network.DayLogResponse;
import org.martin.getfreaky.network.Secured;

/**
 *
 * @author martin
 *
 * REST service for accessing the user's day logs
 */
@Path("dayLog")
public class DayLogService {

    @EJB
    private DayLogDao dayLogDao;

    private final Gson gson;

    public DayLogService() {
        super();
        gson = new Gson();
    }

    @GET
    @Secured
    @Path("{userId}/dayLogs")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDayLogs(@PathParam("userId") String userId) {
        List<DayLog> dayLogs = dayLogDao.getDayLogs(userId);
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
        DayLog dayLog = dayLogDao.getDayLog(userId, date);
        return gson.toJson(dayLog);
    }

    @PUT
    @Secured
    @Path("{userId}/dayLogs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadOrUpdateDaylog(String content, @PathParam("userId") String userId) {
        DayLog dayLog = gson.fromJson(content, DayLog.class);
        DayLogResponse response = dayLogDao.insertOrUpdateDayLog(dayLog, userId);
        return gson.toJson(response);
    }
}
