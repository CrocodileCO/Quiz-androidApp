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
    @SerializedName("answer1")
    @Expose
    private String answer1;
    @SerializedName("answer2")
    @Expose
    private String answer2;
    @SerializedName("answer3")
    @Expose
    private String answer3;
    @SerializedName("answer4")
    @Expose
    private String answer4;
    @SerializedName("topicId")
    @Expose
    private String topicId;
    @SerializedName("answer1_num")
    @Expose
    private int answer1_num;
    @SerializedName("answer2_num")
    @Expose
    private int answer2_num;
    @SerializedName("answer3_num")
    @Expose
    private int answer3_num;
    @SerializedName("answer4_num")
    @Expose
    private int answer4_num;

    @SerializedName("answers")
    @Expose
    private ArrayList<Answer> answers;


    private ArrayList<Answer> shuffledAnswers;
    private int shuffledRightAnswerIndex;
    private int playerAnswerIndex;
    private boolean playerAnsweredRight;
    private ProxyBitmap image;

    public void setup() {
        /*answers = new ArrayList<Answer>();
        answers.add(new Answer(answer1, answer1_num));
        answers.add(new Answer(answer2, answer2_num));
        answers.add(new Answer(answer3, answer3_num));
        answers.add(new Answer(answer4, answer4_num));*/


        shuffledAnswers = new ArrayList<>(getAnswers());
        Collections.shuffle(shuffledAnswers);

        for (Answer ans : shuffledAnswers) {
            if (ans.equals(getRightAnswer())) {
                shuffledRightAnswerIndex = shuffledAnswers.indexOf(ans);
            }
        }
    }

    public void setPlayerAnswerIndex(int index) {
        playerAnswerIndex = index;
        if (playerAnswerIndex == shuffledRightAnswerIndex) {
            playerAnsweredRight = true;
        } else {
            playerAnsweredRight = false;
        }
    }

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

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setAnswer1_num(int answer1_num) { this.answer1_num = answer1_num; }

    public int getAnswer1_num() { return answer1_num; }

    public void setAnswer2_num(int answer2_num) { this.answer2_num = answer2_num; }

    public int getAnswer2_num() {
        return answer2_num;
    }

    public void setAnswer3_num(int answer3_num) {
        this.answer3_num = answer3_num;
    }

    public int getAnswer3_num() {
        return answer3_num;
    }

    public int getAnswer4_num() {
        return answer4_num;
    }

    public void setAnswer4_num(int answer4_num) {
        this.answer4_num = answer4_num;
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
