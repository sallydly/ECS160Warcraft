package com.warcraftII.screens;

import java.util.*;
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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.math.Vector3;
import com.warcraftII.Warcraft;
import com.warcraftII.parser.MapParser;

public class SinglePlayer implements Screen, GestureDetector.GestureListener {
    private Warcraft game;
    private TextureAtlas terrain;
    private SpriteBatch batch;
    private Sprite tile;
    private Music readySound;

    private Sprite peasant;
    private SpriteBatch sb;
    private Texture texture;

    private Stage stage;
    private Skin skin;
    private Table table;
    private Vector<Sprite> peasant_vector;
    private MapParser map;

    private OrthographicCamera camera;

    // height and width of each map tile in pixels
    // TODO: may want to put these in a constants file or get MapParser.getTileHeight/getTileWidth working
    private int tileHeight = 32;
    private int tileWidth = 32;

    private float prevZoom = 1;
    // camera zoom levels to fit map height/width
    private float heightZoomRatio;
    private float widthZoomRatio;

    float prevScale = 1; //initial scale for zoom

    private double prevDistance = 0;
    private float currentxmove;
    private float currentymove;
    private int movement_flag;
   // public static void addPeasant(Peasant b) {
  //      peasants.add(b);
 //   }
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

        // limit panning to edge of map
        calculateCameraBounds();

        return true;
    }

    @Override
    public void show() {
        peasant_vector = new Vector<Sprite>(50);
        movement_flag = 0;
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

        sb = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("img/PeasantStatic.png"));
        peasant = new Sprite(texture);
        peasant.setPosition(67*32,3*32);
        peasant_vector.addElement(peasant);
        peasant = new Sprite(texture);
        peasant.setPosition(9*32,4*32);
        peasant_vector.addElement(peasant);
        peasant = new Sprite(texture);
        peasant.setPosition(121*32,40*32);
        peasant_vector.addElement(peasant);
        peasant = new Sprite(texture);
        peasant.setPosition(47*32,68*32);
        peasant_vector.addElement(peasant);
        peasant = new Sprite(texture);
        peasant.setPosition(91*32,123*32);
        peasant_vector.addElement(peasant);
        peasant = new Sprite(texture);
        peasant.setPosition(5*32,123*32);
        peasant_vector.addElement(peasant);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new MapParser(Gdx.files.internal("map/hedges.map"));
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);
        Gdx.input.setInputProcessor(new GestureDetector(this));

        // calculate zoom levels to show entire map height/width
        heightZoomRatio = map.getHeight() * tileHeight / camera.viewportHeight;
        widthZoomRatio = map.getWidth() * tileWidth / camera.viewportWidth;
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
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        //peasant.draw(sb);
        int counter = 0;
        int new_peasant = 0;
        while(counter < peasant_vector.size()){
            Sprite temp_peasant = peasant_vector.elementAt(counter);
            temp_peasant.draw(sb);
            counter+=1;
        }
        sb.end();
        if (peasant.getX() != currentxmove && movement_flag == 1) {
            if (peasant.getX() < currentxmove)
                peasant.setX(peasant.getX() + 1);
            if (peasant.getX() > currentxmove)
                peasant.setX(peasant.getX() - 1);
            if (peasant.getY() < currentymove)
                peasant.setY(peasant.getY() + 1);
            if (peasant.getY() > currentymove)
                peasant.setY(peasant.getY() - 1);
        }


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
    public void movement(Sprite character, Vector3 position){
        float currentx = character.getX();
        float currenty = character.getY();
        while (currentx != position.x) {
            if (currentx < position.x) {
                character.setPosition(currentx + 1, currenty);
                currentx += 1;
            }
            if (currentx > position.x) {
                character.setPosition(currentx - 1, currenty);
                currentx -= 1;
            }
        }
        while (currenty != position.y){
            if (currenty < position.y) {
                character.setPosition(currentx, currenty + 1);
                currenty += 1;
            }
            if (currenty > position.y) {
                character.setPosition(currentx, currenty - 1);
                currenty -= 1;
            }
        }
    }
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(x,y,0);
        Vector3 position = camera.unproject(clickCoordinates);
        int counter = 0;
        int new_peasant = 0;
        while(counter < peasant_vector.size()){
            Sprite temp_peasant = peasant_vector.elementAt(counter);
            if (temp_peasant.getX() <= position.x && temp_peasant.getX() + temp_peasant.getWidth() >= position.x && temp_peasant.getY() <= position.y && temp_peasant.getY() + temp_peasant.getWidth() >= position.y) {
                //peasant.setPosition(peasant.getX()+1, peasant.getY()+1);
                // TODO Play Peasant Sound here
                // PEASANT SELECTED ==
                readySound.play();

                peasant = temp_peasant;
                new_peasant = 1;
            }
            counter+=1;
        }
        if (new_peasant == 0) {
            //movement(peasant, position);
            currentxmove = position.x;
            currentymove = position.y;
            movement_flag = 1;
            //peasant.setX(peasant.getX() + 1);
            //peasant.setY(peasant.getY() + 1);
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // readySound.play();
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

        // limit zoom to showing full map
        calculateCameraBounds();

        return true;
    }

    private void calculateCameraBounds() {
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
        if (map.getWidth() * tileWidth / camera.zoom < camera.viewportWidth) {
            // if can zoom out more to show entire map height
            if (widthZoomRatio < heightZoomRatio) {
                // position camera at center of map horizontally
                camera.position.x = mapRight / 2;
            } else {
                // limit zoom to fit width of map
                camera.zoom = widthZoomRatio;
            }
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
        if (map.getHeight() * tileHeight / camera.zoom < camera.viewportHeight) {
            // if can zoom out more to show entire map width
            if (widthZoomRatio > heightZoomRatio) {
                // position camera at center of map vertically
                camera.position.y = mapTop / 2;
            } else {
                // limit zoom to fit height of map
                camera.zoom = heightZoomRatio;
            }
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
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
