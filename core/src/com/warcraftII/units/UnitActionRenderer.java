package com.warcraftII.units;

import com.warcraftII.GameDataTypes;
import com.warcraftII.player_asset.PlayerAsset;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.warcraftII.GameDataTypes.EAssetAction.Capability;

public class UnitActionRenderer {
    protected Vector<GameDataTypes.EAssetCapabilityType> DDisplayedCommands;
    protected GameDataTypes.EPlayerColor DPlayerColor;

    void DrawUnitAction(List<PlayerAsset> selectionlist, GameDataTypes.EAssetCapabilityType currentaction) {
        boolean AllSame = true;
        boolean IsFirst = true;
        boolean Moveable = true;
        boolean HasCargo = false;
        GameDataTypes.EAssetType UnitType;

        for (GameDataTypes.EAssetCapabilityType Command : DDisplayedCommands) {
            Command = GameDataTypes.EAssetCapabilityType.None;
        }
        if (selectionlist.size() != 0) {
            return;
        }
        for (PlayerAsset asset : selectionlist) {
            if (!DPlayerColor.equals(asset.Color())) {
                return;
            }
            if (IsFirst) {
                UnitType = asset.Type();
                IsFirst = false;
                Moveable = 0 < asset.Speed();
            } else if (!UnitType.equals(asset.Type())) {
                AllSame = false;
            }
            if (asset.Lumber() != 0 || asset.Gold() != 0) {
                HasCargo = true;
            }
        }

        PlayerAsset asset = null;
        if (selectionlist.size() > 0) {
            asset = selectionlist.get(0);
        }

        if (GameDataTypes.EAssetCapabilityType.None.equals(currentaction)) {
            if (Moveable) {

                DDisplayedCommands.set(0, HasCargo ? GameDataTypes.EAssetCapabilityType.Convey : GameDataTypes.EAssetCapabilityType.Move);
                DDisplayedCommands.set(1, GameDataTypes.EAssetCapabilityType.StandGround);
                DDisplayedCommands.set(2, GameDataTypes.EAssetCapabilityType.Attack);
                if (!asset.equals(null)) {
                    if (asset.HasCapability(GameDataTypes.EAssetCapabilityType.Repair)) {
                        DDisplayedCommands.set(3, GameDataTypes.EAssetCapabilityType.Repair);
                    }
                    if (asset.HasCapability(GameDataTypes.EAssetCapabilityType.Patrol)) {
                        DDisplayedCommands.set(3, GameDataTypes.EAssetCapabilityType.Patrol);
                    }
                    if (asset.HasCapability(GameDataTypes.EAssetCapabilityType.Mine)) {
                        DDisplayedCommands.set(4, GameDataTypes.EAssetCapabilityType.Mine);
                    }
                    if (asset.HasCapability(GameDataTypes.EAssetCapabilityType.BuildSimple) && (1 == selectionlist.size())) {
                        DDisplayedCommands.set(6, GameDataTypes.EAssetCapabilityType.BuildSimple);
                    }
                }
            } else {
                if (!asset.equals(null)) {
                    if ((GameDataTypes.EAssetAction.Construct.equals(asset.Action())) || (Capability.equals(asset.Action()))) {
                        DDisplayedCommands.set(DDisplayedCommands.size() - 1, GameDataTypes.EAssetCapabilityType.Cancel);
                    } else {
                        int Index = 0;
                        for (GameDataTypes.EAssetCapabilityType Capability : asset.Capabilities()) {
                            DDisplayedCommands.set(Index, Capability);
                            Index++;
                            if (DDisplayedCommands.size() <= Index) {
                                break;
                            }
                        }
                    }
                }
            }
        } else if (GameDataTypes.EAssetCapabilityType.BuildSimple == currentaction) {
            if (!asset.equals(null)) {
                int Index = 0;
                List<GameDataTypes.EAssetCapabilityType> capabilityList = new ArrayList<GameDataTypes.EAssetCapabilityType>();
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildFarm);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildTownHall);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildBarracks);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildLumberMill);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildBlacksmith);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildKeep);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildCastle);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildScoutTower);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildGuardTower);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildCannonTower);
                capabilityList.add(GameDataTypes.EAssetCapabilityType.BuildWall);

                for (GameDataTypes.EAssetCapabilityType Capability : capabilityList) {
                    if (asset.HasCapability(Capability)) {
                        DDisplayedCommands.set(Index, Capability);
                        Index++;
                        if (DDisplayedCommands.size() <= Index) {
                            break;
                        }
                    }
                }
                DDisplayedCommands.set(DDisplayedCommands.size() - 1, GameDataTypes.EAssetCapabilityType.Cancel);
            }
        } else {
            DDisplayedCommands.set(DDisplayedCommands.size() - 1, GameDataTypes.EAssetCapabilityType.Cancel);
        }

//        int XOffset = DBevel->Width();
//        int YOffset = DBevel->Width();
        int Index = 0;

        /**
         * @brief Draws Possible Icons with Bevel Shading
         */
//        for(auto IconType : DDisplayedCommands){
//            if(GameDataTypes.EAssetCapabilityType.None != IconType){
//                auto PlayerCapability = CPlayerCapability.FindCapability(IconType);
//                DBevel->DrawBevel(surface, XOffset, YOffset, DIconTileset->TileWidth(), DIconTileset->TileHeight());
//                DIconTileset->DrawTile(surface, XOffset, YOffset, DCommandIndices[to_underlying(IconType)]);
//                if(PlayerCapability){
//                    if(!PlayerCapability->CanInitiate(selectionlist.front().lock(), DPlayerData)){
//                        DIconTileset->DrawTile(surface, XOffset, YOffset, DDisabledIndex);
//                    }
//                }
//            }
//
//            XOffset += DFullIconWidth + DBevel->Width();
//            Index++;
//            if(0 == (Index % 3)){
//                XOffset = DBevel->Width();
//                YOffset += DFullIconHeight + DBevel->Width();
//            }
//        }
    }
}
