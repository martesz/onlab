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
