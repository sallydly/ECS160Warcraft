package com.warcraftII.parser;

/*
* Adds stationary assets onto a new layer of map
* Only demos placing random stationary assets onto map
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

import java.util.Arrays;

public class StaticAssetParser {
    private static final Logger log = new Logger("StaticAssetParser", 2);

    private TextureAtlas staticAssets;
    private TiledMap tiledMap;
    private TiledMapTileLayer assetLayer;
    private String[] staticAssetsArray;
    private final String mapName;
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
        this.mapName = mapName;
        /*
        * Unsure if textures larger than 32x32 can be placed on layer easily
        * May have to separate atlas into 32x32, 64x64, 128x128 pages and create separete layers for those
         */

    }

    private void parseMap() {
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
    }

    public TiledMapTileLayer addStaticAssets() {
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
    }
}
