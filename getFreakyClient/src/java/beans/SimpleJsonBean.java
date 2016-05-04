/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.google.gson.Gson;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import json.objects.SimpleObject;

/**
 *
 * @author martin
 */
@ManagedBean
@SessionScoped
public class SimpleJsonBean {
    private SimpleObject object;
    private int id = 15;
    
    public SimpleJsonBean(){
        object = new SimpleObject(10, "bean test");
    }
    
    public SimpleObject getObject() {
        return object;
    }

    public int getId() {
        return id;
    }
    
    public void refresh(){
        Client client = ClientBuilder.newClient();
        String json = client.target("http://192.168.1.4:8080/HelloWorldApplication/HelloWorldApplication")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        Gson gson = new Gson();
        object = gson.fromJson(json, SimpleObject.class);
    }
}
