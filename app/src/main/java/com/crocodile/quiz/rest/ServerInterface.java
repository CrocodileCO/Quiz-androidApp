package com.crocodile.quiz.rest;


import com.crocodile.quiz.model.QuestionsResponse;
import com.crocodile.quiz.model.TopicsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerInterface {

    @GET("topics")
    Call<TopicsResponse> getTopics();

    @GET("questions")
    Call<QuestionsResponse> getQuestions();
}
