package org.martin.getfreaky.dataObjects;

import java.util.Random;

/**
 * Created by martin on 2016. 05. 03..
 * Generates a unique id for the objects
 */

public class DataObject {

    private static Random random = new Random();
    protected long id;

    public DataObject() {
        id = random.nextLong();
    }

    public DataObject(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
