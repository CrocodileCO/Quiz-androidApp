package com.crocodile.quiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crocodile.quiz.R;

/**
 * Created by vshir on 01.02.2018.
 */

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);
    }
}
