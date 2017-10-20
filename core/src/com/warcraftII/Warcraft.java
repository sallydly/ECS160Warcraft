package com.warcraftII;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.screens.MainMenuScreen;

public class Warcraft extends Game {
    private static final Logger log = new Logger("Warcraft", 2);
	public SpriteBatch batch;

	public void create () {
        log.info("Starting up game");
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
    }

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
