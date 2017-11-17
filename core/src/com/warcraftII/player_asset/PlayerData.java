package com.warcraftII.player_asset;

import com.badlogic.gdx.utils.Logger;
import com.warcraftII.GameDataTypes;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.terrain_map.initialization.SAssetInitialization;
import com.warcraftII.terrain_map.initialization.SResourceInitialization;
import com.warcraftII.units.Unit;

import java.util.List;
import java.util.Map;
import java.util.Vector;
//import com.warcraftII.asset.VisibilityMap;

public class PlayerData {
    private Logger log = new Logger("PlayerData", 2);

    protected boolean DIsAI;
    protected EPlayerColor DColor;
    //VisibilityMap DVisibilityMap;
    //CAssetDecoratedMap DActualMap;
    //CAssetDecoratedMa DPlayerMap;
    protected Map< String, PlayerAssetType > DAssetTypes;
    protected List< StaticAsset > DStaticAssets;
    //Vector< Boolean > DUpgrades;
    //Vector< SGameEvent > DGameEvents;
    protected int DGold;
    protected int DLumber;
    protected int DGameCycle;

    public PlayerData(AssetDecoratedMap map, EPlayerColor color, Unit allUnits){
        DIsAI = true;
        DGameCycle = 0;
        DColor = color;
        //DActualMap = map;
        //DAssetTypes = PlayerAssetType.DuplicateRegistry(color);
        //DPlayerMap = DActualMap->CreateInitializeMap();
        //DVisibilityMap = DActualMap->CreateVisibilityMap();
        DGold = 0;
        DLumber = 0;

        /*DUpgrades.resize(to_underlying(EAssetCapabilityType::Max));
        for(int Index = 0; Index < DUpgrades.size(); Index++){
            DUpgrades[Index] = false;
        }
        */
        for(SResourceInitialization ResourceInit: map.ResourceInitializationList()){
            if(ResourceInit.DColor == color){
                DGold = ResourceInit.DGold;
                DLumber = ResourceInit.DLumber;
            }
        }

        DStaticAssets = new Vector<StaticAsset>();
        for(SAssetInitialization AssetInit: map.AssetInitializationList()){
            if(AssetInit.DColor == color){
                log.debug(AssetInit.DType);
                log.debug(String.valueOf(AssetInit.DTilePosition.X()));
                log.debug(String.valueOf(AssetInit.DTilePosition.Y()));
                EAssetType assetType = PlayerAssetType.NameToType(AssetInit.DType);
                if (GameDataTypes.is_static(assetType))
                {
                    StaticAsset InitAsset = CreateStaticAsset(AssetInit.DType);
                    InitAsset.tilePosition(AssetInit.DTilePosition);
                    if(EAssetType.GoldMine == PlayerAssetType.NameToType(AssetInit.DType)){
                        InitAsset.gold(DGold);
                    }
                    DStaticAssets.add(InitAsset);
                }
                else
                {
                    // initialize units
                    allUnits.AddUnit(AssetInit.DTilePosition, GameDataTypes.to_unitType(assetType), color);
                }

            }
        }
    }

    /*int GameCycle() {
        return DGameCycle;
    }
    
    void IncrementCycle(){
        DGameCycle++;
    }
    */
    
    public EPlayerColor Color() {
        return DColor;
    }

    public boolean IsAI() {
        return DIsAI;
    }

    public boolean IsAI(boolean isai){
        return DIsAI = isai;
    }

    /*public boolean IsAlive() {
        //return DAssets.size();
    }
    */
    public int Gold() {
        return DGold;
    }
    
    public int Lumber() {
        return DLumber;
    }
    public int IncrementGold(int gold){
        DGold += gold;
        return DGold;
    }

    public int DecrementGold(int gold){
        DGold -= gold;
        return DGold;
    }
    public int IncrementLumber(int lumber){
        DLumber += lumber;
        return DLumber;
    }
    public int DecrementLumber(int lumber){
        DLumber -= lumber;
        return DLumber;
    }

    /*
    public int FoodConsumption() {};
    public int FoodProduction() {};
    */

    /*VisibilityMap VisibilityMap() {
        return DVisibilityMap;
    }
    std::shared_ptr< CAssetDecoratedMap > PlayerMap() {
        return DPlayerMap;
    }
    */
    public List<StaticAsset> StaticAssets() {
        return DStaticAssets;
    }

    public StaticAsset CreateStaticAsset(String name){
        return PlayerAssetType.ConstructStaticAsset(name);
    }


    /*

    std::shared_ptr< CPlayerAsset > CreateMarker( CPixelPosition &pos, boolean addtomap);
    void DeleteAsset(std::shared_ptr< CPlayerAsset > asset);
    boolean AssetRequirementsMet( std::string &assettypename);
    void UpdateVisibility();
    std::list< std::weak_ptr< CPlayerAsset > > SelectAssets( SRectangle &selectarea, EAssetType assettype, boolean selectidentical = false);
    std::weak_ptr< CPlayerAsset > SelectAsset( CPixelPosition &pos, EAssetType assettype);
    std::weak_ptr< CPlayerAsset > FindNearestOwnedAsset( CPixelPosition &pos,  std::vector< EAssetType > assettypes);
    std::shared_ptr< CPlayerAsset > FindNearestAsset( CPixelPosition &pos, EAssetType assettype);
    std::weak_ptr< CPlayerAsset > FindNearestEnemy( CPixelPosition &pos, int range);
    CTilePosition FindBestAssetPlacement( CTilePosition &pos, std::shared_ptr< CPlayerAsset > builder, EAssetType assettype, int buffer);
    std::list< std::weak_ptr< CPlayerAsset > > IdleAssets() ;
    */

   /* public int PlayerAssetCount(EAssetType type){

        }

    public int FoundAssetCount(EAssetType type){

        }
        */
    //public void AddUpgrade( std::string &upgradename);
    /*boolean HasUpgrade(EAssetCapabilityType upgrade) {
        if((0 > to_underlying(upgrade))||(DUpgrades.size() <= static_cast<decltype(DUpgrades.size())>(upgrade))){
            return false;
        }
        return DUpgrades[static_cast<decltype(DUpgrades.size())>(upgrade)];
    }

         std::vector< SGameEvent > &GameEvents() {
        return DGameEvents;
    }
    void ClearGameEvents(){
        DGameEvents.clear();
    }
    void AddGameEvent( SGameEvent &event){
        DGameEvents.push_back(event);
    }
    void AppendGameEvents( std::vector< SGameEvent > &events){
        DGameEvents.insert(DGameEvents.end(), events.begin(), events.end());
    }
    */

    public static Vector<PlayerData> LoadAllPlayers(AssetDecoratedMap map, Unit allUnits)
    {
        Vector<PlayerData> PlayerVector = new Vector<PlayerData>();
        for(EPlayerColor color : map.Players())
        {
            PlayerData newPlayer = new PlayerData(map,color, allUnits);
            PlayerVector.add(newPlayer);
        }
        return PlayerVector;
    }
}
