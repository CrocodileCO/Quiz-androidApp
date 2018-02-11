package com.crocodile.quiz.model;

import java.util.ArrayList;

/**
 * Created by vshir on 11.02.2018.
 */

public class TracerUserQuest {
    ArrayList<String> answerMass;
    ArrayList<Boolean> answerBooleanMass;

    public TracerUserQuest() {
        answerMass = new ArrayList<String>();
        answerBooleanMass = new ArrayList<Boolean>();

    }

    public void setAnswer(String answer, Boolean right) {
        answerMass.add(answer);
        answerBooleanMass.add(right);
    }

    public ArrayList<Boolean> getAnswerBooleanMass() {
        return answerBooleanMass;
    }

    public ArrayList<String> getAnswerMass() {
        return answerMass;
    }
}
