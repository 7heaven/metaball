package com.heaven.application.metaballdemo.metaballsystem;

/**
 * Created by caifangmao on 15/2/11.
 */
public class Vector2D {

    public float x;
    public float y;

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getLength(){
        return (float) Math.sqrt(lengthSq());
    }

    public void setLength(float length){
        x *= (length /= getLength());
        y *= length;
    }

    public float lengthSq(){
        return (x * x + y * y);
    }

    public float getAngle(){
        return (float) Math.atan2(x, y);
    }

    public void setAngle(float angle){
        float len = getLength();
        x = (float) Math.cos(angle) * len;
        y = (float) Math.sin(angle) * len;
    }

    public float dot(Vector2D v2){
        return (x * v2.x) + (y * v2.y);
    }

    public float dist(Vector2D v2){
        return Math.sqrt(distSq(v2));
    }

    public float distSq(Vector2D v2){
        float dx = v2.x - x;
        float dy = v2.y - y;

        return (dx * dx + dy * dy);
    }

    public Vector2D zero(){
        x = 0;
        y = 0;

        return this;
    }

    public Vector2D copy(Vector2D v2){
        x = v2.x;
        y = v2.y;

        return this;
    }

    public Vector2D norm(){
        if( x== 0 && y == 0){
            x = 1;
            return this;
        }

        float len = length;
        x /= len;
        y /= len;

        return this;
    }

    public Vector2D limit(float max){
        float len = length;
        if(len > max){
            x *= (max /= len);
            y *= max;
        }

        return this;
    }

    public Vector2D add(Vector2D v2){
        x += v2.x;
        y += v2.y;

        return this;
    }

    public Vector2D subtract(Vector2D v2){
        x -= v2.x;
        y -= v2.y;

        return this;
    }

    public Vector2D multiply(float value){
        x *= value;
        y *= value;

        return this;
    }

    public Vector2D divide(float value){
        x /= value;
        y /= value;

        return this;
    }

    public Vector2D clone(){
        return new Vector2D(x, y);
    }

    public Vector2D getPerpLeft(){
        return new Vector2D(-y, x);
    }

    public Vector2D getPerpRight(){
        return new Vector2D(y, -x);
    }

    public static float angleBetween(Vector2D v1, Vector2D v2){
        return (float) (Math.atan2(v2.y, v2.x) - Math.atan2(v1.y, v1.x));
    }

    public static Vector2D add(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2D subtract(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    @Override
    public String toString(){
        return "[object Vector2D][x=" + x + "][y=" + y + "]";
    }

}
