package com.crocodile.quiz.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Answer implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pickAmount")
    @Expose
    private int pickAmount;

    public Answer(String text, int pickAmount) {
        this.text = text;
        this.pickAmount = pickAmount;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPickAmount() {
        return pickAmount;
    }

    public void setPickAmount(int pickAmount) {
        this.pickAmount = pickAmount;
    }
}
