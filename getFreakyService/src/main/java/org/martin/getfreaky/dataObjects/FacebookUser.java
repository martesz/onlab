/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.dataObjects;

/**
 *
 * @author martin
 */
public class FacebookUser {

    private String name;
    private String id;
    private FacebookFriends friends;

    public FacebookUser(){
    }

    public FacebookUser(String name, String id, FacebookFriends friends) {
        this.name = name;
        this.id = id;
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FacebookFriends getFriends() {
        return friends;
    }

    public void setFriends(FacebookFriends friends) {
        this.friends = friends;
    }
}
