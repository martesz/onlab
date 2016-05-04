package org.martin.getfreaky.dataObjects;

import io.realm.RealmObject;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents a picture
 */
public class ProgressPicture extends RealmObject{

    // TODO this class should handle pictures
    // Maybe only store the filepath of the picture as a string
    private String filePath;

    public ProgressPicture(){

    }

    // Many to one relationship
    DayLog dayLog;

    public DayLog getDayLog() {
        return dayLog;
    }

    public void setDayLog(DayLog dayLog) {
        this.dayLog = dayLog;
    }
}
