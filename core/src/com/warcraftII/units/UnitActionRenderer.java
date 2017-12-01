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

    void DrawUnitAction(List<Unit.IndividualUnit> selectionlist, EAssetCapabilityType currentaction) {
        boolean AllSame = true;
        boolean IsFirst = true;
        boolean Moveable = true;
        boolean HasCargo = false;

        //old value of UnitType -> EAssetType UnitType = EAssetType.None;
        EUnitType unitType = EUnitType.None;


        Collections.fill(DDisplayedCommands, EAssetCapabilityType.None);

        if (selectionlist.size() == 0) {
            return;
        }
        for (Unit.IndividualUnit unit : selectionlist) {
            //if selection is somehow not your team color, exit function
            if (!DPlayerColor.equals(unit.color)) {
                return;
            }

            if (IsFirst) {
                unitType = unit.unitClass;
                IsFirst = false;
                Moveable = 0 < unit.speed;
            } else if (!unitType.equals(unit.unitClass)) {
                AllSame = false;
            }

//            FIXME: Not sure how to deal with this since IndividualUnit
//              doesnt keep track of cargo rn
//
//            if (asset.Lumber() != 0 || asset.Gold() != 0) {
//                HasCargo = true;
//            }
        }

        Unit.IndividualUnit unit = null;
        if (selectionlist.size() > 0) {
            unit = selectionlist.get(0);
        }

        if (EAssetCapabilityType.None.equals(currentaction)) {
            if (Moveable) {

                DDisplayedCommands.set(0, EAssetCapabilityType.Move);
                DDisplayedCommands.set(1, EAssetCapabilityType.StandGround);
                DDisplayedCommands.set(2, EAssetCapabilityType.Attack);
                if (!(unit == null)) {
                    if (unit.hasCapability(EAssetCapabilityType.Repair)) {
                        DDisplayedCommands.set(3, EAssetCapabilityType.Repair);
                    }
                    else if (unit.hasCapability(EAssetCapabilityType.Patrol)) {
                        DDisplayedCommands.set(3, EAssetCapabilityType.Patrol);
                    }
                    if (unit.hasCapability(EAssetCapabilityType.Mine)) {
                        DDisplayedCommands.set(4, EAssetCapabilityType.Mine);
                    }

                    //NOTE: figure out second boolean expression!
                    if (unit.hasCapability(EAssetCapabilityType.BuildSimple) && (1 == selectionlist.size())) {
                        DDisplayedCommands.set(6, EAssetCapabilityType.BuildSimple);
                    }
                }
            } else {
                if (!(unit == null)) {
                    //this first if adds a cancel button, but we don't have the Action() method handy
                    //  because we switched to IndividualUnit instead of playerasset
//                    if ((EAssetAction.Construct.equals(unit.Action())) || (Capability.equals(unit.Action()))) {
//                        DDisplayedCommands.set(DDisplayedCommands.size() - 1, EAssetCapabilityType.Cancel);
//                    } else {
                        int Index = 0;
                        for (EAssetCapabilityType Capability : unit.abilities) {
                            DDisplayedCommands.set(Index, Capability);
                            Index++;
                            if (DDisplayedCommands.size() <= Index) {
                                break;
                            }
                        }
//                    }
                }
            }
        } else if (EAssetCapabilityType.BuildSimple == currentaction) {
            if (!(unit == null)) {
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
                    if (unit.hasCapability(Capability)) {
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
