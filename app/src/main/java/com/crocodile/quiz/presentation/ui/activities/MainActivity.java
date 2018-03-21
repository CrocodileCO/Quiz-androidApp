package com.crocodile.quiz.presentation.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.crocodile.quiz.R;
import com.crocodile.quiz.domain.executor.impl.ThreadExecutor;
import com.crocodile.quiz.domain.repository.Impl.TopicsRepositoryImpl;
import com.crocodile.quiz.presentation.presenters.MainPresenter;
import com.crocodile.quiz.presentation.presenters.impl.MainPresenterImpl;
import com.crocodile.quiz.presentation.ui.adapters.GroupAdapter;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.model.Group;
import com.crocodile.quiz.network.rest.ApiClient;
import com.crocodile.quiz.network.rest.ServerInterface;
import com.crocodile.quiz.network.rest.ServiceGenerator;
import com.crocodile.quiz.threading.MainThreadImpl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crocodile.quiz.network.rest.ApiClient.BASE_URL;

public class MainActivity extends AppCompatActivity implements MainPresenter.View{

    private RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainPresenterImpl mMainPresenter;

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

        init();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(getApplicationContext()).questionDao().nukeTable();
                Log.d("lol", "db nuked");
                Log.d("lol", AppDatabase.getAppDatabase(getApplicationContext()).questionDao().getAll().size() + "");
            }
        }).start();*/
    }

    private void init() {

        mAdapter = new GroupAdapter(R.layout.group_fragment, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mMainPresenter = new MainPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new TopicsRepositoryImpl(this)
        );

    }

    @Override
    protected void onResume() {
        super.onResume();

        mMainPresenter.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }


    @Override
    public void showTopics(List<Group> groups) {
        mAdapter.addNewGroups(groups);
    }

    @Override
    public void test() {
        Toast.makeText(getApplicationContext(), "lol", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

    }
}
