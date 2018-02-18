package com.crocodile.quiz.fragment;



import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.crocodile.quiz.R;
import com.crocodile.quiz.activity.QuestionActivity;
import com.crocodile.quiz.model.Answer;
import com.crocodile.quiz.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    List<ButtonContainer> buttons;
    ButtonContainer rightButton;
    Question question;
    ImageView imageView;
    List<Answer> answers;
    RelativeLayout container;
    Boolean answered;
    TextSwitcher textSwitcher;

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
        buttons.add(new ButtonContainer(button1, (TextSwitcher) getActivity().findViewById(R.id.textSwitcher1)));
        buttons.add(new ButtonContainer(button2, (TextSwitcher) getActivity().findViewById(R.id.textSwitcher2)));
        buttons.add(new ButtonContainer(button3, (TextSwitcher) getActivity().findViewById(R.id.textSwitcher3)));
        buttons.add(new ButtonContainer(button4, (TextSwitcher) getActivity().findViewById(R.id.textSwitcher4)));
        imageView = getActivity().findViewById(R.id.imageViewQuestion);
        container = getActivity().findViewById(R.id.containerQuestion);
        for (ButtonContainer bc : buttons) {
            textSwitcher = bc.getTextSwitcher();
            textSwitcher.setInAnimation(getContext(), android.R.anim.fade_in);
            textSwitcher.setOutAnimation(getContext(), android.R.anim.fade_out);
            textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    TextView view = new TextView(getContext());
                    view.setTextColor(Color.WHITE);
                    view.setGravity(Gravity.CENTER);
                    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    view.setAllCaps(true);
                    return view;
                }
            });
        }


        answered = false;
        question = (Question) getArguments().getSerializable("question");

        setupQuestion();

    }

    private void setupQuestion() {
        buttons.get(0).setText("kek");
        answers = question.getShuffledAnswers();
        for (ButtonContainer button: buttons) {
            button.setCurrentText(answers.get(buttons.indexOf(button)).getText());
        }

        rightButton = buttons.get(question.getShuffledRightAnswerIndex());

        imageView.setImageBitmap(question.getImage());

        for (final ButtonContainer button: buttons) {
            final Button btn = button.getButton();
            final ButtonContainer bc = button;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = buttons.indexOf(bc);
                    if (!answered) {
                        question.setPlayerAnswerIndex(index);
                        if (!(bc == rightButton)) {
                            btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_false));
                        }

                        rightButton.getButton().setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_true));
                        answered = true;
                        ((QuestionActivity) getActivity()).setQuestionAnswered();
                    } else {
                        bc.switchText();
                    }
                }
            });
        }

        /*container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answered) {
                    showNextQuestion();
                }
            }
        });*/

        container.setVisibility(View.VISIBLE);
    }

    /*private void showNextQuestion() {
        ((QuestionActivity) getActivity()).showNextQuestion();
    }*/

    private class ButtonContainer {
        private Button button;
        private TextSwitcher textSwitcher;
        private ButtonState state;

        public ButtonContainer(Button button, TextSwitcher textSwitcher) {
            this.button = button;
            this.textSwitcher = textSwitcher;
            state = ButtonState.ANSWER;
        }

        public TextSwitcher getTextSwitcher() {
            return textSwitcher;
        }

        public Button getButton() {
            return button;
        }

        public void setCurrentText(String text) {

            //textSwitcher.setCurrentText(text);
            setText(text);
        }

        public void setText(String text) {

            button.setText(text);
            //textSwitcher.setText(text);
        }

        public void switchText() {
            switch (state) {
                case ANSWER:
                    switchToPercentage();
                    break;
                case PERCENTAGE:
                    switchToAnswer();
                    break;
            }
        }

        public void switchToAnswer() {
            if (state != ButtonState.ANSWER) {
                setText(answers.get(buttons.indexOf(this)).getText());
                state = ButtonState.ANSWER;
            }
        }

        public void switchToPercentage() {
            if (state != ButtonState.PERCENTAGE) {
                setText(question.getShuffledAnswerPercentage(buttons.indexOf(this)) + "%");
                state = ButtonState.PERCENTAGE;
            }
        }


    }

    private enum ButtonState { ANSWER, PERCENTAGE}
}
