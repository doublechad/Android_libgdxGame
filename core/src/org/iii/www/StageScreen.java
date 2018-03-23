package org.iii.www;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import org.iii.www.actors.BoxActor;
import org.iii.www.actors.BulletActor;
import org.iii.www.actors.GroundActor;
import org.iii.www.actors.MainActor;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by User on 2018/3/21.
 */

public class StageScreen extends Background {
    private MainGame mainGame;
    private Stage stage;
    private MainActor mainActor;
    private GroundActor groundActor;
    private BoxActor boxActor;
    private ArrayList<Texture> textrueList;
    private World world;
    private LinkedList<BoxActor> boxActors = new LinkedList<BoxActor>();
    private LinkedList<BulletActor> bulletManager = new LinkedList<BulletActor>();
    //遊戲搖桿
    private Touchpad touchPad;
    private Button buttonFire,buttonJump;


    private int touchPadX =0,buttonX;
    public StageScreen(MainGame mainGame) {
        this.mainGame = mainGame;
        //場景
        stage = new Stage();
        world = new World(new Vector2(0, -9.81f), true);
        //存放圖形的List
        textrueList = new ArrayList<Texture>();
        Texture t1 = mainGame.getAssetManager().get("skating.png");
        Texture t2 = mainGame.getAssetManager().get("box.png");
        Texture t3 = mainGame.getAssetManager().get("ground.png");
        Texture t4 = mainGame.getAssetManager().get("bullet.jpg");
        textrueList.add(t1);
        textrueList.add(t2);
        textrueList.add(t3);
        textrueList.add(t4);
        world.setContactListener(new GameListener());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

        mainActor = new MainActor(world, textrueList.get(0), 0.5f, 4f);
        groundActor = new GroundActor(world, textrueList.get(2), 0, 1000, 0);
        for (int i = 0; i < 20; i++) {
            boxActor = new BoxActor(world, textrueList.get(1), i * 8, 2.5f);
            stage.addActor(boxActor);
        }
        stage.addActor(mainActor);
        stage.addActor(groundActor);
        createButton();
        createTouchpad();
        stage.addActor(touchPad);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update();
        stage.act();
        world.step(delta, 6, 2);
        stage.draw();
    }



    @Override
    public void hide() {
        Gdx.app.log("chad", "ishide");
        groundActor.detach();
        groundActor.remove();
        for (BulletActor ba : bulletManager) {
            ba.remove();
        }
        for (BoxActor actor : boxActors) {
            actor.detach();
            actor.remove();
            boxActors.remove(actor);
        }
        mainActor.detach();
        mainActor.remove();

    }

    @Override
    public void dispose() {
        stage.dispose();
        for (Texture t : textrueList) {
            t.dispose();
        }
        super.dispose();
    }

    private class GameListener implements ContactListener {
        //檢查碰撞是否相同
        private boolean checkCollision(Contact contact, Object a, Object b) {

            return (contact.getFixtureA().getUserData().equals(a) && contact.getFixtureB().getUserData().equals(b) ||
                    contact.getFixtureA().getUserData().equals(a) && contact.getFixtureB().getUserData().equals(b));
        }

        ;

        @Override
        public void beginContact(Contact contact) {
            if (checkCollision(contact, "player", "ground")) {
                mainActor.jumping = false;
            }

            if (checkCollision(contact, "player", "tri")) {
                mainActor.alive = false;
            }

        }

        @Override
        public void endContact(Contact contact) {
            if (checkCollision(contact, "player", "ground")) {
                mainActor.jumping = true;
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    }
    private void createButton(){
        Texture myTexture = new Texture(Gdx.files.internal("touch.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture,0,0,200,100);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        buttonFire = new ImageButton(myTexRegionDrawable); //Set the button up
        buttonX=(int)(Gdx.graphics.getWidth()-buttonFire.getWidth());
        buttonFire.setPosition(buttonX,0);
        stage.addActor(buttonFire);
        buttonFire.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
//                Vector2 point = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX() , Gdx.input.getY()));
                Vector2 vector2 = new Vector2(mainActor.getRight()-mainActor.getWidth()/2, mainActor.getTop()-mainActor.getHeight()/2);
                BulletActor b1 = new BulletActor(stage,textrueList.get(3), vector2, mainActor.faceRight);
                bulletManager.add(b1);
                stage.addActor(b1);
            }
        });
        buttonJump = new ImageButton(myTexRegionDrawable); //Set the button up
        buttonX=(int)(Gdx.graphics.getWidth()-buttonFire.getWidth());
        buttonJump.setPosition(buttonX,200);
        stage.addActor(buttonJump);
        buttonJump.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(mainActor.jumping){

                }else {
                    mainActor.dojump = true;
                }
            }
        });

    }
    private void createTouchpad() {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchKnob", new Texture("touch1.png"));
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
        Pixmap background;background = new Pixmap(300, 300, Pixmap.Format.RGBA8888);
        background.setBlending(Pixmap.Blending.None);
        background.setColor(1, 1, 1, .6f);
        background.fillCircle(150, 150, 150);
        touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(background)));
        touchPad = new Touchpad(10, touchpadStyle);

        touchPad.setBounds(0, 0, 300, 300);
    }

    public void update() {
        if (touchPad.isTouched()) {//判断摇杆是否被按下
            int x = (int) (touchPad.getKnobPercentX() * 4);//改变相应的坐标.
            int y = (int) (touchPad.getKnobPercentY() * 4);
            Vector2 change = stage.screenToStageCoordinates(new Vector2(600,0));
           if(x>=0){
               if (mainActor.getX() > change.x ) {
                   stage.getCamera().translate(12, 0, 0);
                   touchPad.setPosition(touchPadX+=12,y);
                   buttonFire.setPosition(buttonX+=12,0);
                   buttonJump.setPosition(buttonX,200);
               }
               mainActor.setForward();
               mainActor.faceRight=true;
           }else if(x<0){
               if(mainActor.getX() < change.x ) {
                   stage.getCamera().translate(-12, 0, 0);
                   touchPad.setPosition(touchPadX -= 12, y);
                   buttonFire.setPosition(buttonX-=12,0);
                   buttonJump.setPosition(buttonX,200);
               }
               mainActor.setBack();
               mainActor.faceRight=false;
            }
        }else{
            mainActor.setStop();
        }

    }
}
