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
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.GameDataTypes;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.terrain_map.initialization.SAssetInitialization;
import com.warcraftII.player_asset.StaticAsset;

import java.util.Random;
import java.util.Vector;
import java.util.List;

import javax.xml.soap.Text;

public class StaticAssetRenderer {
    private static final Logger log = new Logger("StaticAssetRenderer", 2);

    private TextureAtlas staticAssetTextures;
    private TextureAtlas animationTextures;
    private TiledMap tiledMap;
    private TiledMapTileLayer assetLayer;
    private String[] staticAssetsArray;

    private static final int BuildingDeathMaxIndex = 15;

    //private final String mapName;
    private static final String TOWNHALL = "TownHall";
    private static final String GOLDMINE = "GoldMine";
    private static final String PEASANT = "Peasant";

    public StaticAssetRenderer(TiledMap tiledMap,
                               int mapWidth,
                               int mapHeight,
                               String mapName) {
        this.staticAssetTextures = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets_32.atlas"));
        this.animationTextures = new TextureAtlas(Gdx.files.internal("atlas/animations.atlas"));
        this.tiledMap = tiledMap;
        this.assetLayer = new TiledMapTileLayer(mapWidth, mapHeight, 32, 32);
        //this.mapName = mapName;
        /*
        * Unsure if textures larger than 32x32 can be placed on layer easily
        * May have to separate atlas into 32x32, 64x64, 128x128 pages and create separate layers for those
         */

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
        assetLayer.setName("StaticAssets");

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

    public void UpdateStaticAssets(TiledMap tmap, AssetDecoratedMap map, Vector<PlayerData> playerData) {
        TiledMapTileLayer assetLayer =  (TiledMapTileLayer) tmap.getLayers().get("StaticAssets");
        TiledMapTileLayer effectsLayer = (TiledMapTileLayer) tmap.getLayers().get("BuildingEffects");

        if (effectsLayer == null){
            effectsLayer = new TiledMapTileLayer(map.Width(), map.Height(), 32, 32);
            tmap.getLayers().add(effectsLayer);
        }

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

                int XPos = StatAsset.tilePosition().X();
                //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
                int YPos = map.Height() - StatAsset.tilePosition().Y() - 1; // -1 to account for 0 index


                switch(StatAsset.Action())
                {

                    case None:
                        stateName = "inactive";
                        tileName = typeName + stateName;

                        GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, tileName);
                        log.info("placed at: " + String.valueOf(XPos) +" " + String.valueOf(YPos));

                        if (StatAsset.hitPoints() < StatAsset.maxHitPoints()/2){
                            Random random = new Random();
                            int fireindex = random.nextInt(9);
                            TextureRegion firetexture = animationTextures.findRegion("large-fire", fireindex);
                            TiledMapTileLayer.Cell firecell = new TiledMapTileLayer.Cell();
                            firecell.setTile(new StaticTiledMapTile(firetexture));
                            effectsLayer.setCell(XPos, YPos , firecell);
                        }

                        break;



                    case Construct:
                        //ActionSteps = DConstructIndices[to_underlying(TempRenderData.DType)].size();
                        int  ActionSteps = 2; // TODO: This was hard-coded.  please fix.
                        if(ActionSteps > 0){
                            //int TotalSteps = StatAsset.assetType().BuildTime() * CPlayerAsset::UpdateFrequency();
                            int TotalSteps = 10; //TODO: This was hard-coded.  please fix.
                            //int TotalSteps = StatAsset.assetType().BuildTime() / 10; // TODO: This was hard-coded.  please fix.
                            int CurrentStep = StatAsset.Step() * ActionSteps / TotalSteps;
                            /*
                            if(CurrentStep == DConstructIndices[to_underlying(TempRenderData.DType)].size()){
                                CurrentStep--;
                            }
                            */
                            int ConstructTileIndex = CurrentStep;

                            stateName = "construct-" + String.valueOf(ConstructTileIndex);

                            tileName = typeName + stateName;

                            GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, tileName);
                            log.info("placed at: " + String.valueOf(XPos) +" " + String.valueOf(YPos));
                        }
                        break;

                    case Death:
                        effectsLayer.setCell(XPos,YPos,null);
                        if(StatAsset.Step() <= BuildingDeathMaxIndex){
                            TextureRegion deathtexture = animationTextures.findRegion("buildingdeath",StatAsset.Step());
                            TiledMapTileLayer.Cell deathcell = new TiledMapTileLayer.Cell();
                            deathcell.setTile(new StaticTiledMapTile(deathtexture));
                            assetLayer.setCell(XPos, YPos , deathcell);
                        }
                        break;

                }
            }
        }
    }
}

