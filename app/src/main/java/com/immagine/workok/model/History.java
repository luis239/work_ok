package com.immagine.workok.model;

/**
 * Created by Alejandro on 14/06/2016.
 */
public class History {

    private String message,title;

    public History(String title, String message) {

        this.message = message;
        this.title = title;
    }

    public History(String message) {

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
