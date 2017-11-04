package com.warcraftII.terrain;

/**
 * Created by Kimi on 11/3/2017.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;


import com.warcraftII.terrain.TileTypes.*;

import java.util.Vector;


/**
 * Reads the atlas file, and a 3D ragged vector, where the first index indicates the type of terrain,
 * the second index indicates terrain transition, and third index indicates alternative tile
 */


public class MapRenderer {

    TextureAtlas DTerrainTextures = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));
    TerrainMap DMap;
    Vector<Vector<Vector<TextureRegion> > > DTileTextures;

    public MapRenderer(FileHandle atlas, FileHandle terrainDatFile) {

        // Resize DTileTextures with the terrain map
        DTileTextures.setSize(TileTypes.to_underlying(ETileType.Max));
        for(int Index = 0; Index = DTileTextures.size(); Index++){
            DTileTextures.get(Index).setSize(16);
        }

        // Generate terrain for AltTileIndex
        for(int Index = 0; Index < 16; Index++){
            int AltTileIndex;
            String indexStr = Integer.toHexString(Index).toUpperCase();

            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("light-grass-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.LightGrass)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("dark-grass-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.DarkGrass)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("light-dirt-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.LightDirt)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("light-grass-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.LightGrass)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("dark-dirt-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.LightGrass)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("light-grass-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.DarkDirt)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("rock-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.Rock)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("forest-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.Forest)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
            AltTileIndex = 0;
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("stump-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                DTileTextures.get(TileTypes.to_underlying(ETileType.Stump)).get(Index).add(textureRegion);
                AltTileIndex++;
            }
        }
        for(int Index = 0; Index < 16; Index++){
            DTileTextures.get(TileTypes.to_underlying(ETileType.Rubble)).get(Index).add(DTileTextures(TileTypes.to_underlying(ETileType.Rock)).get(0).get(0));
        }

    } // end MapRenderer() constructor


    /**
     * Draws the map based on dimensions of the surface given
     *
     * @param[in] surface Shared pointer of CGraphicSurface
     * @param[in] typesurface Shared pointer of CGraphic surface
     * @param[in] rect Constant reference to SRectangle object
     *
     * @return None
     */
    TiledMapTileLayer DrawMap(){
        // Initialize local variables and set TileWidth and TileHeight

        int Width = DMap.Width();
        int Height = DMap.Height();

        TiledMapTileLayer tileLayerBase = new TiledMapTileLayer(Width, Height, 32, 32);

        // Draw map based on TileWidth and TileHeight

        for(int YIndex = 0; YIndex < Height; YIndex++){
            for(int XIndex = 0; XIndex < Width; XIndex++ ){
                ETileType ThisTileType = DMap.TileType(XIndex, YIndex);
                int TileIndex = DMap.TileTypeIndex(XIndex, YIndex);

                if((0 <= TileIndex)&&(16 > TileIndex)){
                    TextureRegion textureRegion = null;
                    int AltTileCount = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).size();
                    if(AltTileCount){
                        int AltIndex = (XIndex + YIndex) % AltTileCount;

                        textureRegion = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).get(AltIndex);
                    }
                    if(null != textureRegion){
                        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                        cell.setTile(new StaticTiledMapTile(textureRegion));
                        tileLayerBase.setCell(XIndex, YIndex, cell);
                    }
                }
                else{

                    return;
                }
            }
        }

        return tileLayerBase;

    } // end DrawMap() function
} // end MapRenderer Class