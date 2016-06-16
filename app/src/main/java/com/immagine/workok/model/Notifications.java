package com.immagine.workok.model;

/**
 * Created by Alejandro on 14/06/2016.
 */
public class Notifications {

    private String message,title;

    public Notifications(String title,String message) {

        this.message = message;
        this.title = title;
    }

    public Notifications(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }
}
