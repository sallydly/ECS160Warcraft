package com.warcraftII.player_asset;

import com.warcraftII.GameDataTypes;
import com.warcraftII.data_source.DataSource;
import com.warcraftII.player_asset.ETargetType;
import com.warcraftII.player_asset.PlayerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.crypto.Data;

import static com.warcraftII.GameDataTypes.to_underlying;

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

public class PlayerAsset {
//    protected int DAssetID;
//    protected int DCreationCycle;
//    protected int DHitPoints;
    protected int DGold;
    protected int DLumber;
//    protected int DStone;
//    protected int DStep;
//    protected int DMoveRemainderX;
//    protected int DMoveRemainderY;

    // random number to assign each turn
//    protected unsigned int DTurnOrder;
//    protected CPixelPosition DPosition;
//    protected EDirection DDirection;
    protected Vector< SAssetCommand > DCommands;
    protected PlayerAssetType DType;
//    protected static int DUpdateFrequency;
//    protected static int DUpdateDivisor;
//    protected static CRandomNumberGenerator DGenerateRandomNum;

    // NOTE: all of the following C++ methods are public
//    CPlayerAsset(std::shared_ptr< CPlayerAssetType > type);
//        ~CPlayerAsset();
//
//    std::vector< SAssetCommand > GetCommands(){
//        return DCommands;
//    }
//
//    static int UpdateFrequency(){
//        return DUpdateFrequency;
//    };
//
//    static int UpdateFrequency(int freq);
//
//    bool Alive() const{
//        return 0 < DHitPoints;
//    };
//
//    int CreationCycle() const{
//        return DCreationCycle;
//    };
//
//    int CreationCycle(int cycle){
//        return DCreationCycle = cycle;
//    };
//
//    int HitPoints() const{
//        return DHitPoints;
//    };
//
//    int HitPoints(int hitpts){
//        return DHitPoints = hitpts;
//    };
//
//    int IncrementHitPoints(int hitpts){
//        DHitPoints += hitpts;
//        if(MaxHitPoints() < DHitPoints){
//            DHitPoints = MaxHitPoints();
//        }
//        return DHitPoints;
//    };
//
//    int DecrementHitPoints(int hitpts){
//        DHitPoints -= hitpts;
//        if(0 > DHitPoints){
//            DHitPoints = 0;
//        }
//        return DHitPoints;
//    };

    public int Gold() {
        return DGold;
    };

//    int Gold(int gold){
//        return DGold = gold;
//    };
//
//    int IncrementGold(int gold){
//        DGold += gold;
//        return DGold;
//    };
//
//    int DecrementGold(int gold){
//        DGold -= gold;
//        return DGold;
//    };

    public int Lumber() {
        return DLumber;
    };

//    int Lumber(int lumber){
//        return DLumber = lumber;
//    };
//
//    int IncrementLumber(int lumber){
//        DLumber += lumber;
//        return DLumber;
//    };
//
//    int DecrementLumber(int lumber){
//        DLumber -= lumber;
//        return DLumber;
//    };
//
//    int Stone() const{
//        return DStone;
//    };
//
//    int Stone(int stone){
//        return DStone = stone;
//    };
//
//    int IncrementStone(int stone){
//        DStone += stone;
//        return DStone;
//    };
//
//    int DecrementStone(int stone){
//        DStone -= stone;
//        return DStone;
//    };
//
//    int Step() const{
//        return DStep;
//    };
//
//    int Step(int step){
//        return DStep = step;
//    };
//
//    void ResetStep(){
//        DStep = 0;
//    };
//
//    void IncrementStep(){
//        DStep++;
//    };
//
//    void AssignTurnOrder(){
//        DTurnOrder = DGenerateRandomNum.Random();
//    }
//
//    unsigned int GetTurnOrder(){
//        return DTurnOrder;
//    }
//
//    friend bool CompareTurnOrder(std::shared_ptr< CPlayerAsset > a, std::shared_ptr< CPlayerAsset > b);
//
//    CTilePosition TilePosition() const;
//
//    CTilePosition TilePosition(const CTilePosition &pos);
//
//    int TilePositionX() const;
//
//    int TilePositionX(int x);
//
//    int TilePositionY() const;
//
//    int TilePositionY(int y);
//
//    CPixelPosition Position() const{
//        return DPosition;
//    };
//
//    CPixelPosition Position(const CPixelPosition &pos);
//
//    bool TileAligned() const{
//        return DPosition.TileAligned();
//    };
//
//    int PositionX() const{
//        return DPosition.X();
//    };
//
//    int PositionX(int x);
//
//    int PositionY() const{
//        return DPosition.Y();
//    };
//
//    int PositionY(int y);
//
//    CPixelPosition ClosestPosition(const CPixelPosition &pos) const;
//
//    int CommandCount() const{
//        return DCommands.size();
//    };
//
//    void ClearCommand(){
//        PrintDebug(DEBUG_HIGH, "Cleared commands of %d\n", Type());
//        DCommands.clear();
//    };
//
//    void PushCommand(const SAssetCommand &command){
//        DCommands.push_back(command);
//    };
//
//    void EnqueueCommand(const SAssetCommand &command){
//        DCommands.insert(DCommands.begin(),command);
//    };
//
//    void PopCommand(){
//        if(!DCommands.empty()){
//            PrintDebug(DEBUG_LOW, "Popped command from asset color %d type %d actions\n", (int)Type(), CommandCount());
//            DCommands.pop_back();
//        }
//    };
//
//    SAssetCommand CurrentCommand() const{
//        if(!DCommands.empty()){
//            return DCommands.back();
//        }
//        SAssetCommand RetVal;
//        RetVal.DAction = EAssetAction::None;
//        return RetVal;
//    };
//
//    SAssetCommand NextCommand() const{
//        if(1 < DCommands.size()){
//            return DCommands[DCommands.size() - 2];
//        }
//        SAssetCommand RetVal;
//        RetVal.DAction = EAssetAction::None;
//        return RetVal;
//    };

    public GameDataTypes.EAssetAction Action() {
        if(!DCommands.isEmpty()){
            return DCommands.lastElement().DAction;
        }
        return GameDataTypes.EAssetAction.None;
    };

//    bool HasAction(EAssetAction action) const{
//        for(auto Command : DCommands){
//            if(action == Command.DAction){
//                return true;
//            }
//        }
//        return false;
//    };
//
//    bool HasActiveCapability(EAssetCapabilityType capability) const{
//        for(auto Command : DCommands){
//            if(EAssetAction::Capability == Command.DAction){
//                if(capability == Command.DCapability){
//                    return true;
//                }
//            }
//        }
//        return false;
//    };
//
//    bool Interruptible() const;
//
//    EDirection Direction() const{
//        return DDirection;
//    };
//
//    EDirection Direction(EDirection direction){
//        return DDirection = direction;
//    };
//
//    int MaxHitPoints() const{
//        return DType->HitPoints();
//    };

    public GameDataTypes.EAssetType Type() {
        return DType.Type();
    };

//    std::shared_ptr< CPlayerAssetType > AssetType() const{
//        return DType;
//    };
//
//    void ChangeType(std::shared_ptr< CPlayerAssetType > type){
//        DType = type;
//    };

    public GameDataTypes.EPlayerColor Color() {
        return DType.Color();
    };

//    int Armor() const{
//        return DType->Armor();
//    };
//
//    int Sight() const{
//        return EAssetAction::Construct == Action() ? DType->ConstructionSight() : DType->Sight();
//    };
//
//    int Size() const{
//        return DType->Size();
//    };

    public int Speed() {
        return DType.Speed();
    };

//    int GoldCost() const{
//        return DType->GoldCost();
//    };
//
//    int LumberCost() const{
//        return DType->LumberCost();
//    };
//
//    int StoneCost() const{
//        return DType->StoneCost();
//    };
//
//    int FoodConsumption() const{
//        return DType->FoodConsumption();
//    };
//
//    int BuildTime() const{
//        return DType->BuildTime();
//    };
//
//    int AttackSteps() const{
//        return DType->AttackSteps();
//    };
//
//    int ReloadSteps() const{
//        return DType->ReloadSteps();
//    };
//
//    int BasicDamage() const{
//        return DType->BasicDamage();
//    };
//
//    int PiercingDamage() const{
//        return DType->PiercingDamage();
//    };
//
//    int Range() const{
//        return DType->Range();
//    };
//
//    int ArmorUpgrade() const{
//        return DType->ArmorUpgrade();
//    };
//
//    int SightUpgrade() const{
//        return DType->SightUpgrade();
//    };
//
//    int SpeedUpgrade() const{
//        return DType->SpeedUpgrade();
//    };
//
//    int BasicDamageUpgrade() const{
//        return DType->BasicDamageUpgrade();
//    };
//
//    int PiercingDamageUpgrade() const{
//        return DType->PiercingDamageUpgrade();
//    };
//
//    int RangeUpgrade() const{
//        return DType->RangeUpgrade();
//    };
//
//    int EffectiveArmor() const{
//        return Armor() + ArmorUpgrade();
//    };
//
//    int EffectiveSight() const{
//        return Sight() + SightUpgrade();
//    };
//
//    int EffectiveSpeed() const{
//        return Speed() + SpeedUpgrade();
//    };
//
//    int EffectiveBasicDamage() const{
//        return BasicDamage() + BasicDamageUpgrade();
//    };
//
//    int EffectivePiercingDamage() const{
//        return PiercingDamage() + PiercingDamageUpgrade();
//    };
//
//    int EffectiveRange() const{
//        return Range() + RangeUpgrade();
//    };

    public boolean HasCapability(GameDataTypes.EAssetCapabilityType capability) {
        return DType.HasCapability(capability);
    };

    //public Vector<GameDataTypes.EAssetCapabilityType> Capabilities() {
    //    return DType.Capabilities();
    //};

//    bool MoveStep(std::vector< std::vector< std::shared_ptr< CPlayerAsset > > > &occupancymap, std::vector< std::vector< bool > > &diagonals);
//
//
//    int AssetID() const{
//        return DAssetID;
//    }
//
//    int AssetID(int ID){
//        return DAssetID = ID;
//    }
}