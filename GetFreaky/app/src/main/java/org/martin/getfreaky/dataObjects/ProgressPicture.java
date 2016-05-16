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

    public ProgressPicture(ProgressPicture pp){
        this.filePath = pp.getFilePath();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
