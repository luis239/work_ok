package com.immagine.workok.model;

import java.io.Serializable;

/**
 * Created by Alejandro on 07/06/2016.
 */
public class Task implements Serializable {

    private String title;
    private String description;
    private String date_start;
    private String date_end;
    private int percentage;
    private int status_id;
    private int project_id;
    private int user_id;
    private String fullname;
    private String assignedBy;
    private int task_id;
    private int ownerId;
    private String projectName;

    public Task() {
        this.title = "";
        this.description = "";
        this.date_start = "";
        this.date_end = "";
        this.percentage = 0;
        this.status_id = 0;
        this.project_id = 0;
        this.user_id = 0;
        this.task_id = 0;
        fullname = null;

    }

    public Task(String title, String description, String date_start, String date_end, int percentage, int status_id, int project_id, int user_id, int task_id,String fullname,int ownerId,String projectTitle,String assignedBy) {
        this.title = title;
        this.description = description;
        this.date_start = date_start;
        this.date_end = date_end;
        this.percentage = percentage;
        this.status_id = status_id;
        this.project_id = project_id;
        this.user_id = user_id;
        this.task_id = task_id;
        this.fullname = fullname;
        this.ownerId = ownerId;
        this.projectName = projectTitle;
        this.assignedBy = assignedBy;
    }

    public Task(String title, String description, String date_start, String date_end, int percentage, int status_id, int project_id, int user_id, int task_id,String fullname,String projectTitle,String assignedBy) {
        this.title = title;
        this.description = description;
        this.date_start = date_start;
        this.date_end = date_end;
        this.percentage = percentage;
        this.status_id = status_id;
        this.project_id = project_id;
        this.user_id = user_id;
        this.task_id = task_id;
        this.fullname = fullname;
        this.projectName = projectTitle;
        this.assignedBy = assignedBy;
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

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }
}
