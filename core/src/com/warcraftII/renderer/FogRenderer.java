package com.warcraftII.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
    public VisibilityMap visibilityMap;

    public FogRenderer(AssetDecoratedMap assetDecoratedMap) {
        this.fogAtlas = new TextureAtlas(Gdx.files.internal("atlas/fog.atlas"));
        this.allBlackFog = fogAtlas.findRegion("pb-00");
        this.partialFog = fogAtlas.findRegion("partial");
        this.partialPartialFog = fogAtlas.findRegion("pf-00");
        visibilityMap = assetDecoratedMap.CreateVisibilityMap();

    }

    public TiledMapTileLayer createFogLayer(AssetDecoratedMap assetDecoratedMap,
                                       PlayerData currentPlayer,
                                       List<Unit.IndividualUnit> currentPlayerUnitList) {
        TiledMapTileLayer fogLayer = new TiledMapTileLayer(assetDecoratedMap.Width(), assetDecoratedMap.Height(), 32, 32);
        fogLayer.setName("Fog");

        visibilityMap.updateAssets(currentPlayer.StaticAssets());
        visibilityMap.updateUnits(currentPlayerUnitList);

        for(int yIndex = 0 ; yIndex < assetDecoratedMap.Height(); ++yIndex) {
            for(int xIndex = 0; xIndex < assetDecoratedMap.Width(); ++xIndex) {
                int Xpos =  xIndex;
                int Ypos = assetDecoratedMap.Height() - yIndex - 1;
                final VisibilityMap.ETileVisibility CURRENT_TILE = visibilityMap.TileType(xIndex, yIndex);

                switch (CURRENT_TILE) {
                    case None:
                        TiledMapTileLayer.Cell blackCell = new TiledMapTileLayer.Cell();
                        blackCell.setTile(new StaticTiledMapTile(allBlackFog));
                        fogLayer.setCell(Xpos, Ypos, blackCell);
                        break;
                    case SeenPartial:
                    case Seen:
                        TiledMapTileLayer.Cell partialPartialCell = new TiledMapTileLayer.Cell();
                        partialPartialCell.setTile(new StaticTiledMapTile(partialPartialFog));
                        fogLayer.setCell(Xpos, Ypos, partialPartialCell);
                        break;
                    case Partial:
                    case PartialPartial:
                        TiledMapTileLayer.Cell partialCell = new TiledMapTileLayer.Cell();
                        partialCell.setTile(new StaticTiledMapTile(partialFog));
                        fogLayer.setCell(Xpos, Ypos, partialCell);
                    case Visible:
                }
            }
        }
        return fogLayer;
    }
}
