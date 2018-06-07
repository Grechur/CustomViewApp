package com.grechur.customviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.grechur.customviewapp.view.LoveLayout;

public class LoveActivity extends AppCompatActivity {
    LoveLayout ll_love;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        ll_love = findViewById(R.id.ll_love);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_love.addLove();
            }
        });
    }
}
