package com.parse.starter;

/**
 * Created by Aostia on 19/02/2015.
 */
public class Message {
    private String objectId;
    private String message;
    private String subject;
    private boolean read;

    public Message(){
        this.objectId = "";
        this.message = "";
        this.subject = "";
        this.read = false;
    }

    public Message(String o, String m, String s, boolean r) {
        this.objectId = o;
        this.message = m;
        this.subject = s;
        this.read = r;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public void setRead() {
        //do nothing for now
    }
}
