package org.martin.getfreaky.dataObjects;

/**
 * Created by martin on 2016. 10. 10..
 * <p>
 * Identify user session.
 * Contains a unique token and expiry date.
 */

public class AccessToken {

    private String token;
    private long expiryTime;

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getToken() throws TokenExpiredException {
        long currentTime = System.currentTimeMillis();
        if (currentTime > expiryTime) {
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
