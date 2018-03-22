package org.iii.www.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import org.iii.www.MyAPI;

/**
 * Created by User on 2018/3/22.
 */

public class GroundActor extends Actor{
    Texture texture;
    World world;
    Body body;
    Fixture fixture;
    public GroundActor(World world,Texture texture,float x,float width,float y){
        this.world=world;
        this.texture=texture;
        BodyDef bodyDef=new BodyDef();
        //起始位置
        bodyDef.position.set(x+width/2,y);
        //靜態的物件
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body=world.createBody(bodyDef);
        //設置角色形狀
        PolygonShape shape = new PolygonShape();
        //設置角色長寬 單位為 公尺 長半徑0.5寬半徑0.5
        shape.setAsBox(width/2,0.5f);
        //設置腳色載具
        fixture=body.createFixture(shape,1);
        fixture.setUserData("ground");
        setSize(MyAPI.PIXE_TRANSFOR_MILE*width,MyAPI.PIXE_TRANSFOR_MILE*1);
        setPosition((body.getPosition().x-width/2)*MyAPI.PIXE_TRANSFOR_MILE,(body.getPosition().y-0.5f)*MyAPI.PIXE_TRANSFOR_MILE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //從body 位置乘上換算常數

        batch.draw(texture,getX(),getY(),getWidth(),getHeight());

    }
    //設定一個方法不需要的時候消除，載具跟身體
    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
