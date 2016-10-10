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
public class MergeResponse {

    public enum Message {
        FACEBOOK_ACCOUNT_ASSOCIATED, GOOGLE_ACCOUNT_ASSOCIATED,
        EMAIL_ASSOCIATED, MERGE_NOT_SUCCESSFUL, ALREADY_ASSOCIATED_WITH_OTHER_ACCOUNT
    }

    private Message message;

    public MergeResponse() {
    }

    public MergeResponse(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
