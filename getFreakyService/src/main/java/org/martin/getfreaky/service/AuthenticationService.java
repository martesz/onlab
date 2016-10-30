package org.martin.getfreaky.service;

import com.google.gson.Gson;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.martin.getfreaky.dataObjects.MergeData;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.database.AuthenticationDao;
import org.martin.getfreaky.network.LoginResponse;
import org.martin.getfreaky.network.MergeResponse;
import org.martin.getfreaky.network.Secured;
import org.martin.getfreaky.utils.FacebookServices;
import org.martin.getfreaky.utils.GoogleSignIn;

/**
 *
 * @author martin
 *
 * Restful services for user authentication.
 */
@Path("authentication")
public class AuthenticationService {

    @EJB
    private AuthenticationDao authenticationDao;
    
    @EJB
    private FacebookServices facebookServices;

    private final Gson gson;

    public AuthenticationService() {
        super();
        gson = new Gson();
    }

    @PUT
    @Path("signInOrRegisterEmail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithEmail(String content) {
        User user = gson.fromJson(content, User.class);
        LoginResponse response;
        if (user.getEmail() != null) {
            response = authenticationDao.signInOrRegisterUser(user);
        } else {
            response = new LoginResponse(LoginResponse.ResponseMessage.EMAIL_NULL);
        }
        return gson.toJson(response);
    }

    @PUT
    @Path("signInOrRegisterGoogle")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithGoogle(String googleIdToken) {
        User user = GoogleSignIn.authenticateWeb(googleIdToken);
        LoginResponse response = authenticationDao.signInOrRegisterGoogle(user);
        return gson.toJson(response);
    }

    @PUT
    @Path("signInOrRegisterGoogleAndroid")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithGoogleAndroid(String googleIdToken) {
        User user = GoogleSignIn.authenticateAndroid(googleIdToken);
        LoginResponse response = authenticationDao.signInOrRegisterGoogle(user);
        return gson.toJson(response);
    }

    @PUT
    @Path("signInOrRegisterFacebook")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String signInOrRegisterWithFacebook(String facebookAccessToken) {
        User user = facebookServices.login(facebookAccessToken);
        LoginResponse response = authenticationDao.signInOrRegisterFacebook(user);
        return gson.toJson(response);
    }

    @PUT
    @Secured
    @Path("mergeAccountsAndroid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String mergeAccountsAndroid(String content) {
        MergeData mergeData = gson.fromJson(content, MergeData.class);
        MergeResponse response = authenticationDao.mergeAccounts(mergeData);
        return gson.toJson(response);
    }

}
