package com.warcraftII.renderer;


/*
* Adds stationary assets onto a new layer of map
*
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.warcraftII.Volume;
import com.warcraftII.player_asset.PlayerAssetType;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.player_asset.StaticAsset;
import com.warcraftII.position.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
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

    private Sound burningSound;
    private Sound[] buildingDeathSounds;


    private int DUpdateFrequency;

    private static int[] DConstructionStages; // Gives the max construction stage # for each type.

    private static Animation<TextureRegion> BuildingDeathAnim, LargeFireAnim, SmallFireAnim;
    private static Texture placeGoodTexture, placeBadTexture;

    private Map<StaticAsset, BuildingEffect> DEffectMapping;

    private StaticAsset shadowStaticAsset;
    private Sprite placementStatusBox;
    private boolean lastCanPlace;

    private List<StaticAsset> DeathRowBuildings;

    private Random rando = new Random();
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
            TypeNameTranslation.put(EStaticAssetType.Wall, "Wall");
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

            if (sassettype == EStaticAssetType.None || sassettype == EStaticAssetType.Max){ continue;}

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
        BuildingDeathAnim = new Animation<TextureRegion>(0.25f, animationTextures.findRegions("buildingdeath"), Animation.PlayMode.NORMAL);
        LargeFireAnim = new Animation<TextureRegion>(0.2f, animationTextures.findRegions("large-fire"), Animation.PlayMode.LOOP_RANDOM);
        SmallFireAnim = new Animation<TextureRegion>(0.2f, animationTextures.findRegions("small-fire"), Animation.PlayMode.LOOP_RANDOM);
        placeGoodTexture = new Texture(Gdx.files.internal("img/Marker_place-good.png"));
        placeBadTexture = new Texture(Gdx.files.internal("img/Marker_place-bad.png"));

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

        DEffectMapping = new HashMap<StaticAsset, BuildingEffect>();
        DeathRowBuildings = new ArrayList<StaticAsset>();

        burningSound = Gdx.audio.newSound(Gdx.files.internal("data/snd/misc/burning.wav"));

        buildingDeathSounds = new Sound[3];
        buildingDeathSounds[0] = Gdx.audio.newSound(Gdx.files.internal("data/snd/misc/building-explode1.wav"));
        buildingDeathSounds[1] = Gdx.audio.newSound(Gdx.files.internal("data/snd/misc/building-explode2.wav"));
        buildingDeathSounds[2] = Gdx.audio.newSound(Gdx.files.internal("data/snd/misc/building-explode3.wav"));



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
                System.out.println(XPos + " " + YPos);
                GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
            }
        }
        return assetLayer;
    }

    public boolean UpdateStaticAssets(TiledMap tmap, AssetDecoratedMap map, Vector<PlayerData> playerData) {
        boolean assetDeath = false;
        TiledMapTileLayer assetLayer = (TiledMapTileLayer) tmap.getLayers().get("StaticAssets");

        for (PlayerData player : playerData) {
            String colorName = DPlayerColorToString.get(player.Color());

            for (Iterator<StaticAsset> itr = player.StaticAssets().iterator(); itr.hasNext(); ) {
                StaticAsset StatAsset = itr.next();

                String tileName, stateName, pieceName;

                TextureAtlas textures = DStaticAssetTextures.get(StatAsset.staticAssetType());


                int XPos = StatAsset.tilePosition().X();
                //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
                int YPos = map.Height() - StatAsset.tilePosition().Y() - 1; // -1 to account for 0 index

                // Fire is drawn as long as building is not already dead.. also assuming walls dont catch on fire.
                if (StatAsset.Action() != EAssetAction.Death && StatAsset.staticAssetType() != EStaticAssetType.Wall) {
                    int HitRange = StatAsset.hitPoints() * 4 / StatAsset.maxHitPoints();

                    if (HitRange < 2) {
                        int TilesetIndex = 1 - HitRange;
                        float AdjHeight = Position.halfTileHeight() * (StatAsset.assetType().Size());
                        float AdjWidth = Position.halfTileWidth() * (StatAsset.assetType().Size() / 2 - (float) 0.5);

                        BuildingEffect currentEffect = DEffectMapping.get(StatAsset);
                        if (currentEffect == null) {
                            currentEffect = new BuildingEffect();
                        }

                        UnitPosition newUPos = new UnitPosition(StatAsset.tilePosition());

                        if (TilesetIndex == 0) {
                            if (currentEffect.DAnimName != null && currentEffect.DAnimName.equals("SmallFire")) {
                                continue;
                            }

                            Sprite newSprite = new Sprite(animationTextures.findRegion("small-fire"));
                            newSprite.setOriginCenter();
                            newSprite.setPosition(newUPos.X() + AdjWidth, newUPos.Y() - AdjHeight);
                            currentEffect.SetAnimation(newSprite, SmallFireAnim, StatAsset, "SmallFire");
                            currentEffect.LoopSound(burningSound);
                            DEffectMapping.put(StatAsset, currentEffect);
                        } else if (TilesetIndex == 1) {
                            if (currentEffect.DAnimName != null && currentEffect.DAnimName.equals("LargeFire")) {
                                continue;
                            }
                            Sprite newSprite = new Sprite(animationTextures.findRegion("large-fire"));
                            newSprite.setOriginCenter();
                            newSprite.setPosition(newUPos.X() + AdjWidth, newUPos.Y() - AdjHeight);
                            currentEffect.SetAnimation(newSprite, LargeFireAnim, StatAsset, "LargeFire");
                            currentEffect.LoopSound(burningSound);
                            DEffectMapping.put(StatAsset, currentEffect);
                        } else // HP is not in burning range
                        {
                            DEffectMapping.get(StatAsset).StopSound();
                            DEffectMapping.remove(StatAsset);
                        }
                    }
                }

                // If wall, generate the piecenum based on whether there are adjacent sections of wall:
                if (StatAsset.staticAssetType() == EStaticAssetType.Wall) {

                    //direction and encoding bit#, 0 is LSB. 3 is MSB
                    int north0, east1, south2, west3, piecenum;
                    int wallPositionX = StatAsset.tilePositionX();
                    int wallPositionY = StatAsset.tilePositionY();

                    TilePosition northPos = new TilePosition(wallPositionX, wallPositionY - 1);
                    north0 = (map.StaticAssetAt(northPos) != null && map.StaticAssetAt(northPos).staticAssetType() == EStaticAssetType.Wall) ? 1 : 0;

                    TilePosition eastPos = new TilePosition(wallPositionX + 1, wallPositionY);
                    east1 = (map.StaticAssetAt(eastPos) != null && map.StaticAssetAt(eastPos).staticAssetType() == EStaticAssetType.Wall) ? 1 : 0;

                    TilePosition southPos = new TilePosition(wallPositionX, wallPositionY + 1);
                    south2 = (map.StaticAssetAt(southPos) != null && map.StaticAssetAt(southPos).staticAssetType() == EStaticAssetType.Wall) ? 1 : 0;

                    TilePosition westPos = new TilePosition(wallPositionX - 1, wallPositionY);
                    west3 = (map.StaticAssetAt(westPos) != null && map.StaticAssetAt(westPos).staticAssetType() == EStaticAssetType.Wall) ? 1 : 0;

                    piecenum = (north0 * 1) + (east1 * 2) + (south2 * 4) + (west3 * 8);

                    if (piecenum == 5) {
                        int altPiece = wallPositionY % 2;
                        pieceName = Integer.toHexString(piecenum).toUpperCase() + "-" + Integer.toString(altPiece);
                    } else if (piecenum == 10) {
                        int altPiece = wallPositionX % 2;
                        pieceName = Integer.toHexString(piecenum).toUpperCase() + "-" + Integer.toString(altPiece);
                    } else {
                        pieceName = Integer.toHexString(piecenum).toUpperCase();
                    }
                }
                else {
                    pieceName = ""; //Initializing pieceName so compiler stops complaining
                }
                switch (StatAsset.Action()) {

                    case None:
                        if (StatAsset.staticAssetType() == EStaticAssetType.Wall) {

                            if (StatAsset.hitPoints() < StatAsset.maxHitPoints() / 2) {
                                stateName = "damaged_";
                            } else {
                                stateName = "inactive_";
                            }

                            tileName = stateName + pieceName;
                        } else { // any other static asset
                            stateName = "inactive";
                            if (IsColored(StatAsset.staticAssetType())) {
                                tileName = colorName + stateName;
                            } else {
                                tileName = stateName;
                            }

                        }

                        GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
                        break;

                    case MineGold:
                        //Should only be GoldMine
                        if (StatAsset.staticAssetType() == EStaticAssetType.GoldMine) {
                            stateName = "active";
                            tileName = stateName;
                        } else { // any other static asset
                            stateName = "inactive";
                            tileName = colorName + stateName;
                        }
                        GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
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

                            if (IsColored(StatAsset.staticAssetType())) {
                                tileName = colorName + stateName;
                            } else {
                                tileName = stateName;
                            }

                            GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
                        } else {
                            stateName = "inactive";
                            if (IsColored(StatAsset.staticAssetType())) {
                                tileName = colorName + "inactive";
                            } else {
                                tileName = stateName;
                            }

                            GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
                        }
                        break;

                    case Death:
                        if (StatAsset.staticAssetType() == EStaticAssetType.Wall) {
                            stateName = "destroyed_";
                            tileName = stateName + pieceName;
                            GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);
                            //Wall's StaticAsset is not removed because the rubble is not traversable/buildable

                        } else { // for all non-wall static assets
                            BuildingEffect currentEffect = DEffectMapping.get(StatAsset);
                            if (currentEffect == null) {
                                currentEffect = new BuildingEffect();
                            }

                            currentEffect.PlaySound(buildingDeathSounds[rando.nextInt(3)]);

                            GraphicTileset.RemoveTile(assetLayer,XPos,YPos,StatAsset.Size());

                            UnitPosition newUPos = new UnitPosition(StatAsset.tilePosition());
                            Sprite newSprite = new Sprite();
                            newSprite.setPosition(newUPos.X() - Position.halfTileWidth(), newUPos.Y() - StatAsset.assetType().Size() * Position.tileHeight());
                            newSprite.setSize(Position.tileWidth() * StatAsset.assetType().Size(), Position.tileWidth() * StatAsset.assetType().Size());
                            currentEffect.SetAnimation(newSprite, BuildingDeathAnim, StatAsset, "BuildingDeath");

                            DEffectMapping.put(StatAsset, currentEffect);
                            DeathRowBuildings.add(StatAsset);
                            itr.remove();
                            map.RemoveStaticAsset(StatAsset);
                            assetDeath = true;
                        }
                        break;

                }
            }
        }
        return assetDeath;
    }

    protected class BuildingEffect {
        protected Sprite DSprite;
        protected Animation<TextureRegion> DAnim;
        protected StaticAsset DBuilding;
        protected float DElapsedTime;
        protected String DAnimName;
        protected long DSoundID;
        protected Sound DCurrentSound;

        protected BuildingEffect(){}


        protected void SetAnimation(Sprite sprite, Animation<TextureRegion> anim, StaticAsset building, String name) {
            DSprite = sprite;
            DAnim = anim;
            DBuilding = building;
            DAnimName = name;
            DElapsedTime = 0;
        }

        protected void LoopSound(Sound sound) {
            if (DCurrentSound != null)
            {
                DCurrentSound.stop(DSoundID);
            }
            DCurrentSound = sound;
            DSoundID = sound.loop();
        }

        protected void PlaySound(Sound sound) {
            if (DCurrentSound != null)
            {
                DCurrentSound.stop(DSoundID);
            }
            DCurrentSound = sound;
            DSoundID = sound.play(Volume.getFxVolume()/100);
        }

        protected void StopSound(){
            DCurrentSound.stop(DSoundID);
            DCurrentSound = null;
        }
    }

// To be called in the render() step of singleplayer, between sb.begin() and sb.end()
    public void DrawEffects(SpriteBatch sb, float deltaTime) {
        if (placementStatusBox != null){
            Texture placeTexture = lastCanPlace ? placeGoodTexture : placeBadTexture;
            sb.draw(placeTexture,placementStatusBox.getX(),placementStatusBox.getY(),
                    placementStatusBox.getWidth(), placementStatusBox.getHeight()) ;
        }

        for(Iterator<Map.Entry<StaticAsset, BuildingEffect>> it = DEffectMapping.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<StaticAsset, BuildingEffect> entry = it.next();

            BuildingEffect buildEff = entry.getValue();
            StaticAsset sasset = entry.getKey();

            buildEff.DElapsedTime += deltaTime;

            sb.draw(buildEff.DAnim.getKeyFrame(buildEff.DElapsedTime), buildEff.DSprite.getX(), buildEff.DSprite.getY(),
                    buildEff.DSprite.getWidth(),buildEff.DSprite.getHeight());

            if(buildEff.DAnim.isAnimationFinished(buildEff.DElapsedTime) && DeathRowBuildings.contains(sasset)) {
                DeathRowBuildings.remove(sasset);
                it.remove();
            }
        }
    }


    public void CreateShadowAsset(EStaticAssetType type, EPlayerColor color, TilePosition pos, TiledMap tmap, AssetDecoratedMap map){
        TiledMapTileLayer assetLayer = (TiledMapTileLayer) tmap.getLayers().get("StaticAssets");

        shadowStaticAsset = PlayerAssetType.ConstructStaticAsset(PlayerAssetType.TypeToName(GameDataTypes.to_assetType(type)));
        shadowStaticAsset.owner(color);
        shadowStaticAsset.tilePosition(pos);

        String colorName = DPlayerColorToString.get(color);
        TextureAtlas textures = DStaticAssetTextures.get(shadowStaticAsset.staticAssetType());

        String tileName;

        if (IsColored(shadowStaticAsset.staticAssetType()))
        {
            tileName = colorName + "place";
        }
        else
        {
            tileName = "place";
        }
        //log.info("tileName: " + tileName);
        int XPos = shadowStaticAsset.tilePosition().X();
        //flipping Y because TiledMap sets (0,0) as bottom left, while game files think of (0,0) as top left
        int YPos = map.Height() - shadowStaticAsset.tilePosition().Y() - 1; // -1 to account for 0 index
        GraphicTileset.DrawTile(textures, assetLayer, XPos, YPos, tileName);

        UnitPosition newUPos = new UnitPosition(shadowStaticAsset.tilePosition());
        placementStatusBox = new Sprite();
        placementStatusBox.setPosition(newUPos.X() - Position.halfTileWidth(), newUPos.Y() - shadowStaticAsset.assetType().Size() * Position.tileHeight());
        placementStatusBox.setSize(Position.tileWidth() * shadowStaticAsset.assetType().Size(), Position.tileWidth() * shadowStaticAsset.assetType().Size());

        lastCanPlace = map.CanPlaceStaticAsset(shadowStaticAsset.tilePosition(), shadowStaticAsset.Size());
    }

    public boolean MoveShadowAsset(TilePosition pos, TiledMap tmap, AssetDecoratedMap map){
        if (shadowStaticAsset != null ) {
            TiledMapTileLayer assetLayer = (TiledMapTileLayer) tmap.getLayers().get("StaticAssets");

            int XPos = shadowStaticAsset.tilePosition().X();
            int YPos = map.Height() - shadowStaticAsset.tilePosition().Y() - 1; // -1 to account for 0 index
            GraphicTileset.RemoveTile(assetLayer, XPos, YPos, shadowStaticAsset.Size());

            shadowStaticAsset.tilePosition(pos);
            int newXPos = shadowStaticAsset.tilePosition().X();
            int newYPos = map.Height() - shadowStaticAsset.tilePosition().Y() - 1; // -1 to account for 0 index

            String colorName = DPlayerColorToString.get(shadowStaticAsset.owner());
            TextureAtlas textures = DStaticAssetTextures.get(shadowStaticAsset.staticAssetType());

            String tileName;

            if (IsColored(shadowStaticAsset.staticAssetType())) {
                tileName = colorName + "place";
            } else {
                tileName = "place";
            }
            GraphicTileset.DrawTile(textures, assetLayer, newXPos, newYPos, tileName);

            UnitPosition newUPos = new UnitPosition(shadowStaticAsset.tilePosition());
            placementStatusBox.setPosition(newUPos.X() - Position.halfTileWidth(), newUPos.Y() - shadowStaticAsset.assetType().Size() * Position.tileHeight());
            placementStatusBox.setSize(Position.tileWidth() * shadowStaticAsset.assetType().Size(), Position.tileWidth() * shadowStaticAsset.assetType().Size());

            lastCanPlace = map.CanPlaceStaticAsset(shadowStaticAsset.tilePosition(), shadowStaticAsset.Size());
            return lastCanPlace;
        }
        return false;
    }

    public void DestroyShadowAsset(TiledMap tmap, AssetDecoratedMap map) {
        if (shadowStaticAsset != null ) {
            TiledMapTileLayer assetLayer = (TiledMapTileLayer) tmap.getLayers().get("StaticAssets");

            int XPos = shadowStaticAsset.tilePosition().X();
            int YPos = map.Height() - shadowStaticAsset.tilePosition().Y() - 1; // -1 to account for 0 index
            GraphicTileset.RemoveTile(assetLayer, XPos, YPos, shadowStaticAsset.Size());
        }
        shadowStaticAsset = null;
        placementStatusBox = null;
    }


    public int UpdateFrequency(){
        return DUpdateFrequency;
    }

    public int UpdateFrequency(int freq){
        return DUpdateFrequency  = freq;
    }

}
