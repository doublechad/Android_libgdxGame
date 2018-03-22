package org.iii.www;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {
	//資源管理員
	private AssetManager assetManager;
	//創造一個方法回傳管理員
	public AssetManager getAssetManager(){
		return assetManager;
	}

	@Override
	public void create () {
		assetManager = new AssetManager();
		//載入資源
		assetManager.load("skating.png", Texture.class);
		assetManager.load("box.png", Texture.class);
		assetManager.load("ground.png", Texture.class);
		assetManager.load("bullet.jpg", Texture.class);
		//結束載入
		assetManager.finishLoading();
		setScreen(new StageScreen(this));
	}


	

}
