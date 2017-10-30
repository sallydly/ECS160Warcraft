package com.warcraftII;


/* Ported from GameDataTypes.h in ECS160Linux's include directory.
    Converted C++ enum classes to normal enums in java, since java enums have data safety.
*/

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
        BuildSimple,
        BuildAdvanced,
        Convey,
        Cancel,
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

    public enum EAssetType {
        None,
        Peasant,
        Footman,
        Archer,
        Ranger,
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
}

    /* // Code form Effective Modern C++ by Scott Meyers (see Item 10)
    template<typename E>
    constexpr typename std::underlying_type<E>::type to_underlying(E enumerator) noexcept{
        return static_cast<typename std::underlying_type<E>::type>(enumerator);
    }
    */
// TODO: redefine DirectionOpposite function
//#define DirectionOpposite(dir)      static_cast<EDirection>( (to_underlying(dir) + to_underlying(EDirection::Max) / 2) % to_underlying(EDirection::Max))
