package com.warcraftII;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.warcraftII.screens.Splash;

//  Game class also implements ApplicationListener
public class Warcraft extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
        batch = new SpriteBatch();
        setScreen(new Splash(this));
	}

	@Override
	public void render () {
        super.render();
	}
	
	@Override
	public void dispose () {
        super.render();
	}
}
