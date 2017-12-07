package com.warcraftII.player_asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.warcraftII.GameDataTypes;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.Volume;
import com.warcraftII.player_asset.SAssetCommand;
import com.warcraftII.player_asset.PlayerAssetType;
import com.warcraftII.position.TilePosition;

import java.util.Vector;

public class StaticAsset extends PlayerAsset{

    //Add something for asset commands to perform on this static asset
    private int DHitPoints;
    private int DGold;
    private int DLumber;
    private PlayerAssetType DType;
    private TilePosition DPosition;
    private Vector<SAssetCommand> DCommands;
    private int DStep;

    private EPlayerColor DOwner;

    public EStaticAssetType DUpgradedFrom = null;

    public EUnitType DPendingUnitType;
    public int DUnitConstructionTime;


    private Sound goldMineSound = Gdx.audio.newSound(Gdx.files.internal("data/snd/buildings/gold-mine.wav"));

    public StaticAsset() {}

    public StaticAsset(PlayerAssetType type) {
        DType = type;
        DHitPoints = type.HitPoints();
        DGold = 0;
        DLumber = 0;
        DPosition = new TilePosition(0, 0);
        DCommands = new Vector<SAssetCommand>();
    }

    public EPlayerColor owner(){
        return DOwner;
    }

    public EPlayerColor owner(EPlayerColor playerColor){
        return DOwner = playerColor;
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
        if (DHitPoints  == 0)
            return 0; //dont do anything more.

        DHitPoints -= hitPoints;
        if(0 >= DHitPoints){
            DHitPoints = 0;
            SAssetCommand deathcommand = new SAssetCommand();
            deathcommand.DAction = EAssetAction.Death;
            DCommands.clear();
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

    public EAssetType type() {
        return DType.Type();
    }

    public EStaticAssetType staticAssetType(){
        return GameDataTypes.to_staticAssetType(type());
    }

    public PlayerAssetType assetType() {
        return DType;
    }

    public void changeType(PlayerAssetType type) {
        DType = type;
    }

    public EPlayerColor color() {
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
        RetVal.DAction = EAssetAction.None;
        return RetVal;
    }

    public SAssetCommand NextCommand() {
        if(1 < DCommands.size()){
            return DCommands.get(DCommands.size() - 2);
        }
        SAssetCommand RetVal = new SAssetCommand();
        RetVal.DAction = EAssetAction.None;
        return RetVal;
    }

    public EAssetAction Action() {
        if(!DCommands.isEmpty()){
            EAssetAction action = DCommands.lastElement().DAction;
            return action;
        }
        return EAssetAction.None;
    }


    //TODO: add this
    public int Sight() {
        return EAssetAction.Construct == Action() ? DType.ConstructionSight() : DType.Sight();
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

    public int StoneCost(){
        return DType.StoneCost();
    }

    int BuildTime() {
        return DType.BuildTime();
    }

    public int Step(){ return DStep; }
    public int Step(int step){ return DStep = step; }
    public  int IncrementStep() { return DStep = DStep + 1;}

    public void StartMining(){
        if (staticAssetType() == EStaticAssetType.GoldMine){
            SAssetCommand constructCommand = new SAssetCommand();
            constructCommand.DAction = EAssetAction.MineGold;
            EnqueueCommand(constructCommand);
        }
        goldMineSound.play(Volume.getFxVolume()/100);
    }

    public void EndMining(){
        if(staticAssetType() == EStaticAssetType.GoldMine){
            if (CurrentCommand().DAction ==  EAssetAction.MineGold){
                PopCommand();
            }

        }
    }

}

