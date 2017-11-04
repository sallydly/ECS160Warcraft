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
import com.warcraftII.Warcraft;
import com.warcraftII.parser.MapParser;

public class SinglePlayer implements Screen, GestureDetector.GestureListener {
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

    float prevZoom = 1;
    private double prevDistance = 0;

    SinglePlayer(com.warcraftII.Warcraft game) {
        this.game = game;
        //Implemented just to achieve hard goal. Not needed
        this.readySound = Gdx.audio.newMusic(Gdx.files.internal("data/snd/basic/ready.wav"));
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // adjust pointer drag amount by camera zoom level
        deltaX *= camera.zoom;
        deltaY *= camera.zoom;

        // move camera based on distance of pointer drag
        camera.translate(-deltaX, deltaY);

        // height and width of each map tile in pixels
        // TODO: may want to put these in a constants file
        int tileHeight = 32;
        int tileWidth = 32;

        // code adapted from https://gamedev.stackexchange.com/questions/74926/libgdx-keep-camera-within-bounds-of-tiledmap
        // The left boundary of the map (x)
        int mapLeft = 0;
        // The right boundary of the map (x + width)
        int mapRight = map.getWidth() * tileWidth;
        // The bottom boundary of the map (y)
        int mapBottom = 0;
        // The top boundary of the map (y + height)
        int mapTop = map.getHeight() * tileHeight;

        // The camera dimensions, halved
        float cameraHalfWidth = camera.viewportWidth * camera.zoom * .5f;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom * .5f;

        // calculate positions of boundaries of camera
        float cameraLeft = camera.position.x - cameraHalfWidth;
        float cameraRight = camera.position.x + cameraHalfWidth;
        float cameraBottom = camera.position.y - cameraHalfHeight;
        float cameraTop = camera.position.y + cameraHalfHeight;

        // Horizontal axis
        // if map width is smaller than viewport width
        if (map.getWidth() * tileWidth < camera.viewportWidth) {
            // position camera at center of map horizontally
            camera.position.x = mapRight / 2;
        }
        // else if left boundary of camera is outside of map's left boundary
        else if (cameraLeft <= mapLeft) {
            // align camera and map's left edge
            camera.position.x = mapLeft + cameraHalfWidth;
        }
        // else if right boundary of camera is outside of map's right boundary
        else if (cameraRight >= mapRight) {
            // align camera and map's right edge
            camera.position.x = mapRight - cameraHalfWidth;
        }

        // Vertical axis
        // if map height is smaller than viewport height
        if (map.getHeight() * tileWidth < camera.viewportHeight) {
            // position camera at center of map vertically
            camera.position.y = mapTop / 2;
        }
        // else if bottom boundary of camera is outside of map's bottom boundary
        else if (cameraBottom <= mapBottom) {
            // align camera and map's bottom edge
            camera.position.y = mapBottom + cameraHalfHeight;
        }
        // else if top boundary of camera is outside of map's top boundary
        else if (cameraTop >= mapTop) {
            // align camera and map's top edge
            camera.position.y = mapTop - cameraHalfHeight;
        }

        camera.update();

        return true;
    }

    @Override
    public void show() {

        terrain = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));

        batch = new SpriteBatch();

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
        map = new MapParser(Gdx.files.internal("map/hedges.map"));
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        camera.update();
        map.render(camera);
        /*
        for(int i = 0; i < map.getHeight(); i++) {
            for(int j = 0; j < map.getHeight(); j++) {
                map.spriteAt(i, j).draw(batch);
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
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (Math.abs(distance) <= 1) {
            return false;
        }
        if (initialDistance != prevDistance) {
            prevDistance = initialDistance;
            prevZoom = camera.zoom;
        }

        float ratio = initialDistance / distance;
        float newZoomLevel = prevZoom * ratio;
        // change zoom level only if above minimum level
        if (.5f <= newZoomLevel) {
            camera.zoom = newZoomLevel;
        }

        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
