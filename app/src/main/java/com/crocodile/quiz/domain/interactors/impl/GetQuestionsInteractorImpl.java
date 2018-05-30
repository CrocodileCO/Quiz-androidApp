package com.crocodile.quiz.domain.interactors.impl;

import com.crocodile.quiz.domain.executor.Executor;
import com.crocodile.quiz.domain.executor.MainThread;
import com.crocodile.quiz.domain.interactors.base.AbstractInteractor;
import com.crocodile.quiz.domain.interactors.GetQuestionsInteractor;
import com.crocodile.quiz.domain.repository.QuestionsRepository;
import com.crocodile.quiz.model.Question;

import java.util.List;


public class GetQuestionsInteractorImpl extends AbstractInteractor implements GetQuestionsInteractor, QuestionsRepository.Callback {

    private Callback mCallback;
    private QuestionsRepository mQuestionsRepository;
    private String topicId;

    public GetQuestionsInteractorImpl(Executor threadExecutor, MainThread mainThread, QuestionsRepository repository, Callback callback, String topicId) {
        super(threadExecutor, mainThread);

        mCallback = callback;
        mQuestionsRepository = repository;
        this.topicId = topicId;
    }

    @Override
    public void onQuestionsLoaded(final List<Question> questions) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onQuestionsRetrieved(questions);
            }
        });
    }

    @Override
    public void run() {
        mQuestionsRepository.getNewQuestionSet(this, topicId);
    }
}
