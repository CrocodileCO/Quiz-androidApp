package com.crocodile.quiz.domain.interactors;

import com.crocodile.quiz.domain.interactors.base.Interactor;
import com.crocodile.quiz.model.Group;
import com.crocodile.quiz.model.Question;

import java.util.List;


public interface GetQuestionsInteractor extends Interactor {

    interface Callback {
        void onQuestionsRetrieved(List<Question> questions);
    }
}
