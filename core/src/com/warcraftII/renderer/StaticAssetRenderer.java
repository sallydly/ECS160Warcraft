package com.warcraftII.renderer;


/*
* Adds stationary assets onto a new layer of map
*
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.terrain_map.initialization.SAssetInitialization;
import com.warcraftII.player_asset.StaticAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class StaticAssetRenderer {
    private static final Logger log = new Logger("StaticAssetRenderer", 2);

    private static Map<EStaticAssetType,TextureAtlas> DStaticAssetTextures;
    private static Map<EStaticAssetType,String> DTypeNameTranslation;

    private TiledMap tiledMap;
    private TiledMapTileLayer assetLayer;
    private String[] staticAssetsArray;
    //private final String mapName;


    static{ MakeAtlases();}
    private static void MakeAtlases(){
        DTypeNameTranslation = new HashMap<EStaticAssetType,String>();
        {
            DTypeNameTranslation.put( EStaticAssetType.GoldMine, "GoldMine");
            DTypeNameTranslation.put(EStaticAssetType.TownHall, "TownHall");
            DTypeNameTranslation.put(EStaticAssetType.Keep, "Keep");
            DTypeNameTranslation.put(EStaticAssetType.Castle, "Castle");
            DTypeNameTranslation.put(EStaticAssetType.Farm,"Farm");
            DTypeNameTranslation.put(EStaticAssetType.Barracks, "Barracks");
            DTypeNameTranslation.put(EStaticAssetType.LumberMill,"LumberMill");
            DTypeNameTranslation.put(EStaticAssetType.Blacksmith, "Blacksmith");
            DTypeNameTranslation.put(EStaticAssetType.ScoutTower, "ScoutTower");
            DTypeNameTranslation.put(EStaticAssetType.GuardTower,"GuardTower");
            DTypeNameTranslation.put(EStaticAssetType.CannonTower,"CannonTower");
            DTypeNameTranslation.put(EStaticAssetType.Wall, "Wall");
        }

        for (EStaticAssetType assetType : DTypeNameTranslation.keySet())
        {
            String filePathString = "atlas/" + DTypeNameTranslation.get(assetType) + ".atlas";
            TextureAtlas Textures = new TextureAtlas(Gdx.files.internal(filePathString));
            DStaticAssetTextures.put(assetType,Textures);
        }

    }

    public StaticAssetRenderer(TiledMap tiledMap,
                               int mapWidth,
                               int mapHeight,
                               String mapName) {
        this.DStaticAssetTextures = new HashMap<EStaticAssetType, TextureAtlas>();
        this.tiledMap = tiledMap;
        this.assetLayer = new TiledMapTileLayer(mapWidth, mapHeight, 32, 32);
        //this.mapName = mapName;
        /*
        * Unsure if textures larger than 32x32 can be placed on layer easily
        * May have to separate atlas into 32x32, 64x64, 128x128 pages and create separate layers for those
         */

    }



    // This one takes in the map AND PlayerData object, and a tiledMapTileLayer that it edits.
    public TiledMapTileLayer addStaticAssets(AssetDecoratedMap map, Vector<PlayerData> playerData) {
        System.out.println("start render");

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

