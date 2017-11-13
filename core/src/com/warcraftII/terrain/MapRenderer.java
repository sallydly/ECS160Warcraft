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


import com.warcraftII.position.TilePosition;
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
    int DMapHeight;
    int DMapWidth;

    public MapRenderer(TerrainMap map) {
        DMap = map;

        DMapWidth = DMap.Width();
        DMapHeight = DMap.Height();

        // Resize DTileTextures with the terrain map
        DTileTextures = new Vector<Vector<Vector<TextureRegion> > >();
        DTileTextures.setSize(TileTypes.to_underlying(ETileType.Max));
        for(int Index = 0; Index < DTileTextures.size(); Index++){
            Vector<Vector<TextureRegion>> newVec = new Vector<Vector<TextureRegion>  >();
            newVec.setSize(16);
            DTileTextures.set(Index,newVec);
        }

        // Generate terrain for AltTileIndex
        for(int Index = 0; Index < 16; Index++){
            int AltTileIndex;
            int TileTypeNum;
            Vector<Vector<TextureRegion> > OuterVec;
            Vector<TextureRegion> InnerVec;
            String indexStr = Integer.toHexString(Index).toUpperCase();


            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.LightGrass);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("light-grass-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.DarkGrass);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("dark-grass-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.LightDirt);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("light-dirt-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.DarkDirt);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("dark-dirt-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.Rock);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("rock-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.Forest);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("forest-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.ShallowWater);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("shallow-water-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.DeepWater);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("deep-water-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.Stump);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("stump-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);
        }

        for(int Index = 0; Index < 16; Index++){
            Vector<Vector<TextureRegion> > OuterVec;
            Vector<TextureRegion> InnerVec;

            int AltTileIndex = 0;
            int TileTypeNum = TileTypes.to_underlying(ETileType.Rubble);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();

            TextureRegion textureRegion = DTileTextures.get(TileTypes.to_underlying(ETileType.Rock)).get(0).get(0);
            InnerVec.add(textureRegion);

            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);
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
    public TiledMapTileLayer DrawMap(){
        // Initialize local variables and set TileWidth and TileHeight
        if (!DMap.IsRendered()) {
            DMap.RenderTerrain();
        }

        TiledMapTileLayer tileLayerBase = new TiledMapTileLayer(DMapWidth, DMapHeight, 32, 32);
        tileLayerBase.setName("Terrain");
        // Draw map based on TileWidth and TileHeight

        for(int YIndex = 0; YIndex < DMapHeight; YIndex++){
            for(int XIndex = 0; XIndex < DMapWidth; XIndex++ ){
                ETileType ThisTileType = DMap.TileType(XIndex, YIndex);
                int TileIndex = DMap.TileTypeIndex(XIndex, YIndex);

                if((0 <= TileIndex)&&(16 > TileIndex)){
                    TextureRegion textureRegion = null;
                    int AltTileCount = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).size();
                    if(AltTileCount >0){
                        int AltIndex = (XIndex + YIndex) % AltTileCount;

                        textureRegion = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).get(AltIndex);
                    }
                    if(null != textureRegion){
                        // need to invert both y axis:
                        int Xpos =  XIndex;
                        int Ypos = DMapHeight - 1 - YIndex;
                        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                        cell.setTile(new StaticTiledMapTile(textureRegion));
                        tileLayerBase.setCell(Xpos, Ypos, cell);
                    }
                }
                else{

                    return tileLayerBase;
                }
            }
        }

        return tileLayerBase;

    } // end DrawMap() function

    /**
     * UpdateTile takes in a tile position object, and the tile layer.
     * and updates the tile at that position
     * Will be called by RemoveLumber.
     */
    public void UpdateTile(TilePosition pos, TiledMapTileLayer terrainLayer){
        int XIndex = pos.X();
        int YIndex = pos.Y();
        ETileType ThisTileType = DMap.TileType(XIndex, YIndex);
        int TileIndex = DMap.TileTypeIndex(XIndex, YIndex);

        if((0 <= TileIndex)&&(16 > TileIndex)){
            TextureRegion textureRegion = null;
            int AltTileCount = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).size();
            if(AltTileCount >0){
                int AltIndex = (XIndex + YIndex) % AltTileCount;

                textureRegion = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).get(AltIndex);
            }
            if(null != textureRegion){
                // need to invert both y axis:
                int Xpos =  XIndex;
                int Ypos = DMapHeight - 1 - YIndex;
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(textureRegion));
                terrainLayer.setCell(Xpos, Ypos, cell);
            }
        }
        else{

            return;
        }
        return;

    }
} // end MapRenderer Class