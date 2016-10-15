/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.network;

import java.io.Serializable;
import org.martin.getfreaky.dataObjects.AccessToken;
import org.martin.getfreaky.dataObjects.User;

/**
 *
 * @author martin Using this instead of JAX-RS Response, because of the
 * compatibility with Android Retrofit
 */
public class LoginResponse implements Serializable {

    public enum ResponseMessage {
        USER_REGISTERED, USER_SIGNED_IN, WRONG_PASSWORD,
        EMAIL_NULL, WRONG_GOOGLE_ID_TOKEN, WRONG_FACEBOOK_ACCESS_TOKEN
    }

    private ResponseMessage message;
    private String assignedUserId;
    private User user;
    private AccessToken accessToken;

    public LoginResponse(ResponseMessage message) {
        this.message = message;
    }

    public LoginResponse(ResponseMessage message, String assignedUserId) {
        this.message = message;
        this.assignedUserId = assignedUserId;
    }

    public LoginResponse(ResponseMessage message, String assignedUserId, User user) {
        this.message = message;
        this.assignedUserId = assignedUserId;
        this.user = user;
    }

    public LoginResponse(ResponseMessage message, String assignedUserId, User user, AccessToken accessToken) {
        this.message = message;
        this.assignedUserId = assignedUserId;
        this.user = user;
        this.accessToken = accessToken;
    }
    
    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public ResponseMessage getMessage() {
        return message;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public User getUser() {
        return this.user;
    }
}
