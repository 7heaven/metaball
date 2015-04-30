package com.heaven.application.metaballdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.heaven.application.metaballdemo.metaballsystem.*;

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

    private int pathSlitCount = 50;
    private List<Path> bouncingPath;
    private PathMeasure pathMeasure;
    private List<PointF> points;
    private List<Path> outlines;

    private MetaballManager metaballManager;

    private Metaball[] targetBall = new Metaball[4];
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
                Point origin = originPositions.get(i);
                PointF a = as.get(i);
                if(!ball.attached){
                    Vector2D v = ball.getPosition();

                    a.x += (v.x - origin.x) * 0.3F;
                    a.y += (v.y - origin.y) * 0.3F;

                    v.x += a.x;
                    v.y += a.y;

                    ball.setPosition(v);

                    a.x *= 0.81F;
                    a.y *= 0.81F;

                    invalidate();

                }

            }

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
//
//        metaballManager.addMetaball(new Metaball(new Vector2D(500, 300), 4.0F));
//        metaballManager.addMetaball(new Metaball(new Vector2D(100, 150), 4.0F));

        subMetaball = new ArrayList<Metaball>();
        as = new ArrayList<PointF>();
        originPositions = new ArrayList<Point>();

        for(int i = 0; i < targetBall.length; i++){
            targetBall[i] = new Metaball(new Vector2D(0, 0), 1.0F);
        }

        bouncingPath = new ArrayList<Path>();
        pathMeasure = new PathMeasure();


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(0xFF00CCFF);

        points = new ArrayList<PointF>();

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

                float dx = event.getX() - centerX;
                float dy = event.getY() - centerY;

                float degree = (float)  Math.atan2(dy, dx);

                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                for(int i = 1; i < targetBall.length; i++){
                    Point p = centerRadiusPoint(new Point(centerX, centerY), degree + (i * 0.8F), distance - (i * 30));

                    targetBall[i].setPosition(new Vector2D(p.x, p.y));
                }

                for(int i = 0; i < subMetaball.size(); i++){
                    Metaball m = subMetaball.get(i);
                    Vector2D v = m.getPosition();

                    dx = event.getX() - v.x;
                    dy = event.getY() - v.y;

                    float d = dx * dx + dy * dy;

                    if(d > 2500){
                        m.attached = false;
                    }else{
                        m.attached = true;

                        v.x = event.getX();
                        v.y = event.getY();

                        m.setPosition(v);
                    }
                }

                metaballManager.freeze();

//                points.clear();
//
////                for(float i = 0; i <= length; i+= step){
////                    pathMeasure.getPosTan(i, pos, tan);
////
////                    if(pPos[0] != -1){
////                        bouncingPath.moveTo(pPos[0], pPos[1]);
////                        bouncingPath.lineTo(pos[0], pos[1]);
////                    }else{
////                        bouncingPath.moveTo(pos[0], pos[1]);
////                    }
////
////
////                    pPos[0] = pos[0];
////                    pPos[1] = pos[1];
////
////                    points.add(new PointF(pos[0], pos[1]));
////
////                }
//
//                outlines = metaballManager.getOutlines();
//                bouncingPath.clear();
//                for(Path p : outlines){
//                    pathMeasure.setPath(p, false);
//
//                    float length = pathMeasure.getLength();
//                    float step = length / pathSlitCount;
//                    float[] pos = new float[2];
//                    float[] tan = new float[2];
//                    float[] pPos = new float[]{-1, -1};
//
//
//                    bouncingPath.add(new Path());
//                    for(float i = 0; i <= length; i+= step){
//                        pathMeasure.getPosTan(i, pos, tan);
////
//                        if(pPos[0] != -1){
//                            bouncingPath.get(bouncingPath.size() - 1).lineTo(pos[0], pos[1]);
//                        }else{
//                            bouncingPath.get(bouncingPath.size() - 1).moveTo(pos[0], pos[1]);
//                        }
//
//
//                        pPos[0] = pos[0];
//                        pPos[1] = pos[1];
//                    }
//
//                    bouncingPath.get(bouncingPath.size() - 1).close();
//                }

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
//
        Path outline = metaballManager.getOutline();

        if(outline != null){
            canvas.drawPath(outline, paint);
        }

//        if(bouncingPath != null){
//            for(Path path : bouncingPath){
//                canvas.drawPath(path, paint);
//            }
//        }
    }

}
