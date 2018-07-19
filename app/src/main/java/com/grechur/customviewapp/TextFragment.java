package com.grechur.customviewapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zz on 2018/6/8.
 */

public class TextFragment extends Fragment{
    private TextView tv_desc;

    public static Fragment newInstance(String s){
        Fragment fragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_fragment,container,false);
        tv_desc = view.findViewById(R.id.tv_desc);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String title = getArguments().getString("title");
        tv_desc.setText(title);
    }
}
