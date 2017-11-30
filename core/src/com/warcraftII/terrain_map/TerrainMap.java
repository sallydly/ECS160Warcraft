package com.warcraftII.terrain_map;


import com.badlogic.gdx.utils.Logger;
import com.warcraftII.Tokenizer;
import com.warcraftII.data_source.CommentSkipLineDataSource;
import com.warcraftII.data_source.DataSource;
import com.warcraftII.position.TilePosition;
import com.warcraftII.terrain_map.TileTypes.*;

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
    protected Vector<Vector<ETileType>> DMap;
    protected Vector<Vector<ETerrainTileType>> DTerrainMap;
    protected Vector<Vector<Byte>> DPartials; // uint8_t in C++ converts to Byte in Java, except Byte is signed (and it with 0xFF to get unsigned val)
    protected Vector<Vector<Integer>> DMapIndices;
    // the current state of tree growth, 0 = forest, 1 = adolescent, 2 = seedling 3 = stump
    protected Vector<Vector<Integer>> DTreeGrowthState;
    protected boolean DRendered;
    protected String DMapName;
    protected String DMapDescription;

    protected Logger log;

    public TerrainMap() {
        this.DRendered = false;

        log = new Logger("TerrainMap", 2);
    }

    public TerrainMap(final TerrainMap map) {
        this.DTerrainMap = map.DTerrainMap;
        this.DPartials = map.DPartials;
        this.DMapName = map.DMapName;
        this.DMap = map.DMap;
        this.DMapIndices = map.DMapIndices;
        this.DRendered = map.DRendered;
        this.DTreeGrowthState = map.DTreeGrowthState;

        log = new Logger("TerrainMap", 2);
    }

    /*  The important get() functions of TerrainMap: */
    public boolean IsRendered(){
        return DRendered;
    }

    public ETileType TileType(int xindex, int yindex) {
        if((-1 > xindex)||(-1 > yindex)){
            return ETileType.None;
        }
        if(DMap.size() <= yindex+1){
            return ETileType.None;
        }
        if(DMap.get(yindex+1).size() <= xindex+1){
            return ETileType.None;
        }
        return DMap.get(yindex+1).get(xindex+1);
    }

    public ETileType TileType(TilePosition pos) {
        return TileType(pos.X(), pos.Y());
    }

    public int TileTypeIndex(int xindex, int yindex) {
        if((-1 > xindex)||(-1 > yindex)){
            return -1;
        }
        if(DMapIndices.size() <= yindex+1){
            return -1;
        }
        if(DMapIndices.get(yindex+1).size() <= xindex+1){
            return -1;
        }
        return DMapIndices.get(yindex+1).get(xindex+1);
    }

    public int TileTypeIndex(TilePosition pos) {
        return TileTypeIndex(pos.X(), pos.Y());
    }

    public ETerrainTileType TerrainTileType(int xindex, int yindex){
        if((0 > xindex)||(0 > yindex)){
            return ETerrainTileType.None;
        }
        if(DTerrainMap.size() <= yindex){
            return ETerrainTileType.None;
        }
        if(DTerrainMap.get(yindex).size() <= xindex){
            return ETerrainTileType.None;
        }
        return DTerrainMap.get(yindex).get(xindex);
    }

    public ETerrainTileType TerrainTileType(TilePosition pos) {
        return TerrainTileType(pos.X(), pos.Y());
    }

    public byte TilePartial(int xindex, int yindex) {
        if((0 > xindex)||(0 > yindex)){
            return -1;
        }
        if(DPartials.size() <= yindex){
            return -1;
        }
        if(DPartials.get(yindex).size() <= xindex){
            return -1;
        }
        return DPartials.get(yindex).get(xindex);
    }

    public byte TilePartial(TilePosition pos) {
        return TilePartial(pos.X(), pos.Y());
    }

    /*  The get() functions of TerrainMap for gameMap metadata: */
    /**
     * Returns string containing the name of the gameMap
     *
     * @param[in] Nothing
     *
     * @return string gameMap name
     *
     */
    public String MapName(){
        return DMapName;
    }

    /**
     * Returns the width of the gameMap
     *
     * @note the gameMap is represented by a vector of vectors of TileTypes
     *
     * @param[in] Nothing
     *
     * @return int width of the gameMap
     *
     */
    public int Width() {
        if(DTerrainMap.size() > 0){
            return DTerrainMap.get(0).size()-1;
        }
        return 0;
    }

    /**
     * Returns the height of the gameMap
     *
     * @note the gameMap is represented by a vector of vectors of TileTypes
     *
     * @param[in] Nothing
     *
     * @return in height of the gameMap
     *
     */
    public int Height() {
        return DTerrainMap.size()-1;
    }

    /**
     * Checks if a tile type can be traversed
     *
     * @param[in] type The ETileType you want to check
     *
     * @return true if the type can be traversed
     *
     */
    public boolean IsTraversable(ETileType type){
        switch(type){
            case None:
            case DarkGrass:
            case LightGrass:
            case DarkDirt:
            case LightDirt:
            case Rubble:
            case Stump:
            case Seedling: return true;
            default:                    return false;
        }
    }

    /**
     * Checks if a tile type is the correct type to have an object
     * placed on it
     *
     * @param[in] type The ETileType you want to check
     *
     * @return true if the type can be placed on
     *
     */

    public boolean CanPlaceOn(ETileType type){
        switch(type){
            case DarkGrass:
            case LightGrass:
            case DarkDirt:
            case LightDirt:
            case Rubble:
            case Stump:
            case Seedling: return true;
            default:         return false;
        }
    }

    public boolean LoadMap(DataSource source) {
        CommentSkipLineDataSource LineSource = new CommentSkipLineDataSource(source, '#');
        return LoadMap(LineSource);
    }

    public boolean LoadMap(CommentSkipLineDataSource lineSource) {
        CommentSkipLineDataSource LineSource = lineSource;
        String tempString;
        Vector<String> tokens;
        int mapWidth, mapHeight;
        boolean returnStatus = false;

        DTerrainMap = new Vector<Vector<ETerrainTileType>>();
        DTerrainMap.clear();

        //read map name
        DMapName = LineSource.read();
        if(DMapName.isEmpty()) {
            return returnStatus;
        }

        tempString = LineSource.read();
        if(tempString.isEmpty()) {
            return returnStatus;
        }
        tokens = Tokenizer.Tokenize(tempString);
        if (2 != tokens.size()) {
            return returnStatus;
        }

        Vector<String> StringMap = new Vector<String>();
        mapWidth = Integer.valueOf(tokens.get(0));
        mapHeight = Integer.valueOf(tokens.get(1));

        if ((8 > mapWidth) || (8 > mapHeight)) {
            return returnStatus;
        }

        /*
         * HM: expecting map's description here
         * TODO: may want to have some code to check for error while reading map description
         */
        tempString = LineSource.read();
        if(tempString.isEmpty()) {
            return returnStatus;
        }
        if(LineSource.isDIsAfterComment()) {
            //first line of map description
            DMapDescription = tempString;
        }
        tempString = LineSource.read();
        if(tempString.isEmpty()) {
            return returnStatus;
        }
        /*
         * HM: expecting that there's no comments in map description
         * if encounters a comment, expect the next field be map tileset (path to Terrain.dat)
         */
        while(LineSource.isDIsAfterComment() == false) {
            DMapDescription += ("\n" + tempString);
            tempString = LineSource.read();
            if(tempString.isEmpty()) {
                return returnStatus;
            }
        }
        log.info("Map Description: " + DMapDescription + " END OF MAP DESCRIPTION");
        //IMPORTANT: at this point tempString should already contain map tileset (path to Terrain.dat)

        //TODO: codes related to Map Tileset goes here, if necessary

        while (StringMap.size() < mapHeight + 1) {
            tempString = LineSource.read();
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

        DTerrainMap.setSize(mapHeight + 1);
        for (int i = 0; i < DTerrainMap.size(); i++) {
            Vector<ETerrainTileType> TempRow = new Vector<ETerrainTileType>();
            TempRow.setSize(mapWidth + 1);

            for (int j = 0; j < mapWidth + 1; j++) {
                switch (StringMap.get(i).charAt(j)) {
                    case 'G':
                        TempRow.set(j, ETerrainTileType.DarkGrass);
                        break;
                    case 'g':
                        TempRow.set(j, ETerrainTileType.LightGrass);
                        break;
                    case 'D':
                        TempRow.set(j, ETerrainTileType.DarkDirt);
                        break;
                    case 'd':
                        TempRow.set(j, ETerrainTileType.LightDirt);
                        break;
                    case 'R':
                        TempRow.set(j, ETerrainTileType.Rock);
                        break;
                    case 'r':
                        TempRow.set(j, ETerrainTileType.RockPartial);
                        break;
                    case 'F':
                        TempRow.set(j, ETerrainTileType.Forest);
                        break;
                    case 'f':
                        TempRow.set(j, ETerrainTileType.ForestPartial);
                        break;
                    case 'W':
                        TempRow.set(j, ETerrainTileType.DeepWater);
                        break;
                    case 'w':
                        TempRow.set(j, ETerrainTileType.ShallowWater);
                        break;
                    default:
                        return returnStatus;
                }
                    /* Should not be given invalid maps?s
                    if (j >= 1) {
                        if (!DAllowedAdjacent[DTerrainMap.get(i).get(j).ordinal()][DTerrainMap.get(i).get(j-1).ordinal()]) {
                            return returnStatus;
                        }
                    }
                    if (i >= 1) {
                        if (!DAllowedAdjacent[DTerrainMap.get(i).get(j).ordinal()][DTerrainMap.get(i-1).get(j).ordinal()]) {
                            return returnStatus;
                        }
                    }
                    */
            }
            DTerrainMap.set(i,TempRow);
        }

        // Now starts reading map partial bits.
        StringMap.clear();
        while (StringMap.size() < mapHeight + 1) {
            tempString = LineSource.read();
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

        DPartials = new Vector<Vector<Byte>>();
        DPartials.setSize(mapHeight + 1);
        for (int i = 0; i < DTerrainMap.size(); i++) {
            Vector<Byte> TempPartialsRow = new Vector<Byte>();
            TempPartialsRow.setSize(mapWidth + 1);
            for (int j = 0; j < mapWidth + 1; j++) {
                if (('0' <= StringMap.get(i).charAt(j)) && ('9' >= StringMap.get(i).charAt(j))) {
                    TempPartialsRow.set(j, (byte)(StringMap.get(i).charAt(j) - '0'));
                } else if (('A' <= StringMap.get(i).charAt(j)) && ('F' >= StringMap.get(i).charAt(j))) {
                    TempPartialsRow.set(j, (byte)(StringMap.get(i).charAt(j) - 'A' + 0x0A));
                } else {
                    return returnStatus;
                }
            }
            DPartials.set(i,TempPartialsRow);
        }
        returnStatus = true;
        return returnStatus;
    }

    /**
     * Constructs the ETileType 2D vector gameMap (DMap) based on the
     * TerrainMap and adds a rock tile border around the gameMap
     *
     * @param[in] Nothing
     *
     * @return Nothing
     *
     */
    public void RenderTerrain(){
        DMap = new Vector<Vector<ETileType>>();
        DMap.setSize(DTerrainMap.size()+1);
        DMapIndices = new Vector<Vector<Integer>>();
        DMapIndices.setSize(DTerrainMap.size()+1);

        DTreeGrowthState = new Vector<Vector<Integer>>();
        //DTreeGrowthState does not include rock border
        DTreeGrowthState.setSize(DTerrainMap.size()-1);
        for(int i = 0; i < DTreeGrowthState.size(); i++) {
            Vector<Integer> internalVector = new Vector<Integer>();
            internalVector.setSize(DTerrainMap.get(i).size()-1);
            for(int j = 0; j < internalVector.size(); j++) {
                internalVector.set(j, 3);
            }
            DTreeGrowthState.set(i, internalVector);
        }

        for (int i = 0; i < DMap.size(); i++){
            Vector<ETileType> newVec1 = new Vector<ETileType>();
            newVec1.setSize(DTerrainMap.get(0).size() + 1);
            DMap.set(i,newVec1);
            Vector<Integer> newVec2 = new Vector<Integer>();
            newVec2.setSize(DTerrainMap.get(0).size()+ 1);
            DMapIndices.set(i,newVec2);
        }

        for(int YPos = 0; YPos < DMap.size(); YPos++){
            if((0 == YPos)||(DMap.size() - 1 == YPos)){
                for(int XPos = 0; XPos < DTerrainMap.get(0).size() + 1; XPos++){
                    SetTileTypeAndIndex(XPos,YPos,ETileType.Rock,0xF);
                }
            }
            else{
                for(int XPos = 0; XPos < DTerrainMap.get(YPos-1).size() + 1; XPos++){
                    if((0 == XPos)||(DTerrainMap.get(YPos-1).size() == XPos)){
                        SetTileTypeAndIndex(XPos,YPos,ETileType.Rock,0xF);
                    }
                    else{
                        SetTileTypeAndIndex(XPos-1, YPos-1);
                    }
                }
            }
        }
        DRendered = true;
    }

    /**
     *
     * Changed from CalculateTileTypeAndIndex...to SetTileTypeAndIndex
     * Given a gameMap coordinate, determines the ETileType based on its TerrainTileType
     * and calculates the index of that tile.
     *
     * Changes DMap and DMapIndices at the x and y coordinates.
     *
     * @return Nothing
     * @param[in] x The x coordinate
     * @param[in] y The y coordinate
     *
     */
    protected void SetTileTypeAndIndex(int x, int y) {
        ETileType Type;
        int Index;

        ETerrainTileType UL = DTerrainMap.get(y).get(x);
        ETerrainTileType UR = DTerrainMap.get(y).get(x + 1);
        ETerrainTileType LL = DTerrainMap.get(y + 1).get(x);
        ETerrainTileType LR = DTerrainMap.get(y + 1).get(x + 1);

        int TypeIndex = ((DPartials.get(y).get(x) & 0x8) >> 3) | ((DPartials.get(y).get(x + 1) & 0x4) >> 1) | ((DPartials.get(y + 1).get(x) & 0x2) << 1) | ((DPartials.get(y + 1).get(x + 1) & 0x1) << 3);

        if ((ETerrainTileType.DarkGrass == UL) || (ETerrainTileType.DarkGrass == UR) || (ETerrainTileType.DarkGrass == LL) || (ETerrainTileType.DarkGrass == LR)) {
            TypeIndex &= (ETerrainTileType.DarkGrass == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.DarkGrass == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.DarkGrass == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.DarkGrass == LR) ? 0xF : 0x7;
            Type = ETileType.DarkGrass;
            Index = TypeIndex;
        } else if ((ETerrainTileType.DarkDirt == UL) || (ETerrainTileType.DarkDirt == UR) || (ETerrainTileType.DarkDirt == LL) || (ETerrainTileType.DarkDirt == LR)) {
            TypeIndex &= (ETerrainTileType.DarkDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.DarkDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.DarkDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.DarkDirt == LR) ? 0xF : 0x7;
            Type = ETileType.DarkDirt;
            Index = TypeIndex;
        } else if ((ETerrainTileType.DeepWater == UL) || (ETerrainTileType.DeepWater == UR) || (ETerrainTileType.DeepWater == LL) || (ETerrainTileType.DeepWater == LR)) {
            TypeIndex &= (ETerrainTileType.DeepWater == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.DeepWater == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.DeepWater == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.DeepWater == LR) ? 0xF : 0x7;
            Type = ETileType.DeepWater;
            Index = TypeIndex;
        } else if ((ETerrainTileType.ShallowWater == UL) || (ETerrainTileType.ShallowWater == UR) || (ETerrainTileType.ShallowWater == LL) || (ETerrainTileType.ShallowWater == LR)) {
            TypeIndex &= (ETerrainTileType.ShallowWater == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.ShallowWater == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.ShallowWater == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.ShallowWater == LR) ? 0xF : 0x7;
            Type = ETileType.ShallowWater;
            Index = TypeIndex;
        } else if ((ETerrainTileType.Rock == UL) || (ETerrainTileType.Rock == UR) || (ETerrainTileType.Rock == LL) || (ETerrainTileType.Rock == LR)) {
            TypeIndex &= (ETerrainTileType.Rock == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.Rock == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.Rock == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.Rock == LR) ? 0xF : 0x7;
//            Type = TypeIndex != 0 ? ETileType.Rock : ETileType.Rubble;
//            Index = TypeIndex;
            if (TypeIndex != 0) {
                Type = ETileType.Rock;
                Index = TypeIndex;
            } else {
                Type = ETileType.Rubble;
                log.debug("I am rubble");
                Index = ((ETerrainTileType.Rock == UL) ? 0x1 : 0x0) | ((ETerrainTileType.Rock == UR) ? 0x2 : 0x0) | ((ETerrainTileType.Rock == LL) ? 0x4 : 0x0) | ((ETerrainTileType.Rock == LR) ? 0x8 : 0x0);
            }
        } else if ((ETerrainTileType.Forest == UL) || (ETerrainTileType.Forest == UR) || (ETerrainTileType.Forest == LL) || (ETerrainTileType.Forest == LR)) {
            TypeIndex &= (ETerrainTileType.Forest == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.Forest == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.Forest == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.Forest == LR) ? 0xF : 0x7;
            if (TypeIndex != 0) {
                Type = ETileType.Forest;
                Index = TypeIndex;
            } else {
                switch(DTreeGrowthState.get(y).get(x)) {
                    case 3:
                        Type = ETileType.Stump;
                        break;
                    case 2:
                        Type = ETileType.Seedling;
                        break;
                    case 1:
                        Type = ETileType.Adolescent;
                        break;
                    default:
                        Type = ETileType.Forest;
                }
                //Type = ETileType.Stump;
                log.debug("I am stump");
                Index = ((ETerrainTileType.Forest == UL) ? 0x1 : 0x0) | ((ETerrainTileType.Forest == UR) ? 0x2 : 0x0) | ((ETerrainTileType.Forest == LL) ? 0x4 : 0x0) | ((ETerrainTileType.Forest == LR) ? 0x8 : 0x0);
            }
        } else if ((ETerrainTileType.LightDirt == UL) || (ETerrainTileType.LightDirt == UR) || (ETerrainTileType.LightDirt == LL) || (ETerrainTileType.LightDirt == LR)) {
            TypeIndex &= (ETerrainTileType.LightDirt == UL) ? 0xF : 0xE;
            TypeIndex &= (ETerrainTileType.LightDirt == UR) ? 0xF : 0xD;
            TypeIndex &= (ETerrainTileType.LightDirt == LL) ? 0xF : 0xB;
            TypeIndex &= (ETerrainTileType.LightDirt == LR) ? 0xF : 0x7;
            Type = ETileType.LightDirt;
            Index = TypeIndex;
        } else {
            Type = ETileType.LightGrass;
            Index = 0xF;
        }

        Vector<ETileType> changeVec1 = DMap.get(y+1);
        changeVec1.set(x+1,Type);
        DMap.set(y+1, changeVec1);

        Vector<Integer> changeVec2 = DMapIndices.get(y+1);
        changeVec2.set(x+1, Integer.valueOf(Index));
        DMapIndices.set(y+1, changeVec2);


/*  The y+1 is to compensate for its use in RenderTerrain()
        ETileType Type;
        int Index;
        CalculateTileTypeAndIndex(XPos-1, YPos-1, Type, Index);
        DMap[YPos].push_back(Type);
        DMapIndices[YPos].push_back(Index);
*/
    }


    /**
     *
     * Changed from CalculateTileTypeAndIndex...to SetTileTypeAndIndex
     * Given a gameMap coordinate, an ETileType, and an integer index,
     *
     * Changes DMap and DMapIndices at the x and y coordinates
     * to the given ETileType and index
     *
     * @return Nothing
     * @param[in] x The x coordinate
     * @param[in] y The y coordinate
     * @param[in] tile The ETileType
     * @param[in] index The new index
     *
     */
    protected void SetTileTypeAndIndex(int x, int y, ETileType type, int index) {

        Vector<ETileType> changeVec1 = DMap.get(y);
        changeVec1.set(x,type);
        DMap.set(y, changeVec1);

        Vector<Integer> changeVec2 = DMapIndices.get(y);
        changeVec2.set(x,Integer.valueOf(index));
        DMapIndices.set(y, changeVec2);
    }


    /**
     * This function changes the tile to match the surrounding terrain,
     * for example used when lumber is removed from forest tile
     *
     * @param[in] xindex The x coordinate of the tile
     * @param[in] yindex The y coordinate of the tile
     * @param[in] val New value of partial at the coordinate
     *
     * @return Nothing
     *
     */

    public void ChangeTerrainTilePartial(int xindex, int yindex, byte val){
        log.info("Changing Terrain Tile Partial");
        if((0 > yindex)||(0 > xindex)){
            return;
        }
        if(yindex >= DPartials.size()){
            return;
        }
        if(xindex >= DPartials.get(0).size()){
            return;
        }
        Vector<Byte> modRow = DPartials.get(yindex);
        modRow.set(xindex, Byte.valueOf(val));
        DPartials.set(yindex,modRow);
        for(int YOff = 0; YOff < 2; YOff++){
            for(int XOff = 0; XOff < 2; XOff++){
                if(DRendered){
                    int XPos = xindex + XOff;
                    int YPos = yindex + YOff;
                    if((0 < XPos)&&(0 < YPos)){
                        if((YPos + 1 < DMap.size())&&(XPos + 1 < DMap.get(YPos).size())){
                            SetTileTypeAndIndex(XPos-1, YPos-1);
                        }
                    }
                }
            }
        }
    }
}
