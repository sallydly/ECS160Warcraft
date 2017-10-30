package com.warcraftII.asset;

import com.warcraftII.terrain.TerrainMap;
import com.warcraftII.terrain.TileTypes.*;
import com.warcraftII.position.TilePosition;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.data_source.*;
import com.warcraftII.Tokenizer;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import java.lang.Math;

/**
 * Created by Kimi on 10/29/2017.
 */

public class AssetDecoratedMap extends TerrainMap {
    //TODO: Uncomment DAssets when PlayerAsset become available
    //protected List<PlayerAsset> DAssets;
    protected List< SAssetInitialization > DAssetInitializationList;
    protected List< SResourceInitialization > DResourceInitializationList;
    protected Vector< Vector< int > > DSearchMap;
    protected Vector< Vector< int > > DLumberAvailable;

    protected static Map< String, Integer > DMapNameTranslation = new HashMap<String,Integer>;
    protected static Vector < AssetDecoratedMap > DAllMaps;

    /**
     * Constructor
     *
     * @param[in] Nothing
     *
     * @return Nothing
     *
     */
    
    public AssetDecoratedMap() {
        super();        
    }

    /**
     * Constructor, copies data members from input class
     *
     * @param[in] map AssetDecoratedMap class object that you want to copy
     *
     * @return Nothing
     *
     */
//TODO: uncomment when PlayerAsset Becomes Available
    /*
    public AssetDecoratedMap(AssetDecoratedMap map){
        DAssets = map.DAssets;
        DLumberAvailable = map.DLumberAvailable;
        DAssetInitializationList = map.DAssetInitializationList;
        DResourceInitializationList = map.DResourceInitializationList;
    }
*/

/**
 * Constructor, add new players starting assets to the
 * asset and resource initialization lists
 *
 * @param[in] map AssetDecoratedMap object to add to its initialization lists
 * @param[in] newcolors Array of players to add
 *
 * @return Nothing
 *
 */

// TODO: fix this constructor:
/*
    public AssetDecoratedMap(AssetDecoratedMap map, ArrayList< EPlayerColor> newcolors){
    //formerly :  AssetDecoratedMap(AssetDecoratedMap map, ArrayList< EPlayerColor, to_underlying(EPlayerColor.Max)> , // FIXME: 10/29/2017
        DAssets = map.DAssets;
        DLumberAvailable = map.DLumberAvailable;

        for(map.DAssetInitializationList){
            auto NewInitVal = InitVal;
            if(newcolors.size() > to_underlying(NewInitVal.DColor)){
                NewInitVal.DColor = newcolors[to_underlying(NewInitVal.DColor)];
            }
            DAssetInitializationList.push_back(NewInitVal);
        }
        for(auto &InitVal : map.DResourceInitializationList){
            auto NewInitVal = InitVal;
            if(newcolors.size() > to_underlying(NewInitVal.DColor)){
                NewInitVal.DColor = newcolors[to_underlying(NewInitVal.DColor)];
            }
            DResourceInitializationList.push_back(NewInitVal);
        }
    }
 */

    /**
     * = Assignment operator,
     * CHANGED TO SET function.
     * copy all data member values from another
     * AssetDecoratedMap class object
     *
     * @param[in] map AssetDecoratedMap object on rhs of =
     *
     * @return AssetDecoratedMap object on lhs of =
     *
     */
// TODO: Implement this function:?
  /*
    public Set(AssetDecoratedMap map){
        if(this != &map){
            CTerrainMap.operator=(map);
            DAssets = map.DAssets;
            DLumberAvailable = map.DLumberAvailable;
            DAssetInitializationList = map.DAssetInitializationList;
            DResourceInitializationList = map.DResourceInitializationList;
        }
        return *this;
    }
*/

    /**
     * Load in the data for all the maps from .map files
     *
     * @param[in] container Used to load in data from .map files
     *
     * @return true if iterated through file
     *
     */
// TODO: implement this function

/*
    boolean LoadMaps(std.shared_ptr< CDataContainer > container){
        auto FileIterator = container.First();
        if(FileIterator == nullptr){
            PrintError("FileIterator == nullptr\n");
            return false;
        }
        while((FileIterator != nullptr)&&(FileIterator.IsValid())){
            std.string Filename = FileIterator.Name();
            FileIterator.Next();
            if(Filename.rfind(".map") == (Filename.length() - 4)){
                std.shared_ptr< AssetDecoratedMap > TempMap = std.make_shared< AssetDecoratedMap >();

                if(!TempMap.LoadMap(container.DataSource(Filename))){
                    PrintError("Failed to load map \"%s\".\n",Filename.c_str());
                    continue;
                }
                else{
                    PrintDebug(DEBUG_LOW,"Loaded map \"%s\".\n",Filename.c_str());
                }
                TempMap.RenderTerrain();
                DMapNameTranslation[TempMap.MapName()] = DAllMaps.size();
                DAllMaps.push_back(TempMap);
            }
        }
        //PrintDebug(DEBUG_LOW, "Maps loaded\n");
        return true;
    }
*/

    /**
     * Find the index of a map based on a map name
     *
     * @param[in] name The string name of the map
     *
     * @return index as an integer
     *
     */

    public int FindMapIndex(String name) {
        Integer found = DMapNameTranslation.get(name);

        if (found != null) {
            return found.intValue();
        } else {
            return -1;

        }
    }

/**
 * Given an index, return a pointer to the corresponding map in DAllMaps
 *
 * @param[in] index The index of the map in DAllMaps
 *
 * @return a pointer to a map or an empty pointer if index is out of bounds
 *
 */

    public AssetDecoratedMap GetMap(int index){
        if((0 > index)||(DAllMaps.size() <= index)){
            return new AssetDecoratedMap();
        }
        return DAllMaps.get(index);
    }

/**
 * Duplicate a map at given index
 *
 * @param[in] index The index of the map in DAllMaps to duplicate
 * @param[in] newcolors Array of player colors
 *
 * @return pointer to the duplicated map
 *
 */
//TODO: Implement this function
/*
    AssetDecoratedMap DuplicateMap(int index, std.array< EPlayerColor, to_underlying(EPlayerColor.Max)> &newcolors){
        //formerly :  AssetDecoratedMap(AssetDecoratedMap map, ArrayList< EPlayerColor, to_underlying(EPlayerColor.Max)> , // FIXME: 10/29/2017

        if((0 > index)||(DAllMaps.size() <= index)){
            return std.shared_ptr< AssetDecoratedMap >();
        }
        return std.make_shared< AssetDecoratedMap >( *DAllMaps[index], newcolors );
    }
*/

    /**
     * Add an asset to the list of a player's assets
     *
     * @param[in] asset Asset to add
     *
     * @return true if asset added
     *
     */
// TODO: uncomment when playerasset is available
/*
    boolean AddAsset(PlayerAsset asset){
        DAssets.add(asset);
        return true;
    }
*/

    /**
     * Remove an asset from a player's list of assets
     *
     * @param[in] asset Asset to remove
     *
     * @return true if asset removed
     *
     */
// TODO: uncomment when playerasset is available
/*
    boolean RemoveAsset(PlayerAsset asset){
        DAssets.remove(asset);
        return true;
    }
*/
    /**
     * Determine if an asset can be placed at a certain tile position
     *
     * @param[in] pos TilePosition object of the position where you want to place the asset
     * @param[in] size The size of the asset you want to place
     * @param[in] ignoreasset An asset to ignore when checking if two assets will overlap
     *
     * @return true if the asset can be placed at that position, false if not
     *
     */
//TODO: Fix when PlayerAsset is available
    /*
    boolean CanPlaceAsset(TilePosition pos, int size, PlayerAsset ignoreasset){
        int RightX, BottomY;

        for(int YOff = 0; YOff < size; YOff++){
            for(int XOff = 0; XOff < size; XOff++){
                ETileType TileTerrainType = TileType(pos.X() + XOff, pos.Y() + YOff);

                //if((ETileType.Grass != TileTerrainType)&&(ETileType.Dirt != TileTerrainType)&&(ETileType.Stump != TileTerrainType)&&(ETileType.Rubble != TileTerrainType)){
                if(!CanPlaceOn(TileTerrainType)){
                    return false;
                }
            }
        }
        RightX = pos.X() + size;
        BottomY = pos.Y() + size;
        if(RightX >= Width()){
            return false;
        }
        if(BottomY >= Height()){
            return false;
        }
        for(PlayerAsset Asset : DAssets){
            int Offset = EAssetType.GoldMine == Asset.Type() ? 1 : 0;

            if(EAssetType.None == Asset.Type()){
                continue;
            }
            if(ignoreasset == Asset){
                continue;
            }
            if(RightX <= Asset.TilePositionX() - Offset){
                continue;
            }
            if(pos.X() >= (Asset.TilePositionX() + Asset.Size() + Offset)){
                continue;
            }
            if(BottomY <= Asset.TilePositionY() - Offset){
                continue;
            }
            if(pos.Y() >= (Asset.TilePositionY() + Asset.Size() + Offset)){
                continue;
            }
            return false;
        }
        return true;
    }
*/
    /**
     * Find a valid tile position to place a new asset
     *
     * @param[in] placeasset The new asset to find a position for
     * @param[in] fromasset An asset to place the new asset near
     * @param[in] nexttiletarget The target position of the new asset
     *
     * @return a valid position for the new asset
     *
     */
//TODO: Fix when PlayerAsset is available
/*    TilePosition FindAssetPlacement(PlayerAsset placeasset, PlayerAsset fromasset, TilePosition nexttiletarget){
        int TopY, BottomY, LeftX, RightX;
        int BestDistance = -1, CurDistance;
        TilePosition BestPosition(-1, -1);
        TopY = fromasset.TilePositionY() - placeasset.Size();
        BottomY = fromasset.TilePositionY() + fromasset.Size();
        LeftX = fromasset.TilePositionX() - placeasset.Size();
        RightX = fromasset.TilePositionX() + fromasset.Size();

        while(true){
            int Skipped = 0;
            if(0 <= TopY){
                int ToX = Math.min(RightX, Width() - 1);
                for(int CurX = Math.max(LeftX, 0); CurX <= ToX; CurX++){
                    if(CanPlaceAsset(TilePosition(CurX, TopY), placeasset.Size(), placeasset)){
                        TilePosition TempPosition(CurX, TopY);
                        CurDistance = TempPosition.DistanceSquared(nexttiletarget);
                        if((-1 == BestDistance)||(CurDistance < BestDistance)){
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            else{
                Skipped++;
            }
            if(Width() > RightX){
                int ToY = Math.min(BottomY, Height() - 1);
                for(int CurY = Math.max(TopY, 0); CurY <= ToY; CurY++){
                    if(CanPlaceAsset(TilePosition(RightX, CurY), placeasset.Size(), placeasset)){
                        TilePosition TempPosition(RightX, CurY);
                        CurDistance = TempPosition.DistanceSquared(nexttiletarget);
                        if((-1 == BestDistance)||(CurDistance < BestDistance)){
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            else{
                Skipped++;
            }
            if(Height() > BottomY){
                int ToX = Math.max(LeftX, 0);
                for(int CurX = Math.min(RightX, Width() - 1); CurX >= ToX; CurX--){
                    if(CanPlaceAsset(TilePosition(CurX, BottomY), placeasset.Size(), placeasset)){
                        TilePosition TempPosition(CurX, BottomY);
                        CurDistance = TempPosition.DistanceSquared(nexttiletarget);
                        if((-1 == BestDistance)||(CurDistance < BestDistance)){
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            else{
                Skipped++;
            }
            if(0 <= LeftX){
                int ToY = Math.max(TopY, 0);
                for(int CurY = Math.min(BottomY, Height() - 1); CurY >= ToY; CurY--){
                    if(CanPlaceAsset(TilePosition(LeftX, CurY), placeasset.Size(), placeasset)){
                        TilePosition TempPosition(LeftX, CurY);
                        CurDistance = TempPosition.DistanceSquared(nexttiletarget);
                        if((-1 == BestDistance)||(CurDistance < BestDistance)){
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            else{
                Skipped++;
            }
            if(4 == Skipped){
                break;
            }
            if(-1 != BestDistance){
                break;
            }
            TopY--;
            BottomY++;
            LeftX--;
            RightX++;
        }
        return BestPosition;
    }
*/

/**
 * Given a postion, find the nearest asset of a certain type and
 * belonging to a certain player
 *
 * @param[in] pos The position to find an asset near
 * @param[in] color The player the nearest asset should belong to
 * @param[in] type The type of asset to find
 *
 * @return pointer to the nearest asset
 *
 */
//TODO: Fix??
/*
    PlayerAsset FindNearestAsset(CPixelPosition &pos, EPlayerColor color, EAssetType type){
        std.shared_ptr< CPlayerAsset > BestAsset;
        int BestDistanceSquared = -1;

        for(auto &Asset : DAssets){
            if((Asset.Type() == type)&&(Asset.Color() == color)&&(EAssetAction.Construct != Asset.Action())){
                int CurrentDistance = Asset.Position().DistanceSquared(pos);

                if((-1 == BestDistanceSquared)||(CurrentDistance < BestDistanceSquared)){
                    BestDistanceSquared = CurrentDistance;
                    BestAsset = Asset;
                }
            }
        }
        return BestAsset;
    }
*/

    /**
     * Remove lumber from a given tile position and update the partials map
     *
     * @param[in] pos The tile to remove lumber from
     * @param[in] from Position used to determine which terrain tile to remove lumber from
     * @param[in] amount The amount of lumber to remove
     *
     * @return Nothing
     *
     */

    // TODO: Fix this function later
/*
    void RemoveLumber(TilePosition pos, TilePosition from, int amount){
        int Index = 0;

        for(int YOff = 0; YOff < 2; YOff++){
            for(int XOff = 0; XOff < 2; XOff++){
                int XPos = pos.X() + XOff;
                int YPos = pos.Y() + YOff;
                Index |= (ETerrainTileType.Forest == DTerrainMap.get(YPos.get(XPos) && DPartials.get(YPos).get(XPos) ? 1<<(YOff * 2 + XOff) : 0;
            }
        }
        if(Index && (0xF != Index)){
            switch(Index){
                case 1:     Index = 0;
                    break;
                case 2:     Index = 1;
                    break;
                case 3:     Index = from.X() > pos.X() ? 1 : 0;
                    break;
                case 4:     Index = 2;
                    break;
                case 5:     Index = from.Y() < pos.Y() ? 0 : 2;
                    break;
                case 6:     Index = from.Y() > pos.Y() ? 2 : 1;
                    break;
                case 7:     Index = 2;
                    break;
                case 8:     Index = 3;
                    break;
                case 9:     Index = from.Y() > pos.Y() ? 0 : 3;
                    break;
                case 10:    Index = from.Y() > pos.Y() ? 3 : 1;
                    break;
                case 11:    Index = 0;
                    break;
                case 12:    Index = from.X() < pos.X() ? 2 : 3;
                    break;
                case 13:    Index = 3;
                    break;
                case 14:    Index = 1;
                    break;
            }
            switch(Index){
                case 0: DLumberAvailable[pos.Y()][pos.X()] -= amount;
                    if(0 >= DLumberAvailable[pos.Y()][pos.X()]){
                        DLumberAvailable[pos.Y()][pos.X()] = 0;
                        ChangeTerrainTilePartial(pos.X(), pos.Y(), 0);
                    }
                    break;
                case 1: DLumberAvailable[pos.Y()][pos.X()+1] -= amount;
                    if(0 >= DLumberAvailable[pos.Y()][pos.X()+1]){
                        DLumberAvailable[pos.Y()][pos.X()+1] = 0;
                        ChangeTerrainTilePartial(pos.X()+1, pos.Y(), 0);
                    }
                    break;
                case 2: DLumberAvailable[pos.Y()+1][pos.X()] -= amount;
                    if(0 >= DLumberAvailable[pos.Y()+1][pos.X()]){
                        DLumberAvailable[pos.Y()+1][pos.X()] = 0;
                        ChangeTerrainTilePartial(pos.X(), pos.Y()+1, 0);
                    }
                    break;
                case 3: DLumberAvailable[pos.Y()+1][pos.X()+1] -= amount;
                    if(0 >= DLumberAvailable[pos.Y()+1][pos.X()+1]){
                        DLumberAvailable[pos.Y()+1][pos.X()+1] = 0;
                        ChangeTerrainTilePartial(pos.X()+1, pos.Y()+1, 0);
                    }
                    break;
            }
        }
    }
*/

    /**
     * Fills the asset and resource initialization lists and the available
     * lumber vector based on the data provided by source
     *
     * @param[in] source Initialization info is loaded from file and stored in source
     *
     * @return true or false if loaded successfully or not
     *
     */

    public boolean LoadMap(DataSource source){
        CommentSkipLineDataSource LineSource(source, '#');
        String TempString;
        Vector< String > Tokens;
        SResourceInitialization TempResourceInit;
        SAssetInitialization TempAssetInit;
        int ResourceCount, AssetCount, InitialLumber = 400;
        boolean ReturnStatus = false;

        if(!TerrainMap.LoadMap(source)){
            return false;
        }
        try{
            TempString = LineSource.read();
            ResourceCount = Integer.parseInt(TempString);
            DResourceInitializationList.clear();
            for(int Index = 0; Index <= ResourceCount; Index++){
                TempString = LineSource.read();
                Tokens = Tokenizer.Tokenize(TempString);
                if(3 > Tokens.size()){
                    //Bad stuff!
                    //TODO: Create and throw custom exception
                    //PrintError("Too few tokens for resource %d.\n", Index);
                }
                
                // TODO: Deal with player colors
                /*
                TempResourceInit.DColor = static_cast<EPlayerColor>(std.stoi(Tokens.get(0)));
                if((0 == Index)&&(EPlayerColor.None != TempResourceInit.DColor)){
                    PrintError("Expected first resource to be for color None.\n");
                goto LoadMapExit;
                }
                */
                
                TempResourceInit.DGold = Integer.parseInt(Tokens.get(1));
                TempResourceInit.DLumber = Integer.parseInt(Tokens.get(2));
                if(EPlayerColor.None == TempResourceInit.DColor){
                    InitialLumber = TempResourceInit.DLumber;
                }

                DResourceInitializationList.add(TempResourceInit);
            }


            TempString = LineSource.read();
            AssetCount = Integer.parseInt((TempString);
            DAssetInitializationList.clear();
            for(int Index = 0; Index < AssetCount; Index++){
                TempString = LineSource.read();
                Tokens = Tokenizer.Tokenize(TempString);
                if(4 > Tokens.size()){
                    //Bad stuff!
                    //TODO: Create and throw custom exception
                    // PrintError("Too few tokens for asset %d.\n", Index);
                }
                TempAssetInit.DType = Tokens.get(0);
                //TODO: Fix this static cast into PlayerColor enum.
                //TempAssetInit.DColor = static_cast<EPlayerColor>(Integer.parseInt((Tokens.get(1)));
                TempAssetInit.DTilePosition.X(Integer.parseInt((Tokens.get(2)));
                TempAssetInit.DTilePosition.Y(Integer.parseInt((Tokens.get(3)));

                if((0 > TempAssetInit.DTilePosition.X())||(0 > TempAssetInit.DTilePosition.Y())){
                    //TODO: Create and throw custom exception
                    //PrintError("Invalid resource position %d (%d, %d).\n", Index, TempAssetInit.DTilePosition.X(), TempAssetInit.DTilePosition.Y());
                }
                if((Width() <= TempAssetInit.DTilePosition.X())||(Height() <= TempAssetInit.DTilePosition.Y())){
                    //TODO: Create and throw custom exception
                    //PrintError("Invalid resource position %d (%d, %d).\n", Index, TempAssetInit.DTilePosition.X(), TempAssetInit.DTilePosition.Y());
                }
                DAssetInitializationList.add(TempAssetInit);
            }

            DLumberAvailable.setSize(DTerrainMap.size());
            for(int RowIndex = 0; RowIndex < DLumberAvailable.size(); RowIndex++){
                DLumberAvailable[RowIndex].resize(DTerrainMap.get(RowIndex).size());
                for(int ColIndex = 0; ColIndex < DTerrainMap.get(RowIndex).size(); ColIndex++){
                    if(ETerrainTileType.Forest == DTerrainMap.get(RowIndex).get(ColIndex)){
                        int Initlumb;
                        if(DPartials.get(RowIndex).get(ColIndex)) {
                            Initlumb = InitialLumber;
                        } else {
                            Initlumb = 0;
                        }
                        DLumberAvailable.get(RowIndex).set(ColIndex,Initlumb);
                    }
                    else{
                        DLumberAvailable.get(RowIndex).set(ColIndex, 0);
                    }
                }
            }

            ReturnStatus = true;
        }
        return ReturnStatus;

    }

    /**
     * Get function, return the list of assets DAssets
     *
     * @param[in] Nothing
     *
     * @return DAssets
     *
     */
    // TODO: Uncomment when PlayerAsset is available
    /*
    public List< PlayerAsset > Assets(){
        return DAssets;
    }
*/

    /**
     * Get function, return the asset initialization list
     *
     * @param[in] Nothing
     *
     * @return DAssetInitializationList
     *
     */

    public List< SAssetInitialization > AssetInitializationList(){
            return DAssetInitializationList;
        }

    /**
     * Get function, return the resource initiliazation list
     *
     * @param[in] Nothing
     *
     * @return DResourceInitializationList
     *
     */

    public < SResourceInitialization > ResourceInitializationList() {
        return DResourceInitializationList;
    }

/**
 * Initialize a blank map with dimensions matching DMap and DTerrainMap
 *
 * @param[in] Nothing
 *
 * @return pointer to blank map
 *
 */
//TODO: figure out why we need this and fix it
/*
    AssetDecoratedMap CreateInitializeMap() {
        std.shared_ptr< AssetDecoratedMap > ReturnMap = AssetDecoratedMap();

        if(ReturnMap.DMap.size() != DMap.size()){
            ReturnMap.DTerrainMap = DTerrainMap;
            ReturnMap.DPartials = DPartials;

            // Initialize to empty grass
            ReturnMap.DMap.resize(DMap.size());
            for(auto &Row : ReturnMap.DMap){
                Row.resize(DMap[0].size());
                for(auto &Cell : Row){
                    Cell = ETileType.None;
                }
            }
            ReturnMap.DMapIndices.resize(DMap.size());
            for(auto &Row : ReturnMap.DMapIndices){
                Row.resize(DMapIndices[0].size());
                for(auto &Cell : Row){
                    Cell = 0;
                }
            }
        }
        return ReturnMap;
    }
*/

/**
 * Create a visibility map with dimensions of your current map
 *
 * @param[in] Nothing
 *
 * @return pointer to visibility map
 *
 */
//TODO: Do we need the visibility map?
/*
    std.shared_ptr< CVisibilityMap > CreateVisibilityMap() const{
        return std.make_shared< CVisibilityMap >(Width(), Height(), CPlayerAssetType.MaxSight());
    }*/


    /**
     * Update your current map to match the input resmap
     *
     * @param[in] vismap Visibility map to remove visible assets so they can be updated
     * @param[in] resmap The map to copy
     *
     * @return true if successfully updated
     *
     */
//TODO: Do we need the visibility map?
/*
    boolean UpdateMap(CVisibilityMap &vismap, AssetDecoratedMap &resmap){
        auto Iterator = DAssets.begin();

        if(DMap.size() != resmap.DMap.size()){
            DTerrainMap = resmap.DTerrainMap;
            DPartials = resmap.DPartials;
            DMap.resize(resmap.DMap.size());
            for(auto &Row : DMap){
                Row.resize(resmap.DMap[0].size());
                for(auto &Cell : Row){
                    Cell = ETileType.None;
                }
            }
            DMapIndices.resize(resmap.DMapIndices.size());
            for(auto &Row : DMapIndices){
                Row.resize(resmap.DMapIndices[0].size());
                for(auto &Cell : Row){
                    Cell = 0;
                }
            }
        }
        while(Iterator != DAssets.end()){
            TilePosition CurPosition = (*Iterator).TilePosition();
            int AssetSize = (*Iterator).Size();
            boolean RemoveAsset = false;
            if((*Iterator).Speed()||(EAssetAction.Decay == (*Iterator).Action())||(EAssetAction.Attack == (*Iterator).Action())){  // Remove all movable units
                Iterator = DAssets.erase(Iterator);
                continue;
            }
            for(int YOff = 0; YOff < AssetSize; YOff++){
                int YPos = CurPosition.Y() + YOff;
                for(int XOff = 0; XOff < AssetSize; XOff++){
                    int XPos = CurPosition.X() + XOff;

                    CVisibilityMap.ETileVisibility VisType = vismap.TileType(XPos, YPos);
                    if((CVisibilityMap.ETileVisibility.Partial == VisType)||(CVisibilityMap.ETileVisibility.PartialPartial == VisType)||(CVisibilityMap.ETileVisibility.Visible == VisType)){ // Remove visible so they can be updated
                        RemoveAsset = EAssetType.None != (*Iterator).Type();
                        break;
                    }
                }
                if(RemoveAsset){
                    break;
                }
            }
            if(RemoveAsset){
                Iterator = DAssets.erase(Iterator);
                continue;
            }
            Iterator++;
        }
        for(int YPos = 0; YPos < DMap.size(); YPos++){
            for(int XPos = 0; XPos < DMap[YPos].size(); XPos++){
                CVisibilityMap.ETileVisibility VisType = vismap.TileType(XPos-1, YPos-1);
                if((CVisibilityMap.ETileVisibility.Partial == VisType)||(CVisibilityMap.ETileVisibility.PartialPartial == VisType)||(CVisibilityMap.ETileVisibility.Visible == VisType)){
                    DMap[YPos][XPos] = resmap.DMap[YPos][XPos];
                    DMapIndices[YPos][XPos] = resmap.DMapIndices[YPos][XPos];
                }
            }
        }
        for(auto &Asset : resmap.DAssets){
            TilePosition CurPosition = Asset.TilePosition();
            int AssetSize = Asset.Size();
            boolean AddAsset = false;

            for(int YOff = 0; YOff < AssetSize; YOff++){
                int YPos = CurPosition.Y() + YOff;
                for(int XOff = 0; XOff < AssetSize; XOff++){
                    int XPos = CurPosition.X() + XOff;

                    CVisibilityMap.ETileVisibility VisType = vismap.TileType(XPos, YPos);
                    if((CVisibilityMap.ETileVisibility.Partial == VisType)||(CVisibilityMap.ETileVisibility.PartialPartial == VisType)||(CVisibilityMap.ETileVisibility.Visible == VisType)){ // Add visible resources
                        AddAsset = true;
                        break;
                    }
                }
                if(AddAsset){
                    DAssets.push_back(Asset);
                    break;
                }
            }
        }

        return true;
    }

#define SEARCH_STATUS_UNVISITED 0
            #define SEARCH_STATUS_QUEUED    1
            #define SEARCH_STATUS_VISITED   2

    typedef struct{
        int DX;
        int DY;
    } SSearchTile, *SSearchTileRef;
*/
    /**
     * Find the position of the nearest reachable tile of a certain type
     *
     * @param[in] pos Starting position
     * @param[in] type The type of tile you want to reach
     *
     * @return the position of the new tile
     *
     */
//TODO: implement this function
/*
    TilePosition FindNearestReachableTileType(TilePosition &pos, ETileType type){
        std.queue< SSearchTile > SearchQueue;
        SSearchTile CurrentSearch, TempSearch;
        int MapWidth = Width();
        int MapHeight = Height();
        int SearchXOffsets[] = {0,1,0,-1};
        int SearchYOffsets[] = {-1,0,1,0};

        if(DSearchMap.size() != DMap.size()){
            DSearchMap.resize(DMap.size());
            for(auto &Row : DSearchMap){
                Row.resize(DMap[0].size());
                for(auto &Cell : Row){
                    Cell = 0;
                }
            }
            int LastYIndex = DMap.size() - 1;
            int LastXIndex = DMap[0].size() - 1;
            for(int Index = 0; Index < DMap.size(); Index++){
                DSearchMap[Index][0] = SEARCH_STATUS_VISITED;
                DSearchMap[Index][LastXIndex] = SEARCH_STATUS_VISITED;
            }
            for(int Index = 1; Index < LastXIndex; Index++){
                DSearchMap[0][Index] = SEARCH_STATUS_VISITED;
                DSearchMap[LastYIndex][Index] = SEARCH_STATUS_VISITED;
            }
        }
        for(int Y = 0; Y < MapHeight; Y++){
            for(int X = 0; X < MapWidth; X++){
                DSearchMap[Y+1][X+1] = SEARCH_STATUS_UNVISITED;
            }
        }
        for(auto Asset : DAssets){
            if(Asset.TilePosition() != pos){
                for(int Y = 0; Y < Asset.Size(); Y++){
                    for(int X = 0; X < Asset.Size(); X++){
                        DSearchMap[Asset.TilePositionY()+Y+1][Asset.TilePositionX()+X+1] = SEARCH_STATUS_VISITED;
                    }
                }
            }
        }

        CurrentSearch.DX = pos.X() + 1;
        CurrentSearch.DY = pos.Y() + 1;
        SearchQueue.push(CurrentSearch);
        while(SearchQueue.size()){
            CurrentSearch = SearchQueue.front();
            SearchQueue.pop();
            DSearchMap[CurrentSearch.DY][CurrentSearch.DX] = SEARCH_STATUS_VISITED;
            for(int Index = 0; Index < (sizeof(SearchXOffsets)/sizeof(int)); Index++){
                TempSearch.DX = CurrentSearch.DX + SearchXOffsets[Index];
                TempSearch.DY = CurrentSearch.DY + SearchYOffsets[Index];
                if(SEARCH_STATUS_UNVISITED == DSearchMap[TempSearch.DY][TempSearch.DX]){
                    ETileType CurTileType =DMap[TempSearch.DY][TempSearch.DX];

                    DSearchMap[TempSearch.DY][TempSearch.DX] = SEARCH_STATUS_QUEUED;
                    if(type == CurTileType){
                        return TilePosition(TempSearch.DX - 1, TempSearch.DY - 1);
                    }
                    //if((ETileType.Grass == CurTileType)||(ETileType.Dirt == CurTileType)||(ETileType.Stump == CurTileType)||(ETileType.Rubble == CurTileType)||(ETileType.None == CurTileType)){
                    if(IsTraversable(CurTileType)){
                        SearchQueue.push(TempSearch);
                    }
                }
            }
        }
        return TilePosition(-1, -1);
    }
*/


}