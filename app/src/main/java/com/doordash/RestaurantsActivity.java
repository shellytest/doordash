package com.doordash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.doordash.R;
import com.doordash.api.IDoorDashService;
import com.doordash.datamodel.RestaurantInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.doordash.api.RestaurantResource;
import com.doordash.event.EventBus;
import com.doordash.event.RestaurantInfoListEvent;
import com.doordash.event.RestaurantInfoChangeEvent;

import com.squareup.otto.Subscribe;

public class RestaurantsActivity extends AppCompatActivity {
    private static String TAG = RestaurantsActivity.class.getName();

    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.rview) RecyclerView mRecyclerView;

    private RestaurantsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        RecyclerView recylerView = (RecyclerView) findViewById(R.id.rview);
        ButterKnife.bind(this);
        mAdapter= new RestaurantsAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
        new RestaurantResource().asyncGetData(getApplicationContext(), 37.422740f, -122.139956f);
    }

    @Override
    protected void onStop() {
        EventBus.getInstance().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onData(RestaurantInfoListEvent ev) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setData(ev.getData());
    }

    @Subscribe
    public void onData(RestaurantInfoChangeEvent ev) {
        mAdapter.updateData(ev.getData());
    }

}
