package com.crocodile.quiz.domain.repository;


import com.crocodile.quiz.model.Group;

import java.util.List;

public interface TopicsRepository {

    interface Callback {
        void onTopicsLoaded(List<Group> groups);
    }

    void getTopics(Callback callback);
}
