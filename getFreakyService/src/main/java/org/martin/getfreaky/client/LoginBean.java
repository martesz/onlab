/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.client;

import com.google.gson.Gson;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.network.LoginResponse;

/**
 *
 * @author martin
 */
@ManagedBean
@SessionScoped
public class LoginBean {

    public static final String BASE_URL = "http://192.168.1.2:8080/getFreakyService/getFreakyService/signInOrRegister";

    private String email;
    private String password;
    private String loginResult;

    private int id = 15;

    public LoginBean() {

    }

    public int getId() {
        return id;
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

    public String login() {
        Client client = ClientBuilder.newClient();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Gson gson = new Gson();
        Response response = client.target(BASE_URL)
                .request()
                .put(Entity.entity(user, MediaType.APPLICATION_JSON));
        String responseEntity = response.readEntity(String.class);
        LoginResponse lr = gson.fromJson(responseEntity, LoginResponse.class);

        if (lr.getMessage().equals(LoginResponse.ResponseMessage.USER_REGISTERED)
                || lr.getMessage().equals(LoginResponse.ResponseMessage.USER_SIGNED_IN)) {
            return "loginSuccesful";
        } else {
            // Login not succesful
            loginResult = lr.getMessage().toString();
            return null;
        }
    }
}
