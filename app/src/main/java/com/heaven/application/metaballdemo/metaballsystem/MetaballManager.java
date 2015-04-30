package com.heaven.application.metaballdemo.metaballsystem;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caifangmao on 15/2/12.
 */
public class MetaballManager {

    private static MetaballManager instance;

    private static float gooieness = 2.0F;
    private static float threshold = 0.0006F;
    private static float resolution = 5.0F;
    private static int maxSteps = 600;

    private List<Metaball> metaballs;
    private Path outline;
    private float minStrength;

    private List<Path> outlines;

    private MetaballManager(){
        metaballs = new ArrayList<Metaball>();
        outline = new Path();

        outlines = new ArrayList<Path>();

        minStrength = Metaball.MIN_STRENGTH;
    }

    public static MetaballManager getInstance(){
        if(instance == null){
            instance = new MetaballManager();
        }

        return instance;
    }

    public Path getOutline(){
        return outline;
    }

    public List<Path> getOutlines(){
        return outlines;
    }

    public void addMetaball(Metaball metaball){
        minStrength = Math.min(metaball.getStrength(), minStrength);

        metaballs.add(metaball);
    }

    public void removeMetaball(Metaball metaball){
        metaballs.remove(metaball);
    }

    public void removeAllMetaball(){ metaballs.clear();}

    public int getSize(){
        return metaballs.size();
    }

    public void freeze(){
        outline.reset();
        outlines.clear();

        Vector2D seeker = new Vector2D(0, 0);
//        Metaball metaball;
        int i;

        for(Metaball metaball : metaballs){
            metaball.tracked = false;

            seeker.copy(metaball.getPosition());
            i = 0;
            while((stepToEdge(seeker) > threshold) && (++i < 50)){}
            metaball.edge.copy(seeker);
        }

        int edgeSteps = 0;
        Metaball current = untrackedMetaball();

        seeker.copy(current.edge);
        outline.moveTo(seeker.x, seeker.y);
        outlines.add(new Path());
        outlines.get(outlines.size() - 1).moveTo(seeker.x, seeker.y);

        while(current != null && edgeSteps < maxSteps){
            rk2(seeker, resolution);

            outline.lineTo(seeker.x, seeker.y);
            outlines.get(outlines.size() - 1).lineTo(seeker.x, seeker.y);

            for(Metaball metaball : metaballs){
                if(seeker.dist(metaball.edge) < (resolution * 0.9F)){
                    seeker.copy(metaball.edge);
                    outline.lineTo(seeker.x, seeker.y);
                    outlines.get(outlines.size() - 1).lineTo(seeker.x, seeker.y);

                    current.tracked = true;

                    if(metaball.tracked){
                        current = untrackedMetaball();

                        if(current != null){
                            seeker.copy(current.edge);
                            outline.moveTo(seeker.x, seeker.y);
                            outlines.add(new Path());
                            outlines.get(outlines.size() - 1).moveTo(seeker.x, seeker.y);
                        }
                    }else{
                        current = metaball;
                    }

                    break;
                }
            }

            ++edgeSteps;
        }

        outline.close();
    }

    private Metaball untrackedMetaball(){
        for(Metaball metaball : metaballs){
            if(!metaball.tracked){
                return metaball;
            }
        }

        return null;
    }

    private float stepToEdge(Vector2D seeker){
        float force = fieldStrength(seeker);
        float stepsize;

        stepsize = (float) (Math.pow(minStrength / threshold, 1 / gooieness) - Math.pow(minStrength / force, 1 / gooieness) + 0.01F);

        seeker.add(fieldNormal(seeker).multiply(stepsize));

        return force;
    }

    private float fieldStrength(Vector2D v){
        float force = 0.0F;

        for(Metaball metaball : metaballs){
            force += metaball.strengthAt(v, gooieness);
        }

        return force;
    }

    private Vector2D fieldNormal(Vector2D v){
        Vector2D force = new Vector2D(0, 0);
        Vector2D radius;

        for(Metaball metaball : metaballs){
            radius = Vector2D.subtract(metaball.getPosition(), v);

            if(radius.lengthSq() == 0){
                continue;
            }

            radius.multiply((float) (-gooieness * metaball.getStrength() * (1.0F / Math.pow(radius.lengthSq(), (2.0F + gooieness) * 0.5F))));

            force.add(radius);
        }

        return force.norm();
    }

    private void rk2(Vector2D v, float h){
        Vector2D t1 = fieldNormal(v).getPerpLeft();
        t1.multiply(h * 0.5F);

        Vector2D t2 = fieldNormal(Vector2D.add(v, t1)).getPerpLeft();
        t2.multiply(h);

        v.add(t2);
    }
}
