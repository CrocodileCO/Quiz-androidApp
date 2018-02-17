package com.crocodile.quiz.helper;

import android.os.AsyncTask;
import android.util.Log;

import com.crocodile.quiz.model.Question;
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.rest.ApiClient.BASE_URL;

/**
 * Created by vshir on 17.02.2018.
 */

public class SetStatistics extends AsyncTask<List<Question>,Void,Void>{

    @Override
    protected Void doInBackground(List<Question>... questions) {

        for (Question question:questions[0]) {
            ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
            ApiClient.getClient().create(ServerInterface.class);


            Call<Void> call = apiService.setStatistics(question.get_id(),question.getPlayerAnswerIndex()+1);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("setStatistic","sucsess");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("setStatistic","kek");
                }
            });
        }
        return null;
    }

}
