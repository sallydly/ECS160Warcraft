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
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.math.Vector3;
import com.warcraftII.GameDataTypes;
import com.warcraftII.Warcraft;
import com.warcraftII.asset.AssetDecoratedMap;
import com.warcraftII.asset.SAssetInitialization;
import com.warcraftII.asset.player.PlayerAssetType;
import com.warcraftII.asset.player.PlayerData;
import com.warcraftII.asset.static_assets.StaticAssetParser;
import com.warcraftII.position.*;
import com.warcraftII.terrain.MapRenderer;
import com.warcraftII.terrain.TileTypes;
import com.warcraftII.units.Unit;


public class SinglePlayer implements Screen, GestureDetector.GestureListener{
    private Logger log = new Logger("SinglePlayer", 2);
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
    private Vector<Sprite> peasantVector;


    private AssetDecoratedMap map;
    private TiledMap tiledMap;
    private MapRenderer mapRenderer;
    private OrthogonalTiledMapRenderer orthomaprenderer;
    private MapProperties properties;

    private Unit allUnits;
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
    private float currentXMove;
    private float currentYMove;
    private int movementFlag;
    SinglePlayer(com.warcraftII.Warcraft game) {
        this.game = game;
        this.batch = game.batch;
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
        camera.update();

        return true;
    }

    @Override
    public void show() {
        allUnits = new Unit();
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
        Gdx.input.setInputProcessor(stage);
        sb = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("img/PeasantStatic.png"));
/*      allUnits.AddUnit(67,3,texture);
        allUnits.AddUnit(9,4,texture);
        allUnits.AddUnit(121,40,texture);
        allUnits.AddUnit(47,68,texture);
        allUnits.AddUnit(67,3,texture);
        allUnits.AddUnit(91,123,texture);

        allUnits.AddUnit(5,123,texture);*/
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        // Loading the map:
        tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();


        /* This section reads in from the terrainmap,
        feeds it to the map renderer, and adds a layer to the tilemap */
        int MapNum = AssetDecoratedMap.FindMapIndex(game.DMapName);
        log.info(String.valueOf(MapNum));
        map = AssetDecoratedMap.GetMap(MapNum);

        mapRenderer = new MapRenderer(map);
        StaticAssetParser staticAssetParser = new StaticAssetParser();


        // DEBUG
        UnitPosition.setMapDimensions(map);
        Position.setTileDimensions(32,32);

        PlayerAssetType.LoadTypes();

        Vector<PlayerData> playerData = PlayerData.LoadAllPlayers(map,allUnits);


        TiledMapTileLayer tileLayerBase = mapRenderer.DrawMap();
        layers.add(tileLayerBase);

        TiledMapTileLayer staticAssetsLayer = null;



            staticAssetsLayer = staticAssetParser.addStaticAssets(map,playerData);

        if (null != staticAssetsLayer){
            layers.add(staticAssetsLayer);
        }

        orthomaprenderer = new OrthogonalTiledMapRenderer(tiledMap);

        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);
        Gdx.input.setInputProcessor(new GestureDetector(this));

        // calculate zoom levels to show entire map height/width
        heightZoomRatio = map.Height() * tileHeight / camera.viewportHeight;
        widthZoomRatio = map.Width() * tileWidth / camera.viewportWidth;
    }

    //not sure where to put this function...
    public void RemoveLumber(TilePosition lumberlocation, TilePosition unitlocation, int amount)
    /* Original version had this:, but using data members of SinglePlayer instead for cleaner api...
    public void RemoveLumber(TilePosition lumberlocation, TilePosition unitlocation, int amount, AssetDecoratedMap map, MapRenderer maprend,  TiledMap tiledMap)
    */
    {
        log.info("Removing lumber from:" + String.valueOf(lumberlocation.X())+ " "+ String.valueOf(lumberlocation.Y()));

        if (map.RemoveLumber(lumberlocation, unitlocation, amount)){
            TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Terrain");
            mapRenderer.UpdateTile(lumberlocation,terrainLayer);

            terrainLayer.getCell(lumberlocation.X(), lumberlocation.Y()).getTile().getTextureRegion();

            if (map.TileType(lumberlocation) == TileTypes.ETileType.Stump)
                log.debug("I is stump");
        }
    }

//now for stone...
    public void RemoveStone(TilePosition stonelocation, TilePosition unitlocation, int amount)
    {
        log.info("Removing stone from:" + String.valueOf(stonelocation.X())+ " "+ String.valueOf(stonelocation.Y()));

        if (map.RemoveStone(stonelocation, unitlocation, amount)){
            TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Terrain");
            mapRenderer.UpdateTile(stonelocation,terrainLayer);

            terrainLayer.getCell(stonelocation.X(), stonelocation.Y()).getTile().getTextureRegion();

            if (map.TileType(stonelocation) == TileTypes.ETileType.Rubble)
                log.debug("I is rubble");
        }
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        camera.update();
        orthomaprenderer.setView(camera);
        orthomaprenderer.render();

        batch.end();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        int counter = 0;
        while(counter < allUnits.unitVector.size()){
            Sprite temp_peasant = allUnits.unitVector.elementAt(counter).sprite;
            temp_peasant.draw(sb);
            counter+=1;
        }
        sb.end();
        allUnits.AllMovement();
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
        tiledMap.dispose();
        orthomaprenderer.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        //TESTING REMOVELUMBER
        TilePosition tposunit = new TilePosition(12,1);
        TilePosition tree1 = new TilePosition(11,0);
        TilePosition tree2 = new TilePosition(12,1);
        TilePosition tree3 = new TilePosition(13,2);

        RemoveLumber(tree1,tposunit,400);
        RemoveLumber(tree2,tposunit,400);
        RemoveLumber(tree3,tposunit,400);



        Vector3 clickCoordinates = new Vector3(x,y,0);
        Vector3 position = camera.unproject(clickCoordinates);
        int counter = 0;
        int unit_selected = 0;
        while(counter < allUnits.unitVector.size()){
            Sprite temp_peasant = allUnits.unitVector.elementAt(counter).sprite;
            if (temp_peasant.getX() <= position.x && temp_peasant.getX() + temp_peasant.getWidth() >= position.x && temp_peasant.getY() <= position.y && temp_peasant.getY() + temp_peasant.getWidth() >= position.y) {
                //peasant.setPosition(peasant.getX()+1, peasant.getY()+1);
                // TODO Play Peasant Sound here
                // PEASANT SELECTED ==
                allUnits.selectedUnitIndex = counter;
                unit_selected = 1;
            }
            counter+=1;
        }
        if (unit_selected == 0) {
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = position.y;
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = position.x;
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
        if (.25f <= newZoomLevel) {
            camera.zoom = newZoomLevel;
        }

        // limit zoom to showing full map
        calculateCameraBounds();
        camera.update();

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
        if (map.Width() * tileWidth / camera.zoom < camera.viewportWidth) {
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
        if (map.Height() * tileHeight / camera.zoom < camera.viewportHeight) {
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

    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
