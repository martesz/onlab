package org.martin.getfreaky.dataObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import io.realm.RealmObject;

/**
 * Created by martin on 2016. 04. 20..
 * This class represents a picture
 */
public class ProgressPicture extends RealmObject {

    // BASE64 encoded byte array
    private String image;

    public ProgressPicture() {
    }

    public ProgressPicture(ProgressPicture progressPicture) {
        this.image = progressPicture.getImage();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @param image The image to be set
     *              Stores the image BASE64 encoded
     */
    public void setImage(Bitmap image) {
        Bitmap imagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        this.image = Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     *
     * @return The stored BASE64 image decoded
     */
    public Bitmap getImageBitmap() {
        if (image != null) {
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } else {
            return null;
        }
    }
}
