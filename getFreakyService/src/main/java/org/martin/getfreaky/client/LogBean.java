/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.client;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.dataObjects.WorkingSet;
import org.martin.getfreaky.dataObjects.Workout;
import org.martin.getfreaky.network.LoginResponse;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author martin
 *
 * The user can select a date and display the DayLog of that day
 */
@ManagedBean
@SessionScoped
public class LogBean {

    public static final String BASE_URL = "http://localhost:10516/getFreakyService/getFreakyService/";

    private String userId;
    private String email;
    private String password;
    private String loginResult;

    // The selected date
    private Date date;
    private String output;
    private DayLog actualDayLog;
    private List<String> workoutResults;

    /**
     * Creates a new instance of LogBean
     */
    public LogBean() {
        workoutResults = new ArrayList<>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public List<String> getWorkoutResults() {
        return workoutResults;
    }

    public void setWorkoutResults(List<String> workoutResults) {
        this.workoutResults = workoutResults;
    }

    public String login() {
        Client client = ClientBuilder.newClient();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Gson gson = new Gson();
        Response response = client.target(BASE_URL)
                .path("signInOrRegisterEmail")
                .request()
                .put(Entity.entity(user, MediaType.APPLICATION_JSON));
        String responseEntity = response.readEntity(String.class);
        LoginResponse lr = gson.fromJson(responseEntity, LoginResponse.class);
        
        if (lr.getMessage().equals(LoginResponse.ResponseMessage.USER_REGISTERED)
                || lr.getMessage().equals(LoginResponse.ResponseMessage.USER_SIGNED_IN)) {
            userId = lr.getAssignedUserId();
            return "loginSuccesful";
        } else {
            // Login not succesful
            loginResult = lr.getMessage().toString();
            return null;
        }
    }

    /**
     * When the user selects a date, display the DayLog adherent to that day
     */
    public void dateSelected(SelectEvent event) {

        date = (Date) event.getObject();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        Client client = ClientBuilder.newClient();
        Gson gson = new Gson();
        String path = userId + "/dayLogs/" + fmt.format(date);
        System.out.println(path);
        Response response = client.target(BASE_URL)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .get();
        String responseEntity = response.readEntity(String.class);
        actualDayLog = gson.fromJson(responseEntity, DayLog.class);
        parseWorkoutResults();
    }

    private void parseWorkoutResults() {
        workoutResults.clear();
        for (Workout workout : actualDayLog.getWorkoutResults()) {
            StringBuilder sb;
            for (Exercise exercise : workout.getExercises()) {
                sb = new StringBuilder();
                sb.append(exercise.getName() + ":");
                int i = 1;
                for (WorkingSet ws : exercise.getSets()) {
                    sb.append(" set " + i++ + ": " + ws.getWeight() + " x " + ws.getRepetition());
                }
                workoutResults.add(sb.toString());
            }
        }
    }

}
