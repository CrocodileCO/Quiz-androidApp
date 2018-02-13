package com.crocodile.quiz.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.crocodile.quiz.R;
import com.crocodile.quiz.adapter.MenuAdapter;
import com.crocodile.quiz.helper.DownloadHelper;
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

public class MainActivity extends AppCompatActivity implements DownloadHelper.OnImageDownloadListener{

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


        Call<List<Topic>> call = apiService.getTopics();

        call.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                int statusCode = response.code();

                List<Topic> topics = response.body();

                mRecyclerView.setAdapter(new MenuAdapter(topics, R.layout.menu_item, getApplicationContext()));

            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Log.e("lol", t.toString());

            }
        });


    }

    private String[] getDataSet() {

        String[] mDataSet = new String[10];
        for (int i = 0; i < 10; i++) {
            mDataSet[i] = "item" + i;
        }
        return mDataSet;
    }

    @Override
    public void onImageDownloaded(Bitmap image) {

    }
}
