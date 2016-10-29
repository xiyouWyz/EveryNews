package com.example.wyz.everynews1.ui.photo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wyz.everynews1.R;

/**
 * Created by Wyz on 2016/10/27.
 */

public class PhotoMainFragment  extends Fragment{
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_photo_main,container,false);
        return view;
    }
}
