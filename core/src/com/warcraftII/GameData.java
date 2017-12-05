package com.warcraftII;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.warcraftII.player_asset.PlayerAssetType;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.player_asset.StaticAsset;
import com.warcraftII.player_asset.VisibilityMap;
import com.warcraftII.position.Position;
import com.warcraftII.position.TilePosition;
import com.warcraftII.position.UnitPosition;
import com.warcraftII.renderer.FogRenderer;
import com.warcraftII.renderer.MapRenderer;
import com.warcraftII.renderer.StaticAssetRenderer;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.units.Unit;
import com.warcraftII.units.UnitActions;

import java.util.Vector;

/**
 * Created by Kimi on 11/15/2017.
 *
 * This is a class that will hold all of the objects used in the game
 * so that they can be easily be passed around and accessed by other objects.
 */

public class GameData {
    // height and width of each map tile in pixels
    public static final int TILE_HEIGHT = 32;
    public static final int TILE_WIDTH = 32;
    public static final float UPDATE_INTERVAL = (float) 0.001;
    public static final float UPDATE_FREQUENCY = 1/UPDATE_INTERVAL;
    public static final int SPEEDUP_FACTOR = 50;

    public TextureAtlas terrain;
    public TextureAtlas peasant;
    public SpriteBatch batch;

    public SpriteBatch sb;
    public Texture texture;
    public Skin skin;

    public AssetDecoratedMap map;
    public TiledMap tiledMap;
    public MapRenderer mapRenderer;
    public StaticAssetRenderer staticAssetRenderer;
    public FogRenderer fogRenderer;
    public OrthographicCamera mapCamera;

    public Vector<PlayerData> playerData;
    public PlayerData currentPlayer;

    public UnitActions unitActions;
    public Unit allUnits;
    public Vector<Unit.IndividualUnit> selectedUnits = new Vector<Unit.IndividualUnit>(9);
    public float elapsedTime;
    public float cumulativeTime = 0; // for slowing down timestep a bit.

    public GameData(){
        sb = new SpriteBatch();
        allUnits = new Unit();
        unitActions = new UnitActions();
        terrain = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));
        tiledMap = new TiledMap();
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        // initialize things that aren't dependent on the map.
        Position.setTileDimensions(TILE_HEIGHT,TILE_WIDTH);
        PlayerAssetType.LoadTypes();
    }

    public GameData(String mapName){
        this();
        int MapNum = AssetDecoratedMap.FindMapIndex(mapName);
        map = AssetDecoratedMap.GetMap(MapNum);

        UnitPosition.setMapDimensions(map);

        mapRenderer = new MapRenderer(map);
        staticAssetRenderer = new StaticAssetRenderer(tiledMap, map.Width(), map.Height(), mapName);
        fogRenderer = new FogRenderer();

        staticAssetRenderer.UpdateFrequency((int)UPDATE_FREQUENCY/SPEEDUP_FACTOR);
        playerData = PlayerData.LoadAllPlayers(map,allUnits);
        playerData.get(1).DIsAI = true;
        currentPlayer = playerData.get(1);
    }

    //ONLY USE AT THE BEGINNING.
    public void RenderMap(){
                /* Rendering the map: */
        MapLayers layers = tiledMap.getLayers();
        Vector<PlayerData> temp = new Vector<PlayerData>();
        Vector<Unit.IndividualUnit> currentPlayerUnits = new Vector<Unit.IndividualUnit>();

        temp.add(currentPlayer);
        for (Unit.IndividualUnit individualUnit : allUnits.GetAllUnits()) {
            if(individualUnit.color == currentPlayer.Color()) {
                currentPlayerUnits.add(individualUnit);
            }
        }

        TiledMapTileLayer fogLayer = fogRenderer.createFogLayer(map, currentPlayer, currentPlayerUnits);

        TiledMapTileLayer tileLayerBase = mapRenderer.DrawMap();
        layers.add(tileLayerBase);

        TiledMapTileLayer staticAssetsLayer = staticAssetRenderer.addStaticAssets(map, playerData);
        if (null != staticAssetsLayer){
            layers.add(staticAssetsLayer);
        }

        tiledMap.getLayers().add(fogLayer);
    }

    public void renderFog(Vector<Unit.IndividualUnit> currentPlayerUnits) {
        TiledMapTileLayer fogLayer = fogRenderer.createFogLayer(map, currentPlayer, currentPlayerUnits);
        int oldFogLayerIndex = tiledMap.getLayers().getIndex("Fog");

        tiledMap.getLayers().remove(oldFogLayerIndex);
        tiledMap.getLayers().add(fogLayer);
    }

    //not sure where to put this function...putting it here because it's a function that uses multiple classes
    public void RemoveLumber(TilePosition lumberlocation, TilePosition unitlocation, int amount)
    /* Original version had this:, but using data members of SinglePlayer instead for cleaner api...
    public void RemoveLumber(TilePosition lumberlocation, TilePosition unitlocation, int amount, AssetDecoratedMap map, MapRenderer maprend,  TiledMap tiledMap)
    */
    {
        //log.info("Removing lumber from:" + String.valueOf(lumberlocation.X())+ " "+ String.valueOf(lumberlocation.Y()));

        if (map.RemoveLumber(lumberlocation, unitlocation, amount)){
            TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Terrain");
            mapRenderer.UpdateTile(lumberlocation,terrainLayer);

            terrainLayer.getCell(lumberlocation.X(), lumberlocation.Y()).getTile().getTextureRegion();

        /*  if (map.TileType(lumberlocation) == TileTypes.ETileType.Stump)
                log.debug("I is stump");*/
        }
    }

    //now for stone...
    public void RemoveStone(TilePosition stonelocation, TilePosition unitlocation, int amount)
    {
        //log.info("Removing stone from:" + String.valueOf(stonelocation.X())+ " "+ String.valueOf(stonelocation.Y()));

        if (map.RemoveStone(stonelocation, unitlocation, amount)){
            TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Terrain");
            mapRenderer.UpdateTile(stonelocation,terrainLayer);

            terrainLayer.getCell(stonelocation.X(), stonelocation.Y()).getTile().getTextureRegion();

            /*if (map.TileType(stonelocation) == TileTypes.ETileType.Rubble)
                log.debug("I is rubble");*/
        }
    }

    //Naive timestep.
    public void TimeStep(){

        if (cumulativeTime < UPDATE_INTERVAL){
            return;
        }
        else{
            cumulativeTime = 0;
        }

        for (StaticAsset sasset : map.StaticAssets()) {
            if (GameDataTypes.EAssetAction.None == sasset.Action()) {
                //Do nothing. for now.
            }
            if (GameDataTypes.EAssetAction.Construct == sasset.Action()) {
                if (sasset.Step() < sasset.assetType().BuildTime() * staticAssetRenderer.UpdateFrequency()) {
                    sasset.IncrementStep();
                } else {
                    sasset.PopCommand();
                }
                //Do nothing. for now.
            }
            if (GameDataTypes.EAssetAction.Death == sasset.Action()) {
                //do nothing for now.
            }
            if(GameDataTypes.EAssetAction.Capability == sasset.Action()){
                //Thing is building a unit.  deal with it.
                if(sasset.Step() < sasset.DUnitConstructionTime * staticAssetRenderer.UpdateFrequency()) {
                    sasset.IncrementStep();
                }
                else
                {
                    TilePosition tpos = map.FindAssetPlacement(sasset); //fix asap
                    allUnits.AddUnit(tpos,sasset.DPendingUnitType,playerData.get(1).Color());
                    sasset.PopCommand();
                    sasset.DUnitConstructionTime = 0;
                    sasset.DPendingUnitType = GameDataTypes.EUnitType.None;
                }

            }
        }

        Vector<Unit.IndividualUnit> currentPlayerUnits = new Vector<Unit.IndividualUnit>();

        for (Unit.IndividualUnit individualUnit : allUnits.GetAllUnits()) {
            if(individualUnit.color == currentPlayer.Color()) {
                currentPlayerUnits.add(individualUnit);
            }
        }

        if (staticAssetRenderer.UpdateStaticAssets(tiledMap, map, playerData)) {
            renderFog(currentPlayerUnits);
        }

        for(Unit.IndividualUnit individualUnit : currentPlayerUnits) {
            if(individualUnit.isVisible) {
                renderFog(currentPlayerUnits);
                break;
            }
        }
    }

    public void dispose() {
        terrain.dispose();
        skin.dispose();
        tiledMap.dispose();
    }
}
