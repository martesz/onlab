package org.martin.getfreaky.network;

/**
 * Created by martin on 2016. 05. 15..
 */
public class LoginResponse {
    public enum ResponseMessage {
        USER_REGISTERED, USER_SIGNED_IN, WRONG_PASSWORD, EMAIL_NULL, COULD_NOT_CONNECT
    }

    private ResponseMessage message;

    public LoginResponse(ResponseMessage message) {
        this.message = message;
    }

    public ResponseMessage getMessage() {
        return message;
    }
}
