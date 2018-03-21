package com.crocodile.quiz.presentation.presenters;

public interface BasePresenter {

    void resume();

    void pause();

    void stop();

    void destroy();

    void onError(String message);
}
