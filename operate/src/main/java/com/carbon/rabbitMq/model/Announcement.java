package com.carbon.rabbitMq.model;

import java.io.Serializable;

public class Announcement implements Serializable {
    private String id;
    private String title;
    private String time;
    private String context;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
