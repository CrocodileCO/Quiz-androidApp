
package com.crocodile.quiz.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicsResponse {

    @SerializedName("topics")
    private List<Topic> topics = null;

    public TopicsResponse() {
    }

    public TopicsResponse(List<Topic> topics) {
        super();
        this.topics = topics;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public TopicsResponse withTopics(List<Topic> topics) {
        this.topics = topics;
        return this;
    }

}
