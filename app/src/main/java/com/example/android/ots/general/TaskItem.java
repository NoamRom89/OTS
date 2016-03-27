package com.example.android.ots.general;

import java.util.Date;

/**
 * class task item represents a task
 */
public class TaskItem {

    private String id;
    private String category;
    private int priority;
    private String location;
    private Date dueTime;
    private String assignee;
    private String status;
    private String accept;
    private String imageUrl = "";
    private String taskName;

    public TaskItem(String category, int priority, String location, Date dueTime, String assignee, String status, String accept, String taskName) {
        this.category = category;
        this.priority = priority;
        this.location = location;
        this.dueTime = dueTime;
        this.assignee = assignee;
        this.status = status;
        this.accept = accept;
        this.taskName = taskName;
    }



    public TaskItem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date cal) {
        dueTime = cal;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", location='" + location + '\'' +
                ", dueTime=" + dueTime +
                ", assignee='" + assignee + '\'' +
                ", status='" + status + '\'' +
                ", accept='" + accept + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
