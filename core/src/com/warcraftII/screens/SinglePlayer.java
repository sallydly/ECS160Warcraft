package com.warcraftII.screens;

import java.util.*;
import java.lang.String;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.warcraftII.GameDataTypes;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.warcraftII.Warcraft;
import com.warcraftII.asset.AssetDecoratedMap;
import com.warcraftII.asset.StaticAssetParser;
import com.warcraftII.terrain.MapRenderer;
import com.warcraftII.units.Unit;
import com.warcraftII.units.UnitActions;
import static java.lang.Math.round;
import java.util.Vector;

public class SinglePlayer implements Screen, GestureDetector.GestureListener{
    private Logger log = new Logger("SinglePlayer", 2);
    private Warcraft game;
    private TextureAtlas terrain;
    private TextureAtlas peasant;
    private SpriteBatch batch;
    private Sprite tile;
    private Music readySound;
    private SpriteBatch sb;
    private Texture texture;
    private Skin skin;
    private Table table;
    private float elapsedTime;
    private int movement;
    public int attack;
    public int patrol;
    public int mine;
    public int ability;
    private InputMultiplexer multiplexer;

    private TextButton movementButton;
    private TextButton stopButton;
    private TextButton patrolButton;
    private TextButton attackButton;
    private TextButton newAbility;

    private AssetDecoratedMap map;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthomaprenderer;
    private MapProperties properties;

    private UnitActions unitActions;
    private Unit allUnits;

    private OrthographicCamera mapCamera;
    private FitViewport mapViewport;
    private Stage mapStage;

    private OrthographicCamera sidebarCamera;
    private FitViewport sidebarViewport;
    private Stage sidebarStage;

    private Table sidebarTable;

    // height and width of each map tile in pixels
    // TODO: may want to put these in a constants file or get MapParser.getTileHeight/getTileWidth working
    private int tileHeight = 32;
    private int tileWidth = 32;

    private float prevZoom = 1;
    // mapCamera zoom levels to fit map height/width
    private float heightZoomRatio;
    private float widthZoomRatio;

    private double prevDistance = 0;
    SinglePlayer(com.warcraftII.Warcraft game) {
        this.game = game;
        this.batch = game.batch;
        //Implemented just to achieve hard goal. Not needed
        this.readySound = Gdx.audio.newMusic(Gdx.files.internal("data/snd/basic/ready.wav"));
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // adjust pointer drag amount by mapCamera zoom level
        deltaX *= mapCamera.zoom;
        deltaY *= mapCamera.zoom;

        // move mapCamera based on distance of pointer drag
        mapCamera.translate(-deltaX, deltaY);

        // limit panning to edge of map
        calculateCameraBounds();
        mapCamera.update();

        return true;
    }

    @Override
    public void show() {
        allUnits = new Unit();
        unitActions = new UnitActions();

        movement = 0;
        attack = 0;
        patrol = 0;
        ability = 0;
        mine = 0;
        terrain = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));
        TextureAtlas[] unitTextures = {
                new TextureAtlas(Gdx.files.internal("atlas/Peasant.atlas")),
                new TextureAtlas(Gdx.files.internal("atlas/Footman.atlas")),
                new TextureAtlas(Gdx.files.internal("atlas/Archer.atlas")),
                new TextureAtlas(Gdx.files.internal("atlas/Ranger.atlas"))
        };

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        tile = new Sprite(terrain.findRegion("shallow-water-F-0"));
        tile.setScale(5);
        tile.setPosition(300, 300);
        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.bottomLeft);

//        stage = new Stage(new ScreenViewport());
        sb = new SpriteBatch();

        allUnits.AddUnit(690,3, GameDataTypes.EUnitType.Archer);
        allUnits.AddUnit(600,4, GameDataTypes.EUnitType.Footman);
        allUnits.AddUnit(770,40, GameDataTypes.EUnitType.Peasant);
        allUnits.AddUnit(900,68, GameDataTypes.EUnitType.Ranger);

//        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Make Buttons for the Unit Actions
        unitActions.createBasicSkin();
        movementButton = new TextButton("Move", unitActions.skin);
        stopButton = new TextButton("Stop", unitActions.skin);
        patrolButton = new TextButton("Patrol", unitActions.skin);
        attackButton = new TextButton("Attack", unitActions.skin);
        movementButton.setPosition(5 , 10);
        stopButton.setPosition(5 , 30+(1*Gdx.graphics.getHeight() / 10));
        patrolButton.setPosition(5 , 50+(2*Gdx.graphics.getHeight() / 10));
        attackButton.setPosition(5, 70+(3*Gdx.graphics.getHeight() / 10));
        movementButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                movement = 1;
                return true;
            }
        });

        stopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                allUnits.stopMovement();
                movement = 0;
                patrol = 0;
                attack = 0;
                mine = 0;
                ability = 0;
                return true;
            }
        });

        attackButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                attack = 1;
                return true;
            }
        });

        patrolButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                patrol = 1;
                return true;
            }
        });

//        stage.addActor(movementButton);
//        stage.addActor(stopButton);
//        stage.addActor(patrolButton);
//        stage.addActor(attackButton);
        
	mapCamera = new OrthographicCamera();
        mapViewport = new FitViewport(Gdx.graphics.getWidth() * .75f, Gdx.graphics.getHeight(), mapCamera);
        mapStage = new Stage(mapViewport);

        mapStage.getViewport().apply();
        mapStage.act();
        mapStage.draw();
        // set size of map viewport to 75% of the screen width and 100% height
        mapStage.getViewport().update(Math.round(Gdx.graphics.getWidth() * .75f), Gdx.graphics.getHeight(), false);
        // position map viewport on right 75% of the screen
        mapStage.getViewport().setScreenBounds(Math.round(Gdx.graphics.getWidth() * .25f), 0, Math.round(Gdx.graphics.getWidth() * .75f), Gdx.graphics.getHeight());
        mapStage.getViewport().apply();

        sidebarCamera = new OrthographicCamera();
//        sidebarCamera.setToOrtho(false, Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight());
        sidebarViewport = new FitViewport(Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight(), sidebarCamera);
        sidebarStage = new Stage(sidebarViewport);

        sidebarStage.getViewport().apply();
        sidebarStage.act();
        sidebarStage.draw();
        // set size of sidebar viewport to 25% of the screen width and 100% height
        sidebarStage.getViewport().update(Math.round(Gdx.graphics.getWidth() * .25f), Gdx.graphics.getHeight(), false);
        // position sidebar viewport on left 25% of the screen
        sidebarStage.getViewport().setScreenBounds(0, 0, Math.round(Gdx.graphics.getWidth() * .25f), Gdx.graphics.getHeight());
        sidebarStage.getViewport().apply();

        // set background texture image for sidebar menu
        // code adapted from https://libgdx.info/basic_image/
        Texture backgroundImageTexture = new Texture("img/Texture.png");
        backgroundImageTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion backgroundImageTextureRegion = new TextureRegion(backgroundImageTexture);
        backgroundImageTextureRegion.setRegion(0, 0, sidebarStage.getWidth(), sidebarStage.getHeight());

        //backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Image backgroundImage = new Image(backgroundImageTextureRegion);
        backgroundImage.setPosition(0, 0);
        sidebarStage.addActor(backgroundImage);

        //add the menu and pause buttons
        TextureAtlas atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        Skin skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);

        TextButton menuButton = new TextButton("Menu", skin);
        TextButton pauseButton = new TextButton("Pause", skin);
        TextButton attackButton = new TextButton("Attack", skin);
        TextButton patrolButton = new TextButton("Patrol", skin);
        TextButton stopButton = new TextButton("Stop", skin);
        TextButton moveButton = new TextButton("Move", skin);

        // table for layout of sidebar
        sidebarTable = new Table();
        sidebarTable.setDebug(true, true); // TODO: remove when done laying out table
        sidebarTable.setFillParent(true);
        sidebarTable.align(Align.top);
        sidebarStage.addActor(sidebarTable);
        sidebarStage.draw();

        /*
        Label nameLabel = new Label("Name:", skin);
        TextField nameText = new TextField("", skin);
        Label addressLabel = new Label("Address:", skin);
        TextField addressText = new TextField("", skin);
         */

        //add buttons to the sidebar menu
        sidebarTable.add(menuButton).width(sidebarStage.getWidth() * .5f);
        sidebarTable.add(pauseButton).width(sidebarStage.getWidth() * .5f);
        sidebarTable.row();
        sidebarTable.add(attackButton).width(sidebarStage.getWidth()).colspan(2);
        sidebarTable.row();
        sidebarTable.add(patrolButton).width(sidebarStage.getWidth()).colspan(2);
        sidebarTable.row();
        sidebarTable.add(stopButton).width(sidebarStage.getWidth()).colspan(2);
        sidebarTable.row();
        sidebarTable.add(moveButton).width(sidebarStage.getWidth()).colspan(2);
        sidebarStage.draw();

//        sidebarStage.getViewport().getCamera().lookAt(0,0,0);
//        sidebarStage.getViewport().getCamera().update();

        // Loading the map:
        tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();


        /* This section reads in from the terrainmap,
        feeds it to the map renderer, and adds a layer to the tilemap */
        int MapNum = AssetDecoratedMap.FindMapIndex(game.DMapName);
        log.info(String.valueOf(MapNum));
        map = AssetDecoratedMap.GetMap(MapNum);

        MapRenderer mapRenderer = new MapRenderer(map);
        StaticAssetParser staticAssetParser = new StaticAssetParser();

        TiledMapTileLayer tileLayerBase = mapRenderer.DrawMap();
        layers.add(tileLayerBase);

        TiledMapTileLayer staticAssetsLayer = staticAssetParser.addStaticAssets(map);
        layers.add(staticAssetsLayer);

        orthomaprenderer = new OrthogonalTiledMapRenderer(tiledMap);

//        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);

        multiplexer = new InputMultiplexer();
//        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(multiplexer);
        // Gdx.input.setInputProcessor(stage);
        //Gdx.input.setInputProcessor(new GestureDetector(this));

        // calculate zoom levels to show entire map height/width
//        heightZoomRatio = map.Height() * tileHeight / camera.viewportHeight;
//        widthZoomRatio = map.Width() * tileWidth / camera.viewportWidth;
        elapsedTime = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += Gdx.graphics.getDeltaTime();
        batch.begin();
        mapStage.getViewport().apply();
        mapStage.act();
        mapStage.draw();
        orthomaprenderer.setView(mapCamera);
        orthomaprenderer.render();

        batch.end();
        sb.setProjectionMatrix(mapCamera.combined);
        sb.begin();
        Texture selected = new Texture(Gdx.files.internal("img/select.png"));
        int counter = 0;
        while(counter < allUnits.unitVector.size()){
            Unit.IndividualUnit temp_peasant = allUnits.unitVector.elementAt(counter);
            //temp_peasant.draw(sb);
            // TODO: This isn't a good way of doing the selection and should be improved
            if (temp_peasant.selected) {
                sb.draw(selected,temp_peasant.sprite.getX(), temp_peasant.sprite.getY());
            }
            sb.draw(temp_peasant.curAnim.getKeyFrame(elapsedTime, true), temp_peasant.sprite.getX(), temp_peasant.sprite.getY());
            counter+=1;
        }
        sb.end();
//        stage.act();
//        stage.draw();
	sidebarStage.getViewport().apply();
        sidebarStage.act();
        sidebarStage.draw();
        allUnits.UnitStateHandler(elapsedTime, map);
    }

    public void specialButtons() {
        int counter = 0;
//        for (Actor actor : stage.getActors()) {
//            if (counter > 3)
//                actor.remove();
//            counter = counter + 1;
//        }
        for (int i = 0; i < allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).abilities.size(); i++) {
            if (allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).abilities.elementAt(i) == GameDataTypes.EAssetCapabilityType.Mine) {
                newAbility = new TextButton("Mine", unitActions.skin);
                newAbility.setPosition(5, 70+(20*(i+1))+((3+i+1)*Gdx.graphics.getHeight() / 10));
                newAbility.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        mine = 1;
                        return true;
                    }
                });
//                stage.addActor(newAbility);
            }
            if (allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).abilities.elementAt(i) == GameDataTypes.EAssetCapabilityType.RangerScouting) {
                newAbility = new TextButton("Ranger Scouting", unitActions.skin);
                newAbility.setPosition(5, 70 + (20 * (i + 1)) + ((3 + i + 1) * Gdx.graphics.getHeight() / 10));
                newAbility.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        ability = 1;
                        return true;
                    }
                });
//                stage.addActor(newAbility);
            }
        }
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
        mapStage.dispose();
        sidebarStage.dispose();
        skin.dispose();
        tiledMap.dispose();
        orthomaprenderer.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(x,y,0);
        Vector3 position = mapCamera.unproject(clickCoordinates);
        int counter = 0;
        int unit_selected = 0;
        while(counter < allUnits.unitVector.size()){
            Sprite temp_peasant = allUnits.unitVector.elementAt(counter).sprite;
            if (temp_peasant.getX() <= position.x && temp_peasant.getX() + temp_peasant.getWidth() >= position.x && temp_peasant.getY() <= position.y && temp_peasant.getY() + temp_peasant.getWidth() >= position.y) {
                //peasant.setPosition(peasant.getX()+1, peasant.getY()+1);
                // TODO Play Peasant Sound here
                // PEASANT SELECTED ==
                if (attack == 1) {
                    unit_selected = 1;
                    allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).target = allUnits.unitVector.elementAt(counter);
                    attack = 0;
                    allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Attack;
                    break;
                }
                //TODO
                //if (ability == 1) {
                //}
                allUnits.selectedUnitIndex = counter;
                unit_selected = 1;
                allUnits.unitVector.elementAt(counter).selected = true;
            } else {
                allUnits.unitVector.elementAt(counter).selected = false;
            }
            counter+=1;
        }
        specialButtons();
        //if asset is at position.x position.y then assetSelected = 1 and selectedAsset =  asset
        if (unit_selected == 0 && movement == 1) {
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Move;
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = round(position.y);
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = round(position.x);
	        movement = 0;
        }
        if (unit_selected == 0 && patrol == 1) {
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Patrol;
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = round(position.y);
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = round(position.x);
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).patrolxmove = allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).sprite.getX();
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).patrolymove = allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).sprite.getY();
            patrol = 0;
        }
        //TODO
        //if (unit_selected == 0 && assetSelected == 1){
          //  if (assetSelected == Goldmine && mine == 1) {
            //    allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Mine;
              //  allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = round(position.y);
               // allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = round(position.x);
                //mine = 1;
            //}
        //}
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
            prevZoom = mapCamera.zoom;
        }

        float ratio = initialDistance / distance;
        float newZoomLevel = prevZoom * ratio;
        // change zoom level only if above minimum level
        if (.5f <= newZoomLevel) {
            mapCamera.zoom = newZoomLevel;
        }

        // limit zoom to showing full map
        calculateCameraBounds();
        mapCamera.update();

        return true;
    }

    private void calculateCameraBounds() {
        // code adapted from https://gamedev.stackexchange.com/questions/74926/libgdx-keep-camera-within-bounds-of-tiledmap
        // The left boundary of the map (x)
        int mapLeft = 0;
        // The right boundary of the map (x + width)
        int mapRight = map.Width() * tileWidth;
        // The bottom boundary of the map (y)
        int mapBottom = 0;
        // The top boundary of the map (y + height)
        int mapTop = map.Height() * tileHeight;

        // The mapCamera dimensions, halved
        float cameraHalfWidth = mapCamera.viewportWidth * mapCamera.zoom * .5f;
        float cameraHalfHeight = mapCamera.viewportHeight * mapCamera.zoom * .5f;

        // calculate positions of boundaries of mapCamera
        float cameraLeft = mapCamera.position.x - cameraHalfWidth;
        float cameraRight = mapCamera.position.x + cameraHalfWidth;
        float cameraBottom = mapCamera.position.y - cameraHalfHeight;
        float cameraTop = mapCamera.position.y + cameraHalfHeight;

        // Horizontal axis
        // if map width is smaller than viewport width
        if (map.Width() * tileWidth / mapCamera.zoom < mapCamera.viewportWidth) {
            // if can zoom out more to show entire map height
            if (widthZoomRatio < heightZoomRatio) {
                // position mapCamera at center of map horizontally
                mapCamera.position.x = mapRight / 2;
            } else {
                // limit zoom to fit width of map
                mapCamera.zoom = widthZoomRatio;
            }
        }
        // else if left boundary of mapCamera is outside of map's left boundary
        else if (cameraLeft <= mapLeft) {
            // align mapCamera and map's left edge
            mapCamera.position.x = mapLeft + cameraHalfWidth;
        }
        // else if right boundary of mapCamera is outside of map's right boundary
        else if (cameraRight >= mapRight) {
            // align mapCamera and map's right edge
            mapCamera.position.x = mapRight - cameraHalfWidth;
        }

        // Vertical axis
        // if map height is smaller than viewport height
        if (map.Height() * tileHeight / mapCamera.zoom < mapCamera.viewportHeight) {
            // if can zoom out more to show entire map width
            if (widthZoomRatio > heightZoomRatio) {
                // position mapCamera at center of map vertically
                mapCamera.position.y = mapTop / 2;
            } else {
                // limit zoom to fit height of map
                mapCamera.zoom = heightZoomRatio;
            }
        }
        // else if bottom boundary of mapCamera is outside of map's bottom boundary
        else if (cameraBottom <= mapBottom) {
            // align mapCamera and map's bottom edge
            mapCamera.position.y = mapBottom + cameraHalfHeight;
        }
        // else if top boundary of mapCamera is outside of map's top boundary
        else if (cameraTop >= mapTop) {
            // align mapCamera and map's top edge
            mapCamera.position.y = mapTop - cameraHalfHeight;
        }

    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
