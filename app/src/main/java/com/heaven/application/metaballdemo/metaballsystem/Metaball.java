package com.heaven.application.metaballdemo.metaballsystem;

import android.graphics.PointF;

/**
 * Created by caifangmao on 15/2/11.
 */
public class Metaball {

    public static final int MIN_STRENGTH = 1;
    public static final int MAX_STRENGTH = 100;

    private Vector2D position;
    private float strength;

    private boolean tracked;
    private Vector2D edge;
    private Vector2D direction;

    public Metaball(Vector2D position, float strength){
        this.position = position.clone();
        this.strength = strength;

        tracked = false;
        edge = position.clone();
        direction = new Vector2D((float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1);
    }

    public float strengthAt(Vector2D vector, float c){
        float div = (float) Math.pow(Vector2D.subtract(position, vector).lengthSq(), c * 0.5F);

        return (div != 0) ? (strength / div) : 10000;
    }

    public Vector2D getPosition(){
        return position;
    }

    public void setPosition(Vector2D position){
        this.position.copy(position);
    }

    public setStrength(float value){
        strength = MathUtil.
    }
}
