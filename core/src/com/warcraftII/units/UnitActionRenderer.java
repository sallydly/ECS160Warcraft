package com.warcraftII.units;

import com.warcraftII.GameDataTypes.*;
import com.warcraftII.player_asset.PlayerAsset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static com.warcraftII.GameDataTypes.EAssetAction.Capability;

public class UnitActionRenderer {
    protected Vector<EAssetCapabilityType> DDisplayedCommands;
    protected EPlayerColor DPlayerColor;

    void DrawUnitAction(List<PlayerAsset> selectionlist, EAssetCapabilityType currentaction) {
        boolean AllSame = true;
        boolean IsFirst = true;
        boolean Moveable = true;
        boolean HasCargo = false;
        EAssetType UnitType = EAssetType.None;

        Collections.fill(DDisplayedCommands, EAssetCapabilityType.None);

        if (selectionlist.size() == 0) {
            return;
        }
        for (PlayerAsset asset : selectionlist) {
            //if selection is somehow not your team color, exit function
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

        if (EAssetCapabilityType.None.equals(currentaction)) {
            if (Moveable) {

                DDisplayedCommands.set(0, HasCargo ? EAssetCapabilityType.Convey : EAssetCapabilityType.Move);
                DDisplayedCommands.set(1, EAssetCapabilityType.StandGround);
                DDisplayedCommands.set(2, EAssetCapabilityType.Attack);
                if (!asset.equals(null)) {
                    if (asset.HasCapability(EAssetCapabilityType.Repair)) {
                        DDisplayedCommands.set(3, EAssetCapabilityType.Repair);
                    }
                    else if (asset.HasCapability(EAssetCapabilityType.Patrol)) {
                        DDisplayedCommands.set(3, EAssetCapabilityType.Patrol);
                    }
                    if (asset.HasCapability(EAssetCapabilityType.Mine)) {
                        DDisplayedCommands.set(4, EAssetCapabilityType.Mine);
                    }
                    if (asset.HasCapability(EAssetCapabilityType.BuildSimple) && (1 == selectionlist.size())) {
                        DDisplayedCommands.set(6, EAssetCapabilityType.BuildSimple);
                    }
                }
            } else {
                if (!asset.equals(null)) {
                    if ((EAssetAction.Construct.equals(asset.Action())) || (Capability.equals(asset.Action()))) {
                        DDisplayedCommands.set(DDisplayedCommands.size() - 1, EAssetCapabilityType.Cancel);
                    } else {
                        int Index = 0;
                        for (EAssetCapabilityType Capability : asset.Capabilities()) {
                            DDisplayedCommands.set(Index, Capability);
                            Index++;
                            if (DDisplayedCommands.size() <= Index) {
                                break;
                            }
                        }
                    }
                }
            }
        } else if (EAssetCapabilityType.BuildSimple == currentaction) {
            if (!asset.equals(null)) {
                int Index = 0;
                List<EAssetCapabilityType> capabilityList = new ArrayList<EAssetCapabilityType>();
                capabilityList.add(EAssetCapabilityType.BuildFarm);
                capabilityList.add(EAssetCapabilityType.BuildTownHall);
                capabilityList.add(EAssetCapabilityType.BuildBarracks);
                capabilityList.add(EAssetCapabilityType.BuildLumberMill);
                capabilityList.add(EAssetCapabilityType.BuildBlacksmith);
                capabilityList.add(EAssetCapabilityType.BuildKeep);
                capabilityList.add(EAssetCapabilityType.BuildCastle);
                capabilityList.add(EAssetCapabilityType.BuildScoutTower);
                capabilityList.add(EAssetCapabilityType.BuildGuardTower);
                capabilityList.add(EAssetCapabilityType.BuildCannonTower);
                capabilityList.add(EAssetCapabilityType.BuildWall);

                for (EAssetCapabilityType Capability : capabilityList) {
                    if (asset.HasCapability(Capability)) {
                        DDisplayedCommands.set(Index, Capability);
                        Index++;
                        if (DDisplayedCommands.size() <= Index) {
                            break;
                        }
                    }
                }
                DDisplayedCommands.set(DDisplayedCommands.size() - 1, EAssetCapabilityType.Cancel);
            }
        } else {
            DDisplayedCommands.set(DDisplayedCommands.size() - 1, EAssetCapabilityType.Cancel);
        }

//        int XOffset = DBevel->Width();
//        int YOffset = DBevel->Width();
        int Index = 0;

        /**
         * @brief Draws Possible Icons with Bevel Shading
         */
//        for(auto IconType : DDisplayedCommands){
//            if(EAssetCapabilityType.None != IconType){
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
