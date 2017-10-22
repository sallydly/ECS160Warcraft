package com.warcraft2.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by hqmai on 10/20/17.
 */

public class MainMenu implements Screen {

    //private SpriteBatch batch;

    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton singlePlayerButton, multiPlayerButton, exitButton;

    @Override
    public void show() {
        //batch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        singlePlayerButton = new TextButton("Single Player Mode", skin);
        multiPlayerButton = new TextButton("Multible Players Mode", skin);
        exitButton = new TextButton("Exit", skin);

        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)(Gdx.app.getApplicationListener())).setScreen(new SinglePlayer());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.add(singlePlayerButton).row();
        table.add(multiPlayerButton).row();
        table.add(exitButton).row();

        //table.debug();

        stage = new Stage();
        stage.addActor(table);

        // This allow the button to be pressed
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.9f, 0.7f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.begin();

        stage.act(delta);
        stage.draw();

        //batch.end();
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
        stage.dispose();
        skin.dispose();
    }
}
