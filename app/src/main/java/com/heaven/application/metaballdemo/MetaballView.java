package com.heaven.application.metaballdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by caifangmao on 15/2/11.
 */
public class MetaballView extends View {

    private int width;
    private int height;

    private int centerX;
    private int centerY;

    public MetaballView(Context context){
        this(context, null);
    }

    public MetaballView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public MetaballView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

}
