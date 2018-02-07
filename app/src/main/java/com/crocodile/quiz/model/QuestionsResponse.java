package com.crocodile.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionsResponse {

@SerializedName("qustionsResponse")
@Expose
private List<Question> qustionsResponse = null;

/**
* No args constructor for use in serialization
* 
*/
public QuestionsResponse() {
}

/**
* 
* @param qustionsResponse
*/
public QuestionsResponse(List<Question> qustionsResponse) {
super();
this.qustionsResponse = qustionsResponse;
}

public List<Question> getQustionsResponse() {
return qustionsResponse;
}

public void setQustionsResponse(List<Question> qustionsResponse) {
this.qustionsResponse = qustionsResponse;
}

}