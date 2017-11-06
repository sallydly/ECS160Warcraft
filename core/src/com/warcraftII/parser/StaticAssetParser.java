package com.warcraftII.parser;

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
import com.warcraftII.asset.SAssetInitialization;
import com.warcraftII.position.TilePosition;

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
        this.staticAssets = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets.atlas"));
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

    /*private void parseMap() {
        FileHandle handle = Gdx.files.internal("map/" + mapName);
        String text = handle.readString();
        String[] fileArray = text.split("\\r?\\n");

        int index = 0;
        int assetIndexStart = 0;
        for(String word : fileArray) {
            if(word.equals("# Starting assets Type Owner X Y")) {
                assetIndexStart = index + 1;
            }
            index ++;
        }

        this.staticAssetsArray = Arrays.copyOfRange(fileArray, assetIndexStart, index);
    }*/

    /*public TiledMapTileLayer addStaticAssets() {
        parseMap();

        for(String staticAssetLine : staticAssetsArray) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion textureRegion = new TextureRegion();
            String[] staticAssetInfo = staticAssetLine.split(" ");

            if (GOLDMINE.equals(staticAssetInfo[0])) {
                textureRegion = staticAssets.findRegion("goldmine-inactive");
                cell.setTile(new StaticTiledMapTile(textureRegion));
                assetLayer.setCell(Integer.valueOf(staticAssetInfo[2]), Integer.valueOf(staticAssetInfo[3]), cell);
            } else if (PEASANT.equals(staticAssetInfo[0])) {
                textureRegion = staticAssets.findRegion("scouttower-place");
                cell.setTile(new StaticTiledMapTile(textureRegion));
                assetLayer.setCell(Integer.valueOf(staticAssetInfo[2]), Integer.valueOf(staticAssetInfo[3]), cell);
            } else if (TOWNHALL.equals(staticAssetInfo[0])) {
                textureRegion = staticAssets.findRegion("townhall-inactive");
                cell.setTile(new StaticTiledMapTile(textureRegion));
                assetLayer.setCell(Integer.valueOf(staticAssetInfo[2]), Integer.valueOf(staticAssetInfo[3]), cell);
            }
        }

        return assetLayer;
    }*/

    // takes in an AssetDecoratedMap instead
    public TiledMapTileLayer addStaticAssets(AssetDecoratedMap map) {
        List< SAssetInitialization > AssetInitializationList = map.AssetInitializationList();
        System.out.println("start render");

        this.staticAssets = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets.atlas"));
        this.assetLayer = new TiledMapTileLayer(map.Width(), map.Height(), 32, 32);

        for(SAssetInitialization AssetInit : AssetInitializationList) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion textureRegion = new TextureRegion();

            int XPos = AssetInit.DTilePosition.X();
            //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
            int YPos = map.Height() - AssetInit.DTilePosition.Y() - 3; // dont ask about the -3 haha

            String AssetType = AssetInit.DType;


            if (GOLDMINE.equals(AssetType)) {
                textureRegion = staticAssets.findRegion("goldmine-inactive");
                cell.setTile(new StaticTiledMapTile(textureRegion));
                System.out.println("goldminebuilt");
                System.out.println(AssetInit.DTilePosition.X());
                System.out.println(AssetInit.DTilePosition.X());

                assetLayer.setCell(XPos, YPos, cell);
            } else if (PEASANT.equals(AssetType)) {
                textureRegion = staticAssets.findRegion("scouttower-place");
                cell.setTile(new StaticTiledMapTile(textureRegion));
                assetLayer.setCell(XPos, YPos, cell);
            } else if (TOWNHALL.equals(AssetType)) {
                textureRegion = staticAssets.findRegion("townhall-inactive");
                cell.setTile(new StaticTiledMapTile(textureRegion));
                assetLayer.setCell(XPos, YPos, cell);
            }
        }

        return assetLayer;
    }
}

