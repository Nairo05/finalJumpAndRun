package de.nichtsroffler.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.nichtsroffler.screens.ScreenManager;

public class JumpAndRunMain extends Game {

	public SpriteBatch spriteBatch;
	public AssetManager assetManager;
	public ScreenManager screenManager;

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		assetManager = new AssetManager();
		screenManager = new ScreenManager(JumpAndRunMain.this);
	}

	@Override
	public void render () {
		super.render();
		if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
			screenManager.nextScreen();
		}
	}
	
	@Override
	public void dispose () {
		super.dispose();
		assetManager.dispose();
		spriteBatch.dispose();
	}
}
