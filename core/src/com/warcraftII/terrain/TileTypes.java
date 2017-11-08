package com.warcraftII.terrain;

/**
 * Created by Kimi on 10/28/2017.
 */

import java.util.Arrays;
import java.util.List;

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
    public static int to_underlying(Enum enumerator) {
        int enumIndex = 0;
        Class<?> enumeratorClass = enumerator.getClass();
        List<?> enumValuesList = Arrays.asList(enumeratorClass.getEnumConstants());

        for (Object enumValue : enumValuesList) {
            if (enumerator == enumValue) {
                return enumIndex;
            } else {
                enumIndex++;
            }
        }
        return 0;
    }

}
