package com.warcraftII;


/* Ported from GameDataTypes.h in ECS160Linux's include directory.
    Converted C++ enum classes to normal enums in java, since java enums have data safety.
*/

import java.util.Arrays;
import java.util.List;

public class GameDataTypes {

    public enum EPlayerColor {
        None,
        Red,
        Blue,
        Green,
        Purple,
        Orange,
        Yellow,
        Black,
        White,
        Max
    }

    public static String toString(EPlayerColor inColor) {
        if (inColor == EPlayerColor.Max) {
            return "none";
        }
        return inColor.toString().toLowerCase();
    }

    public enum EAssetAction {
        None,
        Construct,
        Build,
        Repair,
        Walk,
        StandGround,
        Attack,
        HarvestLumber,
        MineGold,
        ConveyLumber,
        ConveyGold,
        Death,
        Decay,
        Capability
    }

    public enum EAssetCapabilityType {
        None,
        BuildPeasant,
        BuildFootman,
        BuildArcher,
        BuildRanger,
        BuildFarm,
        BuildTownHall,
        BuildBarracks,
        BuildLumberMill,
        BuildBlacksmith,
        BuildKeep,
        BuildCastle,
        BuildScoutTower,
        BuildGuardTower,
        BuildCannonTower,
        Move,
        Repair,
        Mine,
        Lumber,
        BuildSimple,
        BuildAdvanced,
        Convey,
        Cancel,
        CarryingLumber,
        CarryingGold,
        CarryingStone,
        BuildWall,
        Attack,
        StandGround,
        Patrol,
        WeaponUpgrade1,
        WeaponUpgrade2,
        WeaponUpgrade3,
        ArrowUpgrade1,
        ArrowUpgrade2,
        ArrowUpgrade3,
        ArmorUpgrade1,
        ArmorUpgrade2,
        ArmorUpgrade3,
        Longbow,
        RangerScouting,
        Marksmanship,
        Max
    }

    // This can disappear at some point, to be replaced by the two below
    public enum EAssetType {
        None,
        Peasant,
        Footman,
        Archer,
        Ranger,
        Knight,
        GoldMine,
        TownHall,
        Keep,
        Castle,
        Farm,
        Barracks,
        LumberMill,
        Blacksmith,
        ScoutTower,
        GuardTower,
        CannonTower,
        Wall,
        Max
    }

    public enum EStaticAssetType {
        None,
        GoldMine,
        TownHall,
        Keep,
        Castle,
        Farm,
        Barracks,
        LumberMill,
        Blacksmith,
        ScoutTower,
        GuardTower,
        CannonTower,
        Wall,
        Max
    }

    public enum EUnitType {
        None,
        Peasant,
        Footman,
        Archer,
        Ranger,
        Knight,
        Max
    }

    public enum EDirection {
        North,
        NorthEast,
        East,
        SouthEast,
        South,
        SouthWest,
        West,
        NorthWest,
        Max
    }

    public static String toAbbr(EDirection inDir) {
        switch (inDir) {
            case North:
                return "n";
            case NorthEast:
                return "ne";
            case East:
                return "e";
            case SouthEast:
                return "se";
            case South:
                return "s";
            case SouthWest:
                return "sw";
            case West:
                return "w";
            case NorthWest:
                return "nw";
            default:
                return "n";
        }
    }

    public static String toAbbrDeath(EDirection inDir) {
        switch (inDir) {
            case North:
                return "ne";
            case NorthEast:
                return "ne";
            case East:
                return "ne";
            case SouthEast:
                return "se";
            case South:
                return "se";
            case SouthWest:
                return "sw";
            case West:
                return "sw";
            case NorthWest:
                return "nw";
            default:
                return "nw";
        }
    }

    public enum EUnitState {
        Idle,
        Move,
        ReturnMine,
        Mine,
        Lumber,
        ReturnLumber,
        Stone,
        ReturnStone,
        Convey,
        Repair,
        Attack,
        AttackBuilding,
        StandGround,
        BuildSimple,
        Patrol,
        Dead
    }

    public static int to_underlying(Enum enumerator) {
        int enumIndex = 0;
        Class<?> enumeratorClass = enumerator.getClass();
        List<?> enumValuesList = Arrays.asList(enumeratorClass.getEnumConstants());

        for (Object enumValue : enumValuesList) {
            if (enumerator == enumValue) {
                return enumIndex;
            } else {
                enumIndex++;
            }
        }
        return 0;
    }

    public static boolean is_static(EAssetType type) {
        switch(type) {
            case None:
            case Max:
            case Peasant:
            case Footman:
            case Archer:
            case Ranger:
            case Knight:
                return false;
            case GoldMine:
            case TownHall:
            case Keep:
            case Castle:
            case Farm:
            case Barracks:
            case LumberMill:
            case Blacksmith:
            case ScoutTower:
            case GuardTower:
            case CannonTower:
            case Wall:
                return true;
            default:
                return false;
        }
    }

    public static EUnitType to_unitType(EAssetType type) {
        switch(type) {
            case Peasant:
                return EUnitType.Peasant;
            case Footman:
                return EUnitType.Footman;
            case Archer:
                return EUnitType.Archer;
            case Ranger:
                return EUnitType.Ranger;
            case Knight:
                return EUnitType.Knight;
            default:
                return EUnitType.None;
        }
    }

    public static EStaticAssetType to_staticAssetType(EAssetType type) {
        switch(type) {
            case GoldMine:
                return EStaticAssetType.GoldMine;
            case TownHall:
                return EStaticAssetType.TownHall;
            case Keep:
                return EStaticAssetType.Keep;
            case Castle:
                return EStaticAssetType.Castle;
            case Farm:
                return EStaticAssetType.Farm;
            case Barracks:
                return EStaticAssetType.Barracks;
            case LumberMill:
                return EStaticAssetType.LumberMill;
            case Blacksmith:
                return EStaticAssetType.Blacksmith;
            case ScoutTower:
                return EStaticAssetType.ScoutTower;
            case GuardTower:
                return EStaticAssetType.GuardTower;
            case CannonTower:
                return EStaticAssetType.CannonTower;
            case Wall:
                return EStaticAssetType.Wall;
            default:
                return EStaticAssetType.None;
        }

    }

    public static EAssetType to_assetType(EStaticAssetType type) {
        switch (type){
            case None:
                return EAssetType.None;
            case GoldMine:
                return EAssetType.GoldMine;
            case TownHall:
                return EAssetType.TownHall;
            case Keep:
                return EAssetType.Keep;
            case Castle:
                return EAssetType.Castle;
            case Farm:
                return EAssetType.Farm;
            case Barracks:
                return EAssetType.Barracks;
            case LumberMill:
                return EAssetType.LumberMill;
            case Blacksmith:
                return EAssetType.Blacksmith;
            case ScoutTower:
                return EAssetType.ScoutTower;
            case GuardTower:
                return EAssetType.GuardTower;
            case CannonTower:
                return EAssetType.CannonTower;
            case Wall:
                return EAssetType.Wall;
            case Max:
                return EAssetType.Max;
            default:
                return EAssetType.None;
        }

    }
    public static EAssetType to_assetType(EUnitType type) {
        switch (type){
            case None:
                return EAssetType.None;
            case Peasant:
                return EAssetType.Peasant;
            case Footman:
                return EAssetType.Footman;
            case Archer:
                return EAssetType.Archer;
            case Ranger:
                return EAssetType.Ranger;
            case Knight:
                return EAssetType.Knight;
            case Max:
                return EAssetType.Max;
            default:
                return EAssetType.None;
        }

    }

}

// TODO: redefine DirectionOpposite function
//#define DirectionOpposite(dir)      static_cast<EDirection>( (to_underlying(dir) + to_underlying(EDirection::Max) / 2) % to_underlying(EDirection::Max))
