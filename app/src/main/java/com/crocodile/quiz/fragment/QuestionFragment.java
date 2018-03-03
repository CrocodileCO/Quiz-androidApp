package com.crocodile.quiz.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crocodile.quiz.R;
import com.crocodile.quiz.activity.QuestionAbout;
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
   // View informationButton;
    FloatingActionButton informationButton ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.question_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity act = getActivity();
        button1 =  act.findViewById(R.id.button1);
        button2 =  act.findViewById(R.id.button2);
        button3 =  act.findViewById(R.id.button3);
        button4 =  act.findViewById(R.id.button4);
        buttons = new ArrayList<>();
        buttons.add(new ButtonContainer(button1));
        buttons.add(new ButtonContainer(button2));
        buttons.add(new ButtonContainer(button3));
        buttons.add(new ButtonContainer(button4));
        imageView = act.findViewById(R.id.imageViewQuestion);
        container = act.findViewById(R.id.containerQuestion);
        //informationButton = act.findViewById(R.id.info_button);
        informationButton = (FloatingActionButton) act.findViewById(R.id.fab);
        informationButton.hide();


        question = (Question) getArguments().getSerializable("question");

        setupQuestion();
        if (question.isAnswered()) {
            colorButtons(question.getShuffledPlayerAnswerIndex());
            setupInformationButton();
        }

    }

    /*@Override
    public void onResume() {
        super.onResume();
        if ((question.isAnswered()) && (question.getInformation().hasSomethingToShow())) {
            setupInformationButton();
        }
    }*/

    public void hideInformationButton() {
        informationButton.setVisibility(View.INVISIBLE);
       // informationButton.hide();
    }

    private void setupQuestion() {
        buttons.get(0).setText("kek");
        answers = question.getShuffledAnswers();
        for (ButtonContainer button: buttons) {
            button.setText(answers.get(buttons.indexOf(button)).getText());
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
                    if (!question.isAnswered()) {
                        question.setPlayerAnswerIndex(index);
                        colorButtons(index);
                       setupInformationButton(); //why it here ?

                        ((QuestionActivity) getActivity()).setQuestionAnswered();
                    } else {
                        for (ButtonContainer button : buttons) {
                            button.switchText();
                        }
                    }
                }
            });
        }

        container.setVisibility(View.VISIBLE);
    }

    private void colorButtons(int playerAnswerIndex) {
        ButtonContainer bc = buttons.get(playerAnswerIndex);
        if (!(bc == rightButton)) {
            bc.getButton().setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_false));
        }

        rightButton.getButton().setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_true));

    }

    private void setupInformationButton() {
        if (question.getInformation().hasSomethingToShow()) {
            //informationButton.setVisibility(View.VISIBLE);
            informationButton.show();
            informationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInformation();
                }
            });
        }
    }

    private void showInformation() {
        Intent intent = new Intent(getContext(), QuestionAbout.class);
        intent.putExtra("about", question.getInformation().getText());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    /*@Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (question.isAnswered()) {
                    setupInformationButton();
                    Log.d("qqqqq",""+question.isAnswered()+question.getAnswers().get(0).getText());

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return anim;
    }*/

    private class ButtonContainer {
        private Button button;
        private ButtonState state;

        public ButtonContainer(Button button) {
            this.button = button;
            state = ButtonState.ANSWER;
        }


        public Button getButton() {
            return button;
        }


        public void setText(String text) {
            button.setText(text);
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
