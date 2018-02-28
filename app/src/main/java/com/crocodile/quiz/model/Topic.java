
package com.crocodile.quiz.model;

import com.google.gson.annotations.SerializedName;

public class Topic {

    @SerializedName("_id")
    private String _id;
    @SerializedName("title")
    private String title;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("groupId")
    private String groupId;

    public Topic() {
    }


    public Topic(String _id, String title, String imageUrl) {
        super();
        this._id = _id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Topic withId(String id) {
        this._id = id;
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

    public String getImageUrl() { return imageUrl;}

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
