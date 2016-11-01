package org.martin.getfreaky.dataObjects;

import java.io.Serializable;
import java.util.Base64;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by martin on 2016. 04. 20.. This class represents a picture
 */
@Entity
public class ProgressPicture implements Serializable {

    private Long id;

    // BASE64 encoded byte array, this is used to send in JSON
    private String image;

    // Stores the image in byte[], because Derby database can not store
    // long strings
    private byte[] imageBytes;

    public ProgressPicture() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        if (image != null) {
            this.imageBytes = Base64.getMimeDecoder().decode(image);
        }
    }

    @Column(columnDefinition = "BLOB")
    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
        if (imageBytes != null) {
            this.image = Base64.getMimeEncoder().encodeToString(imageBytes);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProgressPicture other = (ProgressPicture) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
