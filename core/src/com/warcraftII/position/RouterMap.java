package com.warcraftII.position;

import com.badlogic.gdx.Gdx;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.units.Unit;
import com.warcraftII.GameDataTypes;
import com.warcraftII.terrain_map.TileTypes;
import com.warcraftII.terrain_map.TerrainMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by 9S on 11/6/2017.
 */

public class RouterMap {
    class SSearchTarget {
        int DX;
        int DY;
        int DSteps;
        TileTypes.ETileType DTileType;
        int DTargetDistanceSquared;
        GameDataTypes.EDirection DInDirection;
        SSearchTarget(){
            DX = 0;
            DY = 0;
            DSteps = 0;
            DTileType = TileTypes.ETileType.None;
            DTargetDistanceSquared = 0;
            DInDirection = GameDataTypes.EDirection.Max;
        }
    }

    ArrayList< ArrayList< Integer > > DMap;
    ArrayList< SSearchTarget > DSearchTargets;
    static GameDataTypes.EDirection DIdealSearchDirection = GameDataTypes.EDirection.North;
    static int DMapWidth = 1;
    static final int SEARCH_STATUS_UNVISITED = -1;
    static final int SEARCH_STATUS_VISITED = -2;
    static final int SEARCH_STATUS_OCCUPIED = -3;

    static boolean MovingAway(GameDataTypes.EDirection dir1, GameDataTypes.EDirection dir2){
        int Value;
        if((0 > GameDataTypes.to_underlying(dir2))||(GameDataTypes.to_underlying(GameDataTypes.EDirection.Max) <= GameDataTypes.to_underlying(dir2))){
            return false;
        }
        Value = ((GameDataTypes.to_underlying(GameDataTypes.EDirection.Max) + GameDataTypes.to_underlying(dir2)) - GameDataTypes.to_underlying(dir1)) % GameDataTypes.to_underlying(GameDataTypes.EDirection.Max);
        if((1 >= Value)||(GameDataTypes.to_underlying(GameDataTypes.EDirection.Max) - 1 <= Value)){
            return true;
        }
        return false;
    }

    //TODO: could change so assetdecorated map has all units?
    GameDataTypes.EDirection FindRoute(AssetDecoratedMap resmap, Unit.IndividualUnit asset, PixelPosition target){
        int MapWidth = resmap.Width();
        int MapHeight = resmap.Height();
        //TODO: Verify if this is correct coordinate!
        UnitPosition unitPosition = new UnitPosition(Math.round(asset.getX()), Math.round(asset.getY()));
        int StartX = unitPosition.X();
        int StartY = unitPosition.Y();
        /*int StartX = asset.TilePositionX(); //TODO: for unit class
        int StartY = asset.TilePositionY(); //TODO: for unit class
        */
        TerrainMap terrainMap = new TerrainMap();
        SSearchTarget CurrentSearch = new SSearchTarget(), BestSearch = new SSearchTarget(), TempSearch = new SSearchTarget();
        TilePosition CurrentTile, TargetTile = new TilePosition(), TempTile = new TilePosition();
        GameDataTypes.EDirection SearchDirecitons[] = {GameDataTypes.EDirection.North, GameDataTypes.EDirection.East, GameDataTypes.EDirection.South, GameDataTypes.EDirection.West};
        int ResMapXOffsets[] = {0,1,0,-1};
        int ResMapYOffsets[] = {-1,0,1,0};
        int DiagCheckXOffset[] = {0,1,1,1,0,-1,-1,-1};
        int DiagCheckYOffset[] = {-1,-1,0,1,1,1,0,-1};
        int SearchDirectionCount = SearchDirecitons.length/ GameDataTypes.EDirection.values().length;
        GameDataTypes.EDirection LastInDirection, DirectionBeforeLast;
        //TODO: Can LinkedLists work??
        Queue< SSearchTarget > SearchQueue = new LinkedList<SSearchTarget>();

        TargetTile.setFromPixel(target);
        if((DMap.size() != MapHeight + 2)||(DMap.get(0).size() != MapWidth + 2)){
            int LastYIndex = MapHeight + 1;
            int LastXIndex = MapWidth + 1;
            //TODO: Check if ensureCapacity is even necessary
            DMap.ensureCapacity(MapHeight + 2);
            for(ArrayList<Integer> Row : DMap){
                Row.ensureCapacity(MapWidth + 2);
            }
            for(int Index = 0; Index < DMap.size(); Index++){
                DMap.get(Index).set(0, SEARCH_STATUS_VISITED);
                DMap.get(Index).set(LastXIndex, SEARCH_STATUS_VISITED);
            }
            for(int Index = 0; Index < MapWidth; Index++){
                DMap.get(0).set(Index+1, SEARCH_STATUS_VISITED);
                DMap.get(Index).set(Index+1, SEARCH_STATUS_VISITED);
            }
            DMapWidth = MapWidth + 2;
        }

        if(unitPosition.equals(TargetTile)){
            int DeltaX = target.X() - unitPosition.X();
            int DeltaY = target.Y() - unitPosition.Y();

            if(0 < DeltaX){
                if(0 < DeltaY){
                    return GameDataTypes.EDirection.NorthEast;
                }
                else if(0 > DeltaY){
                    return GameDataTypes.EDirection.SouthEast;
                }
                return GameDataTypes.EDirection.East;
            }
            else if(0 > DeltaX){
                if(0 < DeltaY){
                    return GameDataTypes.EDirection.NorthWest;
                }
                else if(0 > DeltaY){
                    return GameDataTypes.EDirection.SouthWest;
                }
                return GameDataTypes.EDirection.West;
            }
            if(0 < DeltaY){
                return GameDataTypes.EDirection.North;
            }
            else if(0 > DeltaY){
                return GameDataTypes.EDirection.South;
            }

            return GameDataTypes.EDirection.Max;
        }

        for(int Y = 0; Y < MapHeight; Y++){
            for(int X = 0; X < MapWidth; X++){
                DMap.get(Y+1).set(X+1, SEARCH_STATUS_UNVISITED);
            }
        }

        //TODO: the original function
        /*for(auto &Res : resmap.Assets()){
            if(&asset != Res.get()){
                if(GameDataTypes.EAssetType.None != Res->Type()){
                    if((EAssetAction::Walk != Res->Action())||(asset.Color() != Res->Color())){
                        if((asset.Color() != Res->Color())||((EAssetAction::ConveyGold != Res->Action())&&(EAssetAction::ConveyLumber != Res->Action())&&(EAssetAction::MineGold != Res->Action())&&(EAssetAction::ConveyStone != Res->Action()))){
                            for(int YOff = 0; YOff < Res->Size(); YOff++){
                                for(int XOff = 0; XOff < Res->Size(); XOff++){
                                    DMap[Res->TilePositionY() + YOff + 1][Res->TilePositionX() + XOff + 1] = SEARCH_STATUS_VISITED;
                                }
                            }
                        }
                    }
                    else{
                        DMap[Res->TilePositionY() + 1][Res->TilePositionX() + 1] = SEARCH_STATUS_OCCUPIED - to_underlying(Res->Direction());
                    }
                }
            }
        }*/
        //TODO: work in progress bc idk what this does
        /*
        for(SAssetInitialization Res : resmap.AssetInitializationList()) {
            if (asset.unitTexInd != Res.DType) {
                if (GameDataTypes.EAssetType.None != Res.DType) { //assets hasn't implemented the file types yet
                    if ((GameDataTypes.EAssetAction.Walk != Res -> Action()) || (asset.getColor() != Res -> Color())) {  //TODO: unit class has yet to have color
                        if ((asset.getColor() != Res.DColor()) || ((GameDataTypes.EAssetAction.ConveyGold != Res -> Action()) && (GameDataTypes.EAssetAction.ConveyLumber != Res -> Action()) && (GameDataTypes.EAssetAction.MineGold != Res -> Action()) && (GameDataTypes.EAssetAction.ConveyStone != Res -> Action()))) {
                            for (int YOff = 0; YOff < Res -> Size(); YOff++) {
                                for (int XOff = 0; XOff < Res -> Size(); XOff++) {
                                    DMap[Res -> TilePositionY() + YOff + 1][Res -> TilePositionX() + XOff + 1] = SEARCH_STATUS_VISITED;
                                }
                            }
                        }
                    } else {
                        DMap.get(Res.DTilePosition.Y() + 1).get(Res.DTilePosition.X() + 1) = SEARCH_STATUS_OCCUPIED);// - GameDataTypes.to_underlying(Res.Direction()); //TODO: what is direction for?
                    }
                }
            }
        }
        */

        //TODO: change back to normal direction once unit fixes theirs
        String dir = asset.getDirection();
        if (dir.equals("n")){
            DIdealSearchDirection = GameDataTypes.EDirection.North;
        } else if (dir.equals("ne")){
            DIdealSearchDirection = GameDataTypes.EDirection.NorthEast;
        } else if (dir.equals("e")){
            DIdealSearchDirection = GameDataTypes.EDirection.East;
        } else if (dir.equals("se")){
            DIdealSearchDirection = GameDataTypes.EDirection.SouthEast;
        } else if (dir.equals("s")){
            DIdealSearchDirection = GameDataTypes.EDirection.South;
        } else if (dir.equals("sw")){
            DIdealSearchDirection = GameDataTypes.EDirection.SouthWest;
        } else if (dir.equals("w")){
            DIdealSearchDirection = GameDataTypes.EDirection.West;
        } else if (dir.equals("nw")){
            DIdealSearchDirection = GameDataTypes.EDirection.NorthWest;
        } else {
            DIdealSearchDirection = GameDataTypes.EDirection.Max;
        }
        //DIdealSearchDirection = asset.getDirection();
        CurrentTile = new TilePosition();
        CurrentTile.SetFromUnit(unitPosition);

        CurrentSearch.DX = BestSearch.DX = CurrentTile.X();
        CurrentSearch.DY = BestSearch.DY = CurrentTile.Y();
        CurrentSearch.DSteps = 0;
        CurrentSearch.DTargetDistanceSquared = BestSearch.DTargetDistanceSquared = CurrentTile.distanceSquared(TargetTile);
        CurrentSearch.DInDirection = BestSearch.DInDirection = GameDataTypes.EDirection.Max;
        DMap.get(StartY+1).set(StartX+1, SEARCH_STATUS_VISITED);
        while(true){
            if(CurrentTile == TargetTile){
                BestSearch = CurrentSearch;
                break;
            }
            if(CurrentSearch.DTargetDistanceSquared < BestSearch.DTargetDistanceSquared){
                BestSearch = CurrentSearch;
            }
            //TODO: this loops need verifying
            for(int Index = 0; Index < SearchDirectionCount; Index++){
                TempTile.X(CurrentSearch.DX + ResMapXOffsets[Index]);
                TempTile.Y(CurrentSearch.DY + ResMapYOffsets[Index]);
                if((SEARCH_STATUS_UNVISITED == DMap.get(TempTile.Y() + 1).get(TempTile.X() + 1))
                        || MovingAway(SearchDirecitons[Index], GameDataTypes.EDirection.values()[(SEARCH_STATUS_OCCUPIED - DMap.get(TempTile.Y() + 1).get(TempTile.X() + 1))])){
                    DMap.get(TempTile.Y() + 1).set(TempTile.X() + 1, Index);
                    TileTypes.ETileType CurTileType = resmap.TileType(TempTile.X(), TempTile.Y());
                    //if((CTerrainMap::ETileType::Grass == CurTileType)||(CTerrainMap::ETileType::Dirt == CurTileType)||(CTerrainMap::ETileType::Stump == CurTileType)||(CTerrainMap::ETileType::Rubble == CurTileType)||(CTerrainMap::ETileType::None == CurTileType)){
                    if(terrainMap.IsTraversable(CurTileType)){
                        TempSearch.DX = TempTile.X();
                        TempSearch.DY = TempTile.Y();
                        TempSearch.DSteps = CurrentSearch.DSteps + 1;
                        TempSearch.DTileType = CurTileType;
                        TempSearch.DTargetDistanceSquared = TempTile.distanceSquared(TargetTile);
                        TempSearch.DInDirection = SearchDirecitons[Index];
                        SearchQueue.add(TempSearch);
                    }
                }
            }
            if(SearchQueue.isEmpty()){
                break;
            }
            CurrentSearch = SearchQueue.remove();
            CurrentTile.X(CurrentSearch.DX);
            CurrentTile.Y(CurrentSearch.DY);
        }
        DirectionBeforeLast = LastInDirection = BestSearch.DInDirection;
        CurrentTile.X(BestSearch.DX);
        CurrentTile.Y(BestSearch.DY);
        while((CurrentTile.X() != StartX)||(CurrentTile.Y() != StartY)){
            int Index = DMap.get(CurrentTile.Y()+1).get(CurrentTile.X()+1);

            if((0 > Index)||(SearchDirectionCount <= Index)){
                //exit(0);
                //TODO: what does this do???
                Gdx.app.exit();
            }
            DirectionBeforeLast = LastInDirection;
            LastInDirection = SearchDirecitons[Index];
            CurrentTile.decrementX(ResMapXOffsets[Index]);
            CurrentTile.decrementY(ResMapYOffsets[Index]);
        }
        if(DirectionBeforeLast != LastInDirection){
            TileTypes.ETileType CurTileType = resmap.TileType(StartX + DiagCheckXOffset[GameDataTypes.to_underlying(DirectionBeforeLast)], StartY + DiagCheckYOffset[GameDataTypes.to_underlying(DirectionBeforeLast)]);
            if(terrainMap.IsTraversable(CurTileType)){
                int Sum = GameDataTypes.to_underlying(LastInDirection) + GameDataTypes.to_underlying(DirectionBeforeLast);
                if((6 == Sum)&&((GameDataTypes.EDirection.North == LastInDirection) || (GameDataTypes.EDirection.North == DirectionBeforeLast))){ // NW wrap around
                    Sum += 8;
                }
                Sum /= 2;
                LastInDirection = GameDataTypes.EDirection.values()[Sum];
            }
        }

        return LastInDirection;
    }
}
