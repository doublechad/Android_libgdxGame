package org.iii.www.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import org.iii.www.MyAPI;

/**
 * Created by User on 2018/3/21.
 */

public class BoxActor extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    public BoxActor(World world,Texture texture,float x,float y){
        this.world=world;
        this.texture=texture;
        BodyDef bodyDef=new BodyDef();
        //起始位置
        bodyDef.position.set(x,y);
        //靜態的物件
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body=world.createBody(bodyDef);
        //設置角色形狀
        PolygonShape shape = new PolygonShape();
        Vector2[] varray={new Vector2(-0.5f,-0.5f),new Vector2(0.5f,-0.5f),new Vector2(0.0f,0.5f)};
        //設置角色多邊形 單位為 公尺
        shape.set(varray);
        //設置腳色載具
        fixture=body.createFixture(shape,1);
        fixture.setUserData("tri");
        setSize(MyAPI.PIXE_TRANSFOR_MILE*1,MyAPI.PIXE_TRANSFOR_MILE*1);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x-0.5f)*MyAPI.PIXE_TRANSFOR_MILE,(body.getPosition().y-0.5f)*MyAPI.PIXE_TRANSFOR_MILE);
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }
    //設定一個方法不需要的時候消除，載具跟身體
    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
