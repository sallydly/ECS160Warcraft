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
        log.info(mapName);
        log.info("LOOP");
        for(String name : AssetDecoratedMap.GetMapNames()) {
            if (name.equals("Three ways to cross")) {
                log.info("We found it!");
            }
        }
        int MapNum = AssetDecoratedMap.FindMapIndex(mapName);
        if(MapNum == -1) {
            log.info("Its negative???");
        }
        AssetDecoratedMap map = AssetDecoratedMap.GetMap(MapNum);
        MapRenderer maprend = new MapRenderer(map);

        TiledMapTileLayer tileLayerBase = maprend.DrawMap();

        layers.add(tileLayerBase);


        /* This was the hail-mary asset addition */
        TextureAtlas staticAssets = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets.atlas"));
        TiledMapTileLayer assetLayer = new TiledMapTileLayer(width/2, height/2, 64,64); //needs to be changed to 32 later

        //spriteMap = new Sprite[height][width];
//            for (int i = 0; i < 5; i++) {

        /*TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TextureRegion textureRegion = new TextureRegion();
        textureRegion = staticAssets.findRegion("goldmine-inactive");
        cell.setTile(new StaticTiledMapTile(textureRegion));
        assetLayer.setCell(0, 0, cell);

        textureRegion = staticAssets.findRegion("farm-place");
        cell.setTile(new StaticTiledMapTile(textureRegion));
        assetLayer.setCell(5, 6, cell);

//            }
        layers.add(assetLayer);
*/
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
