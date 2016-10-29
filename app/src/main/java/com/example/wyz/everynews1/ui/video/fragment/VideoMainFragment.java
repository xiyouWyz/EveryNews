package com.example.wyz.everynews1.ui.video.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wyz.everynews1.R;

/**
 * Created by Wyz on 2016/10/27.
 */

public class VideoMainFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_video_main,container,false);
        return view;
    }
}
