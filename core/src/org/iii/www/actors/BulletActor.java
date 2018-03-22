package org.iii.www.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Administrator on 2018/3/22.
 */

public class BulletActor extends Actor{
    private Texture texture;
    private Vector2 postion,point;
    public BulletActor(Texture texture,Vector2 postion,Vector2 point){
        this.texture=texture;
        this.postion=postion;
        this.point=point;
        setSize(64,64);
        setPosition(postion.x,postion.y);
    }

    @Override
    public void act(float delta) {

        float angle=postion.angleRad(point);
        float barX =(float)(getX()+1000*delta*Math.cos(angle*180/3.14));
        float barY =(float)(getY()+1000*delta*Math.sin(angle*180/3.14));
        setY(barY);
        setX(barX);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }
    public void detach(){

    }
}
