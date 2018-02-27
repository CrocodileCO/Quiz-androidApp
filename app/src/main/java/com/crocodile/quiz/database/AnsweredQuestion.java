package com.crocodile.quiz.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class AnsweredQuestion {

    @PrimaryKey(autoGenerate = true)
    private int qid;

    @ColumnInfo(name = "question_id")
    private String questionId;

    public AnsweredQuestion(String questionId) {
        this.questionId = questionId;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}

