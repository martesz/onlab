/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.network;

import java.io.Serializable;

/**
 *
 * @author martin 
 * Using this instead of JAX-RS Response, because of the
 * compatibility with Android Retrofit
 */
public class LoginResponse implements Serializable{

    public enum ResponseMessage {
        USER_REGISTERED, USER_SIGNED_IN, WRONG_PASSWORD, EMAIL_NULL
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
