package org.martin.getfreaky.network;

/**
 * Created by martin on 2016. 05. 15..
 */
public class WorkoutResponse {
    public enum ResponseMessage {
        WORKOUT_UPLOADED, WORKOUT_UPDATED, WORKOUT_DELETED, SOMETHING_WENT_WRONG
    }

    private ResponseMessage message;

    public WorkoutResponse(ResponseMessage message) {
        this.message = message;
    }

    public ResponseMessage getMessage() {
        return message;
    }
}