package com.warcraftII.asset.static_assets;

import com.warcraftII.asset.player.PlayerAssetType;
import com.warcraftII.position.PixelPosition;
import com.warcraftII.position.TilePosition;

public class StaticAsset {
    public enum EState {
        CONSTRUCT_0,
        CONSTRUCT_1,
        ACTIVE,
        INACTIVE,
        PLACE
    }

    private EState DState;
    private int DHitPoints;
    private int DGold;
    private int DLumber;
    private PlayerAssetType DType;
    private TilePosition DPosition;

    StaticAsset(PlayerAssetType type) {
        this.DHitPoints = type.HitPoints();
        this.DGold = 0;
        this.DLumber = 0;
        this.DPosition = new TilePosition(0, 0);
    }

    public TilePosition Position() {
        return DPosition;
    }

    public EState State(){
        return DState;
    }

    public PlayerAssetType Type(){
        return DType;
    }



}

