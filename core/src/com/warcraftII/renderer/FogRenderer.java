package com.warcraftII.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.player_asset.VisibilityMap;
import com.warcraftII.terrain_map.AssetDecoratedMap;

import java.util.Vector;

public class FogRenderer {
    private TextureAtlas fogAtlas;

    public FogRenderer() {
        //TODO: Change this
        this.fogAtlas = new TextureAtlas(Gdx.files.internal("atlas/fog.atlas"));

    }

    public TiledMapTileLayer renderFog(AssetDecoratedMap assetDecoratedMap, Vector<PlayerData> playerDataVector) {
        System.out.println("Rendering Fog");
        TiledMapTileLayer fogLayer = new TiledMapTileLayer(assetDecoratedMap.Width(), assetDecoratedMap.Height(), 32, 32);
        fogLayer.setName("Fog");

        VisibilityMap visibilityMap = assetDecoratedMap.CreateVisibilityMap();
        TextureRegion allBlackFog = fogAtlas.findRegion("pf-00");
        TextureRegion partialFog = fogAtlas.findRegion("partial");
        System.out.println("Num Players: " + playerDataVector.size());

        for(PlayerData playerData : playerDataVector) {
            System.out.println("Num StaticAssets: " + playerData.StaticAssets().size());
            visibilityMap.update(playerData.StaticAssets());
        }
/*

for(int YIndex = rect.DYPosition / TileHeight, YPos = -(rect.DYPosition % TileHeight); YPos < rect.DHeight; YIndex++, YPos += TileHeight){
        for(int XIndex = rect.DXPosition / TileWidth, XPos = -(rect.DXPosition % TileWidth); XPos < rect.DWidth; XIndex++, XPos += TileWidth){

 */
//        for(int YIndex = rect.DYPosition / TileHeight, YPos = -(rect.DYPosition % TileHeight); YPos < rect.DHeight; YIndex++, YPos += TileHeight){
//            for(int XIndex = rect.DXPosition / TileWidth, XPos = -(rect.DXPosition % TileWidth); XPos < rect.DWidth; XIndex++, XPos += TileWidth){
//
        for(int yIndex = 0 ; yIndex < assetDecoratedMap.Height(); ++yIndex) {
            for(int xIndex = 0; xIndex < assetDecoratedMap.Width(); ++xIndex) {
                if(visibilityMap.TileType(xIndex, yIndex) == VisibilityMap.ETileVisibility.None) {
                    int Xpos =  xIndex;
                    int Ypos = assetDecoratedMap.Height() - yIndex - 1;
                    TiledMapTileLayer.Cell blackCell = new TiledMapTileLayer.Cell();
                    blackCell.setTile(new StaticTiledMapTile(allBlackFog));
                    fogLayer.setCell(Xpos, Ypos, blackCell);
                } else if(visibilityMap.TileType(xIndex, yIndex) == VisibilityMap.ETileVisibility.PartialPartial
                        || visibilityMap.TileType(xIndex, yIndex) == VisibilityMap.ETileVisibility.Partial) {
                    int Xpos =  xIndex;
                    int Ypos = assetDecoratedMap.Height() - yIndex - 1;
                    TiledMapTileLayer.Cell partialCell = new TiledMapTileLayer.Cell();
                    partialCell.setTile(new StaticTiledMapTile(partialFog));
                    fogLayer.setCell(Xpos, Ypos, partialCell);
                }
            }
        }

        return fogLayer;
    }
}
