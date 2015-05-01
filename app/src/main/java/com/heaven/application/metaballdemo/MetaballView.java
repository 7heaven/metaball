package com.heaven.application.metaballdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.heaven.application.metaballdemo.metaballsystem.Metaball;
import com.heaven.application.metaballdemo.metaballsystem.MetaballManager;
import com.heaven.application.metaballdemo.metaballsystem.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caifangmao on 15/2/11.
 */
public class MetaballView extends View {

    private int width;
    private int height;

    private int centerX;
    private int centerY;

    private MetaballManager metaballManager;

    private Metaball[] targetBall = new Metaball[1];
    private List<Metaball> subMetaball;
    private List<Point> originPositions;
    private List<PointF> as;

    private Paint paint;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        @Override
        public void run(){
            for(int i = 0; i < as.size(); i++){
                Metaball ball = subMetaball.get(i);
                if(!ball.attached){
                    Point origin = originPositions.get(i);
                    PointF a = as.get(i);
                    Vector2D v = ball.getPosition();

                    a.x += (origin.x - v.x) * 0.1F;
                    a.y += (origin.y - v.y) * 0.1F;

                    v.x += a.x;
                    v.y += a.y;

                    ball.setPosition(v);

                    a.x *= 0.81F;
                    a.y *= 0.81F;
                }

            }

            if(metaballManager != null && metaballManager.getSize() > 0) metaballManager.freeze();

            invalidate();

            handler.postDelayed(this, 20);
        }
    };

    public MetaballView(Context context){
        this(context, null);
    }

    public MetaballView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public MetaballView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        metaballManager = MetaballManager.getInstance();

        subMetaball = new ArrayList<Metaball>();
        as = new ArrayList<PointF>();
        originPositions = new ArrayList<Point>();

        for(int i = 0; i < targetBall.length; i++){
            targetBall[i] = new Metaball(new Vector2D(0, 0), 1.0F);
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(0xFF00CCFF);

        handler.post(runnable);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        centerX = width / 2;
        centerY = height / 2;

        metaballManager.removeAllMetaball();

        subMetaball.clear();
        as.clear();
        originPositions.clear();
        for(float i = 0; i < Math.PI * 2; i+=Math.PI / 3){

            Point p = centerRadiusPoint(new Point(centerX, centerY), i, 5);
            originPositions.add(p);

            Metaball metaball = new Metaball(new Vector2D(p.x, p.y), 2.0F);

            metaballManager.addMetaball(metaball);

            subMetaball.add(metaball);
            as.add(new PointF(0, 0));
        }

        for(Metaball b : targetBall){
            metaballManager.addMetaball(b);
        }
    }

    protected Point centerRadiusPoint(Point center, double angle, double radius){
        Point p = new Point();
        p.x = (int) (radius * Math.cos(angle)) + center.x;
        p.y = (int) (radius * Math.sin(angle)) + center.y;

        return p;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                targetBall[0].setPosition(new Vector2D(event.getX(), event.getY()));

                float dx;
                float dy;

                Metaball tBall = null;
                float dis = Integer.MAX_VALUE;
                float ddis = 0;
                float d = 0;
                float dd = 0;
                for(int i = 0; i < subMetaball.size(); i++){
                    Metaball m = subMetaball.get(i);
                    Vector2D v = m.getPosition();
                    Point p = originPositions.get(i);

                    dx = event.getX() - p.x;
                    dy = event.getY() - p.y;

                    d = dx * dx + dy * dy;



                    dx = v.x - p.x;
                    dy = v.y - p.y;

                    dd = dx * dx + dy * dy;

                    if(d < dis){
                        dis = d;
                        tBall = m;
                        ddis = dd;
                    }

                    if(dis > 100 && ddis > 2500){
                        m.attached = false;
                    }
                }

                if(tBall != null){
                    if(dis > 100 && ddis > 2500){
                        tBall.attached = false;
                    }else{
                        tBall.attached = true;
                        tBall.getPosition().x = event.getX();
                        tBall.getPosition().y = event.getY();
                    }
                }

                metaballManager.freeze();

                invalidate();

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                for(Metaball m : subMetaball){
                    m.attached = false;
                }
                break;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
//
        Path outline = metaballManager.getOutline();

        if(outline != null){
            canvas.drawPath(outline, paint);
        }
    }

}
