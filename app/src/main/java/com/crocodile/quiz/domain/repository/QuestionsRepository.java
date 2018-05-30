package com.crocodile.quiz.domain.repository;


import com.crocodile.quiz.model.Group;
import com.crocodile.quiz.model.Question;

import java.util.List;

public interface QuestionsRepository {

    interface Callback {
        void onQuestionsLoaded(List<Question> questions);
    }

    void getNewQuestionSet(Callback callback, String topicId);
}
