package com.warcraftII.asset.static_assets;

import com.warcraftII.asset.player.PlayerAssetType;
import com.warcraftII.position.PixelPosition;
import com.warcraftII.position.TilePosition;

public class StaticAsset {
    enum State {
        CONSTRUCT_1,
        CONSTRUCT_2,
        ACTIVE,
        INACTIVE
    }

    private int DHitPoints;
    private int DGold;
    private int DLumber;
    private PlayerAssetType DType;
    private PixelPosition DPosition;

    StaticAsset(PlayerAssetType type) {
        this.DHitPoints = type.HitPoints();
        this.DGold = 0;
        this.DLumber = 0;
        this.DPosition = new PixelPosition(0, 0);
    }

    public final TilePosition tilePosition() {
        TilePosition returnPosition = new TilePosition();
        returnPosition.setFromPixel(DPosition);
        return returnPosition;
    }


}

