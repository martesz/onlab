package org.martin.getfreaky;

import android.app.Application;

import org.martin.getfreaky.dataObjects.AccessToken;


/**
 * Created by martin on 2016. 10. 10..
 */

public class GlobalVariables extends Application {
    public AccessToken currentToken;

    public AccessToken getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(AccessToken currentToken) {
        this.currentToken = currentToken;
    }
}
