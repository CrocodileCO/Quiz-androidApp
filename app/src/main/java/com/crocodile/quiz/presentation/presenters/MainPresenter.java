package com.crocodile.quiz.presentation.presenters;


import com.crocodile.quiz.model.Group;
import com.crocodile.quiz.presentation.ui.BaseView;

import java.util.List;

public interface MainPresenter extends BasePresenter{

    interface View extends BaseView {
        void showTopics(List<Group> groups);
        void test();
    }

    void getTopics();
}
