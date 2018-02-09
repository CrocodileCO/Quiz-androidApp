package com.crocodile.quiz.rest;


import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.model.QuestionsResponse;
import com.crocodile.quiz.model.Topic;
import com.crocodile.quiz.model.TopicsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerInterface {

    @GET("topics")
    Call<List<Topic>> getTopics();

    @GET("questions")
    Call<List<Question>> getQuestions();
}
