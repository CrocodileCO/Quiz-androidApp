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
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.rest.ApiClient.BASE_URL;


public class QuestionActivity extends AppCompatActivity {

    List<Question> questions;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button rightButton;
    Question qu;
    ImageView imageView;
    String[] currentMass;
    RelativeLayout container;
    Boolean getAnswer = false;

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

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAnswer&&!(qu.getAnswer1().equals(currentMass[0]))) {
                    button1.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_false));
                }

                rightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_true));
                getAnswer = true;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAnswer&&!(qu.getAnswer1().equals(currentMass[1]))) {
                    button2.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_false));
                }

                rightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_true));
                getAnswer = true;
            }

        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAnswer&&!(qu.getAnswer1().equals(currentMass[1]))) {
                    button3.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_false));
                }

                rightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_true));
                getAnswer = true;
            }

        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAnswer&&!(qu.getAnswer1().equals(currentMass[1]))) {
                    button4.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_false));
                }

                rightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_true));
                getAnswer = true;
            }
        });

        loadItems();



    }


    private void loadItems() {
        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<List<Question>> call = apiService.getQuestions();

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                int statusCode = response.code();
                questions = response.body();

                qu =questions.get(0);
                currentMass= new String[]{qu.getAnswer1(), qu.getAnswer2(), qu.getAnswer3(), qu.getAnswer4()};
                shuffleArray(currentMass);
                button1.setText(currentMass[0]);
                if (currentMass[0].equals(qu.getAnswer1())){rightButton=button1;}
                button2.setText(currentMass[1]);
                if (currentMass[1].equals(qu.getAnswer1())){rightButton=button2;}
                button3.setText(currentMass[2]);
                if (currentMass[2].equals(qu.getAnswer1())){rightButton=button3;}
                button4.setText(currentMass[3]);
                if (currentMass[3].equals(qu.getAnswer1())){rightButton=button4;}
                //imageView.setImageURI(Uri.parse(qu.getImgUrl()));
                new DownloadImageTask(imageView).execute(qu.getImageUrl());


            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("lol", t.toString());
            }
        });
    }





    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;


        private DownloadImageTask(ImageView bmImage) {
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

    private static void shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}
