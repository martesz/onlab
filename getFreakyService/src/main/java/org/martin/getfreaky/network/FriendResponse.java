/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.network;

/**
 *
 * @author martin
 */
public class FriendResponse {

    public enum Message {
        FRIEND_ADDED, ALREADY_FRIENDS, COULD_NOT_FIND_USER, COULD_NOT_ADD_FRIEND,
        FACEBOOK_FRIENDS_ADDED
    }

    private Message message;

    public FriendResponse(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
