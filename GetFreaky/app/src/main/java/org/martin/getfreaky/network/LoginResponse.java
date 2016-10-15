package org.martin.getfreaky.network;

import org.martin.getfreaky.dataObjects.AccessToken;
import org.martin.getfreaky.dataObjects.User;

/**
 * Created by martin on 2016. 05. 15..
 */
public class LoginResponse {
    public enum ResponseMessage {
        USER_REGISTERED, USER_SIGNED_IN, WRONG_PASSWORD,
        EMAIL_NULL, COULD_NOT_CONNECT, WRONG_GOOGLE_ID_TOKEN, WRONG_FACEBOOK_ACCESS_TOKEN,
        SIGNED_IN_OFFLINE
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
