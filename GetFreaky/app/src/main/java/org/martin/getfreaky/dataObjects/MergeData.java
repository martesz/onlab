package org.martin.getfreaky.dataObjects;

public class MergeData {

    private String userId;
    private String email;
    private String password;
    private String googleIdToken;
    private String facebookAccessToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getGoogleIdToken() {
        return googleIdToken;
    }

    public void setGoogleIdToken(String googleIdToken) {
        this.googleIdToken = googleIdToken;
    }

    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }

}