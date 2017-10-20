package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.warcraftII.Warcraft;

/**
 * Created by Kevin on 10/20/2017.
 */

public class MainMenuScreen implements Screen {
    private Warcraft game;
    private Texture texture;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;

    public MainMenuScreen(Warcraft game) {
        this.game = game;
        this.texture = new Texture("warcraft_icon.png");
        this.atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        this.skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);
        this.stage = new Stage(new ScreenViewport(), game.batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table menuTable = new Table();
        menuTable.setFillParent(true);

        TextButton singlePlayerButton = new TextButton("Play Single-Player", skin);
        TextButton multiPlayerButton = new TextButton("Play Multi-Player", skin);
        TextButton optionsButton = new TextButton("Options", skin);

        singlePlayerButton.getLabel().setFontScale(2, 2);
        multiPlayerButton.getLabel().setFontScale(2, 2);
        optionsButton.getLabel().setFontScale(2, 2);

        //TODO: Add Listeners for the buttons

        menuTable.add(singlePlayerButton).fillX().uniformX();
        menuTable.row().pad(100, 0 , 100, 0);
        menuTable.add(multiPlayerButton).fillX().uniformX();
        menuTable.row();
        menuTable.add(optionsButton).fillX().uniformX();

        stage.addActor(menuTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        game.batch.begin();
        game.batch.draw(texture, 0, 0);
        game.batch.end();
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
        skin.dispose();
        atlas.dispose();
    }
}
