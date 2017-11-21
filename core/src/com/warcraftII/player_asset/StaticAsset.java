package com.warcraftII.player_asset;

import com.warcraftII.GameDataTypes;
import com.warcraftII.player_asset.SAssetCommand;
import com.warcraftII.player_asset.PlayerAssetType;
import com.warcraftII.position.TilePosition;

import java.util.Vector;

public class StaticAsset extends PlayerAsset{
    public enum EState {
        CONSTRUCT_0,
        CONSTRUCT_1,
        ACTIVE,
        INACTIVE,
        PLACE
    }

    //Add something for asset commands to perform on this static asset
    private EState DCurrentState;
    private int DHitPoints;
    private int DGold;
    private int DLumber;
    private PlayerAssetType DType;
    private TilePosition DPosition;
    private Vector<SAssetCommand> DCommands;
    private int DStep;

    public GameDataTypes.EPlayerColor DOwner;

    public StaticAsset(PlayerAssetType type) {
        DType = type;
        DHitPoints = type.HitPoints();
        DGold = 0;
        DLumber = 0;
        DPosition = new TilePosition(0, 0);
        DCurrentState = EState.INACTIVE; // TODO: have a cycle of building-> inactive, active etc.
        DCommands = new Vector<SAssetCommand>();
    }

    public EState state(){
        return DCurrentState;
    }

    public boolean Alive() {
        return 0 < DHitPoints;
    }

    //TODO: Check these
//    public int CreationCycle() {
//        return DCreationCycle;
//    }
//    public int CreationCycle(int cycle) {
//        return DCreationCylce = cycle;
//    }

    public int hitPoints() {
        return DHitPoints;
    }

    public int hitPoints(int hitPoints) {
        return DHitPoints = hitPoints;
    }

    public int incrementHitPoints(int hitPoints){
        DHitPoints += hitPoints;
        if(this.maxHitPoints() < DHitPoints){
            DHitPoints = maxHitPoints();
        }
        return DHitPoints;
    }

    public int decrementHitPoints(int hitPoints){
        DHitPoints -= hitPoints;
        if(0 > DHitPoints){
            DHitPoints = 0;
            SAssetCommand deathcommand = new SAssetCommand();
            deathcommand.DAction = GameDataTypes.EAssetAction.Death;
            DCommands.add(deathcommand);
        }
        return DHitPoints;
    }

    public int gold() {
        return DGold;
    }

    public int gold(int gold){
        return DGold = gold;
    }

    public int incrementGold(int gold){
        DGold += gold;
        return DGold;
    }

    public int decrementGold(int gold){
        DGold -= gold;
        return DGold;
    }

    public int lumber() {
        return DLumber;
    }

    public int lumber(int lumber){
        return DLumber = lumber;
    }

    public int incrementLumber(int lumber){
        DLumber += lumber;
        return DLumber;
    }

    public int decrementLumber(int lumber){
        DLumber -= lumber;
        return DLumber;
    }
//
//    void AssignTurnOrder(){
//        DTurnOrder = DGenerateRandomNum.Random();
//    }
//
//    unsigned int GetTurnOrder(){
//        return DTurnOrder;
//    }

    public int Size() {
        return DType.Size();
    }

    public final TilePosition tilePosition() {
        return DPosition;
    }

    public TilePosition tilePosition(final TilePosition pos) {
        return DPosition = pos;
    }

    public final int tilePositionX() {
        return DPosition.X();
    }

    public final int tilePositionX(int x) {
        DPosition.setXFromPixel(x);
        return x;
    }

    public final int tilePositionY() {
        return DPosition.Y();
    }

    public int tilePositionY(int y) {
        DPosition.setYFromPixel(y);
        return y;
    }

//    public PixelPosition position(final PixelPosition pos) {
//        return DPosition = pos;
//    }

    public int positionX() {
        return DPosition.X();
    }

    public int positionX(int x) {
        return DPosition.X(x);
    }

    public int positionY() {
        return DPosition.Y();
    }

    public int positionY(int y) {
        return DPosition.Y(y);
    }

//    public final TilePosition closestPosition(final TilePosition pos) {
//        return pos.closestPosition(DPosition, this.Size());
//    }
//
//    public boolean tileAligned() {
//        return DPosition.();
//    }

    public int maxHitPoints() {
        return DType.HitPoints();
    }

    public GameDataTypes.EAssetType type() {
        return DType.Type();
    }

    public PlayerAssetType assetType() {
        return DType;
    }

    public void changeType(PlayerAssetType type) {
        DType = type;
    }

    public GameDataTypes.EPlayerColor color() {
        return DType.Color();
    }

    public int armor() {
        return DType.Armor();
    }

    public int CommandCount() {
        return DCommands.size();
    }

    public void ClearCommand() {
        DCommands.clear();
    }

    public void PushCommand(SAssetCommand command) {
        DCommands.add(command);
    }

    public void EnqueueCommand(SAssetCommand command) {
        DCommands.add(0, command);
    }

    public void PopCommand() {
        if(!DCommands.isEmpty()) {
            int lastElementIndex = DCommands.lastIndexOf(DCommands.lastElement());
            DCommands.removeElementAt(lastElementIndex);
        }
    }

    public SAssetCommand CurrentCommand() {
        if(!DCommands.isEmpty()){
            return DCommands.lastElement();
        }
        SAssetCommand RetVal = new SAssetCommand();
        RetVal.DAction = GameDataTypes.EAssetAction.None;
        return RetVal;
    }

    public SAssetCommand NextCommand() {
        if(1 < DCommands.size()){
            return DCommands.get(DCommands.size() - 2);
        }
        SAssetCommand RetVal = new SAssetCommand();
        RetVal.DAction = GameDataTypes.EAssetAction.None;
        return RetVal;
    }

    public GameDataTypes.EAssetAction Action() {
        if(!DCommands.isEmpty()){
            GameDataTypes.EAssetAction action = DCommands.lastElement().DAction;

            switch(action) {
                case Construct:
                    this.DCurrentState = EState.CONSTRUCT_0;
                    break;
                case Death:
                    this.DCurrentState = EState.INACTIVE;
                    break;
            }
            return DCommands.lastElement().DAction;
        }
        return GameDataTypes.EAssetAction.None;
    }


//TODO: add this
    public int Sight() {
        return GameDataTypes.EAssetAction.Construct == Action() ? DType.ConstructionSight() : DType.Sight();
    }

    public int SightUpgrade() {
        return DType.SightUpgrade();
    }

    public int EffectiveSight() {
        return Sight() + SightUpgrade();
    }

    public int GoldCost() {
        return DType.GoldCost();
    }

    public int LumberCost() {
        return DType.LumberCost();
    }

    int BuildTime() {
        return DType.BuildTime();
    }

    public int Step(){ return DStep; }
    public int Step(int step){ return DStep = step; }
    public  int IncrementStep() { return DStep = DStep + 1;}


}

