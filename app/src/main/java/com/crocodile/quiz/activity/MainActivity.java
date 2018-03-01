package com.crocodile.quiz.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

import com.crocodile.quiz.R;
import com.crocodile.quiz.adapter.GroupAdapter;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.model.Group;
import com.crocodile.quiz.rest.ApiClient;
import com.crocodile.quiz.rest.ServerInterface;
import com.crocodile.quiz.rest.ServiceGenerator;

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

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadItems();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(getApplicationContext()).questionDao().nukeTable();
                Log.d("lol", "db nuked");
                Log.d("lol", AppDatabase.getAppDatabase(getApplicationContext()).questionDao().getAll().size() + "");
            }
        }).start();*/
    }

    private void loadItems() {

        ServerInterface apiService = ServiceGenerator.createService(ServerInterface.class, BASE_URL);
        ApiClient.getClient().create(ServerInterface.class);


        Call<List<Group>> call = apiService.getGroups();

        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                int statusCode = response.code();

                List<Group> groups =response.body();
                //List<Topic> topics = response.body();
                mRecyclerView.setAdapter(new GroupAdapter(groups, R.layout.group_fragment, getApplicationContext()));

            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("lol", t.toString());

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public void onImageDownloaded(Bitmap image) {

    }
}
