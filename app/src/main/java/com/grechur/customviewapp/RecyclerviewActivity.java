package com.grechur.customviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.grechur.customviewapp.view.recycleview.WrapRecyclerAdapter;
import com.grechur.customviewapp.view.recycleview.defaultrefresh.DefaultLoadCreator;
import com.grechur.customviewapp.view.recycleview.defaultrefresh.DefaultRefreshCreator;
import com.grechur.customviewapp.view.recycleview.refresh_load.LoadRefreshRecyclerView;
import com.grechur.customviewapp.view.recycleview.refresh_load.LoadRefreshRecyclerViewCopy;
import com.grechur.customviewapp.view.recycleview.refresh_load.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewActivity extends AppCompatActivity {
    private LoadRefreshRecyclerViewCopy load_refresh_view;
    private List mData;
    private MyAdapter mAdapter;
    private DefaultRefreshCreator mDefaultRefreshCreator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        load_refresh_view = findViewById(R.id.load_refresh_view);

        load_refresh_view.setLayoutManager(new LinearLayoutManager(this));

        mData = new ArrayList();
        mAdapter = new MyAdapter(this,mData,R.layout.recycler_item);

        load_refresh_view.setAdapter(mAdapter);
        mDefaultRefreshCreator = new DefaultRefreshCreator();
        load_refresh_view.addRefreshViewCreator(mDefaultRefreshCreator);
        load_refresh_view.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData(true);
                            }
                        });
                    }
                }).start();

            }
        });

        load_refresh_view.addLoadViewCreator(new DefaultLoadCreator());
        load_refresh_view.setOnLoadMoreListener(new LoadRefreshRecyclerViewCopy.OnLoadMoreListener() {
            @Override
            public void onLoad() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData(false);
                            }
                        });
                    }
                }).start();
            }
        });

    }

    private void initData(boolean isrefresh) {
        if(isrefresh){
            mData.clear();
        }
        for (int i = 0; i < 20; i++) {
            mData.add(i);
        }
        mAdapter.notifyDataSetChanged();
        if(isrefresh) {
            load_refresh_view.onStopRefresh();
        }else{
            load_refresh_view.onStopLoad();
        }
    }


}
