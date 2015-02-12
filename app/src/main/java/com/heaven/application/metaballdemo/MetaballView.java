package com.heaven.application.metaballdemo;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.heaven.application.metaballdemo.metaballsystem.*;

/**
 * Created by caifangmao on 15/2/11.
 */
public class MetaballView extends View {

    private int width;
    private int height;

    private int centerX;
    private int centerY;

    private MetaballManager metaballManager;

    private Metaball targetBall;

    private Paint paint;

    private DashPathEffect pathEffect;


    public MetaballView(Context context){
        this(context, null);
    }

    public MetaballView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public MetaballView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        metaballManager = MetaballManager.getInstance();


        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        metaballManager.removeAll();

        for(int i = 0; i < 6; i++){
            metaballManager.addMetaball(new Metaball(new Vector2D((float) (width * Math.random()), (float) (height * Math.random())), (float) (6.0F * Math.random())));
        }

        targetBall = new Metaball(new Vector2D(0, 0), 4.0F);

        metaballManager.addMetaball(targetBall);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF00CCFF);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        pathEffect = new DashPathEffect(new float[]{2, 2}, 4);
        paint.setPathEffect(pathEffect);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        centerX = width / 2;
        centerY = height / 2;
        centerX = centerX;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                targetBall.setPosition(new Vector2D(event.getX(), event.getY()));
                metaballManager.freeze();
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Path outline = metaballManager.getOutline();

        if(outline != null){
            canvas.drawPath(outline, paint);
        }
    }

}
