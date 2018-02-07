package com.crocodile.quiz.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.crocodile.quiz.R;
import com.crocodile.quiz.adapter.MenuAdapter;
import com.crocodile.quiz.model.Topic;
import com.crocodile.quiz.model.TopicsResponse;
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.rest.ApiClient.BASE_URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        String[] myDataset = getDataSet();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadItems();

    }

    private void loadItems() {

        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<TopicsResponse> call = apiService.getTopics();

        call.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                //код ответа сервера (200 - ОК), в данном случае далее не используется
                int statusCode = response.code();
                //получаем список фильмов, произведя парсинг JSON ответа с помощью библиотеки Retrofit
                List<Topic> topics = response.body().getTopics();

                mRecyclerView.setAdapter(new MenuAdapter(topics, R.layout.menu_item, getApplicationContext()));
                //Toast.makeText(getBaseContext(), topics.get(0).getTitle(), Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {
                //onFailure вызывается, когда проблема при отправке запроса. Например, сервер не отвечает или нет сети.
                //Заносим сведения об ошибке в журнал методом Log.e(TAG, MESSAGE)
                //Данный метод используется для журнализации ошибок (e = error)
                Log.e("lol", t.toString());

            }
        });

        /*ArrayList<Topic> topics = new ArrayList<Topic>();
        topics.add(new Topic(0, "Science", "https://i.imgur.com/EagZoEp.png"));
        topics.add(new Topic(1, "Art", "https://i.imgur.com/EagZoEp.png"));*/


    }

    private String[] getDataSet() {

        String[] mDataSet = new String[10];
        for (int i = 0; i < 10; i++) {
            mDataSet[i] = "item" + i;
        }
        return mDataSet;
    }
}
