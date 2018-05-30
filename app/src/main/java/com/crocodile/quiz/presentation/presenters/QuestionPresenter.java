package com.crocodile.quiz.presentation.presenters;

import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.presentation.ui.BaseView;

import java.util.List;


public interface QuestionPresenter extends BasePresenter {

    interface View extends BaseView {
        void showResults(int right, int total, int index);
        void slideQuestion(Question question, int index, boolean direction);
        void insertQuestion(Question question, int index);
        void showLoading(boolean slide);
        void onQuestionsLoaded(List<Question> questions);
    }

    void goToNext();
    void goToPrevious();
    void loadQuestions(String id);
    void onQuestionAnswered(Question question, int answerIndex);
}
