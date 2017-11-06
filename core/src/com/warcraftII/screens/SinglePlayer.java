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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.math.Vector3;
import com.warcraftII.Warcraft;
import com.warcraftII.parser.MapParser;
import com.warcraftII.units.Unit;

public class SinglePlayer implements Screen, GestureDetector.GestureListener{
    private Warcraft game;
    private TextureAtlas terrain;
    private SpriteBatch batch;
    private Sprite tile;
    private Music readySound;
    private SpriteBatch sb;
    private Texture texture;
    private Stage stage;
    private Skin skin;
    private Table table;
    private Vector<Sprite> peasant_vector;
    private MapParser map;
    private Unit all_units;
    private OrthographicCamera camera;
    SinglePlayer(com.warcraftII.Warcraft game) {
        this.game = game;
        //Implemented just to achieve hard goal. Not needed
        this.readySound = Gdx.audio.newMusic(Gdx.files.internal("data/snd/basic/ready.wav"));
    }

    @Override
    public void show() {
        all_units = new Unit();
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
        Gdx.input.setInputProcessor(stage);
        sb = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("img/PeasantStatic.png"));
        all_units.AddUnit(67,3,texture);
        all_units.AddUnit(9,4,texture);
        all_units.AddUnit(121,40,texture);
        all_units.AddUnit(47,68,texture);
        all_units.AddUnit(67,3,texture);
        all_units.AddUnit(91,123,texture);
        all_units.AddUnit(5,123,texture);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new MapParser(Gdx.files.internal("map/hedges.map"));
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
        batch.end();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        int counter = 0;
        while(counter < all_units.unit_vector.size()){
            Sprite temp_peasant = all_units.unit_vector.elementAt(counter).sprite;
            temp_peasant.draw(sb);
            counter+=1;
        }
        sb.end();
        all_units.AllMovement();
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
        Vector3 clickCoordinates = new Vector3(x,y,0);
        Vector3 position = camera.unproject(clickCoordinates);
        int counter = 0;
        int unit_selected = 0;
        while(counter < all_units.unit_vector.size()){
            Sprite temp_peasant = all_units.unit_vector.elementAt(counter).sprite;
            if (temp_peasant.getX() <= position.x && temp_peasant.getX() + temp_peasant.getWidth() >= position.x && temp_peasant.getY() <= position.y && temp_peasant.getY() + temp_peasant.getWidth() >= position.y) {
                //peasant.setPosition(peasant.getX()+1, peasant.getY()+1);
                // TODO Play Peasant Sound here
                // PEASANT SELECTED ==
                all_units.selected_unit_index = counter;
                unit_selected = 1;
            }
            counter+=1;
        }
        if (unit_selected == 0) {
            all_units.unit_vector.elementAt(all_units.selected_unit_index).currentymove = position.y;
            all_units.unit_vector.elementAt(all_units.selected_unit_index).currentxmove = position.x;
        }
        return true;
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
