package com.entertainment.subscriber.note.dao;

public class SubscriberDao {
    private String name;
    private String title;
    private String status;

    public SubscriberDao() {
    }

    public SubscriberDao(String name, String title, String status) {
        this.name = name;
        this.title = title;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubscriberDao{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
