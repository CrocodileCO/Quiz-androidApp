
package com.crocodile.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Topic {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("backgroundURL")
    private String backgroundURL;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Topic() {
    }

    /**
     * 
     * @param id
     * @param title
     */
    public Topic(Integer id, String title, String backgroundURL) {
        super();
        this.id = id;
        this.title = title;
        this.backgroundURL = backgroundURL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Topic withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Topic withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBackgroundURL() { return backgroundURL;}

    public void setBackgroundURL(String backgroundURL) {
        this.backgroundURL = backgroundURL;
    }
}
