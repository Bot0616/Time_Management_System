package com.example.timemanagementsystem;

import java.util.Date;

public class Task {
    private int id;
    private String name;
    private Date date;
    private boolean completed;

    public Task(int id, String name, Date date, boolean completed) {
        this.id =  id;
        this.name = name;
        this.date = date;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

