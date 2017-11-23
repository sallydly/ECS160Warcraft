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
import com.badlogic.gdx.utils.Logger;

import com.warcraftII.GameDataTypes;
import com.warcraftII.GameDataTypes.*;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.player_asset.StaticAsset;
import com.warcraftII.position.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class StaticAssetRenderer {
    private static final Logger log = new Logger("StaticAssetRenderer", 2);

    private static Map<EStaticAssetType,TextureAtlas> DStaticAssetTextures;
    private static Map<EPlayerColor, String> DPlayerColorToString;
    private static TextureAtlas animationTextures;

    private TiledMap tiledMap;
    private TiledMapTileLayer assetLayer;
    private String[] staticAssetsArray;

    private int DUpdateFrequency;

    private static int[] DConstructionStages; // Gives the max construction stage # for each type.
    private static int BuildingDeathMaxIndex = 15;


    private static Animation<TextureRegion> LargeFireAnim,SmallFireAnim, BuildingDeathAnim;
    private Map<StaticAsset, SpriteAnimation> DEffectSpriteMapping;

    //private final String mapName;


    static{ Init();}
    private static void Init() {

        DPlayerColorToString = new HashMap<EPlayerColor, String>();
        {
            DPlayerColorToString.put(EPlayerColor.None, "none-");
            DPlayerColorToString.put(EPlayerColor.Red, "red-");
            DPlayerColorToString.put(EPlayerColor.Blue, "blue-");
            DPlayerColorToString.put(EPlayerColor.Green, "green-");
            DPlayerColorToString.put(EPlayerColor.Purple, "purple-");
            DPlayerColorToString.put(EPlayerColor.Orange, "orange-");
            DPlayerColorToString.put(EPlayerColor.Yellow, "yellow-");
            DPlayerColorToString.put(EPlayerColor.Black, "black-");
            DPlayerColorToString.put(EPlayerColor.White, "white-");
        }


        DStaticAssetTextures = new HashMap<EStaticAssetType, TextureAtlas>();
        animationTextures = new TextureAtlas(Gdx.files.internal("atlas/animations.atlas"));

        Map<EStaticAssetType,String> TypeNameTranslation = new HashMap<EStaticAssetType, String>();
        {
            TypeNameTranslation.put(EStaticAssetType.GoldMine, "GoldMine");
            TypeNameTranslation.put(EStaticAssetType.TownHall, "TownHall");
            TypeNameTranslation.put(EStaticAssetType.Keep, "Keep");
            TypeNameTranslation.put(EStaticAssetType.Castle, "Castle");
            TypeNameTranslation.put(EStaticAssetType.Farm, "Farm");
            TypeNameTranslation.put(EStaticAssetType.Barracks, "Barracks");
            TypeNameTranslation.put(EStaticAssetType.LumberMill, "LumberMill");
            TypeNameTranslation.put(EStaticAssetType.Blacksmith, "Blacksmith");
            TypeNameTranslation.put(EStaticAssetType.ScoutTower, "ScoutTower");
            TypeNameTranslation.put(EStaticAssetType.GuardTower, "GuardTower");
            TypeNameTranslation.put(EStaticAssetType.CannonTower, "CannonTower");
            //TypeNameTranslation.put(EStaticAssetType.Wall, "Wall");
        }

        for (EStaticAssetType assetType : TypeNameTranslation.keySet()) {
            String filePathString = "atlas/" + TypeNameTranslation.get(assetType) + ".atlas";
            log.debug(filePathString);
            TextureAtlas Textures = new TextureAtlas(Gdx.files.internal(filePathString));
            DStaticAssetTextures.put(assetType, Textures);
        }


        DConstructionStages = new int[EStaticAssetType.values().length];

        for (EStaticAssetType sassettype : EStaticAssetType.values()) {

            DConstructionStages[GameDataTypes.to_underlying(sassettype)] = -1;

            //if (sassettype == EStaticAssetType.None || sassettype == EStaticAssetType.Max){ continue;}

            if (sassettype == EStaticAssetType.None || sassettype == EStaticAssetType.Max || sassettype == EStaticAssetType.Wall){ continue;}

            TextureAtlas atlas = DStaticAssetTextures.get(sassettype);

            String constructString;

            if (IsColored(sassettype)){
                constructString = "self-construct_";
            }
            else
            {
                constructString = "construct_";
            }

            int i = 0;

            while (true) {
                TextureRegion text = atlas.findRegion(constructString + String.valueOf(i));
                if (text != null) {
                    DConstructionStages[GameDataTypes.to_underlying(sassettype)] = i;
                    i++;
                } else {
                    break;
                }
            }
        }
    }

    private static boolean IsColored(EStaticAssetType type) {
        switch(type) {
            case None:
            case Max:
            case GoldMine:
            case Wall:
                return false;
            case TownHall:
            case Keep:
            case Castle:
            case Farm:
            case Barracks:
            case LumberMill:
            case Blacksmith:
            case ScoutTower:
            case GuardTower:
            case CannonTower:
                return true;
            default:
                return false;
        }
    }



    public StaticAssetRenderer(TiledMap tiledMap,
                               int mapWidth,
                               int mapHeight,
                               String mapName) {
        this.tiledMap = tiledMap;
        this.assetLayer = new TiledMapTileLayer(mapWidth, mapHeight, 32, 32);

        DEffectSpriteMapping = new HashMap<StaticAsset, SpriteAnimation>();

        LargeFireAnim = new Animation<TextureRegion>(0.5f,animationTextures.findRegions("large-fire"));
        SmallFireAnim = new Animation<TextureRegion>(0.5f, animationTextures.findRegions("small-fire"));
        BuildingDeathAnim = new Animation<TextureRegion>(0.5f, animationTextures.findRegions("buildingdeath"));

        //log.debug(typeName+String.valueOf(DConstructionStages[to_underlying(sassettype)] ));

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

            String colorName = DPlayerColorToString.get(player.Color());

            for (StaticAsset StatAsset : player.StaticAssets()) {
                String tileName;

                TextureAtlas textures = DStaticAssetTextures.get(StatAsset.staticAssetType());

                if (IsColored(StatAsset.staticAssetType()))
                {
                    tileName = colorName + "inactive";
                }
                else
                {
                    tileName = "inactive";
                }
                //log.info("tileName: " + tileName);
                int XPos = StatAsset.tilePosition().X();
                //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
                int YPos = map.Height() - StatAsset.tilePosition().Y() - 1; // -1 to account for 0 index
                GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
            }
        }
        return assetLayer;
    }

    public void UpdateStaticAssets(TiledMap tmap, AssetDecoratedMap map, Vector<PlayerData> playerData) {

        TiledMapTileLayer assetLayer = (TiledMapTileLayer) tmap.getLayers().get("StaticAssets");
        TiledMapTileLayer effectsLayer = (TiledMapTileLayer) tmap.getLayers().get("BuildingEffects");

        for (PlayerData player : playerData) {
            String colorName = DPlayerColorToString.get(player.Color());

            for (StaticAsset StatAsset : player.StaticAssets()) {
                String tileName, stateName;

                TextureAtlas textures = DStaticAssetTextures.get(StatAsset.staticAssetType());


                int XPos = StatAsset.tilePosition().X();
                //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
                int YPos = map.Height() - StatAsset.tilePosition().Y() - 1; // -1 to account for 0 index


                switch (StatAsset.Action()) {

                    case None:
                        stateName = "inactive";
                        if (IsColored(StatAsset.staticAssetType())){
                            tileName = colorName + stateName;
                        } else {
                            tileName = stateName;
                        }

                    GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);

                    if (StatAsset.hitPoints() < StatAsset.maxHitPoints() / 2) {
                        UnitPosition newUPos = new UnitPosition(StatAsset.tilePosition());
                        Sprite newSprite = new Sprite();
                        newSprite.setPosition(newUPos.X(), newUPos.Y());
                        newSprite.setSize(Position.tileWidth() * StatAsset.assetType().Size(), Position.tileWidth() * StatAsset.assetType().Size());
                        DEffectSpriteMapping.put(StatAsset, new SpriteAnimation(newSprite, LargeFireAnim));
                    }
                    break;


                    case Construct:
                        int ActionSteps = DConstructionStages[GameDataTypes.to_underlying(StatAsset.staticAssetType())] + 1;

                        //log.debug(StatAsset.assetType().Name() + String.valueOf(ActionSteps));
                        if (ActionSteps > 0) {
                            int TotalSteps = StatAsset.assetType().BuildTime() * UpdateFrequency();
                            int CurrentStep = StatAsset.Step() * ActionSteps / TotalSteps;


                            //log.debug( StatAsset.assetType().Name() +" Current step: " + String.valueOf(CurrentStep));
                            //log.debug( String.valueOf(StatAsset.Step())+ "/" + String.valueOf(TotalSteps));

                            if (CurrentStep == ActionSteps) {
                                CurrentStep--;
                            }

                            int ConstructTileIndex = CurrentStep;

                            stateName = "construct_" + String.valueOf(ConstructTileIndex);

                            if (IsColored(StatAsset.staticAssetType())){
                                tileName = colorName + stateName;
                            } else {
                                tileName = stateName;
                            }

                            GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
                        } else {
                            stateName = "inactive";
                            if (IsColored(StatAsset.staticAssetType())){
                                tileName = colorName + "inactive";
                            } else {
                                tileName = stateName;
                            }

                            GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
                        }
                        break;

                    case Death:

                        if (StatAsset.Step() <= BuildingDeathMaxIndex) {
                            UnitPosition newUPos = new UnitPosition(StatAsset.tilePosition());
                            Sprite newSprite = new Sprite();
                            newSprite.setPosition(newUPos.X(), newUPos.Y());
                            newSprite.setSize(Position.tileWidth() * StatAsset.assetType().Size(), Position.tileWidth() * StatAsset.assetType().Size());
                            DEffectSpriteMapping.put(StatAsset, new SpriteAnimation(newSprite, BuildingDeathAnim));
                        }
                        break;

                }
            }
        }
    }

    protected class SpriteAnimation {
        Sprite DSprite;
        Animation<TextureRegion> DAnim;

        protected SpriteAnimation(Sprite sprite, Animation<TextureRegion> anim) {
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

    public int UpdateFrequency(){
        return DUpdateFrequency;
    }

    public int UpdateFrequency(int freq){
        return DUpdateFrequency  = freq;
    }

}
