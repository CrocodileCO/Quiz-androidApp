package com.crocodile.quiz.model;

import android.graphics.Bitmap;

import com.crocodile.quiz.helper.ProxyBitmap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Question implements Serializable{

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("topicId")
    @Expose
    private String topicId;
    @SerializedName("answers")
    @Expose
    private ArrayList<Answer> answers;
    @SerializedName("information")
    @Expose
    private QuestionInformation information;


    private ArrayList<Answer> shuffledAnswers;
    private int shuffledRightAnswerIndex;
    private int playerAnswerIndex;
    private boolean playerAnsweredRight;
    private boolean playerAnswered;
    private ProxyBitmap image;

    public void setup() {
        playerAnswered = false;
        shuffledAnswers = new ArrayList<>(getAnswers());
        Collections.shuffle(shuffledAnswers);

        for (Answer ans : shuffledAnswers) {
            if (ans.equals(getRightAnswer())) {
                shuffledRightAnswerIndex = shuffledAnswers.indexOf(ans);
            }
        }
    }

    public void setPlayerAnswerIndex(int index) {
        playerAnswered = true;
        playerAnswerIndex = index;
        if (playerAnswerIndex == shuffledRightAnswerIndex) {
            playerAnsweredRight = true;
        } else {
            playerAnsweredRight = false;
        }
    }

    public boolean isAnswered() { return playerAnswered; }

    public boolean isPlayersAnswerRight() { return playerAnsweredRight; }

    public Bitmap getImage() {
        if (image != null) {
            return image.getBitmap();
        } else {
            return null;
        }
    }

    public void setImage(Bitmap image) {
        this.image = new ProxyBitmap(image);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public QuestionInformation getInformation() {
        return information;
    }

    public void setInformation(QuestionInformation information) {
        this.information = information;
    }

    public Answer getRightAnswer() { return answers.get(0); }

    public ArrayList<Answer> getAnswers() {return answers;}

    public ArrayList<Answer> getShuffledAnswers() {return shuffledAnswers;}

    public int getShuffledRightAnswerIndex() { return shuffledRightAnswerIndex;}

    public int getShuffledPlayerAnswerIndex() { return playerAnswerIndex;}

    public int getPlayerAnswerIndex() {

        for (Answer ans : answers) {
            if (ans.equals(shuffledAnswers.get(playerAnswerIndex))) {
                return answers.indexOf(ans);
            }
        }

        return -1;
    }

    public int getShuffledAnswerPercentage(int index) {
        int sum = getAnswersNumSum();
        return (sum > 0) ?  (int) Math.round(((double) shuffledAnswers.get(index).getPickAmount() / (double) sum) * 100) : 0;
    }

    public int getAnswersNumSum() {
        int sum = 0;
        for (Answer answer: answers) {
            sum += answer.getPickAmount();
        }
        return sum;
    }

}
