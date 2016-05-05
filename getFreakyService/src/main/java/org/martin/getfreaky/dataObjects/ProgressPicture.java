package org.martin.getfreaky.dataObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents a picture
 */
@Entity
public class ProgressPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long progressPictureId;
    
    @ManyToOne
    @JoinColumn(name = "DAYLOGID")
    private DayLog dayLog;
    
    // TODO this class should handle pictures

    private Byte[] image;

    public ProgressPicture(){

    }

    public Long getProgressPictureId() {
        return progressPictureId;
    }

    public void setProgressPictureId(Long progressPictureId) {
        this.progressPictureId = progressPictureId;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public DayLog getDayLog() {
        return dayLog;
    }

    public void setDayLog(DayLog dayLog) {
        this.dayLog = dayLog;
    }
}
