package com.warcraftII.player_asset;

import com.warcraftII.GameDataTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.warcraftII.GameDataTypes.to_underlying;

public class PlayerCapability {
    abstract class Abstract {
        public abstract boolean CanInitiate(PlayerAsset actor, PlayerData playerData);
        public abstract boolean CanApply(PlayerAsset actor, PlayerData playerData, PlayerAsset target);
        public abstract boolean ApplyCapability(PlayerAsset actor, PlayerData playerData, PlayerAsset target);
    }

    private static Map<String, GameDataTypes.EAssetCapabilityType> nameToTypeMap = createNameToTypeMap();
    private static List<String> typeStringList = createTypeStringList();

    private static Map<String, GameDataTypes.EAssetCapabilityType> createNameToTypeMap() {
        Map<String, GameDataTypes.EAssetCapabilityType> nameToTypeMap = new HashMap<String, GameDataTypes.EAssetCapabilityType>();
        nameToTypeMap.put("None", GameDataTypes.EAssetCapabilityType.None);
        nameToTypeMap.put("BuildPeasant", GameDataTypes.EAssetCapabilityType.BuildPeasant);
        nameToTypeMap.put("BuildFootman", GameDataTypes.EAssetCapabilityType.BuildFootman);
        nameToTypeMap.put("BuildArcher", GameDataTypes.EAssetCapabilityType.BuildArcher);
        nameToTypeMap.put("BuildRanger", GameDataTypes.EAssetCapabilityType.BuildRanger);
        nameToTypeMap.put("BuildFarm", GameDataTypes.EAssetCapabilityType.BuildFarm);
        nameToTypeMap.put("BuildTownHall", GameDataTypes.EAssetCapabilityType.BuildTownHall);
        nameToTypeMap.put("BuildBarracks", GameDataTypes.EAssetCapabilityType.BuildBarracks);
        nameToTypeMap.put("BuildLumberMill", GameDataTypes.EAssetCapabilityType.BuildLumberMill);
        nameToTypeMap.put("BuildBlacksmith", GameDataTypes.EAssetCapabilityType.BuildBlacksmith);
        nameToTypeMap.put("BuildKeep", GameDataTypes.EAssetCapabilityType.BuildKeep);
        nameToTypeMap.put("BuildCastle", GameDataTypes.EAssetCapabilityType.BuildCastle);
        nameToTypeMap.put("BuildScoutTower", GameDataTypes.EAssetCapabilityType.BuildScoutTower);
        nameToTypeMap.put("BuildGuardTower", GameDataTypes.EAssetCapabilityType.BuildGuardTower);
        nameToTypeMap.put("BuildCannonTower", GameDataTypes.EAssetCapabilityType.BuildCannonTower);
        nameToTypeMap.put("Move", GameDataTypes.EAssetCapabilityType.Move);
        nameToTypeMap.put("Repair", GameDataTypes.EAssetCapabilityType.Repair);
        nameToTypeMap.put("Mine", GameDataTypes.EAssetCapabilityType.Mine);
        nameToTypeMap.put("BuildSimple", GameDataTypes.EAssetCapabilityType.BuildSimple);
        nameToTypeMap.put("BuildAdvanced", GameDataTypes.EAssetCapabilityType.BuildAdvanced);
        nameToTypeMap.put("Convey", GameDataTypes.EAssetCapabilityType.Convey);
        nameToTypeMap.put("Cancel", GameDataTypes.EAssetCapabilityType.Cancel);
        nameToTypeMap.put("BuildWall", GameDataTypes.EAssetCapabilityType.BuildWall);
        nameToTypeMap.put("Attack", GameDataTypes.EAssetCapabilityType.Attack);
        nameToTypeMap.put("StandGround", GameDataTypes.EAssetCapabilityType.StandGround);
        nameToTypeMap.put("Patrol", GameDataTypes.EAssetCapabilityType.Patrol);
        nameToTypeMap.put("WeaponUpgrade1", GameDataTypes.EAssetCapabilityType.WeaponUpgrade1);
        nameToTypeMap.put("WeaponUpgrade2", GameDataTypes.EAssetCapabilityType.WeaponUpgrade2);
        nameToTypeMap.put("WeaponUpgrade3", GameDataTypes.EAssetCapabilityType.WeaponUpgrade3);
        nameToTypeMap.put("ArrowUpgrade1", GameDataTypes.EAssetCapabilityType.ArrowUpgrade1);
        nameToTypeMap.put("ArrowUpgrade2", GameDataTypes.EAssetCapabilityType.ArrowUpgrade2);
        nameToTypeMap.put("ArrowUpgrade3", GameDataTypes.EAssetCapabilityType.ArrowUpgrade3);
        nameToTypeMap.put("ArmorUpgrade1", GameDataTypes.EAssetCapabilityType.ArmorUpgrade1);
        nameToTypeMap.put("ArmorUpgrade2", GameDataTypes.EAssetCapabilityType.ArmorUpgrade2);
        nameToTypeMap.put("ArmorUpgrade3", GameDataTypes.EAssetCapabilityType.ArmorUpgrade3);
        nameToTypeMap.put("Longbow", GameDataTypes.EAssetCapabilityType.Longbow);
        nameToTypeMap.put("RangerScouting", GameDataTypes.EAssetCapabilityType.RangerScouting);
        nameToTypeMap.put("Markmanship", GameDataTypes.EAssetCapabilityType.Marksmanship);

        return nameToTypeMap;
    }

    private static List<String> createTypeStringList() {
        Vector<String> typeStringList = new Vector<String>();
        typeStringList.add("None");
        typeStringList.add("BuildPeasant");
        typeStringList.add("BuildFootman");
        typeStringList.add("BuildArcher");
        typeStringList.add("BuildRanger");
        typeStringList.add("BuildFarm");
        typeStringList.add("BuildTownHall");
        typeStringList.add("BuildBarracks");
        typeStringList.add("BuildLumberMill");
        typeStringList.add("BuildBlacksmith");
        typeStringList.add("BuildKeep");
        typeStringList.add("BuildCastle");
        typeStringList.add("BuildScoutTower");
        typeStringList.add("BuildGuardTower");
        typeStringList.add("BuildCannonTower");
        typeStringList.add("Move");
        typeStringList.add("Repair");
        typeStringList.add("Mine");
        typeStringList.add("BuildSimple");
        typeStringList.add("BuildAdvanced");
        typeStringList.add("Convey");
        typeStringList.add("Cancel");
        typeStringList.add("BuildWall");
        typeStringList.add("Attack");
        typeStringList.add("StandGround");
        typeStringList.add("Patrol");
        typeStringList.add("WeaponUpgrade1");
        typeStringList.add("WeaponUpgrade2");
        typeStringList.add("WeaponUpgrade3");
        typeStringList.add("ArrowUpgrade1");
        typeStringList.add("ArrowUpgrade2");
        typeStringList.add("ArrowUpgrade3");
        typeStringList.add("ArmorUpgrade1");
        typeStringList.add("ArmorUpgrade2");
        typeStringList.add("ArmorUpgrade3");
        typeStringList.add("Longbow");
        typeStringList.add("RangerScouting");
        typeStringList.add("Marksmanship");

        return typeStringList;
    }

 //ACTUAL DATA MEMBERS
    protected String name;
    protected GameDataTypes.EAssetCapabilityType assetCapabilityType;
    protected ETargetType targetType;

    protected PlayerCapability(final String name, ETargetType targetType) {
        this.name = name;
        this.assetCapabilityType = nameToType(name);
        this.targetType = targetType;
    }

    protected static Map<String, PlayerCapability> nameRegistry = new HashMap<String, PlayerCapability>();
    protected static Map<Integer, PlayerCapability> typeRegistry = new HashMap<Integer, PlayerCapability>();

    static boolean register(PlayerCapability capability) {
        if(null != findCapability(capability.getName())) {
            return false;
        }
        nameRegistry.put(capability.getName(), capability);
        typeRegistry.put(to_underlying(nameToType(capability.getName())), capability);
        return true;
    }

    public String getName(){
        return this.name;
    }

    public GameDataTypes.EAssetCapabilityType assetCapabilityType() {
        return this.assetCapabilityType;
    }

    public ETargetType targetType() {
        return this.targetType;
    }

    public static PlayerCapability findCapability(GameDataTypes.EAssetCapabilityType type) {
        if(typeRegistry.containsKey(to_underlying(type))) {
            return typeRegistry.get(to_underlying(type));
        }
        //TODO: Recheck this
        return null;
    }

    public static PlayerCapability findCapability(String name) {
        if(nameRegistry.containsKey(name)) {
            return nameRegistry.get(name);
        }
        //TODO: Recheck this
        return null;
    }

    public static GameDataTypes.EAssetCapabilityType nameToType(String name) {
        if(nameToTypeMap.containsKey(name)) {
            return nameToTypeMap.get(name);
        }
        System.out.println("ERROR Unknown capability name: " + name);
        return GameDataTypes.EAssetCapabilityType.None;
    }

    public static String typeToName(GameDataTypes.EAssetCapabilityType type) {
        if((0 > to_underlying(type))||(to_underlying(type) >= typeStringList.size())){
            return "";
        }
        return typeStringList.get(to_underlying(type));
    }


    public static boolean IsBuildingUnit(GameDataTypes.EAssetCapabilityType type){
        switch (type){
            case BuildPeasant:
            case BuildFootman:
            case BuildArcher:
            case BuildRanger:
                return true;
            default:
                return false;
        }
    }
    public static boolean IsBuildingBuilding (GameDataTypes.EAssetCapabilityType type){
        switch (type) {
            case BuildFarm:
            case BuildTownHall:
            case BuildBarracks:
            case BuildLumberMill:
            case BuildBlacksmith:
            case BuildKeep:
            case BuildCastle:
            case BuildScoutTower:
            case BuildGuardTower:
            case BuildCannonTower:
            case BuildWall:
                return true;
            default:
                return false;
        }
    }

    public static GameDataTypes.EAssetType AssetFromCapability(GameDataTypes.EAssetCapabilityType type){
        switch (type) {
            case BuildPeasant:
                return GameDataTypes.EAssetType.Peasant;
            case BuildFootman:
                return GameDataTypes.EAssetType.Footman;
            case BuildArcher:
                return GameDataTypes.EAssetType.Archer;
            case BuildRanger:
                return GameDataTypes.EAssetType.Ranger;
            case BuildFarm:
                return GameDataTypes.EAssetType.Farm;
            case BuildTownHall:
                return GameDataTypes.EAssetType.TownHall;
            case BuildBarracks:
                return GameDataTypes.EAssetType.Barracks;
            case BuildLumberMill:
                return GameDataTypes.EAssetType.LumberMill;
            case BuildBlacksmith:
                return GameDataTypes.EAssetType.Blacksmith;
            case BuildKeep:
                return GameDataTypes.EAssetType.Keep;
            case BuildCastle:
                return GameDataTypes.EAssetType.Castle;
            case BuildScoutTower:
                return GameDataTypes.EAssetType.ScoutTower;
            case BuildGuardTower:
                return GameDataTypes.EAssetType.GuardTower;
            case BuildCannonTower:
                return GameDataTypes.EAssetType.CannonTower;
            case BuildWall:
                return GameDataTypes.EAssetType.Wall;
            default:
                return (GameDataTypes.EAssetType.None);
        }
    }
}
