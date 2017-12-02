package com.warcraftII.units;

import com.warcraftII.GameDataTypes.EAssetCapabilityType;
import com.warcraftII.GameDataTypes.EPlayerColor;
import com.warcraftII.GameDataTypes.EUnitType;
import com.warcraftII.player_asset.PlayerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static com.warcraftII.GameDataTypes.to_underlying;

public class UnitActionRenderer {
    protected PlayerData DPlayerData;
    protected Vector<Integer> DCommandIndices;
    protected Vector<EAssetCapabilityType> DDisplayedCommands;
    protected EPlayerColor DPlayerColor;

    public UnitActionRenderer(/*GraphicTileset icons, GraphicTileset rangertrackingicon,*/ EPlayerColor color, PlayerData player){
//        DIconTileset = icons;
//        DRangerTrackingIcon = rangertrackingicon;
//        DBevel = bevel;
        DPlayerData = player;
        DPlayerColor = color;

        DCommandIndices = new Vector<Integer>();
        DCommandIndices.setSize(to_underlying(EAssetCapabilityType.Max));
//        DFullIconWidth = DIconTileset->TileWidth() + DBevel->Width() * 2;
//        DFullIconHeight = DIconTileset->TileHeight() + DBevel->Width() * 2;
        DDisplayedCommands = new Vector<EAssetCapabilityType>(9);
        DDisplayedCommands.setSize(9);
        for(int i = 0; i < DDisplayedCommands.size(); i++){
            DDisplayedCommands.set(i, EAssetCapabilityType.None);
        }
//        DCommandIndices[to_underlying(EAssetCapabilityType.None)] = -1;
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildPeasant)] = DIconTileset->FindTile("peasant");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildFootman)] = DIconTileset->FindTile("footman");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildArcher)] = DIconTileset->FindTile("archer");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildRanger)] = DIconTileset->FindTile("ranger");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildKnight)] = DIconTileset->FindTile("knight");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildFarm)] = DIconTileset->FindTile("chicken-farm");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildTownHall)] = DIconTileset->FindTile("town-hall");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildBarracks)] = DIconTileset->FindTile("human-barracks");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildLumberMill)] = DIconTileset->FindTile("human-lumber-mill");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildBlacksmith)] = DIconTileset->FindTile("human-blacksmith");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildKeep)] = DIconTileset->FindTile("keep");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildCastle)] = DIconTileset->FindTile("castle");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildScoutTower)] = DIconTileset->FindTile("scout-tower");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildGuardTower)] = DIconTileset->FindTile("human-guard-tower");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildCannonTower)] = DIconTileset->FindTile("human-cannon-tower");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Move)] = DIconTileset->FindTile("human-move");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Repair)] = DIconTileset->FindTile("repair");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Mine)] = DIconTileset->FindTile("mine");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildSimple)] = DIconTileset->FindTile("build-simple");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildAdvanced)] = DIconTileset->FindTile("build-advanced");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Convey)] = DIconTileset->FindTile("human-convey");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Cancel)] = DIconTileset->FindTile("cancel");
//        DCommandIndices[to_underlying(EAssetCapabilityType.BuildWall)] = DIconTileset->FindTile("human-wall");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Attack)] = DIconTileset->FindTile("human-weapon-1");
//        DCommandIndices[to_underlying(EAssetCapabilityType.StandGround)] = DIconTileset->FindTile("human-armor-1");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Patrol)] = DIconTileset->FindTile("human-patrol");
//        DCommandIndices[to_underlying(EAssetCapabilityType.WeaponUpgrade1)] = DIconTileset->FindTile("human-weapon-1");
//        DCommandIndices[to_underlying(EAssetCapabilityType.WeaponUpgrade2)] = DIconTileset->FindTile("human-weapon-2");
//        DCommandIndices[to_underlying(EAssetCapabilityType.WeaponUpgrade3)] = DIconTileset->FindTile("human-weapon-3");
//        DCommandIndices[to_underlying(EAssetCapabilityType.ArrowUpgrade1)] = DIconTileset->FindTile("human-arrow-1");
//        DCommandIndices[to_underlying(EAssetCapabilityType.ArrowUpgrade2)] = DIconTileset->FindTile("human-arrow-2");
//        DCommandIndices[to_underlying(EAssetCapabilityType.ArrowUpgrade3)] = DIconTileset->FindTile("human-arrow-3");
//        DCommandIndices[to_underlying(EAssetCapabilityType.ArmorUpgrade1)] = DIconTileset->FindTile("human-armor-1");
//        DCommandIndices[to_underlying(EAssetCapabilityType.ArmorUpgrade2)] = DIconTileset->FindTile("human-armor-2");
//        DCommandIndices[to_underlying(EAssetCapabilityType.ArmorUpgrade3)] = DIconTileset->FindTile("human-armor-3");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Longbow)] = DIconTileset->FindTile("longbow");
//        DCommandIndices[to_underlying(EAssetCapabilityType.RangerScouting)] = DIconTileset->FindTile("ranger-scouting");
//        DCommandIndices[to_underlying(EAssetCapabilityType.Marksmanship)] = DIconTileset->FindTile("marksmanship");
//
//        DDisabledIndex = DIconTileset->FindTile("disabled");
    }

    public Vector<EAssetCapabilityType> DrawUnitAction(List<Unit.IndividualUnit> selectionlist, EAssetCapabilityType currentaction) {
        boolean AllSame = true;
        boolean IsFirst = true;
        boolean Moveable = true;
        boolean HasCargo = false;

        //old value of UnitType -> EAssetType UnitType = EAssetType.None;
        EUnitType unitType = EUnitType.None;

        Collections.fill(DDisplayedCommands, EAssetCapabilityType.None);
        //DDisplayedCommands.add(EAssetCapabilityType.None);

        if (selectionlist.size() == 0) {
            return DDisplayedCommands;
        }
        for (Unit.IndividualUnit unit : selectionlist) {
            //if selection is somehow not your team color, exit function
            //FIXME: commented out for now
//            if (!DPlayerColor.equals(unit.color)) {
//                return DDisplayedCommands;
//            }

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

                DDisplayedCommands.set(0, HasCargo ? EAssetCapabilityType.Convey : EAssetCapabilityType.Move);
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

        return DDisplayedCommands;
    }
}
