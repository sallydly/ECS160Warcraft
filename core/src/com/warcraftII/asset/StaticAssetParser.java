package com.warcraftII.asset;


/*
* Adds stationary assets onto a new layer of map
*
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.asset.AssetDecoratedMap;
import com.warcraftII.asset.GraphicTileset;
import com.warcraftII.asset.SAssetInitialization;
import com.warcraftII.position.TilePosition;

import com.warcraftII.asset.player.*;

import java.util.Arrays;
import java.util.List;

public class StaticAssetParser {
    //private static final Logger log = new Logger("StaticAssetParser", 2);

    private TextureAtlas staticAssets;
    private TiledMap tiledMap;
    private TiledMapTileLayer assetLayer;
    private String[] staticAssetsArray;
    //private final String mapName;
    private static final String TOWNHALL = "TownHall";
    private static final String GOLDMINE = "GoldMine";
    private static final String PEASANT = "Peasant";

    public StaticAssetParser(TiledMap tiledMap,
                             int mapWidth,
                             int mapHeight,
                             String mapName) {
        this.staticAssets = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets_32.atlas"));
        this.tiledMap = tiledMap;
        this.assetLayer = new TiledMapTileLayer(mapWidth, mapHeight, 32, 32);
        //this.mapName = mapName;
        /*
        * Unsure if textures larger than 32x32 can be placed on layer easily
        * May have to separate atlas into 32x32, 64x64, 128x128 pages and create separate layers for those
         */

    }

    public StaticAssetParser() {

    }

    // takes in an AssetDecoratedMap instead
    public TiledMapTileLayer addStaticAssets(AssetDecoratedMap map) {
        List< SAssetInitialization > AssetInitializationList = map.AssetInitializationList();
        System.out.println("start render");

        this.staticAssets = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets_32.atlas"));
        this.assetLayer = new TiledMapTileLayer(map.Width(), map.Height(), 32, 32);
        this.assetLayer.setName("StationaryAssets");

        for(SAssetInitialization AssetInit : AssetInitializationList) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion textureRegion = new TextureRegion();

            int XPos = AssetInit.DTilePosition.X();
            //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
            int YPos = map.Height() - AssetInit.DTilePosition.Y() - 1; // -1 to account for 0 index

            String AssetType = AssetInit.DType;


            if (GOLDMINE.equals(AssetType)) {
                GraphicTileset.DrawTile(staticAssets, assetLayer, XPos, YPos, "goldmine-inactive");
            } else if (TOWNHALL.equals(AssetType)) {
                GraphicTileset.DrawTile(staticAssets, assetLayer, XPos, YPos, "townhall-inactive");
            }
        }

        return assetLayer;
    }

    // This one takes in a vector of PlayerData objects
    public TiledMapTileLayer addStaticAssets(PlayerData) {
        for (StationaryAsset StatAsset: PlayerData.StaticAssets()){
            String tileName, typeName, stateName;
            StatAsset.Type
             PlayerAssetType      sassesttype;
            switch (sassesttype.Type()){
                case GoldMine:
                    typeName = "goldmine-";
                case TownHall:
                    typeName = "townhall-";
                case Keep:
                    typeName = "keep-";
                case Castle:
                    typeName = "castle-";
                case Farm:
                    typeName = "farm-";
                case Barracks:
                    typeName = "barracks-";
                case Blacksmith:
                    typeName = "blacksmith-";
                case ScoutTower:
                    typeName = "scouttower-";
                case GuardTower:
                    typeName = "guardtower-"
                case CannonTower:
                    typeName = "cannontower-"
                default:
                    //BAD STUFF
                }
            switch (StatAsset.State()){

            }
        }
    }

}

