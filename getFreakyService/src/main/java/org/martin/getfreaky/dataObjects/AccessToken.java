/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.dataObjects;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccessToken implements Serializable {

    @Id
    private String token;
    private long expiryTime;

    public AccessToken() {
        token = UUID.randomUUID().toString();
        // Current time + one day
        expiryTime = System.currentTimeMillis() + 86400000l;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }
    
    public boolean isExpired(){
        long currentTime = System.currentTimeMillis();
        if(currentTime > expiryTime) {
            return true;
        } else {
            return false;
        }
    }

    public String getToken() throws TokenExpiredException {
        if (isExpired()) {
            throw new TokenExpiredException("Token expired:" + String.valueOf(expiryTime));
        } else {
            return token;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class TokenExpiredException extends Exception {

        public TokenExpiredException(String message) {
            super(message);
        }

        public TokenExpiredException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }

}
