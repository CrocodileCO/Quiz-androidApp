package com.crocodile.quiz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crocodile.quiz.R;
import com.crocodile.quiz.fragment.ExitTestDialogFragment;
import com.crocodile.quiz.fragment.ExitTestResultHandler;
import com.crocodile.quiz.fragment.LoadingFragment;
import com.crocodile.quiz.fragment.QuestionFragment;
import com.crocodile.quiz.fragment.ResultFragment;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.helper.SetStatistics;
import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;
import com.crocodile.quiz.views.QuestionsTrackerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.rest.ApiClient.BASE_URL;


public class QuestionActivity extends AppCompatActivity implements DownloadHelper.OnImageDownloadListener, ExitTestResultHandler{

    private List<Question> questions;
    private Question currentQuestion;
    private int currentQuestionIndex;
    private Question currentImageDownload;
    private int currentImageDownloadIndex;
    //private boolean currentQuestionAnswered;
    private String topicId;
    private boolean statisticSent;

    private RelativeLayout trackerFrame;
    private QuestionsTrackerView trackerView;

    private GestureDetectorCompat mDetector;

    private List<QuestionFragment> questionFragments;

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

        //currentQuestionAnswered = false;
        questionFragments = new ArrayList<>();
        statisticSent = false;

        insertLoadingFragment();

        loadQuestions();

    }

    public void showNextQuestion() {
        //slideLoadingFragment();
        //currentQuestionAnswered = false;
        currentQuestionIndex++;
        goToQuestion(currentQuestionIndex, true);
    }

    public void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            goToQuestion(currentQuestionIndex, false);
        }
    }


    public void setQuestionAnswered() {
        //currentQuestionAnswered = true;
        trackerView.postInvalidate();
    }


    private void loadQuestions() {
        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<List<Question>> call = apiService.getQuestions(topicId);

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                int statusCode = response.code();
                if (this != null) {
                    onQuestionsLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("lol", t.toString());
            }
        });

    }

    private void goToQuestion(int index, boolean direction) {
        trackerView.setCurrentQuestion(index);
        trackerView.postInvalidate();
        if (index < questions.size()) {
            currentQuestion = questions.get(index);
            //currentQuestion.setup();

            if(currentQuestion.getImage() == null) {
                if (currentQuestionIndex != 0) {
                    slideLoadingFragment();
                }
            } else {
                slideQuestionFragment(direction);
            }

            //DownloadHelper.downloadImage(currentQuestion.getImageUrl(), this);
        } else {
            slideResultFragment();

            if (!statisticSent) {
                statisticSent = true;
                new SetStatistics().execute(questions);
            }

            //Toast.makeText(getApplicationContext(), "Quiz ended. Right answers: " + countRightAnswers() + "/" + questions.size() + ".", Toast.LENGTH_LONG).show();
        }
    }

    private void onQuestionsLoaded(List<Question> loadedQuestions) {
        if (loadedQuestions == null) {
            //insertLoadingFragment();
            Toast.makeText(getApplicationContext(), "No questions", Toast.LENGTH_LONG).show();
            return;
        }
        this.questions = loadedQuestions;
        for (Question qst : questions) {
            qst.setup();
        }


        trackerView = new QuestionsTrackerView(trackerFrame.getContext(), trackerFrame, questions);
        trackerFrame.addView(trackerView);

        currentQuestionIndex = 0;
        goToQuestion(currentQuestionIndex, true);
        currentImageDownloadIndex = 0;
        loadImage(currentImageDownloadIndex);
    }

    private void showInformation() {
        if (isCurrentQuestionAnswered()) {
            Intent intent = new Intent(getApplicationContext(), QuestionAbout.class);
            intent.putExtra("about", "hello bob");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }
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
            insertQuestionFragment();
        }
        currentImageDownloadIndex++;
        loadImage(currentImageDownloadIndex);
    }

    private void insertLoadingFragment() {
        insertFragment(new LoadingFragment());
    }

    private void slideLoadingFragment() {
        slideFragment(new LoadingFragment(), true);
    }

    private void insertQuestionFragment() {
        insertFragment(getQuestionFragment(currentQuestion));
    }

    private void slideQuestionFragment(boolean direction) {
        slideFragment(getQuestionFragment(currentQuestion), direction);
    }

    private void slideResultFragment() {
        slideFragment(getResultFragment(countRightAnswers(), questions.size()), true);
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
        int index = questions.indexOf(question);
        QuestionFragment questionFragment;
        if (index < questionFragments.size()) {
            questionFragment = questionFragments.get(index);
        } else {
            questionFragment = new QuestionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("question", question);
            questionFragment.setArguments(bundle);
            questionFragments.add(questionFragment);
        }
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

    private int countRightAnswers() {
        int result = 0;
        for (Question question: questions) {
            result += question.isPlayersAnswerRight() ? 1 : 0;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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
                    if ((isCurrentQuestionAnswered()) && (currentQuestionIndex < questions.size())){
                        showNextQuestion();
                    }
                } else {
                        showPreviousQuestion();
                }
            }

            return true;
        }
    }

    private boolean isCurrentQuestionAnswered() {
        return ((currentQuestion != null) && (currentQuestion.isAnswered()));
    }

    @Override
    public void onBackPressed() {
        if (questions != null) {
            if (currentQuestionIndex < questions.size()) {
                ExitTestDialogFragment dialogFragment = new ExitTestDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "lol");
                return;
            }
        }
        ExitTest();
    }

    @Override
    public void ExitTest() {
        super.onBackPressed();
    }
}
