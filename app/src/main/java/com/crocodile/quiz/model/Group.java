package com.crocodile.quiz.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("topics")
    private List<Topic> topics = null;

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

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

}