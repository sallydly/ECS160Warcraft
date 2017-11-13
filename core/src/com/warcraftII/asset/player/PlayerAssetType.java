package com.warcraftII.asset.player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.utils.Logger;
import com.warcraftII.GameDataTypes;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.data_source.*;

import java.util.HashMap;
import java.util.Vector;
import java.util.Set;
import java.util.Map;

/**
 * Created by Kimi on 11/12/2017.
 */

public class PlayerAssetType {
    private static Logger log = new Logger("PlayerAssetType", 2);

    protected PlayerAssetType DThis;
    protected String DName;
    protected EAssetType DType;
    protected EPlayerColor DColor;
    //protected Vector< Boolean > DCapabilities;
    //protected Vector< EAssetType > DAssetRequirements;
    //protected Vector< PlayerUpgrade > DAssetUpgrades;
    protected int DHitPoints;
    protected int DArmor;
    protected int DSight;
    protected int DConstructionSight;
    protected int DSize;
    protected int DSpeed;
    protected int DGoldCost;
    protected int DLumberCost;
    protected int DFoodConsumption;
    protected int DBuildTime;
    protected int DAttackSteps;
    protected int DReloadSteps;
    protected int DBasicDamage;
    protected int DPiercingDamage;
    protected int DRange;
    protected static Map<String, PlayerAssetType > DRegistry;
    protected static Vector<String> DTypeStrings;
    protected static Map<String, EAssetType > DNameTypeTranslation;

    public PlayerAssetType() {
    }

    public PlayerAssetType(PlayerAssetType res) {
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
/*
    public int ArmorUpgrade(){}
    public int SightUpgrade(){}
    public int SpeedUpgrade(){}
    public int BasicDamageUpgrade(){}
    public int PiercingDamageUpgrade(){}
    public int RangeUpgrade(){}
*/
    /*public boolean HasCapability(EAssetCapabilityType capability) {
        if((0 > to_underlying(capability))||(DCapabilities.size() <= to_underlying(capability))){
            return false;
        }
        return DCapabilities[to_underlying(capability)];
    }

    public Vector< EAssetCapabilityType > Capabilities(){}

    public void AddCapability(EAssetCapabilityType capability){
        if((0 > to_underlying(capability))||(DCapabilities.size() <= to_underlying(capability))){
            return;
        }
        DCapabilities[to_underlying(capability)] = true;
    }

    public void RemoveCapability(EAssetCapabilityType capability){
        if((0 > to_underlying(capability))||(DCapabilities.size() <= to_underlying(capability))){
            return;
        }
        DCapabilities[to_underlying(capability)] = false;
    }

    public void AddUpgrade(std.shared_ptr< CPlayerUpgrade > upgrade){
        DAssetUpgrades.push_back(upgrade);
    }

    public Vector< EAssetType > AssetRequirements() {
        return DAssetRequirements;
    }
    */
    public static EAssetType NameToType(String name){
        if (null == DNameTypeTranslation.get(name))
            return EAssetType.None;
        else
            return DNameTypeTranslation.get(name);
    }

    public static String TypeToName(EAssetType type){
        if((0 > GameDataTypes.to_underlying(type))||(GameDataTypes.to_underlying(type) >= DTypeStrings.size())){
            return "";
        }
        return DTypeStrings.get(GameDataTypes.to_underlying(type));
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

        Name = LineSource.read();
        
        AssetType = NameToType(Name);
        
        if((EAssetType.None == AssetType) && (!Name.equals(DTypeStrings.get(GameDataTypes.to_underlying(EAssetType.None))))){
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

        //PAssetType.DCapabilities.resize(GameDataTypes.to_underlying(EAssetCapabilityType.Max));
        /*for(int Index = 0; Index < PAssetType.DCapabilities.size(); Index++){
            // PAssetType.DCapabilities[Index] = false;
        }
        */
        for(int Index = 0; Index < CapabilityCount; Index++){
            TempString = LineSource.read().trim();
            //PAssetType.AddCapability(CPlayerCapability.NameToType(TempString));
        }

        TempString = LineSource.read().trim();
        AssetRequirementCount = Integer.parseInt(TempString);

        for(int Index = 0; Index < AssetRequirementCount; Index++){
            //PAssetType.DAssetRequirements.push_back(NameToType(TempString));
        }

        ReturnStatus = true;
        return ReturnStatus;
    }
    /*
    public static int MaxSight(){

    }
    public static std.shared_ptr< CPlayerAssetType > FindDefaultFromName( std.string &name){

    }
    public static std.shared_ptr< CPlayerAssetType > FindDefaultFromType(EAssetType type){

    }
    public static std.shared_ptr< std.unordered_map< std.string, std.shared_ptr< CPlayerAssetType > > > DuplicateRegistry(EPlayerColor color);

    public std.shared_ptr< CPlayerAsset > Construct(){

    }*/
}
