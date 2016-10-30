package org.martin.getfreaky.service;

import com.google.gson.Gson;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.martin.getfreaky.dataObjects.FacebookUser;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.database.FriendsDao;
import org.martin.getfreaky.network.FriendResponse;
import org.martin.getfreaky.network.Secured;
import org.martin.getfreaky.utils.FacebookServices;

/**
 *
 * @author martin
 *
 * Rest endpoints for user's friends.
 */
@Path("friend")
public class FriendService {

    @EJB
    private FriendsDao friendsDao;

    @EJB
    private FacebookServices facebookServices;

    @Context
    private SecurityContext securityContext;

    private final Gson gson;

    public FriendService() {
        gson = new Gson();
    }

    /**
     *
     * @param userId User's id
     * @return The user's friends
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @Path("{userId}")
    public String getFriends(@PathParam("userId") String userId) {
        Set<User> friends = friendsDao.getFriends(userId);
        return gson.toJson(friends);
    }

    /**
     *
     * @param friendEmail Friend's email
     * @return The result of the friend addition
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @Path("add/{email}")
    public String addFriendByEmail(@PathParam("email") String friendEmail) {
        String userId = securityContext.getUserPrincipal().getName();
        FriendResponse response = friendsDao.addFriendByEmail(userId, friendEmail);
        return gson.toJson(response);
    }

    @PUT
    @Path("addFacebookFriends")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public String addFriendsFromFacebook(String facebookAccessToken) {
        String userId = securityContext.getUserPrincipal().getName();
        List<FacebookUser> friends = facebookServices.getFacebookFriends(facebookAccessToken);
        FriendResponse response = friendsDao.addFacebookFriends(userId, friends);
        return gson.toJson(response);
    }

}
