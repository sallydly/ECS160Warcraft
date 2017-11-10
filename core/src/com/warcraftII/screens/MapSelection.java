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

import java.util.Vector;

import com.warcraftII.asset.AssetDecoratedMap;



/**
 * Created by Kimi on 11/5/2017.
 */

public class MapSelection implements Screen {
    private Warcraft game;
    private Texture texture;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Music music;

    public MapSelection(Warcraft game) {
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

        AssetDecoratedMap.LoadMaps(Gdx.files.internal("map"));
        Vector<TextButton> MapButtons = new Vector<TextButton>();

        for (final String MapName : AssetDecoratedMap.GetMapNames()) {
            TextButton Button = new TextButton(MapName, skin);
            Button.getLabel().setFontScale(2, 2);
            Button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.DMapName = MapName;
                    game.setScreen(new SinglePlayer(game));
                }
            });
            MapButtons.add(Button);
        }

        // TODO: Make this screen scrollable if there are too many maps
        for (TextButton Button : MapButtons) {
            menuTable.add(Button).fillX().uniformX();
            menuTable.row().pad(50, 0, 50, 0);
        }
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
//        music.pause();
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        music.dispose();
    }
}