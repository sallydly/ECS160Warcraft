package com.warcraftII.asset;

import com.warcraftII.GameDataTypes.*;
import com.warcraftII.position.TilePosition;

/**
 *
 * This was formerly a struct in CAssetDecoratedMap,
 * changed to class because Java doesn't have structs
 *
 * Created by Kimi on 10/27/2017.
 */

public class SAssetInitialization {
    public String DType;
    public EPlayerColor DColor;
    public TilePosition DTilePosition;

    public SAssetInitialization(){
        DTilePosition = new TilePosition();
    }
}
