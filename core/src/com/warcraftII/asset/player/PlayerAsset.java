package com.warcraftII.asset.player;

import com.warcraftII.GameDataTypes;
import com.warcraftII.data_source.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.crypto.Data;

abstract class ActivatedPlayerCapability {
    protected PlayerAsset actor;
    protected PlayerData playerData;
    protected PlayerAsset target;

    public ActivatedPlayerCapability(PlayerAsset actor, PlayerData playerData, PlayerAsset target) {
        this.actor = actor;
        this.playerData = playerData;
        this.target = target;
    }

    public abstract int percentComplete(int max);
    public abstract boolean incrementStep();
    public abstract void cancel();
}


//TODO: Move to separate file
abstract class DataContainerIterator{
    public abstract String name();
    public abstract boolean isContainer();
    public abstract boolean isValid();
    public abstract void next();
}

abstract class DataSink {
    public abstract int write(Object data, int length);
    public DataContainer container() {
        return null;
    }
}

abstract class DataContainer {
    public abstract DataContainerIterator first();
    public abstract DataSource DataSource(String name);
    public abstract DataSink dataSink(String name);
    public DataContainer container() {
        return null;
    }

    public abstract DataContainer dataContainer(String name);
}

class PlayerCapability {
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
        //TODO: Add to_underlying
        if(typeRegistry.containsKey(to_underlying(type))) {
            return typeRegistry.get(to_underlying(type));
        }
        //TODO: Fix
        return new PlayerCapability();
    }

    public static PlayerCapability findCapability(String name) {
        if(nameRegistry.containsKey(name)) {
            return nameRegistry.get(name);
        }
        //TODO: Fix
        return new PlayerCapability();
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
//   TODO: Add these
//    virtual bool CanInitiate(std::shared_ptr< CPlayerAsset > actor, std::shared_ptr< CPlayerData > playerdata) = 0;
//    virtual bool CanApply(std::shared_ptr< CPlayerAsset > actor, std::shared_ptr< CPlayerData > playerdata, std::shared_ptr< CPlayerAsset > target) = 0;
//    virtual bool ApplyCapability(std::shared_ptr< CPlayerAsset > actor, std::shared_ptr< CPlayerData > playerdata, std::shared_ptr< CPlayerAsset > target) = 0;

}

class PlayerUpgrade {
    protected String name;
    protected int armor;
    protected int sight;
    protected int speed;
    protected int basicDamage;
    protected int piercingDamage;
    protected int range;
    protected int goldCost;
    protected int lumberCost;
    protected int researchTime;

    protected List<GameDataTypes.EAssetType> affectedAssets;
    protected static Map<String, PlayerUpgrade> registryByName = new HashMap<String, PlayerUpgrade>();
    protected static Map<Integer, PlayerUpgrade> registryByType = new HashMap<Integer, PlayerUpgrade>();

    public PlayerUpgrade() {

    }

    public String getName() {
        return name;
    }

    public int getArmor() {
        return armor;
    }

    public int getSight() {
        return sight;
    }

    public int getSpeed() {
        return speed;
    }

    public int getBasicDamage() {
        return basicDamage;
    }

    public int getPiercingDamage() {
        return piercingDamage;
    }

    public int getRange() {
        return range;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public int getLumberCost() {
        return lumberCost;
    }

    public int getResearchTime() {
        return researchTime;
    }

    public List<GameDataTypes.EAssetType> getAffectedAssets() {
        return affectedAssets;
    }

    /**
     * Look to refactor and fix
     *
     * @param container
     * @return
     */
    public static boolean loadUpgrades(DataContainer container) {
        DataContainerIterator fileIterator = container.first();

        if(fileIterator != null) {
            System.out.println("FileIterator is null");
            return false;
        }
    }

    public static boolean load(DataSource source) {

    }

    public static PlayerUpgrade findUpgradeFromType(GameDataTypes.EAssetCapabilityType type) {

    }

    public static PlayerUpgrade findUpgradeFromName(String name) {

    }
}

public class PlayerAsset {


    shared_ptr< String > DActor;
    std::shared_ptr< CPlayerData > DPlayerData;
    std::shared_ptr< CPlayerAsset > DTarget;
}
