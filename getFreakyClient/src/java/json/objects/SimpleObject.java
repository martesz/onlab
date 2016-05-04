/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json.objects;

/**
 *
 * @author martin
 */
public class SimpleObject {
   private int id;
   private String content;
    
    public SimpleObject(int pid, String pContent){
        this.id = pid;
        this.content = pContent;
    }
    
    public SimpleObject(){
        
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
   
}