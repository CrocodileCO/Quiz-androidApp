package com.crocodile.quiz.domain.repository.Impl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.crocodile.quiz.domain.repository.QuestionsRepository;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.network.rest.ApiClient;
import com.crocodile.quiz.network.rest.ServerInterface;
import com.crocodile.quiz.network.rest.ServiceGenerator;
import com.crocodile.quiz.presentation.ui.activities.QuestionActivity;
import com.crocodile.quiz.storage.database.AnsweredQuestion;
import com.crocodile.quiz.storage.database.AnsweredQuestionDao;
import com.crocodile.quiz.storage.database.AppDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.network.rest.ApiClient.BASE_URL;


public class QuestionsRepositoryImpl implements QuestionsRepository {

    private Context mContext;
    private Callback callback;
    private List<Question> lastLoadedQuestions;
    private List<Question> questions;
    private int questionLimit;

    public QuestionsRepositoryImpl(Context context) {
        mContext = context;
    }

    @Override
    public void getNewQuestionSet(final Callback callback, String topicId) {

        questionLimit = 10;
        questions = new ArrayList<>();
        this.callback = callback;

        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<List<Question>> call = apiService.getAllQuestions(topicId);

        call.enqueue(new retrofit2.Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                int statusCode = response.code();
                if (this != null) {
                    onQuestionsLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("lol", t.toString());
            }
        });
    }

    private void onQuestionsLoaded(List<Question> loadedQuestions) {

        if (loadedQuestions == null) {
            return;
        }
        Log.d("lol", "Loaded " + loadedQuestions.size() + " questions");
        lastLoadedQuestions = loadedQuestions;
        Collections.shuffle(lastLoadedQuestions);
        if (loadedQuestions.size() < questionLimit) {questionLimit = loadedQuestions.size();}

        new CheckQuestionsTask().execute(lastLoadedQuestions);

    }

    private void onQuestionsChecked(List<Question> newQuestions) {
        if (newQuestions.size() == 0) {
            fillQuestionsFrom(lastLoadedQuestions);
            Log.d("lol","no new questions");
        } else {
            fillQuestionsFrom(newQuestions);
            Log.d("lol", newQuestions.size() + " new questions");
            if (questions.size()<questionLimit) {
                Log.d("lol", "not enough new questions, filling with any");
                fillQuestionsFrom(lastLoadedQuestions);
            }
        }
        Log.d("lol", "enough questions. Starting");
        callback.onQuestionsLoaded(questions);
    }

    private void fillQuestionsFrom(List<Question> list) {
        for (int i = 0; (questions.size() < questionLimit) && (i < list.size()); i++) {
            if (!alreadyExists(list.get(i))) {
                questions.add(list.get(i));
            }
        }
    }

    public class CheckQuestionsTask extends AsyncTask<List<Question> , Void, List<Question>> {
        DownloadHelper.OnImageDownloadListener listener;


        public CheckQuestionsTask() { }

        protected List<Question> doInBackground(List<Question>... list) {
            List<Question> newQuestions = new ArrayList<>();
            List<Question> questions = list[0];
            AnsweredQuestionDao aqd = AppDatabase.getAppDatabase(mContext).questionDao();
            for (Question question: questions) {
                AnsweredQuestion q = aqd.findById(question.get_id());
                if ((q == null) && !alreadyExists(question)) {
                    newQuestions.add(question);
                }
                if (newQuestions.size() >= questionLimit) {break;}
            }
            return newQuestions;
        }

        protected void onPostExecute(List<Question> result) {
            onQuestionsChecked(result);
        }
    }

    private boolean alreadyExists(Question question) {
        for (Question q: questions) {
            if (q.get_id().equals(question.get_id())) {
                return true;
            }
        }
        return false;
    }
}
