package com.warcraft2.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.warcraft2.parser.MapParser;

/**
 * Created by hqmai on 10/21/17.
 */

public class SinglePlayer implements Screen {

    private TextureAtlas terrain;
    private SpriteBatch batch;
    private Sprite tile;

    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton backButton;

    private MapParser map;

    @Override
    public void show() {

        terrain = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));

        batch = new SpriteBatch();

        stage = new Stage();

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        tile = new Sprite(terrain.findRegion("shallow-water-F-0"));
        tile.setScale(5);
        tile.setPosition(300, 300);

        //TODO: this line is for testing purpose, delete it later
        backButton = new TextButton((new MapParser(Gdx.files.internal("map/bay.map"))).getName(), skin);
        //backButton = new TextButton("Back", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)(Gdx.app.getApplicationListener())).setScreen(new MainMenu());
            }
        });

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.add(backButton);
        table.align(Align.bottomLeft);



        stage = new Stage();
        //stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        map = new MapParser(Gdx.files.internal("map/hedges.map"));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for(int i = 0; i < map.getHeight(); i++) {
            for(int j = 0; j < map.getHeight(); j++) {
                map.spriteAt(i, j).draw(batch);
            }
        }




        batch.end();

        stage.act(delta);
        stage.draw();
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
        terrain.dispose();
        stage.dispose();
        skin.dispose();
    }
}
