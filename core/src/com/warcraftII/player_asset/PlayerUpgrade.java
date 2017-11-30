package com.warcraftII.player_asset;

import com.badlogic.gdx.files.FileHandle;
import com.warcraftII.GameDataTypes;
import com.warcraftII.data_source.CommentSkipLineDataSource;
import com.warcraftII.data_source.DataSource;
import com.warcraftII.data_source.FileDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.warcraftII.GameDataTypes.to_underlying;

public class PlayerUpgrade {
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

    protected List<GameDataTypes.EAssetType> affectedAssets = new Vector<GameDataTypes.EAssetType>();
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

    public static boolean loadUpgrades(FileHandle directory) {
        FileHandle[] upgradeFiles = directory.list(".dat");

        for (FileHandle fileHandle : upgradeFiles) {
            FileDataSource source = new FileDataSource(fileHandle);

            if(!load(source)) {
                System.out.println("Failed to load upgrade" + fileHandle.name());
                return false;
            }
        }
        return true;
    }

    public static boolean load(DataSource source) {
        CommentSkipLineDataSource LineSource = new CommentSkipLineDataSource(source, '#');
        String Name, TempString;
        PlayerUpgrade playerUpgrade;
        GameDataTypes.EAssetCapabilityType UpgradeType;
        int AffectedAssetCount;
        boolean ReturnStatus = false;

        if(null == source) {
            return false;
        }
        Name = LineSource.read().trim();

        if(Name.isEmpty()) {
            System.out.println("Failed to get player upgrade name.\n");
            return false;
        }

        UpgradeType = PlayerCapability.nameToType(Name);
        if((GameDataTypes.EAssetCapabilityType.None == UpgradeType) && (!Name.equals(PlayerCapability.typeToName(GameDataTypes.EAssetCapabilityType.None)))){
            System.out.format("Unknown upgrade type %s.\n", Name);
            return false;
        }

        if(registryByName.containsKey(Name)) {
            playerUpgrade = registryByName.get(Name);
        } else {
            playerUpgrade = new PlayerUpgrade();
            playerUpgrade.name = Name;
            registryByName.put(Name, playerUpgrade);
            registryByType.put(to_underlying(UpgradeType), playerUpgrade);
        }
        try{
            TempString = LineSource.read().trim();
            if(TempString.isEmpty()) {
                System.out.println("Failed to get upgrade armor.\n");
                return false;
            }
            playerUpgrade.armor = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()) {
                System.out.println("Failed to get upgrade sight.\n");
                return false;
            }
            playerUpgrade.sight = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()) {
                System.out.println("Failed to get upgrade speed.\n");
                return false;
            }
            playerUpgrade.speed = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()) {
                System.out.println("Failed to get upgrade basic damage.\n");
                return false;
            }
            playerUpgrade.basicDamage = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()) {
                System.out.println("Failed to get upgrade piercing damage.\n");
                return false;
            }
            playerUpgrade.piercingDamage = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()) {
                System.out.println("Failed to get upgrade range.\n");
                return false;
            }
            playerUpgrade.range = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()){
                System.out.println("Failed to get upgrade gold cost.\n");
                return false;
            }
            playerUpgrade.goldCost = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()){
                System.out.println("Failed to get upgrade lumber cost.\n");
                return false;
            }
            playerUpgrade.lumberCost = Integer.parseInt(TempString);

            //TODO: Add stone cost
//            TempString = LineSource.read().trim();
//            if(TempString.isEmpty()){
//                System.out.println("Failed to get upgrade stone cost.\n");
//                return false;
//            }
//            playerUpgrade.stoneCost = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()){
                System.out.println("Failed to get upgrade research time.\n");
                return false;
            }
            playerUpgrade.researchTime = Integer.parseInt(TempString);

            TempString = LineSource.read().trim();
            if(TempString.isEmpty()){
                System.out.println("Failed to get upgrade affected asset count.\n");
                return false;
            }
            AffectedAssetCount = Integer.parseInt(TempString);

            for(int Index = 0; Index < AffectedAssetCount; Index++){
                TempString = LineSource.read().trim();
                if(TempString.isEmpty()) {
                    System.out.format("Failed to read upgrade affected asset %d.\n", Index);
                    return false;
                }
                playerUpgrade.affectedAssets.add(PlayerAssetType.NameToType(TempString));
            }
            ReturnStatus = true;
        } catch(Exception E) {
            System.out.printf("%s\n", E);
        }
        return ReturnStatus;
    }

    public static PlayerUpgrade findUpgradeFromType(GameDataTypes.EAssetCapabilityType type) {
        if(registryByType.containsKey(to_underlying(type))) {
            return registryByType.get(to_underlying(type));
        }

        return new PlayerUpgrade();
    }

    public static PlayerUpgrade findUpgradeFromName(String name) {
        if(registryByName.containsKey(name)) {
            return registryByName.get(name);
        }

        //TODO: Recheck this
        return new PlayerUpgrade();
    }
}
