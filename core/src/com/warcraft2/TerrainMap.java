package com.warcraft2;

import java.util.Vector;

import static com.warcraft2.Tokenizer.Tokenize;

public class TerrainMap {

    enum ETerrainTileType {
        None, // = 0 (Java enum starts indexing at 0)
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

    enum ETileType {
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

    protected Vector<Vector<ETerrainTileType>> DTerrainMap;
    protected Vector<Vector<Byte>> DPartials; // uint8_t in C++ converts to Byte in Java, except Byte is signed
    String DMapName;

    /**
     * Given a map coordiante, determines the ETileType based on its ETerrainTileType
     * and calculates the index of that tile.
     *
     * @return Nothing, but the two parameters type and index are passed
     * in as a reference and are updated in the function
     * @param[in] x The x coordinate
     * @param[in] y The y coordinate
     * @param[in] type The ETileType that will be returned
     * @param[in] index The index that will be returned
     */
    protected void CalculateTileTypeAndIndex(int x, int y, ETileType type, int index) {
        ETerrainTileType UL = DTerrainMap.get(y).get(x);
        ETerrainTileType UR = DTerrainMap.get(y).get(x + 1);
        ETerrainTileType LL = DTerrainMap.get(y + 1).get(x);
        ETerrainTileType LR = DTerrainMap.get(y + 1).get(x + 1);
        int TypeIndex = ((DPartials.get(y).get(x) & 0x8) >> 3) | ((DPartials.get(y).get(x + 1) & 0x4) >> 1) | ((DPartials.get(y + 1).get(x) & 0x2) << 1) | ((DPartials.get(y + 1).get(x + 1) & 0x1) << 3);

        // TODO: all == may need to be refactored to .equals
        if ((ETerrainTileType.DarkGrass == UL) || (ETerrainTileType.DarkGrass == UR) || (ETerrainTileType.DarkGrass == LL) || (ETerrainTileType.DarkGrass == LR)) {
            TypeIndex &= (ETerrainTileType.DarkGrass == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.DarkGrass == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.DarkGrass == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.DarkGrass == LR) ? 0xF : 0x7;
            type = ETileType.DarkGrass;
            index = TypeIndex;
        } else if ((ETerrainTileType.DarkDirt == UL) || (ETerrainTileType.DarkDirt == UR) || (ETerrainTileType.DarkDirt == LL) || (ETerrainTileType.DarkDirt == LR)) {
            TypeIndex &= (ETerrainTileType.DarkDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.DarkDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.DarkDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.DarkDirt == LR) ? 0xF : 0x7;
            type = ETileType.DarkDirt;
            index = TypeIndex;
        } else if ((ETerrainTileType.DeepWater == UL) || (ETerrainTileType.DeepWater == UR) || (ETerrainTileType.DeepWater == LL) || (ETerrainTileType.DeepWater == LR)) {
            TypeIndex &= (ETerrainTileType.DeepWater == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.DeepWater == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.DeepWater == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.DeepWater == LR) ? 0xF : 0x7;
            type = ETileType.DeepWater;
            index = TypeIndex;
        } else if ((ETerrainTileType.ShallowWater == UL) || (ETerrainTileType.ShallowWater == UR) || (ETerrainTileType.ShallowWater == LL) || (ETerrainTileType.ShallowWater == LR)) {
            TypeIndex &= (ETerrainTileType.ShallowWater == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.ShallowWater == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.ShallowWater == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.ShallowWater == LR) ? 0xF : 0x7;
            type = ETileType.ShallowWater;
            index = TypeIndex;
        } else if ((ETerrainTileType.Rock == UL) || (ETerrainTileType.Rock == UR) || (ETerrainTileType.Rock == LL) || (ETerrainTileType.Rock == LR)) {
            TypeIndex &= (ETerrainTileType.Rock == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.Rock == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.Rock == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.Rock == LR) ? 0xF : 0x7;
            type = TypeIndex != 0 ? ETileType.Rock : ETileType.Rubble;
            index = TypeIndex;
        } else if ((ETerrainTileType.Forest == UL) || (ETerrainTileType.Forest == UR) || (ETerrainTileType.Forest == LL) || (ETerrainTileType.Forest == LR)) {
            TypeIndex &= (ETerrainTileType.Forest == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.Forest == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.Forest == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.Forest == LR) ? 0xF : 0x7;
            if (TypeIndex != 0) {
                type = ETileType.Forest;
                index = TypeIndex;
            } else {
                type = ETileType.Stump;
                index = ((ETerrainTileType.Forest == UL) ? 0x1 : 0x0) | ((ETerrainTileType.Forest == UR) ? 0x2 : 0x0) | ((ETerrainTileType.Forest == LL) ? 0x4 : 0x0) | ((ETerrainTileType.Forest == LR) ? 0x8 : 0x0);
            }
        } else if ((ETerrainTileType.LightDirt == UL) || (ETerrainTileType.LightDirt == UR) || (ETerrainTileType.LightDirt == LL) || (ETerrainTileType.LightDirt == LR)) {
            TypeIndex &= (ETerrainTileType.LightDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.LightDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.LightDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.LightDirt == LR) ? 0xF : 0x7;
            type = ETileType.LightDirt;
            index = TypeIndex;
        } else {
            // Error?
            type = ETileType.LightGrass;
            index = 0xF;
        }
    }

    Boolean LoadMap(DataSource source) {
        CommentSkipLineDataSource LineSource (source, '#');
        String TempString;
        Vector<String> Tokens;
        Integer MapWidth, MapHeight;
        Boolean ReturnStatus = false;

        DTerrainMap.clear();

        if (!LineSource.Read(DMapName)) {
            return ReturnStatus;
        }
        if (!LineSource.Read(TempString)) {
            return ReturnStatus;
        }
        Tokenize(Tokens, TempString);
        if (2 != Tokens.size()) {
            return ReturnStatus;
        }
        try {
            Vector<String> StringMap;
            MapWidth = Integer.valueOf(Tokens.get(0));
            MapHeight = Integer.valueOf(Tokens.get(1));

            if ((8 > MapWidth) || (8 > MapHeight)) {
                return ReturnStatus;
            }
            while (StringMap.size() < MapHeight + 1) {
                if (!LineSource.Read(TempString)) {
                    return ReturnStatus;
                }
                StringMap.push_back(TempString);
                if (MapWidth + 1 > StringMap.back().length()) {
                    return ReturnStatus;
                }
            }
            if (MapHeight + 1 > StringMap.size()) {
                return ReturnStatus;
            }
            DTerrainMap.resize(MapHeight + 1);
            for (int Index = 0; Index < DTerrainMap.size(); Index++) {
                DTerrainMap[Index].resize(MapWidth + 1);
                for (int Inner = 0; Inner < MapWidth + 1; Inner++) {
                    switch (StringMap[Index][Inner]) {
                        case 'G':
                            DTerrainMap[Index][Inner] = ETerrainTileType::DarkGrass;
                            break;
                        case 'g':
                            DTerrainMap[Index][Inner] = ETerrainTileType::LightGrass;
                            break;
                        case 'D':
                            DTerrainMap[Index][Inner] = ETerrainTileType::DarkDirt;
                            break;
                        case 'd':
                            DTerrainMap[Index][Inner] = ETerrainTileType::LightDirt;
                            break;
                        case 'R':
                            DTerrainMap[Index][Inner] = ETerrainTileType::Rock;
                            break;
                        case 'r':
                            DTerrainMap[Index][Inner] = ETerrainTileType::RockPartial;
                            break;
                        case 'F':
                            DTerrainMap[Index][Inner] = ETerrainTileType::Forest;
                            break;
                        case 'f':
                            DTerrainMap[Index][Inner] = ETerrainTileType::ForestPartial;
                            break;
                        case 'W':
                            DTerrainMap[Index][Inner] = ETerrainTileType::DeepWater;
                            break;
                        case 'w':
                            DTerrainMap[Index][Inner] = ETerrainTileType::ShallowWater;
                            break;
                        default:    goto LoadMapExit;
                            break;
                    }
                    if (Inner) {
                        if (!DAllowedAdjacent[to_underlying(DTerrainMap[Index][Inner])][to_underlying(DTerrainMap[Index][Inner - 1])]) {
                            return ReturnStatus;
                        }
                    }
                    if (Index) {
                        if (!DAllowedAdjacent[to_underlying(DTerrainMap[Index][Inner])][to_underlying(DTerrainMap[Index - 1][Inner])]) {
                            return ReturnStatus;
                        }
                    }
                }

            }
            StringMap.clear();
            while (StringMap.size() < MapHeight + 1) {
                if (!LineSource.Read(TempString)) {
                    return ReturnStatus;
                }
                StringMap.push_back(TempString);
                if (MapWidth + 1 > StringMap.back().length()) {
                    return ReturnStatus;
                }
            }
            if (MapHeight + 1 > StringMap.size()) {
                return ReturnStatus;
            }
            DPartials.resize(MapHeight + 1);
            for (int Index = 0; Index < DTerrainMap.size(); Index++) {
                DPartials[Index].resize(MapWidth + 1);
                for (int Inner = 0; Inner < MapWidth + 1; Inner++) {
                    if (('0' <= StringMap[Index][Inner]) && ('9' >= StringMap[Index][Inner])) {
                        DPartials[Index][Inner] = StringMap[Index][Inner] - '0';
                    } else if (('A' <= StringMap[Index][Inner]) && ('F' >= StringMap[Index][Inner])) {
                        DPartials[Index][Inner] = StringMap[Index][Inner] - 'A' + 0x0A;
                    } else {
                        return ReturnStatus;
                    }
                }
            }
            ReturnStatus = true;
        } catch (Exception E) {

        }
    }
}
