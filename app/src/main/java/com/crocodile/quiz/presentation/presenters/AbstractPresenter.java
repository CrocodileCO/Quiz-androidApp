package com.crocodile.quiz.presentation.presenters;


import com.crocodile.quiz.domain.executor.Executor;
import com.crocodile.quiz.domain.executor.MainThread;

public abstract class AbstractPresenter {
    protected Executor mExecutor;
    protected MainThread mMainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}
