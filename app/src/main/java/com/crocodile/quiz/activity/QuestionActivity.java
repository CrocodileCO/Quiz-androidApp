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

import com.crocodile.quiz.R;
import com.crocodile.quiz.fragment.LoadingFragment;
import com.crocodile.quiz.fragment.QuestionFragment;
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


public class QuestionActivity extends AppCompatActivity {

    Question currentQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);

        insertLoadingFragment();

        loadItems();

    }

    private void insertLoadingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LoadingFragment fragment = new LoadingFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
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

    private void onQuestionsLoaded(List<Question> questions) {
        currentQuestion = questions.get(0);
        currentQuestion.setup();

        new DownloadImageTask(this).execute(currentQuestion.getImageUrl());
    }

    private void onImageDownloaded(Bitmap image) {
        currentQuestion.setImage(image);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        QuestionFragment fragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", currentQuestion);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        QuestionActivity activity;


        private DownloadImageTask(QuestionActivity activity) {
            this.activity = activity;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            activity.onImageDownloaded(result);
        }
    }


}
