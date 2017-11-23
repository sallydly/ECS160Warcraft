package com.warcraftII.renderer;


/*
* Adds stationary assets onto a new layer of map
*
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Logger;

import com.warcraftII.GameDataTypes;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.terrain_map.initialization.SAssetInitialization;
import com.warcraftII.player_asset.StaticAsset;
import com.warcraftII.position.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class StaticAssetRenderer {
    private static final Logger log = new Logger("StaticAssetRenderer", 2);

    private static Map<EStaticAssetType,TextureAtlas> DStaticAssetTextures;
    private static Map<EStaticAssetType,String> DTypeNameTranslation;

    private TextureAtlas animationTextures;

    private TiledMap tiledMap;
    private TiledMapTileLayer assetLayer;
    private String[] staticAssetsArray;

    private static int DUpdateFrequency;
    private static int[] DConstructionStages; // Gives the max construction stage # for each type.
    private static int BuildingDeathMaxIndex = 15;


    private static Animation<TextureRegion> LargeFireAnim,SmallFireAnim, BuildingDeathAnim;
    private Map<StaticAsset, SpriteAnimation> DEffectSpriteMapping;

    //private final String mapName;


    static{ MakeAtlases();}
    private static void MakeAtlases(){
        DTypeNameTranslation = new HashMap<EStaticAssetType,String>();
        {
            DTypeNameTranslation.put( EStaticAssetType.GoldMine, "GoldMine");
            DTypeNameTranslation.put(EStaticAssetType.TownHall, "TownHall");
            DTypeNameTranslation.put(EStaticAssetType.Keep, "Keep");
            DTypeNameTranslation.put(EStaticAssetType.Castle, "Castle");
            DTypeNameTranslation.put(EStaticAssetType.Farm,"Farm");
            DTypeNameTranslation.put(EStaticAssetType.Barracks, "Barracks");
            DTypeNameTranslation.put(EStaticAssetType.LumberMill,"LumberMill");
            DTypeNameTranslation.put(EStaticAssetType.Blacksmith, "Blacksmith");
            DTypeNameTranslation.put(EStaticAssetType.ScoutTower, "ScoutTower");
            DTypeNameTranslation.put(EStaticAssetType.GuardTower,"GuardTower");
            DTypeNameTranslation.put(EStaticAssetType.CannonTower,"CannonTower");
            DTypeNameTranslation.put(EStaticAssetType.Wall, "Wall");
        }

        for (EStaticAssetType assetType : DTypeNameTranslation.keySet())
        {
            String filePathString = "atlas/" + DTypeNameTranslation.get(assetType) + ".atlas";
            TextureAtlas Textures = new TextureAtlas(Gdx.files.internal(filePathString));
            DStaticAssetTextures.put(assetType,Textures);
        }

    }

    public StaticAssetRenderer(TiledMap tiledMap,
                               int mapWidth,
                               int mapHeight,
                               String mapName) {
        this.animationTextures = new TextureAtlas(Gdx.files.internal("atlas/animations.atlas"));
        this.DStaticAssetTextures = new HashMap<EStaticAssetType, TextureAtlas>();
        this.tiledMap = tiledMap;
        this.assetLayer = new TiledMapTileLayer(mapWidth, mapHeight, 32, 32);

        DEffectSpriteMapping = new HashMap<StaticAsset, SpriteAnimation>();

        LargeFireAnim = new Animation<TextureRegion>(0.5f,animationTextures.findRegions("large-fire"));
        SmallFireAnim = new Animation<TextureRegion>(0.5f, animationTextures.findRegions("small-fire"));
        BuildingDeathAnim = new Animation<TextureRegion>(0.5f, animationTextures.findRegions("buildingdeath"));

        DConstructionStages = new int[EAssetType.values().length];
        for (EAssetType sassettype : EAssetType.values())
        {
            DConstructionStages[GameDataTypes.to_underlying(sassettype)] = -1;

            String typeName;

            switch (sassettype) {
                case GoldMine:
                    typeName = "goldmine-construct-";
                    break;
                case TownHall:
                    typeName = "townhall-construct-";
                    break;
                case Keep:
                    typeName = "keep-construct-";
                    break;
                case Castle:
                    typeName = "castle-construct-";
                    break;
                case Farm:
                    typeName = "farm-construct-";
                    break;
                case Barracks:
                    typeName = "barracks-construct-";
                    break;
                case LumberMill:
                    typeName = "lumbermill-construct-";
                    break;
                case Blacksmith:
                    typeName = "blacksmith-construct-";
                    break;
                case ScoutTower:
                    typeName = "scouttower-construct-";
                    break;
                case GuardTower:
                    typeName = "guardtower-construct-";
                    break;
                case CannonTower:
                    typeName = "cannontower-construct-";
                    break;
                default:
                    typeName = "bad-";
                    break;
            }

            int i = 0;

            while(true){
                TextureRegion text = staticAssetTextures.findRegion(typeName + String.valueOf(i));
                if (text != null){
                    DConstructionStages[GameDataTypes.to_underlying(sassettype)] = i;
                    i++;
                }
                else
                {
                    break;
                }
            }

            //log.debug(typeName+String.valueOf(DConstructionStages[to_underlying(sassettype)] ));
        }

        //this.mapName = mapName;
        /*
        * Unsure if textures larger than 32x32 can be placed on layer easily
        * May have to separate atlas into 32x32, 64x64, 128x128 pages and create separate layers for those
         */

    }



    // This one takes in the map AND PlayerData object, and a tiledMapTileLayer that it edits.
    // ONLY USED FOR INITIAL PLACEMENT...
    public TiledMapTileLayer addStaticAssets(AssetDecoratedMap map, Vector<PlayerData> playerData) {
        System.out.println("start render");

        assetLayer = new TiledMapTileLayer(map.Width(), map.Height(), 32, 32);
        assetLayer.setName("StaticAssets");

        for (PlayerData player : playerData) {
            for (StaticAsset StatAsset : player.StaticAssets()) {
                String tileName, typeName, colorName;

                colorName = "";

                switch (StatAsset.type()) {
                    case GoldMine:
                        typeName = "goldmine-inactive";
                        break;
                    case TownHall:
                        typeName = "townhall-inactive";
                        break;
                    case Keep:
                        typeName = "keep-inactive";
                        break;
                    case Castle:
                        typeName = "castle-inactive";
                        break;
                    case Farm:
                        typeName = "farm-inactive";
                        break;
                    case Barracks:
                        typeName = "barracks-inactive";
                        break;
                    case LumberMill:
                        typeName = "lumbermill-inactive";
                        break;
                    case Blacksmith:
                        typeName = "blacksmith-inactive";
                        break;
                    case ScoutTower:
                        typeName = "scouttower-inactive";
                        break;
                    case GuardTower:
                        typeName = "guardtower-inactive";
                        break;
                    case CannonTower:
                        typeName = "cannontower-inactive";
                        break;
                    default:
                        //BAD STUFF
                        typeName = "badtype-inactive";
                        break;
                }
                tileName = colorName + typeName;

                //log.info("tileName: " + tileName);
                int XPos = StatAsset.tilePosition().X();
                //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
                int YPos = map.Height() - StatAsset.tilePosition().Y() - 1; // -1 to account for 0 index
                GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, tileName);
            }
        }
        return assetLayer;
    }

    public void UpdateStaticAssets(TiledMap tmap, AssetDecoratedMap map, Vector<PlayerData> playerData) {

        TiledMapTileLayer assetLayer =  (TiledMapTileLayer) tmap.getLayers().get("StaticAssets");
        TiledMapTileLayer effectsLayer = (TiledMapTileLayer) tmap.getLayers().get("BuildingEffects");

        if (effectsLayer == null){
            effectsLayer = new TiledMapTileLayer(map.Width(), map.Height(), 32, 32);
            effectsLayer.setName("BuildingEffects") ;
            tmap.getLayers().add(effectsLayer);
        }

        for (PlayerData player : playerData) {
            for (StaticAsset StatAsset : player.StaticAssets()) {
                String tileName, typeName, stateName;

                switch (StatAsset.type()) {
                    case GoldMine:
                        typeName = "goldmine-";
                        break;
                    case TownHall:
                        typeName = "townhall-";
                        break;
                    case Keep:
                        typeName = "keep-";
                        break;
                    case Castle:
                        typeName = "castle-";
                        break;
                    case Farm:
                        typeName = "farm-";
                        break;
                    case Barracks:
                        typeName = "barracks-";
                        break;
                    case LumberMill:
                        typeName = "lumbermill-";
                        break;
                    case Blacksmith:
                        typeName = "blacksmith-";
                        break;
                    case ScoutTower:
                        typeName = "scouttower-";
                        break;
                    case GuardTower:
                        typeName = "guardtower-";
                        break;
                    case CannonTower:
                        typeName = "cannontower-";
                        break;
                    default:
                        //BAD STUFF
                        typeName = "badtype-";
                        break;
                }

                int XPos = StatAsset.tilePosition().X();
                //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
                int YPos = map.Height() - StatAsset.tilePosition().Y() - 1; // -1 to account for 0 index


                switch(StatAsset.Action())
                {

                    case None:
                        stateName = "inactive";
                        tileName = typeName + stateName;

                        GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, tileName);

                        if (StatAsset.hitPoints() < StatAsset.maxHitPoints()/2){
                            UnitPosition newUPos = new UnitPosition(StatAsset.tilePosition());
                            Sprite newSprite = new Sprite();
                            newSprite.setPosition(newUPos.X(),newUPos.Y());
                            newSprite.setSize(Position.tileWidth()*StatAsset.assetType().Size(), Position.tileWidth()*StatAsset.assetType().Size());
                            DEffectSpriteMapping.put(StatAsset, new SpriteAnimation(newSprite, LargeFireAnim));
                        }
                        break;



                    case Construct:
                        int ActionSteps = DConstructionStages[GameDataTypes.to_underlying(StatAsset.assetType().Type())] + 1;

                        if(ActionSteps > 0){
                            int TotalSteps = StatAsset.assetType().BuildTime() * UpdateFrequency();
                            int CurrentStep = StatAsset.Step() * ActionSteps / TotalSteps;


                            //log.debug( StatAsset.assetType().Name() +" Current step: " + String.valueOf(CurrentStep));
                            //log.debug( String.valueOf(StatAsset.Step())+ "/" + String.valueOf(TotalSteps));

                            if(CurrentStep == ActionSteps){
                                CurrentStep--;
                            }

                            int ConstructTileIndex = CurrentStep;

                            stateName = "construct-" + String.valueOf(ConstructTileIndex);

                            tileName = typeName + stateName;

                            GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, tileName);
                        }
                        else
                        {
                            stateName = "inactive";
                            tileName = typeName + stateName;

                            GraphicTileset.DrawTile(staticAssetTextures, assetLayer, XPos, YPos, tileName);
                        }
                        break;

                    case Death:
                        TiledMapTileLayer.Cell nofirecell = new TiledMapTileLayer.Cell();
                        nofirecell.setTile(null);
                        effectsLayer.setCell(XPos, YPos , nofirecell);

                        if(StatAsset.Step() <= BuildingDeathMaxIndex){
                            UnitPosition newUPos = new UnitPosition(StatAsset.tilePosition());
                            Sprite newSprite = new Sprite();
                            newSprite.setPosition(newUPos.X(),newUPos.Y());
                            newSprite.setSize(Position.tileWidth()*StatAsset.assetType().Size(), Position.tileWidth()*StatAsset.assetType().Size());
                            DEffectSpriteMapping.put(StatAsset,new SpriteAnimation(newSprite,BuildingDeathAnim));
                        }
                        break;

                }
            }
        }
    }

    protected class SpriteAnimation {
        Sprite DSprite;
        Animation<TextureRegion> DAnim;

        protected SpriteAnimation(Sprite sprite, Animation<TextureRegion> anim){
            DSprite = sprite;
            DAnim = anim;
        }
    }

    public void DrawEffects(SpriteBatch sb, OrthographicCamera cam, float elapsedTime){
        System.out.println("drawing effects");
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        for(SpriteAnimation  spa : DEffectSpriteMapping.values()){
            boolean looping = spa.DAnim.equals(LargeFireAnim);
            System.out.println(String.valueOf(looping) );

            sb.draw(spa.DAnim.getKeyFrame(elapsedTime, looping), spa.DSprite.getX(), spa.DSprite.getY());
        }
        sb.end();
    }

    public static int UpdateFrequency(){
        return DUpdateFrequency;
    }

    public static int UpdateFrequency(int freq){
        return DUpdateFrequency  = freq;
    }

}

