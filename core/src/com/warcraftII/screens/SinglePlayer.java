package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.Warcraft;
import com.warcraftII.asset.AssetDecoratedMap;
import com.warcraftII.parser.MapParser;

public class SinglePlayer implements Screen, GestureDetector.GestureListener{
    private Logger log = new Logger("SinglePlayer", 2);
    private Warcraft game;
    private TextureAtlas terrain;
    private SpriteBatch batch;
    private Sprite tile;
    private Music readySound;

    private Stage stage;
    private Skin skin;
    private Table table;

    private MapParser map;

    private OrthographicCamera camera;

    SinglePlayer(com.warcraftII.Warcraft game) {
        this.game = game;
        this.batch = game.batch;
        //Implemented just to achieve hard goal. Not needed
        this.readySound = Gdx.audio.newMusic(Gdx.files.internal("data/snd/basic/ready.wav"));
    }

    @Override
    public void show() {

        terrain = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));

        stage = new Stage();

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        tile = new Sprite(terrain.findRegion("shallow-water-F-0"));
        tile.setScale(5);
        tile.setPosition(300, 300);

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.bottomLeft);

        stage = new Stage();
        //stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new MapParser(game.DMapName);
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        camera.update();
        map.render(camera);
        /*
        for(int i = 0; i < gameMap.getHeight(); i++) {
            for(int j = 0; j < gameMap.getHeight(); j++) {
                gameMap.spriteAt(i, j).draw(batch);
            }
        }*/
        batch.end();


        /*
        stage.act(delta);
        stage.draw();*/
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
        map.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        readySound.play();
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.translate(-deltaX,deltaY);
        camera.update();
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
