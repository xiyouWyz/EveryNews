package com.example.wyz.everynews1.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 将图片按照比例显示，这样就出现图片高度参差不齐
 * Created by Wyz on 2016/11/19.
 */
public class RatioImageView extends ImageView {
    int originWidth=-1;
    int originHeight=-1;
    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setOriginSize(int originWidth,int originHeight){
        this.originHeight=originHeight;
        this.originWidth=originWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(originWidth>0&&originHeight>0){
            float ratio=(float)originWidth/(float)originHeight;
            int width=MeasureSpec.getSize(widthMeasureSpec);
            int height=MeasureSpec.getSize(heightMeasureSpec);
            if(width>0){
                height=(int)((float)width/ratio);
            }
            setMeasuredDimension(width,height);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
