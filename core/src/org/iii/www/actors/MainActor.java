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

public class MainActor extends Actor {
    //圖片紋路
    private Texture texture;
    //物理引擎
    private World world;
    //物件身體
    private Body body;
    //骨架
    private Fixture fixture;
    public boolean jumping,alive=true;
    public MainActor(World world,Texture texture,float x,float y){
        this.world=world;
        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(x,y);        //起始位置
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body=world.createBody(bodyDef);
        //設置角色形狀
        PolygonShape shape = new PolygonShape();
        //設置角色長寬 單位為 公尺 長半徑0.5寬半徑0.5
        shape.setAsBox(0.5f,0.5f);
        //設置腳色載具
        fixture=body.createFixture(shape,1);
        fixture.setUserData("player");
        setSize(MyAPI.PIXE_TRANSFOR_MILE*1,MyAPI.PIXE_TRANSFOR_MILE*1);
        this.texture=texture;
    }
    @Override
    public void act(float delta) {
        if(Gdx.input.isTouched()&&alive){
            if(!jumping)jump();
         }
         if(alive){
            //取得目前的Y向量
            float speedY =body.getLinearVelocity().y;
            body.setLinearVelocity(MyAPI.PLAYER_SPEED,speedY);
         }


    }
    private void jump(){
        Vector2 postion =body.getPosition();
        body.applyLinearImpulse(0.0f,3,postion.x,postion.y,true);

    }
    @Override
    public void draw(Batch batch, float parentAlpha) {

        //從body 位置乘上換算常數
        setPosition((body.getPosition().x-0.5f)*MyAPI.PIXE_TRANSFOR_MILE,(body.getPosition().y-0.5f)*MyAPI.PIXE_TRANSFOR_MILE);
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }
    //設定一個方法不需要的時候消除，載具跟身體
    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}
