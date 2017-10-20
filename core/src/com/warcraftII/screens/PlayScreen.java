package com.warcraftII.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.warcraftII.Warcraft;

/**
 * Created by Kevin on 10/20/2017.
 */

public class PlayScreen implements Screen {
    private Warcraft game;
    private Texture texture;

    public PlayScreen(Warcraft game) {
        this.game = game;
        this.texture = new Texture("badlogic.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
