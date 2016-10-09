/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.utils;

import com.google.gson.Gson;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.martin.getfreaky.dataObjects.FacebookUser;
import org.martin.getfreaky.dataObjects.User;

/**
 *
 * @author martin
 */
public class FacebookLogin {

    public static final String BASE_URL = "https://graph.facebook.com/me/?fields=id,name&access_token=";

    public static User login(String facebookAccessToken) {
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
            return null;
        }
    }
}
