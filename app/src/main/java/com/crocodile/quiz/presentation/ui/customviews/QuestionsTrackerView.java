package com.crocodile.quiz.presentation.ui.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.crocodile.quiz.R;
import com.crocodile.quiz.model.Question;

import java.util.List;


public class QuestionsTrackerView extends View {

    private RelativeLayout mFrame;
    private Paint paint;

    private int amount;
    private float width;
    private float height;
    private float radius;
    private float sideMargin;
    private float distance;

    private Bitmap ring;
    private Bitmap ringResized;

    private int currentQuestionIndex;

    private int animationTime;
    private int sleepTime;
    private float offset;

    private List<Question> questions;

    public QuestionsTrackerView(Context context, RelativeLayout frame, List<Question> questions) {
        super(context);

        this.questions = questions;
        this.amount = questions.size();
        mFrame = frame;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        currentQuestionIndex = 0;
        animationTime = 400;
        sleepTime = 30;
        offset = 0;

        ring = BitmapFactory.decodeResource(getResources(), R.drawable.ring);

        setParameters(getWidth(), getHeight());
    }

    public void setCurrentQuestion( int index ) {
        if (index != currentQuestionIndex) {
            offset = (index > currentQuestionIndex) ? -1 : 1;
            currentQuestionIndex = index;
            new Thread(new Runnable() {
                private volatile boolean running = true;

                @Override
                public void run() {
                    while (running) {
                        if (reduceOffset()) {
                            running = false;
                        }
                        postInvalidate();
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public boolean reduceOffset() {
        float offsetMul = (offset < 0) ? -1 : 1;
        offset = offsetMul * Math.max((Math.abs(offset) - ((float) sleepTime)/ ((float) animationTime)),0);
        return (offset == 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setParameters((float) w, (float) h);
    }

    private void setParameters(float width, float height) {
        this.width = width;
        this.height = height;
        radius = height/8;
        sideMargin = height/2;
        distance = (width - sideMargin * 2 - height) / amount;
        if (radius > 0) {
            ringResized = Bitmap.createScaledBitmap(ring, (int) (radius * 4), (int) (radius * 4), false);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (ringResized != null) {
            canvas.drawBitmap(ringResized, sideMargin + height + currentQuestionIndex * distance - ringResized.getWidth()/2 + offset * distance, (height  - ringResized.getHeight()) / 2, paint);
        }

        for (int i = 0; i < amount + 1; i++) {
            if (i < amount) {
                Question question = questions.get(i);
                if (question.isAnswered()) {
                    if (question.isPlayersAnswerRight()) {
                        paint.setColor(ContextCompat.getColor(getContext(),R.color.colorRightAnswer));
                    } else {
                        paint.setColor(ContextCompat.getColor(getContext(),R.color.colorWrongAnswer));
                    }
                } else {
                    paint.setColor(Color.WHITE);
                }
            } else {
                paint.setColor(ContextCompat.getColor(getContext(),R.color.colorYellow));
            }
            canvas.drawCircle( sideMargin + height  + i*distance, height / 2, radius, paint);
        }

    }
}
