package com.grechur.customviewapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_load).setOnClickListener(this);
        findViewById(R.id.btn_love).setOnClickListener(this);
        findViewById(R.id.btn_banner).setOnClickListener(this);
        findViewById(R.id.btn_track).setOnClickListener(this);
        findViewById(R.id.btn_recyclerview).setOnClickListener(this);
        findViewById(R.id.btn_circle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_load:
                startActivity(new Intent(this,LoadingActivity.class));
                break;
            case R.id.btn_circle:
                startActivity(new Intent(this,CircleLoadActivity.class));
                break;
            case R.id.btn_love:
                startActivity(new Intent(this,LoveActivity.class));
                break;
            case R.id.btn_banner:
                startActivity(new Intent(this,BannerActivity.class));
                break;
            case R.id.btn_track:
                startActivity(new Intent(this,IndicatorActivity.class));
                break;
            case R.id.btn_recyclerview:
                startActivity(new Intent(this,RecyclerviewActivity.class));
                break;
        }
    }
}
