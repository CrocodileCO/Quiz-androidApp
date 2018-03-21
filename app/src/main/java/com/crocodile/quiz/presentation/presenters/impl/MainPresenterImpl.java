package com.crocodile.quiz.presentation.presenters.impl;


import android.widget.Toast;

import com.crocodile.quiz.domain.executor.Executor;
import com.crocodile.quiz.domain.executor.MainThread;
import com.crocodile.quiz.domain.interactors.GetTopicsInteractor;
import com.crocodile.quiz.domain.interactors.impl.GetTopicsInteractorImpl;
import com.crocodile.quiz.domain.repository.Impl.TopicsRepositoryImpl;
import com.crocodile.quiz.domain.repository.TopicsRepository;
import com.crocodile.quiz.model.Group;
import com.crocodile.quiz.presentation.presenters.AbstractPresenter;
import com.crocodile.quiz.presentation.presenters.MainPresenter;

import java.util.List;

public class MainPresenterImpl extends AbstractPresenter implements MainPresenter, GetTopicsInteractor.Callback{

    private MainPresenter.View mView;
    private TopicsRepository mTopicsRepository;

    public MainPresenterImpl(Executor executor, MainThread mainThread, View view, TopicsRepository topicsRepository) {
        super(executor, mainThread);

        mView = view;
        mTopicsRepository = topicsRepository;
    }

    @Override
    public void resume() {
        getTopics();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void getTopics() {
        GetTopicsInteractor getTopicsInteractor = new GetTopicsInteractorImpl(
                mExecutor,
                mMainThread,
                mTopicsRepository,
                this
        );
        getTopicsInteractor.execute();
    }

    @Override
    public void onTopicsRetrieved(List<Group> groupList) {
        mView.showTopics(groupList);
    }
}
