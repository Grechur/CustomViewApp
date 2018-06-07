package com.grechur.customviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grechur.customviewapp.view.banner.simple1.BannerAdapter;
import com.grechur.customviewapp.view.banner.simple1.BannerViewPager;
import com.grechur.customviewapp.view.banner.simple4.BannerClickListener;
import com.grechur.customviewapp.view.banner.simple4.BannerView;


import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {
    BannerViewPager bvp_custom;
    com.grechur.customviewapp.view.banner.simple2.BannerViewPager bvp_custom2;
    com.grechur.customviewapp.view.banner.simple3.BannerViewPager bvp_custom3;
    BannerView mBannerView;
    Integer[] RES = {
            R.mipmap.image1,R.mipmap.image2,R.mipmap.image3,R.mipmap.image4,R.mipmap.image5
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        bvp_custom3 = findViewById(R.id.bvp_custom3);
        bvp_custom = findViewById(R.id.bvp_custom);
//        bvp_custom2 = findViewById(R.id.bvp_custom2);
        mBannerView = findViewById(R.id.bv_view);

        bvp_custom.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView imageView = new ImageView(BannerActivity.this);
                Glide.with(BannerActivity.this).load(RES[0]).into(imageView);
                return imageView;
            }
        });

//        bvp_custom2.setAdapter(new BannerAdapter() {
//            @Override
//            public View getView(int position) {
//                ImageView imageView = new ImageView(BannerActivity.this);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(BannerActivity.this).load(RES[0]).into(imageView);
//                return imageView;
//            }
//        });
//        bvp_custom2.startRoll();
//
        bvp_custom3.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView imageView = new ImageView(BannerActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(BannerActivity.this).load(RES[0]).into(imageView);
                return imageView;
            }
        });

        mBannerView.setScrollerDuration(1000);
        mBannerView.setAdapter(new com.grechur.customviewapp.view.banner.simple4.BannerAdapter<Integer>(RES) {
            @Override
            public View getView(int position,View convertView) {
                ImageView imageView = null;
                if(convertView==null){
                    imageView = new ImageView(BannerActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setTag(R.id.image,RES[position]);
                    Glide.with(BannerActivity.this).load(RES[position]).into(imageView);
                }else{
                    imageView = (ImageView) convertView;
                }
                return imageView;
            }

        });

        mBannerView.setBannerListener(new BannerClickListener() {
            @Override
            public void onBannerListener(View view, int position) {
                Log.e("TAG",view.toString()+position);
            }
        });
    }
}
