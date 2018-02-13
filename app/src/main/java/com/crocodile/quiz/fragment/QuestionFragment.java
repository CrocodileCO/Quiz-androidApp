package com.crocodile.quiz.fragment;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crocodile.quiz.R;
import com.crocodile.quiz.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    List<Button> buttons;
    Button rightButton;
    Question question;
    ImageView imageView;
    List<String> answers;
    RelativeLayout container;
    Boolean answered = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.question_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button1 =  getActivity().findViewById(R.id.button1);
        button2 =  getActivity().findViewById(R.id.button2);
        button3 =  getActivity().findViewById(R.id.button3);
        button4 =  getActivity().findViewById(R.id.button4);
        buttons = new ArrayList<>();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        imageView = getActivity().findViewById(R.id.imageViewQuestion);
        container = getActivity().findViewById(R.id.containerQuestion);

        question = (Question) getArguments().getSerializable("question");

        setupQuestion();

    }

    private void setupQuestion() {
        answers = question.getShuffledAnswers();
        for (Button button: buttons) {
            button.setText(answers.get(buttons.indexOf(button)));
        }
        rightButton = buttons.get(question.getShuffledRightAnswerIndex());

        imageView.setImageBitmap(question.getImage());

        for (Button button: buttons) {
            final Button btn = button;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!answered) {
                        question.setPlayerAnswerIndex(buttons.indexOf(btn));
                        if (!(btn == rightButton)) {
                            btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_false));
                        }

                        rightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_true));
                        answered = true;
                    }
                }
            });
        }

        container.setVisibility(View.VISIBLE);
    }
}
