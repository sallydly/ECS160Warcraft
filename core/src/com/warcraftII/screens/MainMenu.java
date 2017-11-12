package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.warcraftII.Warcraft;

public class MainMenu implements Screen {
    private Warcraft game;
    private Texture texture;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Music music;

    public MainMenu(Warcraft game) {
        this.game = game;
        this.texture = new Texture("img/warcraft_icon.png");
        this.atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        this.skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);
        ScreenViewport port = new ScreenViewport();
        port.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.stage = new Stage(port, game.batch);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("data/snd/music/menu.mp3"));
        this.music.setLooping(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table menuTable = createMenuTable();
        music.play();
        stage.addActor(menuTable);
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.setFillParent(true);

        TextButton singlePlayerButton = new TextButton("Single Player Game", skin);
        TextButton multiPlayerButton = new TextButton("Multi Player Game", skin);
        TextButton optionsButton = new TextButton("Options", skin);

        singlePlayerButton.getLabel().setFontScale(2, 2);
        multiPlayerButton.getLabel().setFontScale(2, 2);
        optionsButton.getLabel().setFontScale(2, 2);

        singlePlayerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MapSelection(game));
            }
        });

        multiPlayerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiPlayer(game));
            }
        });

        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Options());
            }
        });

        menuTable.add(singlePlayerButton).fillX().uniformX();
        menuTable.row().pad(100, 0 , 100, 0);
        menuTable.add(multiPlayerButton).fillX().uniformX();
        menuTable.row();
        menuTable.add(optionsButton).fillX().uniformX();
        return menuTable;
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
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        music.pause();
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        music.dispose();
    }
}
