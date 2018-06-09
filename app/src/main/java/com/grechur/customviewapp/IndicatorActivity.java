package com.grechur.customviewapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.grechur.customviewapp.view.TextFragment;
import com.grechur.customviewapp.view.tabIndication.ColorTrackTextView;
import com.grechur.customviewapp.view.tabIndication.TrackAdapter;
import com.grechur.customviewapp.view.tabIndication.TrackTextIndicator;

import java.util.ArrayList;
import java.util.List;

public class IndicatorActivity extends AppCompatActivity {
    TrackTextIndicator track_indicator;
    ViewPager view_pager;
    private static String[] titles={
            "常用网站","个人博客","公司博客","开发社区","常用工具","在线学习",
            "开放平台","互联网资讯","求职招聘","应用加固"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indication);
        view_pager = findViewById(R.id.view_pager);
        track_indicator = findViewById(R.id.track_indicator);

        view_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TextFragment.newInstance(titles[position]);
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        });
        track_indicator.setAdapter(new TrackAdapter(titles) {
            @Override
            public View getView(int position, View convertView) {
                ColorTrackTextView colorTrackTextView = new ColorTrackTextView(IndicatorActivity.this);
                colorTrackTextView.setText(titles[position]);
                return colorTrackTextView;
            }

            @Override
            public void restoreIndicator(View view) {
                ColorTrackTextView textView  = (ColorTrackTextView) view;
//                textView.setTextColor(Color.BLACK,0);
                textView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                textView.setCurrentProgress(0);
            }

            @Override
            public void highlightIndicator(View view) {
                ColorTrackTextView textView  = (ColorTrackTextView) view;
//                textView.setTextColor(Color.RED,1);
                textView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                textView.setCurrentProgress(1);
            }

            @Override
            public View getBottomTrackView() {
                View view = new View(IndicatorActivity.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,8));
                return view;
            }
        },view_pager);

    }


}
