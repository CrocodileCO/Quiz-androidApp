package com.crocodile.quiz.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionInformation implements Serializable{

    @SerializedName("text")
    @Expose
    private String text;

    public QuestionInformation(String text) {
        this.text = text;
    }

    public boolean hasSomethingToShow() {
        return text!="";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
