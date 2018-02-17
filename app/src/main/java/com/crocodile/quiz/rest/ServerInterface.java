package com.crocodile.quiz.rest;


import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.model.Topic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerInterface {

    /*@GET("topics")
    Call<List<Topic>> getTopics();

    @GET("questions")
    Call<List<Question>> getQuestions();*/

    @GET("topics")
    Call<List<Topic>> getTopics();

    @GET("topics/{topic_id}/rnd")
    Call<List<Question>> getQuestions(@Path("topic_id") String topicId);

    @GET("questions/{questionId}/inc_quantity")
    Call<Void> setStatistics(@Path("questionId") String topicId, @Query("num") int id);

}
