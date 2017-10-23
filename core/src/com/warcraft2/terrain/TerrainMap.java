package com.warcraft2.terrain;

import com.warcraft2.Tokenizer;
import com.warcraft2.data_source.CommentSkipLineDataSource;
import com.warcraft2.data_source.DataSource;

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
    protected List<List<TerrainTileType>> dTerrainMap;
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
        TerrainTileType UL = dTerrainMap.get(y).get(x);
        TerrainTileType UR = dTerrainMap.get(y).get(x + 1);
        TerrainTileType LL = dTerrainMap.get(y + 1).get(x);
        TerrainTileType LR = dTerrainMap.get(y + 1).get(x + 1);
        int TypeIndex = ((dPartials.get(y).get(x) & 0x8) >> 3) | ((dPartials.get(y).get(x + 1) & 0x4) >> 1) | ((dPartials.get(y + 1).get(x) & 0x2) << 1) | ((dPartials.get(y + 1).get(x + 1) & 0x1) << 3);

        // TODO: all == may need to be refactored to .equals
        if ((TerrainTileType.DarkGrass == UL) || (TerrainTileType.DarkGrass == UR) || (TerrainTileType.DarkGrass == LL) || (TerrainTileType.DarkGrass == LR)) {
            TypeIndex &= (TerrainTileType.DarkGrass == UL) ? 0xF : 0xE;
            TypeIndex &= (TerrainTileType.DarkGrass == UR) ? 0xF : 0xD;
            TypeIndex &= (TerrainTileType.DarkGrass == LL) ? 0xF : 0xB;
            TypeIndex &= (TerrainTileType.DarkGrass == LR) ? 0xF : 0x7;
            type = TileType.DarkGrass;
            index = TypeIndex;
        } else if ((TerrainTileType.DarkDirt == UL) || (TerrainTileType.DarkDirt == UR) || (TerrainTileType.DarkDirt == LL) || (TerrainTileType.DarkDirt == LR)) {
            TypeIndex &= (TerrainTileType.DarkDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (TerrainTileType.DarkDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (TerrainTileType.DarkDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (TerrainTileType.DarkDirt == LR) ? 0xF : 0x7;
            type = TileType.DarkDirt;
            index = TypeIndex;
        } else if ((TerrainTileType.DeepWater == UL) || (TerrainTileType.DeepWater == UR) || (TerrainTileType.DeepWater == LL) || (TerrainTileType.DeepWater == LR)) {
            TypeIndex &= (TerrainTileType.DeepWater == UL) ? 0xF : 0xE;
            TypeIndex &= (TerrainTileType.DeepWater == UR) ? 0xF : 0xD;
            TypeIndex &= (TerrainTileType.DeepWater == LL) ? 0xF : 0xB;
            TypeIndex &= (TerrainTileType.DeepWater == LR) ? 0xF : 0x7;
            type = TileType.DeepWater;
            index = TypeIndex;
        } else if ((TerrainTileType.ShallowWater == UL) || (TerrainTileType.ShallowWater == UR) || (TerrainTileType.ShallowWater == LL) || (TerrainTileType.ShallowWater == LR)) {
            TypeIndex &= (TerrainTileType.ShallowWater == UL) ? 0xF : 0xE;
            TypeIndex &= (TerrainTileType.ShallowWater == UR) ? 0xF : 0xD;
            TypeIndex &= (TerrainTileType.ShallowWater == LL) ? 0xF : 0xB;
            TypeIndex &= (TerrainTileType.ShallowWater == LR) ? 0xF : 0x7;
            type = TileType.ShallowWater;
            index = TypeIndex;
        } else if ((TerrainTileType.Rock == UL) || (TerrainTileType.Rock == UR) || (TerrainTileType.Rock == LL) || (TerrainTileType.Rock == LR)) {
            TypeIndex &= (TerrainTileType.Rock == UL) ? 0xF : 0xE;
            TypeIndex &= (TerrainTileType.Rock == UR) ? 0xF : 0xD;
            TypeIndex &= (TerrainTileType.Rock == LL) ? 0xF : 0xB;
            TypeIndex &= (TerrainTileType.Rock == LR) ? 0xF : 0x7;
            type = TypeIndex != 0 ? TileType.Rock : TileType.Rubble;
            index = TypeIndex;
        } else if ((TerrainTileType.Forest == UL) || (TerrainTileType.Forest == UR) || (TerrainTileType.Forest == LL) || (TerrainTileType.Forest == LR)) {
            TypeIndex &= (TerrainTileType.Forest == UL) ? 0xF : 0xE;
            TypeIndex &= (TerrainTileType.Forest == UR) ? 0xF : 0xD;
            TypeIndex &= (TerrainTileType.Forest == LL) ? 0xF : 0xB;
            TypeIndex &= (TerrainTileType.Forest == LR) ? 0xF : 0x7;
            if (TypeIndex != 0) {
                type = TileType.Forest;
                index = TypeIndex;
            } else {
                type = TileType.Stump;
                index = ((TerrainTileType.Forest == UL) ? 0x1 : 0x0) | ((TerrainTileType.Forest == UR) ? 0x2 : 0x0) | ((TerrainTileType.Forest == LL) ? 0x4 : 0x0) | ((TerrainTileType.Forest == LR) ? 0x8 : 0x0);
            }
        } else if ((TerrainTileType.LightDirt == UL) || (TerrainTileType.LightDirt == UR) || (TerrainTileType.LightDirt == LL) || (TerrainTileType.LightDirt == LR)) {
            TypeIndex &= (TerrainTileType.LightDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (TerrainTileType.LightDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (TerrainTileType.LightDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (TerrainTileType.LightDirt == LR) ? 0xF : 0x7;
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
                            dTerrainMap.get(i).set(j, TerrainTileType.DarkGrass);
                            break;
                        case 'g':
                            dTerrainMap.get(i).set(j, TerrainTileType.LightGrass);
                            break;
                        case 'D':
                            dTerrainMap.get(i).set(j, TerrainTileType.DarkDirt);
                            break;
                        case 'd':
                            dTerrainMap.get(i).set(j, TerrainTileType.LightDirt);
                            break;
                        case 'R':
                            dTerrainMap.get(i).set(j, TerrainTileType.Rock);
                            break;
                        case 'r':
                            dTerrainMap.get(i).set(j, TerrainTileType.RockPartial);
                            break;
                        case 'F':
                            dTerrainMap.get(i).set(j, TerrainTileType.Forest);
                            break;
                        case 'f':
                            dTerrainMap.get(i).set(j, TerrainTileType.ForestPartial);
                            break;
                        case 'W':
                            dTerrainMap.get(i).set(j, TerrainTileType.DeepWater);
                            break;
                        case 'w':
                            dTerrainMap.get(i).set(j, TerrainTileType.ShallowWater);
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
