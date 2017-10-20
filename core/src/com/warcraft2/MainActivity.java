package com.warcraft2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//  Game class also implements ApplicationListener
public class MainActivity extends Game {
	//SpriteBatch batch;
	
	@Override
	public void create () {
        setScreen(new Splash(this));
		//batch = new SpriteBatch();

	}

	@Override
	public void render () {
        super.render();
		//Gdx.gl.glClearColor(1, 1, 1, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();

		//batch.end();
	}
	
	@Override
	public void dispose () {
        super.render();
		//batch.dispose();

	}
}
