package org.martin.getfreaky.network;

public class DayLogResponse {

    public enum ResponseMessage {
        DAYLOG_UPLOADED, DAYLOG_UPDATED, SOMETHING_WENT_WRONG
    }

    private ResponseMessage message;

    public DayLogResponse(ResponseMessage message) {
        this.message = message;
    }

    public ResponseMessage getMessage() {
        return message;
    }
}