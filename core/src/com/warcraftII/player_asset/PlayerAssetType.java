package com.warcraftII.player_asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.utils.Logger;
import com.warcraftII.GameDataTypes;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.data_source.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Map;

import static com.warcraftII.GameDataTypes.to_underlying;

public class PlayerAssetType {
    private static Logger log = new Logger("PlayerAssetType", 2);

    protected PlayerAssetType DThis;
    protected String DName;
    protected EAssetType DType;
    protected EPlayerColor DColor;
    protected Vector< Boolean > DCapabilities;
    protected Vector< EAssetType > DAssetRequirements;
    protected Vector< PlayerUpgrade > DAssetUpgrades;
    protected int DHitPoints;
    protected int DArmor;
    protected int DSight;
    protected int DConstructionSight;
    protected int DSize;
    protected int DSpeed;
    protected int DGoldCost;
    protected int DLumberCost;
    protected int DStoneCost;
    protected int DFoodConsumption;
    protected int DBuildTime;
    protected int DAttackSteps;
    protected int DReloadSteps;
    protected int DBasicDamage;
    protected int DPiercingDamage;
    protected int DRange;
    protected static Map<String, PlayerAssetType > DRegistry = new HashMap<String, PlayerAssetType>();
    protected static Vector<String> DTypeStrings = new Vector<String>();

    public static Map<String, EAssetType > DNameTypeTranslation = new HashMap<String, EAssetType>();

    static{
        init();
    }

    private static void init() {
        String[] TypeStringsArray = {
                "None",
                "Peasant",
                "Footman",
                "Archer",
                "Ranger",
                "Knight",
                "GoldMine",
                "TownHall",
                "Keep",
                "Castle",
                "Farm",
                "Barracks",
                "LumberMill",
                "Blacksmith",
                "ScoutTower",
                "GuardTower",
                "CannonTower",
                "Wall"};
        
        DTypeStrings = new Vector<String>(Arrays.asList(TypeStringsArray));

        DNameTypeTranslation = new HashMap<String, EAssetType>();
        {

            DNameTypeTranslation.put("None", EAssetType.None);
            DNameTypeTranslation.put("Peasant", EAssetType.Peasant);
            DNameTypeTranslation.put("Footman", EAssetType.Footman);
            DNameTypeTranslation.put("Archer", EAssetType.Archer);
            DNameTypeTranslation.put("Ranger", EAssetType.Ranger);
            DNameTypeTranslation.put("Knight", EAssetType.Knight);
            DNameTypeTranslation.put("GoldMine", EAssetType.GoldMine);
            DNameTypeTranslation.put("TownHall", EAssetType.TownHall);
            DNameTypeTranslation.put("Keep", EAssetType.Keep);
            DNameTypeTranslation.put("Castle", EAssetType.Castle);
            DNameTypeTranslation.put("Farm", EAssetType.Farm);
            DNameTypeTranslation.put("Barracks", EAssetType.Barracks);
            DNameTypeTranslation.put("LumberMill", EAssetType.LumberMill);
            DNameTypeTranslation.put("Blacksmith", EAssetType.Blacksmith);
            DNameTypeTranslation.put("ScoutTower", EAssetType.ScoutTower);
            DNameTypeTranslation.put("GuardTower", EAssetType.GuardTower);
            DNameTypeTranslation.put("CannonTower", EAssetType.CannonTower);
            DNameTypeTranslation.put("Wall", EAssetType.Wall);
        }
    }

    public PlayerAssetType() {
        DCapabilities = new Vector<Boolean>();
        DAssetRequirements = new Vector<EAssetType>();
        DAssetUpgrades =  new Vector<PlayerUpgrade>();
    }

    public PlayerAssetType(PlayerAssetType res) {
        DCapabilities = new Vector<Boolean>();
    }


    public String Name() {
        return DName;
    }

    public EAssetType Type() {
        return DType;
    }

    public EPlayerColor Color() {
        return DColor;
    }

    public int HitPoints() {
        return DHitPoints;
    }

    public int Armor() {
        return DArmor;
    }

    public int Sight() {
        return DSight;
    }

    public int ConstructionSight() {
        return DConstructionSight;
    }

    public int Size() {
        return DSize;
    }

    public int Speed() {
        return DSpeed;
    }

    public int GoldCost() {
        return DGoldCost;
    }

    public int LumberCost() {
        return DLumberCost;
    }

    public int StoneCost() {
        return DStoneCost;
    }

    public int FoodConsumption() {
        return DFoodConsumption;
    }

    public int BuildTime() {
        return DBuildTime;
    }

    public int AttackSteps() {
        return DAttackSteps;
    }

    public int ReloadSteps() {
        return DReloadSteps;
    }

    public int BasicDamage() {
        return DBasicDamage;
    }

    public int PiercingDamage() {
        return DPiercingDamage;
    }

    public int Range() {
        return DRange;
    }

    public int SightUpgrade() {
        int ReturnValue = 0;
        for(PlayerUpgrade Upgrade : DAssetUpgrades){
            ReturnValue += Upgrade.getSight();
        }
        return ReturnValue;
    }

    public int ArmorUpgrade() {
        int ReturnValue = 0;
        for(PlayerUpgrade Upgrade : DAssetUpgrades){
            ReturnValue += Upgrade.getArmor();
        }
        return ReturnValue;
    }

    public int SpeedUpgrade() {
        int ReturnValue = 0;
        for(PlayerUpgrade Upgrade : DAssetUpgrades){
            ReturnValue += Upgrade.getSpeed();
        }
        return ReturnValue;
    }

    public int BasicDamageUpgrade(){
        int ReturnValue = 0;
        for(PlayerUpgrade Upgrade : DAssetUpgrades){
            ReturnValue += Upgrade.getBasicDamage();
        }
        return ReturnValue;
    }
    public int PiercingDamageUpgrade(){
        int ReturnValue = 0;
        for(PlayerUpgrade Upgrade : DAssetUpgrades){
            ReturnValue += Upgrade.getPiercingDamage();
        }
        return ReturnValue;
    }
    public int RangeUpgrade(){
        int ReturnValue = 0;
        for(PlayerUpgrade Upgrade : DAssetUpgrades){
            ReturnValue += Upgrade.getRange();
        }
        return ReturnValue;
    }

    public boolean HasCapability(EAssetCapabilityType capability) {
        if((0 > to_underlying(capability))||(DCapabilities.size() <= to_underlying(capability))){
            return false;
        }
        return DCapabilities.get(to_underlying(capability));
    }

    public Vector< Boolean > Capabilities(){
        return DCapabilities;
    }

    public Vector<EAssetCapabilityType> CapabilitiesVector() {
        Vector<EAssetCapabilityType> ReturnVector = new Vector<EAssetCapabilityType>();
        List< EAssetCapabilityType > values = Arrays.asList(EAssetCapabilityType.values());

        for(int Index = to_underlying(EAssetCapabilityType.None); Index < to_underlying(EAssetCapabilityType.Max); Index++){
            if(DCapabilities.get(Index)){
                ReturnVector.add(values.get(Index));
            }
        }
        return ReturnVector;
    }

    public Vector<EAssetCapabilityType> UnitCapabilitiesVector() {
        Vector<EAssetCapabilityType> ReturnVector = new Vector<EAssetCapabilityType>();
        List< EAssetCapabilityType > values = Arrays.asList(EAssetCapabilityType.values());

        for(int Index = to_underlying(EAssetCapabilityType.None); Index < to_underlying(EAssetCapabilityType.Max); Index++){
            if(DCapabilities.get(Index)&& PlayerCapability.IsBuildingUnit(EAssetCapabilityType.values()[Index])){
                ReturnVector.add(values.get(Index));
            }
        }
        return ReturnVector;
    }

    public Vector<EAssetCapabilityType> BuildingCapabilitiesVector() {
        Vector<EAssetCapabilityType> ReturnVector = new Vector<EAssetCapabilityType>();
        List< EAssetCapabilityType > values = Arrays.asList(EAssetCapabilityType.values());

        for(int Index = to_underlying(EAssetCapabilityType.None); Index < to_underlying(EAssetCapabilityType.Max); Index++){
            if(DCapabilities.get(Index) && PlayerCapability.IsBuildingBuilding(EAssetCapabilityType.values()[Index])){
                ReturnVector.add(values.get(Index));
            }
        }
        return ReturnVector;
    }

    public void AddCapability(EAssetCapabilityType capability){
        if((0 > to_underlying(capability))||(DCapabilities.size() <= to_underlying(capability))){
            return;
        }
        DCapabilities.set(to_underlying(capability), true);
    }

    public void RemoveCapability(EAssetCapabilityType capability){
        if((0 > to_underlying(capability))||(DCapabilities.size() <= to_underlying(capability))){
            return;
        }
        DCapabilities.set(to_underlying(capability), false);
    }
/*
    public void AddUpgrade(std.shared_ptr< CPlayerUpgrade > upgrade){
        DAssetUpgrades.push_back(upgrade);
    }
*/
    public Vector< EAssetType > AssetRequirements() {
        return DAssetRequirements;
    }

    public static EAssetType NameToType(String name){
        if (null == DNameTypeTranslation.get(name))
            return EAssetType.None;
        else
            return DNameTypeTranslation.get(name);
    }

    public static String TypeToName(EAssetType type){
        if((0 > to_underlying(type))||(to_underlying(type) >= DTypeStrings.size())){
            return "";
        }
        return DTypeStrings.get(to_underlying(type));
    }

    public static boolean LoadTypes(){
        boolean ReturnStatus = false;
        DRegistry = new HashMap<String, PlayerAssetType>();

        FileHandle ResDirectory = Gdx.files.internal("res");
        FileHandle[] DatFileArray = ResDirectory.list(".dat");
        for (FileHandle fh : DatFileArray) {
            FileDataSource Source = new FileDataSource(fh);
            if(!(Load(Source))){
                log.error("Failed to load resource: " + fh.name());
                continue;
            }
            else{
                log.debug("Loaded resource: " + fh.name());
            }
        }
        ReturnStatus = true;
        return ReturnStatus;
    }

    private static boolean Load(DataSource source){
        CommentSkipLineDataSource LineSource = new CommentSkipLineDataSource(source, '#');
        String Name, TempString;
        PlayerAssetType temp, PAssetType;
        EAssetType AssetType;
        int CapabilityCount, AssetRequirementCount;
        boolean ReturnStatus = false;

        Name = LineSource.read().trim();
        
        AssetType = NameToType(Name);
        
        if((EAssetType.None == AssetType) && (!Name.equals(DTypeStrings.get(to_underlying(EAssetType.None))))){
            log.error("Unknown resource type: " + Name);
            return false;
        }
        
        
        PAssetType = DRegistry.get(Name);
        if(null == PAssetType){ // if could not find it in map
            PAssetType = new PlayerAssetType();
            PAssetType.DThis = PAssetType;
            PAssetType.DName = Name;
            DRegistry.put(Name,PAssetType);
        }

        PAssetType.DType = AssetType;
        PAssetType.DColor = EPlayerColor.None;


        TempString = LineSource.read().trim();
        PAssetType.DHitPoints = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DArmor = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DSight = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DConstructionSight = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DSize = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DSpeed = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DGoldCost = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DLumberCost  = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DStoneCost  = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DFoodConsumption = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DBuildTime = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DAttackSteps = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DReloadSteps = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DBasicDamage  = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DPiercingDamage = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        PAssetType.DRange  = Integer.parseInt(TempString);

        TempString = LineSource.read().trim();
        CapabilityCount = Integer.parseInt(TempString);

        PAssetType.DCapabilities.setSize(to_underlying(EAssetCapabilityType.Max));

        for(int Index = 0; Index < PAssetType.DCapabilities.size(); Index++) {
            PAssetType.DCapabilities.set(Index, false);
        }

        for(int Index = 0; Index < CapabilityCount; Index++){
            TempString = LineSource.read().trim();
            PAssetType.AddCapability(PlayerCapability.nameToType(TempString));
        }

        TempString = LineSource.read().trim();
        AssetRequirementCount = Integer.parseInt(TempString);

        for(int Index = 0; Index < AssetRequirementCount; Index++){
            TempString = LineSource.read().trim();
            PAssetType.DAssetRequirements.add(NameToType(TempString));
        }

        ReturnStatus = true;
        return ReturnStatus;
    }


    public static StaticAsset ConstructStaticAsset(EStaticAssetType satype){
        String type = PlayerAssetType.TypeToName(GameDataTypes.to_assetType(satype));
        log.info("Constructing: " + type);
        PlayerAssetType playerAssetType = DRegistry.get(type);
        return new StaticAsset(playerAssetType);
    }

    public static StaticAsset ConstructStaticAsset(String type){
        log.info("Constructing: " + type);
        PlayerAssetType playerAssetType = DRegistry.get(type);
        return new StaticAsset(playerAssetType);
    }


    public static int StaticAssetSize(EStaticAssetType type){
        String typeString = DTypeStrings.get(to_underlying(GameDataTypes.to_assetType(type)));
        PlayerAssetType playerAssetType = DRegistry.get(typeString);
        return playerAssetType.Size();
    }

    public static Vector<Boolean> AssetTypeCapabilities(EAssetType type){
        String name = TypeToName(type);
        PlayerAssetType pat = DRegistry.get(name);
        return pat.Capabilities();
    }

    public static Vector<EAssetCapabilityType> AssetTypeCapabilitiesVector(EAssetType type){
        String name = TypeToName(type);
        PlayerAssetType pat = DRegistry.get(name);
        return pat.CapabilitiesVector();
    }

    public static int CanAfford(EAssetType type, int lumber, int gold, int stone){
        int returnstatus = 0;
        /* Code for return status:
            0: nothing lacking
            1: not enough wood
            2: not enough gold
            3: not enough wood and gold
            4: not enough stone
            5: not enough wood and stone
            6: not enough gold and stone
            7: not enough of everything
         */
        String name = TypeToName(type);
        PlayerAssetType pat = DRegistry.get(name);
        if (pat.LumberCost() > lumber)
        returnstatus += 1; //sets first bit
        if (pat.GoldCost() > gold)
            returnstatus += 2; //sets second bit
        if (pat.StoneCost() > stone)
            returnstatus += 4; //sets third bit

        return returnstatus;

    }

    public static int LumberCost(EAssetType type){
        String name = TypeToName(type);
        PlayerAssetType pat = DRegistry.get(name);
        return pat.LumberCost();
    }

    public static int GoldCost(EAssetType type){
        String name = TypeToName(type);
        PlayerAssetType pat = DRegistry.get(name);
        return pat.GoldCost();
    }

    public static int StoneCost(EAssetType type){
        String name = TypeToName(type);
        PlayerAssetType pat = DRegistry.get(name);
        return pat.StoneCost();
    }

    public static int BuildTime(EAssetType type){
        String name = TypeToName(type);
        PlayerAssetType pat = DRegistry.get(name);
        return pat.BuildTime();
    }


    public static int MaxSight(){
        int MaxSightFound = 0;

        for (String assetName : DRegistry.keySet()) {
            PlayerAssetType assetType = DRegistry.get(assetName);
            MaxSightFound = MaxSightFound > (assetType.DSight + assetType.DSight) ? MaxSightFound : (assetType.DSight + assetType.DSight);
        }

        return MaxSightFound;
    }

    /*
    public static std.shared_ptr< CPlayerAssetType > FindDefaultFromName( std.string &name){

    }
    public static std.shared_ptr< CPlayerAssetType > FindDefaultFromType(EAssetType type){

    }
    public static std.shared_ptr< std.unordered_map< std.string, std.shared_ptr< CPlayerAssetType > > > DuplicateRegistry(EPlayerColor color);

    public std.shared_ptr< CPlayerAsset > Construct(){

    }*/
}
