package org.martin.getfreaky.network;

public class MergeResponse {

    public enum Message {
        FACEBOOK_ACCOUNT_ASSOCIATED, GOOGLE_ACCOUNT_ASSOCIATED,
        EMAIL_ASSOCIATED, MERGE_NOT_SUCCESSFUL, COULD_NOT_CONNECT,
        ALREADY_ASSOCIATED_WITH_OTHER_ACCOUNT
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