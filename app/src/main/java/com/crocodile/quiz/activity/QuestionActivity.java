package com.crocodile.quiz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crocodile.quiz.R;
import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.model.QuestionsResponse;
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;

import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.rest.ApiClient.BASE_URL;

/**
 * Created by vshir on 01.02.2018.
 */

public class QuestionActivity extends AppCompatActivity {

    List<Question> questions;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    ImageView imageView;
    RelativeLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);

        button1 =  findViewById(R.id.button1);
        button2 =  findViewById(R.id.button2);
        button3 =  findViewById(R.id.button3);
        button4 =  findViewById(R.id.button4);
        imageView = findViewById(R.id.imageViewQuestion);
        container = findViewById(R.id.containerQuestion);

        loadItems();

    }


    private void loadItems() {
        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<QuestionsResponse> call = apiService.getQuestions();

        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                int statusCode = response.code();
                questions = response.body().getQuestions();

                Question qu =questions.get(0);
                button1.setText(qu.getAnswer1());
                button2.setText(qu.getAnswer2());
                button3.setText(qu.getAnswer3());
                button4.setText(qu.getAnswer4());
                //imageView.setImageURI(Uri.parse(qu.getImgUrl()));
                new DownloadImageTask(imageView).execute(qu.getImgUrl());


            }

            @Override
            public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                Log.e("lol", t.toString());
            }
        });
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;


        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                //Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            container.setVisibility(View.VISIBLE);
        }
    }

}
