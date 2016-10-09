/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.getfreaky.dataObjects.User;

/**
 *
 * @author martin
 */
public class GoogleSignIn {

    public static final String ANDROID_ISSUER = "https://accounts.google.com";
    public static final String WEB_ISSUER = "accounts.google.com";

    public static final String SERVICE_CLIENT_ID = "184243885869-enlbqgos6p3t9fqujkh89squibv04ncu.apps.googleusercontent.com";
    public static final String WEB_CLIENT_ID = "184243885869-65g68jc9lbuv89093ncuc5vt0fj8p78q.apps.googleusercontent.com";

    public static User authenticateWeb(String googleIdToken) {
        GoogleIdTokenVerifier verifier = createVerifier(WEB_CLIENT_ID, WEB_ISSUER);
        User user = authenticate(verifier, googleIdToken);
        return user;
    }

    public static User authenticateAndroid(String googleIdToken) {
        GoogleIdTokenVerifier verifier = createVerifier(SERVICE_CLIENT_ID, ANDROID_ISSUER);
        User user = authenticate(verifier, googleIdToken);
        return user;
    }

    private static User authenticate(GoogleIdTokenVerifier verifier, String googleIdToken) {
        try {
            GoogleIdToken idToken = verifier.verify(googleIdToken);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String googleId = payload.getSubject();
                String name = (String) payload.get("name");
                User user = new User();
                user.setGoogleId(googleId);
                user.setName(name);
                return user;
            } else {
                return null;
            }
        } catch (GeneralSecurityException | IOException ex) {
            Logger.getLogger(GoogleSignIn.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static GoogleIdTokenVerifier createVerifier(String clientId, String issuer) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Arrays.asList(clientId))
                .setIssuer(issuer).build();
        return verifier;
    }

}
