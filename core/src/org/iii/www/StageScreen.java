package org.iii.www;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.iii.www.Background;
import org.iii.www.MainGame;
import org.iii.www.actors.BoxActor;
import org.iii.www.actors.GroundActor;
import org.iii.www.actors.MainActor;

import java.util.ArrayList;

/**
 * Created by User on 2018/3/21.
 */

public class StageScreen extends Background{
    private MainGame mainGame;
    private Stage stage ;
    private MainActor mainActor;
    private GroundActor groundActor;
    private BoxActor boxActor;
    private ArrayList<Texture> textrueList ;
    private  World world;
    public StageScreen(MainGame mainGame) {
        this.mainGame=mainGame;
        //場景
        stage = new Stage();
        world =new World(new Vector2(0,-9.81f),true);
        //存放圖形的List
        textrueList=new ArrayList();
        Texture t1 =mainGame.getAssetManager().get("skating.png");
        Texture t2 =mainGame.getAssetManager().get("box.png");
        Texture t3 =mainGame.getAssetManager().get("ground.png");
        textrueList.add(t1);
        textrueList.add(t2);
        textrueList.add(t3);
        world.setContactListener(new GameListener());

    }

    @Override
    public void show() {
        mainActor= new MainActor(world,textrueList.get(0),0.5f,4f);
        groundActor= new GroundActor(world,textrueList.get(2),0,1000,0);
        boxActor = new BoxActor(world,textrueList.get(1),8,1);
        stage.addActor(mainActor);
        stage.addActor(groundActor);
        stage.addActor(boxActor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0.5f,0.5f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       stage.act();
       if(mainActor.getX()>300&&mainActor.alive) {
           stage.getCamera().translate(delta * MyAPI.PLAYER_SPEED * MyAPI.PIXE_TRANSFOR_MILE, 0, 0);
       }
       world.step(delta,6,2);
       stage.draw();
    }

    @Override
    public void hide() {
        groundActor.detach();
        groundActor.remove();
        boxActor.detach();
        boxActor.remove();
        mainActor.detach();
        mainActor.remove();

    }

    @Override
    public void dispose() {
        stage.dispose();
        for(Texture t :textrueList){
            t.dispose();
        }
        super.dispose();
    }
    private class GameListener implements ContactListener {
        //檢查碰撞是否相同
        private boolean checkCollision(Contact contact,Object a,Object b){

            return (contact.getFixtureA().getUserData().equals(a)&&contact.getFixtureB().getUserData().equals(b)||
                    contact.getFixtureA().getUserData().equals(a)&&contact.getFixtureB().getUserData().equals(b));
        };
        @Override
        public void beginContact(Contact contact) {
            if(checkCollision(contact,"player","ground")){
                mainActor.jumping=false;
            }

            if(checkCollision(contact,"player","tri")){
                mainActor.alive=false;
            }

        }

        @Override
        public void endContact(Contact contact) {
            if(checkCollision(contact,"player","ground")){
                mainActor.jumping=true;
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    }
}
