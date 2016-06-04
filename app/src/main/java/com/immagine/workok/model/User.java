package com.immagine.workok.model;

/**
 * Created by Alejandro on 02/06/2016.
 */
public class User {

    String username;
    String password;
    String fullname;
    int status_id;
    int user_id;

    public User() {

        this.username = "";
        this.password = "";
        this.fullname = "";
        this.status_id = 0;
        this.user_id = 0;

    }

    public User(String username, String password, String fullname, int status_id, int user_id) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.status_id = status_id;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
