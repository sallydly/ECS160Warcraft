package com.warcraftII.parser;

/*
* Adds stationary assets onto a new layer of map
* Only demos placing random stationary assets onto map
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class StaticAssetParser {
    public StaticAssetParser(TiledMap tiledMap, int mapWidth, int mapHeight) {
        TextureAtlas staticAssets = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets.atlas"));

        MapLayers layers = tiledMap.getLayers();

        TiledMapTileLayer assetLayer = new TiledMapTileLayer(mapWidth, mapHeight, 32, 32);
        /*
        * Unsure if textures larger than 32x32 can be placed on layer easily
        * May have to separate atlas into 32x32, 64x64, 128x128 pages and create separete layers for those
         */

    }
}
