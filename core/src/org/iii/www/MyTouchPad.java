package org.iii.www;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by User on 2018/3/23.
 */

public class MyTouchPad implements ApplicationListener {
    Stage stage;

    //绘制游戏摇杆的相关类
    Touchpad touchPad;
    Touchpad.TouchpadStyle style;
    TextureRegionDrawable background;
    TextureRegionDrawable knobRegion;

    Texture texture;
    Texture killer;

    SpriteBatch batch;

    public static int speed = 3;//用于控制图片的移动
    int x = 0;
    int y = 0;
    MyTouchPad(Stage stage){
        this.stage = stage;
    }

    @Override
    public void create() {



//        killer = new Texture(Gdx.files.internal("2.png"));
        texture = new Texture(Gdx.files.internal("touch.png"));
        background = new TextureRegionDrawable(new TextureRegion(texture, 0, 0,128,128));
        knobRegion = new TextureRegionDrawable(new TextureRegion(texture,128,0,128,128));
        style = new Touchpad.TouchpadStyle(background, knobRegion);
        touchPad = new Touchpad(15, style);//初始化游戏摇杆。(摇杆触碰区域的半径大小,TouchPagStyle)
        touchPad.setBounds(0, 0, 150, 150);//设置摇杆的位置和大小

        batch = new SpriteBatch();

        stage.addActor(touchPad);

        Gdx.input.setInputProcessor(stage);
    }
    private void update(){
        if(touchPad.isTouched()){//判断摇杆是否被按下
            x += touchPad.getKnobPercentX()*speed;//改变相应的坐标.
            y += touchPad.getKnobPercentY()*speed;

        }
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();//这个方法别漏了

        batch.begin();
        batch.draw(killer, x,y, 70,70);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
