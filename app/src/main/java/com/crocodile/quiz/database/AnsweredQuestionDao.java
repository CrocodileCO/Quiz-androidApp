package com.crocodile.quiz.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface AnsweredQuestionDao {

    @Query("SELECT * FROM answeredquestion")
    List<AnsweredQuestion> getAll();

    @Query("SELECT * FROM answeredquestion WHERE question_id LIKE :id LIMIT 1")
    AnsweredQuestion findById(String id);

    @Query("DELETE FROM answeredquestion")
    void nukeTable();

    @Insert
    void insertAll(AnsweredQuestion... questions);

    @Delete
    void delete(AnsweredQuestion question);
}
