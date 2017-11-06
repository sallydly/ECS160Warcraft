package com.warcraftII;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.screens.Splash;
import com.warcraftII.asset.AssetDecoratedMap;

//  Game class also implements ApplicationListener
public class Warcraft extends Game {
    private static final Logger log = new Logger("WarcraftMain", 2);
	public SpriteBatch batch;
	public String DMapName;


	@Override
	public void create () {
        log.info("Starting Warcraft Application");
        batch = new SpriteBatch();
        log.info("Starting up Splash screen");
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
