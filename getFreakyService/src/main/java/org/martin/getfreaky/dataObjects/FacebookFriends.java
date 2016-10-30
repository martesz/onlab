/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.dataObjects;

import java.util.List;

/**
 *
 * @author martin
 */
public class FacebookFriends {

    private List<FacebookUser> data;

    public List<FacebookUser> getData() {
        return data;
    }

    public void setData(List<FacebookUser> data) {
        this.data = data;
    }

}
