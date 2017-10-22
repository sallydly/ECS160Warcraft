package com.warcraft2;

import com.badlogic.gdx.Game;
import com.warcraft2.Screen.Splash;

//  Game class also implements ApplicationListener
public class MainActivity extends Game {
	
	@Override
	public void create () {
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
