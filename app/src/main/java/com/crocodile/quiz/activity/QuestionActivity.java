package com.crocodile.quiz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crocodile.quiz.R;
import com.crocodile.quiz.fragment.LoadingFragment;
import com.crocodile.quiz.fragment.QuestionFragment;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;

import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.rest.ApiClient.BASE_URL;


public class QuestionActivity extends AppCompatActivity implements DownloadHelper.OnImageDownloadListener{

    List<Question> questions;
    Question currentQuestion;
    int currentQuestionIndex;
    LoadingFragment loadingFragment;
    QuestionFragment questionFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);

        loadingFragment = new LoadingFragment();

        insertLoadingFragment();

        loadItems();

    }

    public void showNextQuestion() {
        insertLoadingFragment();
        currentQuestionIndex++;
        loadQuestion(currentQuestionIndex);
    }

    private void insertLoadingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, loadingFragment);
        fragmentTransaction.commit();
    }


    private void loadItems() {
        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<List<Question>> call = apiService.getQuestions();

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

    private void loadQuestion(int index) {
        if (index < questions.size()) {
            currentQuestion = questions.get(index);
            currentQuestion.setup();

            DownloadHelper.downloadImage(currentQuestion.getImageUrl(), this);
        } else {
            Toast.makeText(getApplicationContext(), "Quiz ended. Right answers: " + countRightAnswers() + "/" + questions.size() + ".", Toast.LENGTH_LONG).show();
        }
    }

    private void onQuestionsLoaded(List<Question> loadedQuestions) {
        this.questions = loadedQuestions;
        questionFragment = new QuestionFragment();
        currentQuestionIndex = 0;
        loadQuestion(currentQuestionIndex);
    }

    public void onImageDownloaded(Bitmap image) {
        currentQuestion.setImage(image);
        insertQuestionFragment();
    }

    private void insertQuestionFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putSerializable("question", currentQuestion);
        questionFragment.setArguments(bundle);
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

}
