package com.warcraftII.terrain;

/**
 * Created by Kimi on 10/28/2017.
 */

public class TileTypes {
    public enum ETerrainTileType {
        None,
        DarkGrass,
        LightGrass,
        DarkDirt,
        LightDirt,
        Rock,
        RockPartial,
        Forest,
        ForestPartial,
        DeepWater,
        ShallowWater,
        Max
    }

    public enum ETileType {
        None, // = 0 (Java enum starts indexing at 0)
        DarkGrass,
        LightGrass,
        DarkDirt,
        LightDirt,
        Rock,
        Rubble,
        Forest,
        Stump,
        DeepWater,
        ShallowWater,
        Max
    }

}
