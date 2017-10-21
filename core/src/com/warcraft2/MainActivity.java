package com.warcraft2;

import com.badlogic.gdx.Game;

//  Game class also implements ApplicationListener
public class MainActivity extends Game {
	
	@Override
	public void create () {
        setScreen(new com.warcraft2.Screen.Splash(this));
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
