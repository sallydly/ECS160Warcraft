package com.warcraftII.terrain;

import com.warcraftII.Tokenizer;
import com.warcraftII.data_source.CommentSkipLineDataSource;
import com.warcraftII.data_source.DataSource;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class TerrainMap {
    protected static boolean[][] DAllowedAdjacent = {
        {true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true },
        {true,  true,  true,  false, false, false, false, false, false, false, false},
        {true,  true,  true,  false, true,  false, false, true,  true,  false, false},
        {true,  false, false, true,  true,  false, false, false, false, false, false},
        {true,  false, true,  true,  true,  true,  true,  false, false, false, true },
        {true,  false, false, false, true,  true,  true,  false, false, false, false},
        {true,  false, false, false, true,  true,  true,  false, false, false, false},
        {true,  false, true,  false, false, false, false, true,  true,  false, false},
        {true,  false, true,  false, false, false, false, true,  true,  false, false},
        {true,  false, false, false, false, false, false, false, false, true,  true },
        {true,  false, false, false, true,  false, false, false, false, true,  true },
    };
    protected List<List<TileType>> dMap;
    protected List<List<com.warcraftII.terrain.TerrainTileType>> dTerrainMap;
    protected List<List<Byte>> dPartials; // uint8_t in C++ converts to Byte in Java, except Byte is signed
    protected List<List<Integer>> dMapIndices;
    protected boolean dRendered;
    String dMapName;

    TerrainMap() {
        this.dRendered = false;
    }

    TerrainMap(final TerrainMap map) {
        this.dTerrainMap = map.dTerrainMap;
        this.dPartials = map.dPartials;
        this.dMapName = map.dMapName;
        this.dMap = map.dMap;
        this.dMapIndices = map.dMapIndices;
        this.dRendered = map.dRendered;
    }

    /**
     * Given a map coordiante, determines the ETileType based on its TerrainTileType
     * and calculates the index of that tile.
     *
     * @return Nothing, but the two parameters type and index are passed
     * in as a reference and are updated in the function
     * @param[in] x The x coordinate
     * @param[in] y The y coordinate
     * @param[in] type The ETileType that will be returned
     * @param[in] index The index that will be returned
     */
    protected void CalculateTileTypeAndIndex(int x, int y, TileType type, int index) {
        com.warcraftII.terrain.TerrainTileType UL = dTerrainMap.get(y).get(x);
        com.warcraftII.terrain.TerrainTileType UR = dTerrainMap.get(y).get(x + 1);
        com.warcraftII.terrain.TerrainTileType LL = dTerrainMap.get(y + 1).get(x);
        com.warcraftII.terrain.TerrainTileType LR = dTerrainMap.get(y + 1).get(x + 1);
        int TypeIndex = ((dPartials.get(y).get(x) & 0x8) >> 3) | ((dPartials.get(y).get(x + 1) & 0x4) >> 1) | ((dPartials.get(y + 1).get(x) & 0x2) << 1) | ((dPartials.get(y + 1).get(x + 1) & 0x1) << 3);

        // TODO: all == may need to be refactored to .equals
        if ((com.warcraftII.terrain.TerrainTileType.DarkGrass == UL) || (com.warcraftII.terrain.TerrainTileType.DarkGrass == UR) || (com.warcraftII.terrain.TerrainTileType.DarkGrass == LL) || (com.warcraftII.terrain.TerrainTileType.DarkGrass == LR)) {
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkGrass == UL) ? 0xF : 0xE;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkGrass == UR) ? 0xF : 0xD;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkGrass == LL) ? 0xF : 0xB;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkGrass == LR) ? 0xF : 0x7;
            type = TileType.DarkGrass;
            index = TypeIndex;
        } else if ((com.warcraftII.terrain.TerrainTileType.DarkDirt == UL) || (com.warcraftII.terrain.TerrainTileType.DarkDirt == UR) || (com.warcraftII.terrain.TerrainTileType.DarkDirt == LL) || (com.warcraftII.terrain.TerrainTileType.DarkDirt == LR)) {
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DarkDirt == LR) ? 0xF : 0x7;
            type = TileType.DarkDirt;
            index = TypeIndex;
        } else if ((com.warcraftII.terrain.TerrainTileType.DeepWater == UL) || (com.warcraftII.terrain.TerrainTileType.DeepWater == UR) || (com.warcraftII.terrain.TerrainTileType.DeepWater == LL) || (com.warcraftII.terrain.TerrainTileType.DeepWater == LR)) {
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DeepWater == UL) ? 0xF : 0xE;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DeepWater == UR) ? 0xF : 0xD;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DeepWater == LL) ? 0xF : 0xB;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.DeepWater == LR) ? 0xF : 0x7;
            type = TileType.DeepWater;
            index = TypeIndex;
        } else if ((com.warcraftII.terrain.TerrainTileType.ShallowWater == UL) || (com.warcraftII.terrain.TerrainTileType.ShallowWater == UR) || (com.warcraftII.terrain.TerrainTileType.ShallowWater == LL) || (com.warcraftII.terrain.TerrainTileType.ShallowWater == LR)) {
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.ShallowWater == UL) ? 0xF : 0xE;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.ShallowWater == UR) ? 0xF : 0xD;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.ShallowWater == LL) ? 0xF : 0xB;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.ShallowWater == LR) ? 0xF : 0x7;
            type = TileType.ShallowWater;
            index = TypeIndex;
        } else if ((com.warcraftII.terrain.TerrainTileType.Rock == UL) || (com.warcraftII.terrain.TerrainTileType.Rock == UR) || (com.warcraftII.terrain.TerrainTileType.Rock == LL) || (com.warcraftII.terrain.TerrainTileType.Rock == LR)) {
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Rock == UL) ? 0xF : 0xE;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Rock == UR) ? 0xF : 0xD;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Rock == LL) ? 0xF : 0xB;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Rock == LR) ? 0xF : 0x7;
            type = TypeIndex != 0 ? TileType.Rock : TileType.Rubble;
            index = TypeIndex;
        } else if ((com.warcraftII.terrain.TerrainTileType.Forest == UL) || (com.warcraftII.terrain.TerrainTileType.Forest == UR) || (com.warcraftII.terrain.TerrainTileType.Forest == LL) || (com.warcraftII.terrain.TerrainTileType.Forest == LR)) {
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Forest == UL) ? 0xF : 0xE;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Forest == UR) ? 0xF : 0xD;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Forest == LL) ? 0xF : 0xB;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.Forest == LR) ? 0xF : 0x7;
            if (TypeIndex != 0) {
                type = TileType.Forest;
                index = TypeIndex;
            } else {
                type = TileType.Stump;
                index = ((com.warcraftII.terrain.TerrainTileType.Forest == UL) ? 0x1 : 0x0) | ((com.warcraftII.terrain.TerrainTileType.Forest == UR) ? 0x2 : 0x0) | ((com.warcraftII.terrain.TerrainTileType.Forest == LL) ? 0x4 : 0x0) | ((com.warcraftII.terrain.TerrainTileType.Forest == LR) ? 0x8 : 0x0);
            }
        } else if ((com.warcraftII.terrain.TerrainTileType.LightDirt == UL) || (com.warcraftII.terrain.TerrainTileType.LightDirt == UR) || (com.warcraftII.terrain.TerrainTileType.LightDirt == LL) || (com.warcraftII.terrain.TerrainTileType.LightDirt == LR)) {
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.LightDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.LightDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.LightDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (com.warcraftII.terrain.TerrainTileType.LightDirt == LR) ? 0xF : 0x7;
            type = TileType.LightDirt;
            index = TypeIndex;
        } else {
            // Error?
            type = TileType.LightGrass;
            index = 0xF;
        }
    }

    boolean LoadMap(DataSource source) {
        CommentSkipLineDataSource lineSource = new CommentSkipLineDataSource(source, '#');
        String tempString;
        Vector<String> tokens = new Vector<String>();
        int mapWidth, mapHeight;
        boolean returnStatus = false;

        dTerrainMap.clear();

        try {
            dMapName = lineSource.read();
            if(dMapName.isEmpty()) {
                return returnStatus;
            }

            tempString = lineSource.read();
            if(tempString.isEmpty()) {
                return returnStatus;
            }
            tokens = Tokenizer.Tokenize(tempString);
            if (2 != tokens.size()) {
                return returnStatus;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Vector<String> StringMap = new Vector<String>();
            mapWidth = Integer.valueOf(tokens.get(0));
            mapHeight = Integer.valueOf(tokens.get(1));

            if ((8 > mapWidth) || (8 > mapHeight)) {
                return returnStatus;
            }
            while (StringMap.size() < mapHeight + 1) {
                tempString = lineSource.read();
                if(tempString.isEmpty()) {
                    return returnStatus;
                }
                StringMap.add(tempString);
                if (mapWidth + 1 > StringMap.lastElement().length()) {
                    return returnStatus;
                }
            }
            if (mapHeight + 1 > StringMap.size()) {
                return returnStatus;
            }
            dTerrainMap = dTerrainMap.subList(0, mapHeight + 1);
            for (int i = 0; i < dTerrainMap.size(); i++) {
                dTerrainMap.set(i, dTerrainMap.get(i).subList(0, mapWidth + 1));
                for (int j = 0; j < mapWidth + 1; j++) {
                    switch (StringMap.get(i).charAt(j)) {
                        case 'G':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.DarkGrass);
                            break;
                        case 'g':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.LightGrass);
                            break;
                        case 'D':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.DarkDirt);
                            break;
                        case 'd':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.LightDirt);
                            break;
                        case 'R':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.Rock);
                            break;
                        case 'r':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.RockPartial);
                            break;
                        case 'F':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.Forest);
                            break;
                        case 'f':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.ForestPartial);
                            break;
                        case 'W':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.DeepWater);
                            break;
                        case 'w':
                            dTerrainMap.get(i).set(j, com.warcraftII.terrain.TerrainTileType.ShallowWater);
                            break;
                        default:
                            return returnStatus;
                    }
                    if (j >= 1) {
                        //TODO: Implement to_underlying function
                        if (!DAllowedAdjacent[dTerrainMap.get(i).get(j).ordinal()][dTerrainMap.get(i).get(j-1).ordinal()]) {
                            return returnStatus;
                        }
                    }
                    if (i >= 1) {
                        if (!DAllowedAdjacent[dTerrainMap.get(i).get(j).ordinal()][dTerrainMap.get(i-1).get(j).ordinal()]) {
                            return returnStatus;
                        }
                    }
                }
            }
            StringMap.clear();
            while (StringMap.size() < mapHeight + 1) {
                tempString = lineSource.read();
                if(tempString.isEmpty()) {
                    return returnStatus;
                }
                StringMap.add(tempString);
                if (mapWidth + 1 > StringMap.lastElement().length()) {
                    return returnStatus;
                }
            }
            if (mapHeight + 1 > StringMap.size()) {
                return returnStatus;
            }
            dPartials = dPartials.subList(0, mapHeight + 1);
            for (int i = 0; i < dTerrainMap.size(); i++) {
                dPartials.set(i, dPartials.get(i).subList(0, mapWidth + 1));
                for (int j = 0; j < mapWidth + 1; j++) {
                    if (('0' <= StringMap.get(i).charAt(j)) && ('9' >= StringMap.get(i).charAt(j))) {
                        dPartials.get(i).set(j, (byte)(StringMap.get(i).charAt(j) - '0'));
                    } else if (('A' <= StringMap.get(i).charAt(j)) && ('F' >= StringMap.get(i).charAt(j))) {
                        dPartials.get(i).set(j, (byte)(StringMap.get(i).charAt(j) - 'A' + 0x0A));
                    } else {
                        return returnStatus;
                    }
                }
            }
            returnStatus = true;
        } catch (Exception E) {

        }
        return returnStatus;
    }
}
