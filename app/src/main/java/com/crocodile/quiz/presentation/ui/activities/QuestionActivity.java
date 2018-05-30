package com.crocodile.quiz.presentation.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.crocodile.quiz.R;
import com.crocodile.quiz.domain.executor.impl.ThreadExecutor;
import com.crocodile.quiz.domain.repository.Impl.QuestionsRepositoryImpl;
import com.crocodile.quiz.presentation.presenters.QuestionPresenter;
import com.crocodile.quiz.presentation.presenters.impl.QuestionPresenterImpl;
import com.crocodile.quiz.presentation.ui.fragments.ExitTestDialogFragment;
import com.crocodile.quiz.presentation.ui.fragments.ExitTestResultHandler;
import com.crocodile.quiz.presentation.ui.fragments.LoadingFragment;
import com.crocodile.quiz.presentation.ui.fragments.QuestionFragment;
import com.crocodile.quiz.presentation.ui.fragments.ResultFragment;
import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.presentation.ui.customviews.QuestionsTrackerView;
import com.crocodile.quiz.threading.MainThreadImpl;

import java.util.List;


public class QuestionActivity extends AppCompatActivity implements ExitTestResultHandler, QuestionPresenter.View{


    private String topicId;

    private RelativeLayout trackerFrame;
    private QuestionsTrackerView trackerView;

    private GestureDetectorCompat mDetector;

    private QuestionPresenter mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        trackerFrame = findViewById(R.id.tracker_container);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);
        topicId = intent.getStringExtra("id");

        insertLoadingFragment();

        init();
    }

    protected  void init() {
        mPresenter = new QuestionPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new QuestionsRepositoryImpl(this),
                getApplicationContext());
        mPresenter.loadQuestions(topicId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    public void setQuestionAnswered(Question question, int index) {
        mPresenter.onQuestionAnswered(question, index);
        trackerView.postInvalidate();
    }

    private void insertLoadingFragment() {
        insertFragment(new LoadingFragment());
    }

    private void slideLoadingFragment() {
        slideFragment(new LoadingFragment(), true);
    }

    private void insertQuestionFragment(Question question) {
        insertFragment(getQuestionFragment(question));
    }

    private void slideQuestionFragment(boolean direction, Question question) {
        slideFragment(getQuestionFragment(question), direction);
    }

    private void slideResultFragment(int rightAnswers, int totalAnswers) {
        slideFragment(getResultFragment(rightAnswers, totalAnswers), true);
    }

    private ResultFragment getResultFragment(int rightAnswers, int totalAnswers) {
        ResultFragment resultFragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("rightAnswers", rightAnswers);
        bundle.putInt("totalAnswers", totalAnswers);
        resultFragment.setArguments(bundle);
        return resultFragment;
    }

    private QuestionFragment getQuestionFragment(Question question) {
        QuestionFragment questionFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        questionFragment.setArguments(bundle);
        return questionFragment;
    }

    private void insertFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void slideFragment(Fragment fragment, boolean direction) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if ((currentFragment != null) && (currentFragment instanceof QuestionFragment)) {
            ((QuestionFragment) currentFragment).hideInformationButton();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (direction) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void showResults(int right, int total, int index) {
        updateTracker(index);
        slideResultFragment(right, total);
    }

    @Override
    public void slideQuestion(Question question, int index, boolean direction) {
        updateTracker(index);
        slideQuestionFragment(direction, question);
    }

    @Override
    public void insertQuestion(Question question, int index) {
        updateTracker(index);
        insertQuestionFragment(question);
    }

    private void updateTracker(int index) {
        trackerView.setCurrentQuestion(index);
        trackerView.postInvalidate();
    }

    @Override
    public void showLoading(boolean slide) {
        if(slide) {
            slideLoadingFragment();
        } else {
            insertLoadingFragment();
        }
    }

    @Override
    public void onQuestionsLoaded(List<Question> questions) {
        trackerView = new QuestionsTrackerView(trackerFrame.getContext(), trackerFrame, questions);
        trackerFrame.addView(trackerView);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if ((Math.abs(velocityX) > Math.abs(velocityY))) {
                if (velocityX < 0) {
                    mPresenter.goToNext();
                } else {
                    mPresenter.goToPrevious();
                }
            }

            return true;
        }
    }

    @Override
    public void onBackPressed() {
        ExitTestDialogFragment dialogFragment = new ExitTestDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "lol");
    }

    @Override
    public void ExitTest() {
        super.onBackPressed();
    }

}
