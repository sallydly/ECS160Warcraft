package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.Warcraft;

public class Splash implements Screen {
    private static final Logger log = new Logger("SplashScreen", 2);
    private Warcraft game;
    private SpriteBatch batch;
    private Sprite splash;
    private Texture texture;
    //  set SCREEN_DURATION of splash screen
    private final float SCREEN_DURATION = 2;
    //  get time time when splash starts
    private float currentDuration = 0;

    /**
     * @param game contains game parameters passed in from the caller using setScreen()
     */
    public Splash(Warcraft game) {
        // disable continuous rendering
        Gdx.graphics.setContinuousRendering(false);

        this.game = game;
        this.batch = game.batch;
    }

    @Override
    public void show() {
        texture = new Texture("img/Splash.png");
        splash = new Sprite(texture);

        /*
         *  Splash.png contain 2 images, so Gdx.graphics.getHeight()*2 is use to set the upper
         *  image (to set to lower image, use setPosition(0, 0) instead) to be the splash screen,
         *  ultimately we want to parse one of the image.
         *  TODO: find a solution to correctly parse Splash.png image
         */
        splash.setPosition(0, -Gdx.graphics.getHeight());
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //  make splash draw itself onto the screen
        splash.draw(batch);
        batch.end();
        Gdx.graphics.requestRendering();
        currentDuration += delta;
        if(currentDuration > SCREEN_DURATION) {
            game.setScreen(new MainMenu(game));
        }
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

    /*
     *  SELF NOTE: It's a good idea to dispose objects that are disposable, since those tend to be
     *  large objects and they will slow down the game
     */
    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
        game.dispose();
    }
}
