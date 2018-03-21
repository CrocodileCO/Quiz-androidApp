package com.crocodile.quiz.presentation.ui.fragments;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crocodile.quiz.R;
import com.crocodile.quiz.presentation.ui.activities.QuestionActivity;


public class ResultFragment extends Fragment {

    private Button backButton;
    private TextView resultTextView;
    private ClipDrawable mImageDrawable;
    private ImageView imageView;
    private Handler mHandler;
    private Runnable animateImage;
    private int rightAnswers;
    private int totalAnswers;
    private int level;
    private int targetLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.test_result_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backButton = getActivity().findViewById(R.id.button_result_back);
        resultTextView = getActivity().findViewById(R.id.result_text_view);
        imageView = getActivity().findViewById(R.id.result_image_inside);

        rightAnswers = getArguments().getInt("rightAnswers");
        totalAnswers = getArguments().getInt("totalAnswers");
        resultTextView.setText(rightAnswers + "/" + totalAnswers);

        mHandler = new Handler();
        mImageDrawable = (ClipDrawable) imageView.getDrawable();
        level = 0;
        targetLevel = (int)(((double) rightAnswers / (double) totalAnswers ) * 10000D);
        mImageDrawable.setLevel(level);
        animateImage = new Runnable() {

            @Override
            public void run() {
                addClip();
            }
        };
        mHandler.post(animateImage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((QuestionActivity)getActivity()).ExitTest();
            }
        });
    }

    private void addClip() {
        if (level < targetLevel) {
            level += 100;
            mImageDrawable.setLevel(level);
            mHandler.postDelayed(animateImage, 25);
        } else {
            mHandler.removeCallbacks(animateImage);
        }
    }
}
