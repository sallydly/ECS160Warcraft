package com.warcraftII.asset.static_assets;


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
import java.util.Vector;
import java.util.List;

public class StaticAssetParser {
    private static final Logger log = new Logger("StaticAssetParser", 2);

    private TextureAtlas staticAssetTextures;
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
        this.staticAssetTextures = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets_32.atlas"));
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

        this.staticAssetTextures = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets_32.atlas"));
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
                GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, "goldmine-inactive");
            } else if (TOWNHALL.equals(AssetType)) {
                GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, "townhall-inactive");
            }
        }

        return assetLayer;
    }

    // This one takes in the map AND PlayerData object, and a tiledMapTileLayer that it edits.
    public TiledMapTileLayer addStaticAssets(AssetDecoratedMap map, Vector<PlayerData> playerData) {
        System.out.println("start render");

        staticAssetTextures = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets_32.atlas"));

        assetLayer = new TiledMapTileLayer(map.Width(), map.Height(), 32, 32);
        assetLayer.setName("StationaryAssets");

        for (PlayerData player : playerData) {
            for (StaticAsset StatAsset : player.StaticAssets()) {
                String tileName, typeName, stateName;

                switch (StatAsset.type()) {
                    case GoldMine:
                        typeName = "goldmine-";
                        break;
                    case TownHall:
                        typeName = "townhall-";
                        break;
                    case Keep:
                        typeName = "keep-";
                        break;
                    case Castle:
                        typeName = "castle-";
                        break;
                    case Farm:
                        typeName = "farm-";
                        break;
                    case Barracks:
                        typeName = "barracks-";
                        break;
                    case Blacksmith:
                        typeName = "blacksmith-";
                        break;
                    case ScoutTower:
                        typeName = "scouttower-";
                        break;
                    case GuardTower:
                        typeName = "guardtower-";
                        break;
                    case CannonTower:
                        typeName = "cannontower-";
                        break;
                    default:
                        //BAD STUFF
                        typeName = "badtype-";
                        break;
                }
                switch (StatAsset.state()) {
                    case CONSTRUCT_0:
                        stateName = "construct-0";
                        break;
                    case CONSTRUCT_1:
                        stateName = "construct-1";
                        break;
                    case ACTIVE:
                        stateName = "active";
                        break;
                    case INACTIVE:
                        stateName = "inactive";
                        break;
                    case PLACE:
                        stateName = "place";
                        break;
                    default:
                        //BAD STUFF
                        stateName = "badstate";
                        break;
                }
                tileName = typeName + stateName;

                log.info("tileName: " + tileName);
                int XPos = StatAsset.tilePosition().X();
                //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
                int YPos = map.Height() - StatAsset.tilePosition().Y() - 1; // -1 to account for 0 index
                GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, tileName);
                log.info("placed at: " + String.valueOf(XPos) +" " + String.valueOf(YPos));
            }
        }
        return assetLayer;
    }

}

