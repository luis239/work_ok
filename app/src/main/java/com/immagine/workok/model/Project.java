package com.immagine.workok.model;

import java.io.Serializable;

/**
 * Created by Alejandro on 29/05/2016.
 */
public class Project implements Serializable {


    private String title;
    private String description;
    private String dateStart;
    private String dateEnd;
    private int percentage;
    private int status_id;
    private int project_id;
    private boolean selected;
    //private User userAssigned = new User();

    public Project() {
        this.title = null;
        this.description = null;
        this.percentage = 0;
    }
    public Project(String title, String description, int percentage) {
        this.title = title;
        this.description = description;
        this.percentage = percentage;
    }

    public Project(String title, String description, String dateStart, String dateEnd, int percentage, int project_id, int status_id) {
        this.title = title;
        this.description = description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.percentage = percentage;
        this.project_id = project_id;
        this.status_id = status_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /*public User getUserAssigned() {
        return userAssigned;
    }

    public void setUserAssigned(User userAssigned) {
        this.userAssigned = userAssigned;
    }*/
}
