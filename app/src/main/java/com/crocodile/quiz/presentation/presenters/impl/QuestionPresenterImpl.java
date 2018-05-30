package com.crocodile.quiz.presentation.presenters.impl;

import android.content.Context;
import android.graphics.Bitmap;

import com.crocodile.quiz.domain.executor.Executor;
import com.crocodile.quiz.domain.executor.MainThread;
import com.crocodile.quiz.domain.interactors.GetQuestionsInteractor;
import com.crocodile.quiz.domain.interactors.impl.GetQuestionsInteractorImpl;
import com.crocodile.quiz.domain.repository.QuestionsRepository;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.helper.SetStatistics;
import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.presentation.presenters.AbstractPresenter;
import com.crocodile.quiz.presentation.presenters.QuestionPresenter;
import com.crocodile.quiz.storage.database.AnsweredQuestion;
import com.crocodile.quiz.storage.database.AnsweredQuestionDao;
import com.crocodile.quiz.storage.database.AppDatabase;

import java.util.List;


public class QuestionPresenterImpl extends AbstractPresenter implements QuestionPresenter, GetQuestionsInteractor.Callback, DownloadHelper.OnImageDownloadListener {

    QuestionPresenter.View mView;
    QuestionsRepository mQuestionsRepository;
    private Context mContext;
    private boolean statisticSent;
    private boolean needToInsertQF;
    private int currentQuestionIndex;
    private int currentImageDownloadIndex;
    private Question currentImageDownload;
    private boolean isRunning;
    private Question currentQuestion;

    private List<Question> questions;

    public QuestionPresenterImpl(Executor executor, MainThread mainThread, QuestionPresenter.View view, QuestionsRepository repository, Context context) {
        super(executor, mainThread);
        mContext = context;
        mView = view;
        mQuestionsRepository = repository;
        statisticSent = false;
        needToInsertQF = false;
    }

    @Override
    public void loadQuestions(String id) {
        GetQuestionsInteractor interactor = new GetQuestionsInteractorImpl(
                mExecutor,
                mMainThread,
                mQuestionsRepository,
                this,
                id);
        interactor.execute();
    }

    @Override
    public void onQuestionAnswered(Question question, int answerIndex) {
        question.setPlayerAnswerIndex(answerIndex);
    }

    @Override
    public void onQuestionsRetrieved(List<Question> questions) {
        this.questions = questions;
        startQuiz();
    }

    private void startQuiz() {
        for (Question qst : questions) {
            qst.setup();
        }

        mView.onQuestionsLoaded(questions);

        currentQuestionIndex = 0;
        goToQuestion(currentQuestionIndex, true);
        currentImageDownloadIndex = 0;
        loadImage(currentImageDownloadIndex);
    }

    private void loadImage(int index) {
        if (index < questions.size()) {
            currentImageDownload = questions.get(index);
            DownloadHelper.downloadImage(currentImageDownload.getImageUrl(), this);
        }
    }

    public void onImageDownloaded(Bitmap image) {
        if (image != null) {
            currentImageDownload.setImage(image);
        }
        if (currentImageDownloadIndex == currentQuestionIndex) {
            if (isRunning) {
                insertQuestion();
            } else {
                needToInsertQF = true;
            }
        }
        currentImageDownloadIndex++;
        loadImage(currentImageDownloadIndex);
    }

    private void goToQuestion(int index, boolean direction) {
        if (index < questions.size()) {
            currentQuestion = questions.get(index);
            //currentQuestion.setup();

            if(currentQuestion.getImage() == null) {
                if (currentQuestionIndex != 0) {
                    mView.showLoading(true);
                }
            } else {
                slideQuestion(direction);
            }

            //DownloadHelper.downloadImage(currentQuestion.getImageUrl(), this);
        } else {
            mView.showResults(countRightAnswers(), questions.size(), currentQuestionIndex);

            if (!statisticSent) {
                statisticSent = true;
                new SetStatistics().execute(questions);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    AnsweredQuestionDao dao = AppDatabase.getAppDatabase(mContext).questionDao();
                    for (Question question: questions) {
                        if (question.isPlayersAnswerRight()) {
                            AnsweredQuestion q = dao.findById(question.get_id());
                            if (q == null) {
                                dao.insertAll(new AnsweredQuestion(question.get_id()));
                            }
                        }
                    }
                }
            }).start();

        }
    }

    @Override
    public void resume() {
        isRunning = true;
        if (needToInsertQF) {
            insertQuestion();
        }
    }

    private void insertQuestion() {
        mView.insertQuestion(currentQuestion, currentQuestionIndex);
    }

    private void slideQuestion(boolean direction) {
        mView.slideQuestion(currentQuestion, currentQuestionIndex, direction);
    }

    @Override
    public void pause() {
        isRunning = false;
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
    public void goToNext() {
        if ((isCurrentQuestionAnswered()) && (currentQuestionIndex < questions.size())){
            currentQuestionIndex++;
            goToQuestion(currentQuestionIndex, true);
        }
    }

    private boolean isCurrentQuestionAnswered() {
        return ((currentQuestion != null) && (currentQuestion.isAnswered()));
    }

    @Override
    public void goToPrevious() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            goToQuestion(currentQuestionIndex, false);
        }
    }

    private int countRightAnswers() {
        int result = 0;
        for (Question question: questions) {
            result += question.isPlayersAnswerRight() ? 1 : 0;
        }
        return result;
    }
}
