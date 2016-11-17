package com.example.wyz.everynews1.utils;

import com.example.wyz.everynews1.MyApp;

/**
 * Created by Wyz on 2016/11/7.
 * 测量单位转换
 */
public class DimenUtil {
    public static float dp2px(float dp) {
        final float scale = MyApp.getAppContext().getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(float sp) {
        final float scale = MyApp.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenSize() {
        return MyApp.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }
}
