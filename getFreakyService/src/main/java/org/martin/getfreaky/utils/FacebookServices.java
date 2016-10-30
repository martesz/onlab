/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.utils;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.martin.getfreaky.dataObjects.FacebookUser;
import org.martin.getfreaky.dataObjects.User;

/**
 *
 * @author martin
 */
@Stateless
public class FacebookServices {

    public static final String BASE_URL = "https://graph.facebook.com/me/?fields=id,name&access_token=";
    public static final String FRIENDS_URL = "https://graph.facebook.com/me/?fields=id,name,friends&access_token=";

    /**
     *
     * @param facebookAccessToken User's facebook access token
     * @return User object containing the facebook data of the user
     */
    public User login(String facebookAccessToken) {
        try {
            String url = BASE_URL + facebookAccessToken;
            Client client = ClientBuilder.newClient();
            Gson gson = new Gson();
            Response response = client.target(url)
                    .request()
                    .get();
            String responseEntity = response.readEntity(String.class);
            FacebookUser fUser = gson.fromJson(responseEntity, FacebookUser.class);
            User user = new User();
            user.setFacebookId(fUser.getId());
            user.setName(fUser.getName());
            return user;
        } catch (Exception e) {
            System.out.println("Could not get facebook account )" + e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param facebookAccessToken User's facebook access token
     * @return List containing the user's facebook friends who use this
     * application
     */
    public List<FacebookUser> getFacebookFriends(String facebookAccessToken) {
        List<FacebookUser> friends = new ArrayList<>();
        try {
            String url = FRIENDS_URL + facebookAccessToken;
            Client client = ClientBuilder.newClient();
            Gson gson = new Gson();
            Response response = client.target(url)
                    .request()
                    .get();
            String responseEntity = response.readEntity(String.class);
            FacebookUser fUser = gson.fromJson(responseEntity, FacebookUser.class);
            friends.addAll(fUser.getFriends().getData());
        } catch (Exception e) {
            System.out.println("Could not get facebook friends )" + e.getMessage());
        }
        return friends;
    }
}
