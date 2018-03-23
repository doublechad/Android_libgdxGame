package org.iii.www.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Administrator on 2018/3/22.
 */

public class BulletActor extends Actor{
    private Texture texture;
    private Vector2 postion,point;
    private boolean isRight;
    private int right_left;
    private Stage stage;
    public BulletActor(Stage stage, Texture texture, Vector2 postion, boolean isRight){
        this.stage=stage;
        this.texture=texture;
        this.postion=postion;
        this.isRight=isRight;
        setSize(64,64);
        setPosition(postion.x,postion.y);
        right_left =(isRight)?1:-1;
    }

    @Override
    public void act(float delta) {
        Vector2 edgeRight = stage.screenToStageCoordinates(new Vector2(1980,1080));
        Vector2 edgeLeft = stage.screenToStageCoordinates(new Vector2(0,0));
        //檢查子彈是不是在畫面內
        if(edgeRight.x<getX()||edgeLeft.x>getX()){
           this.remove();
        }else {
            float barX = (getX() + 1000 * delta * right_left);
            //      float barY =(float)(getY()+1000*delta*bar);
            //      setY(barY);
            Gdx.app.log("chad","123");
            setX(barX);
        }


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }
    public void detach(){
        texture.dispose();
    }
}
