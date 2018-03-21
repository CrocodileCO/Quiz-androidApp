package com.crocodile.quiz.domain.interactors;

import com.crocodile.quiz.domain.interactors.base.Interactor;
import com.crocodile.quiz.model.Group;

import java.util.List;


public interface GetTopicsInteractor extends Interactor {

    interface Callback {
        void onTopicsRetrieved(List<Group> groupList);
    }
}
