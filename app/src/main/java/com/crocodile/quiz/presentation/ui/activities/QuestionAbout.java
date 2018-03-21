package com.crocodile.quiz.presentation.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.crocodile.quiz.R;

/**
 * Created by vshir on 21.02.2018.
 */

public class QuestionAbout extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question_about);

        Intent intent = getIntent();
        String about = intent.getStringExtra("about");

        textView = findViewById(R.id.textView_about_content);
        textView.setText(about);
    }
}
