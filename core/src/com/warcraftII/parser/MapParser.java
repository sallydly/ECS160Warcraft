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

import java.util.StringTokenizer;

public class MapParser {

    private String name;
    private int height, width;
    private Sprite [][]spriteMap;
    private TiledMap tiledMap;

    private OrthogonalTiledMapRenderer renderer;
    MapProperties properties;

    public MapParser(FileHandle file) {
        String fileAsString = file.readString();
        String []fileAsLines;
        fileAsLines = fileAsString.split("\n");

        //  name of the gameMap is on second line of .gameMap file
        name = fileAsLines[1];
        StringTokenizer st = new StringTokenizer(fileAsLines[3]);
        //  gameMap's width and height are on 4th line of .gameMap file
        width = Integer.valueOf(st.nextToken());
        height = Integer.valueOf(st.nextToken());

        TextureAtlas terrain = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));
        tiledMap = new TiledMap();

        MapLayers layers = tiledMap.getLayers();
        for (int l = 0; l < 1; l++) {
            TiledMapTileLayer tileLayerBase = new TiledMapTileLayer(width, height, 32, 32);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    TextureRegion textureRegion = new TextureRegion();
                    //  gameMap presentation starts on line 6
                    final char TILE_TO_DISPLAY = fileAsLines[i + 5].charAt(j);
                    switch (TILE_TO_DISPLAY) {
                        case 'w': //shallow water
                            textureRegion = terrain.findRegion("shallow-water-F-0");
                            break;
                        case 'W': //deep water
                            textureRegion = terrain.findRegion("deep-water-F-0");
                            break;
                        case 'd': //light dirt
                            textureRegion = terrain.findRegion("light-dirt-F-0");
                            break;
                        case 'D': //dark dirt
                            textureRegion = terrain.findRegion("dark-dirt-F-0");
                            break;
                        case 'g': //light grass
                            textureRegion = terrain.findRegion("light-grass-F-0");
                            break;
                        case 'G': //dart grass
                            textureRegion = terrain.findRegion("dark-grass-F-0");
                            break;
                        case 'F': //forest
                            textureRegion = terrain.findRegion("forest-F-0");
                            break;
                        case 'R': //rock
                            textureRegion = terrain.findRegion("rock-F-0");
                            break;
                        default:
                            textureRegion = terrain.findRegion("rock-F-0");
                    }

                    //Gdx.app.log("MapParser", "status " +(textureRegion != null));
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    cell.setTile(new StaticTiledMapTile(textureRegion));
                    tileLayerBase.setCell(i, j, cell);
                    //spriteMap[i][j].setPosition(j*32, i*32);
                }
            }
            layers.add(tileLayerBase);
        }

        TextureAtlas staticAssets = new TextureAtlas(Gdx.files.internal("atlas/stationary_assets.atlas"));
        TiledMapTileLayer assetLayer = new TiledMapTileLayer(width/2, height/2, 64,64); //needs to be changed to 32 later

        //spriteMap = new Sprite[height][width];
//            for (int i = 0; i < 5; i++) {

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TextureRegion textureRegion = new TextureRegion();
        textureRegion = staticAssets.findRegion("goldmine-inactive");
        cell.setTile(new StaticTiledMapTile(textureRegion));
        assetLayer.setCell(0, 0, cell);

        textureRegion = staticAssets.findRegion("farm-place");
        cell.setTile(new StaticTiledMapTile(textureRegion));
        assetLayer.setCell(5, 6, cell);

//            }
        layers.add(assetLayer);

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
