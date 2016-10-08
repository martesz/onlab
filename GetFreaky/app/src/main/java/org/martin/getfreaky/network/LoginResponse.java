package org.martin.getfreaky.network;

/**
 * Created by martin on 2016. 05. 15..
 */
public class LoginResponse {
    public enum ResponseMessage {
        USER_REGISTERED, USER_SIGNED_IN, WRONG_PASSWORD, EMAIL_NULL, COULD_NOT_CONNECT
    }

    private ResponseMessage message;
    private String assignedUserId;

    public LoginResponse(ResponseMessage message) {
        this.message = message;
    }

    public LoginResponse(ResponseMessage message, String assignedUserId) {
        this.message = message;
        this.assignedUserId = assignedUserId;
    }

    public ResponseMessage getMessage() {
        return message;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }
}
