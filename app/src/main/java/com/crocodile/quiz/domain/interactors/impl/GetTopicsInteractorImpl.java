package com.crocodile.quiz.domain.interactors.impl;

import com.crocodile.quiz.domain.executor.Executor;
import com.crocodile.quiz.domain.executor.MainThread;
import com.crocodile.quiz.domain.interactors.GetTopicsInteractor;
import com.crocodile.quiz.domain.interactors.base.AbstractInteractor;
import com.crocodile.quiz.domain.repository.TopicsRepository;
import com.crocodile.quiz.model.Group;

import java.util.List;


public class GetTopicsInteractorImpl extends AbstractInteractor implements GetTopicsInteractor, TopicsRepository.Callback {

    private Callback mCallback;
    private TopicsRepository mTopicsRepository;

    public GetTopicsInteractorImpl(Executor threadExecutor, MainThread mainThread, TopicsRepository topicsRepository, Callback callback) {
        super(threadExecutor, mainThread);

        mCallback = callback;
        mTopicsRepository = topicsRepository;
    }

    @Override
    public void run() {
        mTopicsRepository.getTopics(this);
    }

    @Override
    public void onTopicsLoaded(final List<Group> groups) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTopicsRetrieved(groups);
            }
        });
    }
}
