package com.warcraftII.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.player_asset.VisibilityMap;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.units.Unit;

import java.util.Vector;
import java.util.List;

public class FogRenderer {
    private TextureAtlas fogAtlas;
    private TextureRegion allBlackFog;
    private TextureRegion partialFog;
    private TextureRegion partialPartialFog;

    public FogRenderer() {
        this.fogAtlas = new TextureAtlas(Gdx.files.internal("atlas/fog.atlas"));
        this.allBlackFog = fogAtlas.findRegion("pb-00");
        this.partialFog = fogAtlas.findRegion("partial");
        this.partialPartialFog = fogAtlas.findRegion("pf-00");
    }

//    public TiledMapTileLayer renderAssetsFog(AssetDecoratedMap assetDecoratedMap,
//                                             Vector<PlayerData> playerDataVector) {
//        System.out.println("Rendering Assets Fog");
//        VisibilityMap visibilityMap = assetDecoratedMap.CreateVisibilityMap();
//
//        for(PlayerData playerData : playerDataVector) {
//            visibilityMap.updateAssets(playerData.StaticAssets());
//        }
//
//        return createFogLayer(assetDecoratedMap, visibilityMap);
//    }

    public TiledMapTileLayer createFogLayer(AssetDecoratedMap assetDecoratedMap,
                                       Vector<PlayerData> playerDataVector,
                                       List<Unit.IndividualUnit> individualUnitList) {
        VisibilityMap visibilityMap = assetDecoratedMap.CreateVisibilityMap();
        TiledMapTileLayer fogLayer = new TiledMapTileLayer(assetDecoratedMap.Width(), assetDecoratedMap.Height(), 32, 32);
        fogLayer.setName("Fog");

        for(PlayerData playerData : playerDataVector) {
            visibilityMap.updateAssets(playerData.StaticAssets());
        }

        visibilityMap.updateUnits(individualUnitList);

        for(int xIndex = 0 ; xIndex < assetDecoratedMap.Height(); ++xIndex) {
            for(int yIndex = 0; yIndex < assetDecoratedMap.Width(); ++yIndex) {
                int Xpos =  xIndex;
                int Ypos = assetDecoratedMap.Height() - yIndex - 1;
                final VisibilityMap.ETileVisibility CURRENT_TILE = visibilityMap.TileType(xIndex, yIndex);

                switch (CURRENT_TILE) {
                    case None:
                        TiledMapTileLayer.Cell blackCell = new TiledMapTileLayer.Cell();
                        blackCell.setTile(new StaticTiledMapTile(allBlackFog));
                        fogLayer.setCell(Xpos, Ypos, blackCell);
                        break;
                    case PartialPartial:
//                        TiledMapTileLayer.Cell partialPartialCell = new TiledMapTileLayer.Cell();
//                        partialPartialCell.setTile(new StaticTiledMapTile(partialPartialFog));
//                        fogLayer.setCell(Xpos, Ypos, partialPartialCell);
                        break;
                    case Partial:
//                        TiledMapTileLayer.Cell partialCell = new TiledMapTileLayer.Cell();
//                        partialCell.setTile(new StaticTiledMapTile(partialFog));
//                        fogLayer.setCell(Xpos, Ypos, partialCell);
                        break;
                    case Visible:
                    case SeenPartial:
                    case Seen:
                        break;
                }
            }
        }
        return fogLayer;
    }
}
