package com.crocodile.quiz.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crocodile.quiz.R;
import com.crocodile.quiz.adapter.MenuAdapter;
import com.crocodile.quiz.model.Topic;
import com.crocodile.quiz.model.TopicsResponse;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        ArrayList<Topic> topics = new ArrayList<Topic>();
        topics.add(new Topic(0, "Science", "https://i.imgur.com/EagZoEp.png"));
        topics.add(new Topic(1, "Art", "https://i.imgur.com/EagZoEp.png"));
        mRecyclerView.setAdapter(new MenuAdapter(topics, R.layout.menu_item, getApplicationContext()));

    }

    private String[] getDataSet() {

        String[] mDataSet = new String[10];
        for (int i = 0; i < 10; i++) {
            mDataSet[i] = "item" + i;
        }
        return mDataSet;
    }
}
