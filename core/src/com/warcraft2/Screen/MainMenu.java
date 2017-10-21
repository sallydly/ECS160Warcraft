package com.warcraft2.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by hqmai on 10/20/17.
 */

public class MainMenu implements Screen {

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Sprite background;

    private Stage stage;
    //  for objects (buttons) position on screen
    private Table table;
    private TextButton singlePlayerButton, multiPlayerButton, exitButton;
    private Skin buttonSkin;
    private BitmapFont font;
    private TextureAtlas atlas;

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("img/Texture.png");
        background = new Sprite(backgroundTexture);
        /*
         *  Currently I just simply zoom the image in Texture.png so it fit the screen.
         *  TODO: find solution to fill the screen with the image in Texture.png instead
         */
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //  load the font file to font member
        font = new BitmapFont(Gdx.files.internal("fonts/WoW_font.fnt"), false);




    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);


        batch.end();
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
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
        buttonSkin.dispose();
        font.dispose();
        atlas.dispose();
    }
}
