package com.warcraftII.parser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Logger;
import com.warcraftII.asset.AssetDecoratedMap;
import com.warcraftII.data_source.FileDataSource;
import com.warcraftII.terrain.TerrainMap;
import com.warcraftII.terrain.MapRenderer;

import java.util.StringTokenizer;

public class MapParser {
    private Logger log = new Logger("MapParser", 2);

    private String name;
    private int height, width;
    private Sprite [][]spriteMap;
    private TiledMap tiledMap;

    private OrthogonalTiledMapRenderer renderer;
    MapProperties properties;

    public MapParser(String mapName) {
        tiledMap = new TiledMap();

        MapLayers layers = tiledMap.getLayers();


        /* This section reads in from the terrainmap,
        feeds it to the map renderer, and adds a layer to the tilemap */
        int MapNum = AssetDecoratedMap.FindMapIndex(mapName);
        log.info(String.valueOf(MapNum));
        AssetDecoratedMap map = AssetDecoratedMap.GetMap(MapNum);
        MapRenderer mapRenderer = new MapRenderer(map);
        StaticAssetParser staticAssetParser;

        switch (MapNum) {
            case(0):
                staticAssetParser = new StaticAssetParser(tiledMap, map.Width(), map.Height(), "bay.map");
                break;
            case(1):
                staticAssetParser = new StaticAssetParser(tiledMap, map.Width(), map.Height(), "hedges.map");
            case(2):
                staticAssetParser = new StaticAssetParser(tiledMap, map.Width(), map.Height(), "mountain.map");
                break;
            case(3):
                staticAssetParser = new StaticAssetParser(tiledMap, map.Width(), map.Height(), "nwhr2rn.map");
                break;
            default:
                staticAssetParser = new StaticAssetParser(tiledMap, map.Width(), map.Height(), "bay.map");
                log.error("MapNum not found");
        }

        TiledMapTileLayer tileLayerBase = mapRenderer.DrawMap();

        layers.add(tileLayerBase);
        TiledMapTileLayer staticAssetsLayer = staticAssetParser.addStaticAssets();
        layers.add(staticAssetsLayer);

        renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public String getName() {
        return name;
    }

    public void render(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Sprite spriteAt(int i, int j) {
        return spriteMap[i][j];
    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }

    public int getTileHeight(){
        return tiledMap.getProperties().get("height", Integer.class);
    }

    public int getTileWidth(){
        return tiledMap.getProperties().get("width", Integer.class);
    }

    public void dispose(){
        tiledMap.dispose();
        renderer.dispose();
    }
}
