package org.iii.www.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.iii.www.Background;
import org.iii.www.MainGame;


public class Box2dScreen extends Background {
    private MainGame mainGame;
    private World world;
    private Box2DDebugRenderer render;
    private OrthographicCamera camera;
    private Body mainBody,groundBody,triBody;
    private Fixture mainfixture,groundfixtrue,trifixtrue;
    private boolean doJump,isJump,isGo;
    Box2dScreen(MainGame mainGame){
        this.mainGame=mainGame;
    }

    @Override
    public void show() {
        //創造Box2D  重力加速度10
        world= new World(new Vector2(0,-10),false);
        //照相機?
        camera= new OrthographicCamera(7.11f*3,4*3);
        //攝影機往下移動
        camera.translate(0,1);
        render = new Box2DDebugRenderer();

        groundBody=world.createBody(createGroundBody());//設置地板
        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(500,0.5f);
        groundfixtrue=groundBody.createFixture(shape1,1);
        groundfixtrue.setUserData("ground");
        shape1.dispose();

        mainBody =world.createBody(createMainBody());  //從world創造物件身體
        PolygonShape shape = new PolygonShape();   //設置一個多邊形
        shape.setAsBox(0.5f,0.5f);           //設置長寬 單位為 公尺
        mainfixture=mainBody.createFixture(shape,1);
        mainfixture.setUserData("player");
        shape.dispose();

        triBody=world.createBody(createTri());
        PolygonShape shape2 = new PolygonShape();   //設置一個多邊形
        Vector2[] varray= {new Vector2(-0.5f,-0.5f),new Vector2(0.5f,-0.5f),new Vector2(0.0f,0.5f)};
        shape2.set(varray);
        trifixtrue=triBody.createFixture(shape2,1);
        trifixtrue.setUserData("tri");
        shape2.dispose();
        world.setContactListener(new GameListener());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0.5f,0.5f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(delta,6,2);
        camera.update();
        render.render(world,camera.combined);

        if(isGo){
            mainBody.setLinearVelocity(10,mainBody.getLinearVelocity().y);
        }
        if(Gdx.input.isTouched(2)){
            doJump=true;
        }
        if(Gdx.input.isTouched()){
            isGo=true;
        }else{
//            mainBody.setLinearVelocity(0,mainBody.getLinearVelocity().y);
            isGo=false;
        }
        if(doJump&&!isJump){
            jump();
        }
    }

    @Override
    public void hide() {
        world.dispose();
        render.dispose();
        mainBody.destroyFixture(mainfixture);
        groundBody.destroyFixture(groundfixtrue);
        triBody.destroyFixture(trifixtrue);
        world.destroyBody(mainBody);
        world.destroyBody(groundBody);
        world.destroyBody(triBody);
    }

    //創建物件身體的方法
    private BodyDef createMainBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(-10.0f,0f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        return bodyDef;
    };
    private BodyDef createGroundBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0.0f,-1);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        return bodyDef;
    };
    private BodyDef createTri(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(10f,0);    //設定位置
        bodyDef.type = BodyDef.BodyType.StaticBody;  //設定動態或是靜態物件
        return bodyDef;
    };
    private void jump(){
        Vector2 postion =mainBody.getPosition();
        mainBody.applyLinearImpulse(0.0f,10,postion.x,postion.y,true);
        doJump=false;
    }
    private class GameListener implements ContactListener{

        @Override
        public void beginContact(Contact contact) {
            Fixture fixtureA =contact.getFixtureA();  Fixture fixtureB =contact.getFixtureB();
            if(fixtureA.getUserData().equals("player")&&fixtureB.getUserData().equals("ground")||
                    fixtureA.getUserData().equals("ground")&&fixtureB.getUserData().equals("player")){
                isJump=false;
                if(Gdx.input.isTouched()){
                    Gdx.app.log("chad","isTouch");
                    doJump=true;
                }
//                isGo=true;
            }

            if(fixtureA.getUserData().equals("tri")&&fixtureB.getUserData().equals("player")||
                    fixtureA.getUserData().equals("player")&&fixtureB.getUserData().equals("tri")){
                isGo=false;
             }

        }

        @Override
        public void endContact(Contact contact) {
            Fixture fixtureA =contact.getFixtureA();  Fixture fixtureB =contact.getFixtureB();
            if(fixtureA.getUserData().equals("player")&&fixtureB.getUserData().equals("ground")){
                Gdx.app.log("chad","endContact");
                isJump=true;
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Fixture fixtureA =contact.getFixtureA();  Fixture fixtureB =contact.getFixtureB();
            if(fixtureA.getUserData().equals("player")&&fixtureB.getUserData().equals("ground")){
//                Gdx.app.log("chad","preSolve");
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            Fixture fixtureA =contact.getFixtureA();  Fixture fixtureB =contact.getFixtureB();
            if(fixtureA.getUserData().equals("player")&&fixtureB.getUserData().equals("ground")){
//                Gdx.app.log("chad","postSolve");
            }
        }
    }
}
