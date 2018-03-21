package com.crocodile.quiz.domain.repository.Impl;


import android.content.Context;
import android.util.Log;

import com.crocodile.quiz.R;
import com.crocodile.quiz.domain.repository.TopicsRepository;
import com.crocodile.quiz.model.Group;
import com.crocodile.quiz.network.rest.ApiClient;
import com.crocodile.quiz.network.rest.ServerInterface;
import com.crocodile.quiz.network.rest.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.network.rest.ApiClient.BASE_URL;

public class TopicsRepositoryImpl implements TopicsRepository{

    private Context mContext;

    public TopicsRepositoryImpl(Context context) {
        mContext = context;
    }

    @Override
    public void getTopics(final Callback callback) {
        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<List<Group>> call = apiService.getGroups();

        call.enqueue(new retrofit2.Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                int statusCode = response.code();

                List<Group> groups =response.body();

                callback.onTopicsLoaded(groups);
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("lol", t.toString());

            }
        });
    }
}
