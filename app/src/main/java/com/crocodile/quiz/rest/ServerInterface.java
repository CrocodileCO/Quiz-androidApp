package com.crocodile.quiz.rest;


import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.model.QuestionsResponse;
import com.crocodile.quiz.model.Topic;
import com.crocodile.quiz.model.TopicsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServerInterface {

    /*@GET("topics")
    Call<List<Topic>> getTopics();

    @GET("questions")
    Call<List<Question>> getQuestions();*/

    @GET("topics")
    Call<List<Topic>> getTopics();

    @GET("topics/{topic_id}/rnd")
    Call<List<Question>> getQuestions(@Path("topic_id") String topicId);

}
