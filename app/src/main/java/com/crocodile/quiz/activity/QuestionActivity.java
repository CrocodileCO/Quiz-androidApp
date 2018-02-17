package com.crocodile.quiz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.crocodile.quiz.R;
import com.crocodile.quiz.fragment.LoadingFragment;
import com.crocodile.quiz.fragment.QuestionFragment;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.helper.SetStatistics;
import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.rest.ApiClient.BASE_URL;


public class QuestionActivity extends AppCompatActivity implements DownloadHelper.OnImageDownloadListener{

    private List<Question> questions;
    private Question currentQuestion;
    private int currentQuestionIndex;
    private Question currentImageDownload;
    private int currentImageDownloadIndex;
    private boolean currentQuestionAnswered;
    private String topicId;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);
        topicId = intent.getStringExtra("id");

        currentQuestionAnswered = false;

        insertLoadingFragment();

        loadQuestions();

    }

    public void showNextQuestion() {
        //slideLoadingFragment();
        currentQuestionAnswered = false;
        currentQuestionIndex++;
        goToQuestion(currentQuestionIndex);
    }

    public void setQuestionAnswered() {
        currentQuestionAnswered = true;
    }


    private void loadQuestions() {
        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<List<Question>> call = apiService.getQuestions(topicId);

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                int statusCode = response.code();
                onQuestionsLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("lol", t.toString());
            }
        });

    }

    private void goToQuestion(int index) {
        if (index < questions.size()) {
            currentQuestion = questions.get(index);
            currentQuestion.setup();

            if(currentQuestion.getImage() == null) {
                if (currentQuestionIndex != 0) {
                    slideLoadingFragment();
                }
            } else {
                slideQuestionFragment();
            }

            //DownloadHelper.downloadImage(currentQuestion.getImageUrl(), this);
        } else {
            slideLoadingFragment();

            new SetStatistics().execute(questions);

            Toast.makeText(getApplicationContext(), "Quiz ended. Right answers: " + countRightAnswers() + "/" + questions.size() + ".", Toast.LENGTH_LONG).show();
        }
    }

    private void onQuestionsLoaded(List<Question> loadedQuestions) {
        if (loadedQuestions == null) {
            //insertLoadingFragment();
            Toast.makeText(getApplicationContext(), "No questions", Toast.LENGTH_LONG).show();
            return;
        }
        this.questions = loadedQuestions;
        currentQuestionIndex = 0;
        goToQuestion(currentQuestionIndex);
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
        currentImageDownload.setImage(image);
        if (currentImageDownloadIndex == currentQuestionIndex) {
            insertQuestionFragment();
        }
        currentImageDownloadIndex++;
        loadImage(currentImageDownloadIndex);
    }

    private void insertLoadingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LoadingFragment loadingFragment = new LoadingFragment();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, loadingFragment);
        fragmentTransaction.commit();
    }

    private void slideLoadingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LoadingFragment loadingFragment = new LoadingFragment();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.fragment_container, loadingFragment);
        fragmentTransaction.commit();
    }

    private void insertQuestionFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        QuestionFragment questionFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", currentQuestion);
        questionFragment.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, questionFragment);
        fragmentTransaction.commit();
    }

    private void slideQuestionFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        QuestionFragment questionFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", currentQuestion);
        questionFragment.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.fragment_container, questionFragment);
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
            if ((Math.abs(velocityX) > Math.abs(velocityY)) && velocityX < 0)
                if (currentQuestionAnswered) {
                    showNextQuestion();
                }
            return true;
        }
    }

}
