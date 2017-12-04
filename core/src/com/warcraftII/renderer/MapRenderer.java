package com.warcraftII.renderer;

/**
 * Created by Kimi on 11/3/2017.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;


import com.badlogic.gdx.utils.Logger;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.position.TilePosition;
import com.warcraftII.terrain_map.TerrainMap;
import com.warcraftII.terrain_map.TileTypes;
import com.warcraftII.terrain_map.TileTypes.*;

import java.util.Vector;


/**
 * Reads the atlas file, and a 3D ragged vector, where the first index indicates the type of terrain,
 * the second index indicates terrain transition, and third index indicates alternative tile
 */


public class MapRenderer {
    private static Logger log = new Logger("MapRenderer", 2);
    TextureAtlas DTerrainTextures = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));
    //for tree growth atlas
    TextureAtlas DTreeGrowth = new TextureAtlas(Gdx.files.internal("atlas/TreeGrowth.atlas"));
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

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.Rubble);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTerrainTextures.findRegion("rubble-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.Seedling);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTreeGrowth.findRegion("seedling-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);

            AltTileIndex = 0;
            TileTypeNum = TileTypes.to_underlying(ETileType.Adolescent);
            OuterVec = DTileTextures.get(TileTypeNum);
            InnerVec = new Vector<TextureRegion>();
            while(true){
                TextureRegion textureRegion = DTreeGrowth.findRegion("adolescent-" + indexStr + "-" + Integer.toString(AltTileIndex));
                if(null == textureRegion){
                    break;
                }
                InnerVec.add(textureRegion);
                AltTileIndex++;
            }
            OuterVec.set(Index,InnerVec);
            DTileTextures.set(TileTypeNum, OuterVec);
        }

        //HM: in case of rubble texture not render correctly, enable this code
//        for(int Index = 0; Index < 16; Index++){
//            Vector<Vector<TextureRegion> > OuterVec;
//            Vector<TextureRegion> InnerVec;
//
//            int AltTileIndex = 0;
//            int TileTypeNum = TileTypes.to_underlying(ETileType.Rubble);
//            OuterVec = DTileTextures.get(TileTypeNum);
//            InnerVec = new Vector<TextureRegion>();
//
//            TextureRegion textureRegion = DTileTextures.get(TileTypes.to_underlying(ETileType.Rock)).get(0).get(0);
//            InnerVec.add(textureRegion);
//
//            OuterVec.set(Index,InnerVec);
//            DTileTextures.set(TileTypeNum, OuterVec);
//        }

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
    public void UpdateTile(TilePosition tpos, TiledMapTileLayer terrainLayer){
        int OrigXIndex = tpos.X();
        int OrigYIndex = tpos.Y();

        Vector<TilePosition> tilePositions = new Vector<TilePosition>();

        tilePositions.add(tpos);

        TilePosition PosXplus = new TilePosition(OrigXIndex+1, OrigYIndex);
        tilePositions.add(PosXplus);

        TilePosition PosYplus = new TilePosition(OrigXIndex, OrigYIndex+1);
        tilePositions.add(PosYplus);

        TilePosition PosXYplus = new TilePosition(OrigXIndex+1, OrigYIndex+1);
        tilePositions.add(PosXYplus);

        TilePosition PosXminus = new TilePosition(OrigXIndex-1, OrigYIndex);
        tilePositions.add(PosXminus);

        TilePosition PosYminus = new TilePosition(OrigXIndex, OrigYIndex-1);
        tilePositions.add(PosYminus);

        TilePosition PosXYminus = new TilePosition(OrigXIndex-1, OrigYIndex-1);
        tilePositions.add(PosXYminus);

        TilePosition PosXplusYminus = new TilePosition(OrigXIndex+1, OrigYIndex-1);
        tilePositions.add(PosXplusYminus);

        TilePosition PosXminusYplus = new TilePosition(OrigXIndex-1, OrigYIndex+1);
        tilePositions.add(PosXminusYplus);


        for (TilePosition pos : tilePositions) {
            int XIndex = pos.X();
            int YIndex = pos.Y();
            ETileType ThisTileType = DMap.TileType(XIndex, YIndex);
            int TileIndex = DMap.TileTypeIndex(XIndex, YIndex);


            if (ThisTileType == TileTypes.ETileType.Stump && DMap.GetGrowthState(XIndex, YIndex) == 0) {
                System.out.println("I will be stump");
                DMap.SetGrowthState(XIndex, YIndex, 1);
//                ThisTileType = DMap.TileType(XIndex, YIndex);
                DMap.SetTimeStep(XIndex, YIndex);
            }
            if (ThisTileType == ETileType.Rubble)
                System.out.println("I will be Rubble");

            if ((0 <= TileIndex) && (16 > TileIndex)) {
                TextureRegion textureRegion = null;
                int AltTileCount = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).size();
                if (AltTileCount > 0) {
                    int AltIndex = (XIndex + YIndex) % AltTileCount;

                    textureRegion = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).get(AltIndex);
                }

                if (null != textureRegion) {
                    // need to invert both y axis:
                    int Xpos = XIndex;
                    int Ypos = DMapHeight - 1 - YIndex;
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    cell.setTile(new StaticTiledMapTile(textureRegion));
                    terrainLayer.setCell(Xpos, Ypos, cell);
                }
            } else {

                return;
            }
        }
        return;

    }

    public void UpdateTreeGrowth(TilePosition tpos, TiledMapTileLayer terrainLayer) {
        int XIndex = tpos.X();
        int YIndex = tpos.Y();
        ETileType ThisTileType = DMap.TileType(XIndex, YIndex);
        int TileIndex = DMap.TileTypeIndex(XIndex, YIndex);
        if ((0 <= TileIndex) && (16 > TileIndex)) {
            TextureRegion textureRegion = null;
            int AltTileCount = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).size();
            if (AltTileCount > 0) {
                int AltIndex = (XIndex + YIndex) % AltTileCount;

                textureRegion = DTileTextures.get(TileTypes.to_underlying(ThisTileType)).get(TileIndex).get(AltIndex);
            }

            if (null != textureRegion) {
                // need to invert both y axis:
                int Xpos = XIndex;
                int Ypos = DMapHeight - 1 - YIndex;
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(textureRegion));
                terrainLayer.setCell(Xpos, Ypos, cell);
            }
        } else {

            return;
        }
    }


} // end MapRenderer Class