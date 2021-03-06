package com.warcraftII.units;

/**
 * Created by Ian on 10/29/2017.
 * Is the basis for all units.
 */
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.warcraftII.GameData;
import com.warcraftII.GameDataTypes;
import com.warcraftII.Volume;
import com.warcraftII.player_asset.PlayerAssetType;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.player_asset.StaticAsset;
import com.warcraftII.position.CameraPosition;
import com.warcraftII.position.Position;
import com.warcraftII.position.TilePosition;
import com.warcraftII.position.UnitPosition;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.terrain_map.TileTypes;

//import org.omg.CORBA.UNKNOWN;

import static com.warcraftII.GameDataTypes.EAssetCapabilityType.*;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class Unit {
    //public Vector<IndividualUnit> unitVector;
    public Vector<IndividualUnit> deleteUnits;
    public Map<GameDataTypes.EPlayerColor, Vector<IndividualUnit>> unitMap;
    public int selectedUnitIndex;
    public Vector<IndividualUnit> allUnits = new Vector<IndividualUnit>();
    private Map<GameDataTypes.EUnitType, TextureAtlas> unitTextures;

    protected Sound[] peasantAckSounds = new Sound[4];
    protected Sound[] knightAckSounds = new Sound[4];
    protected Sound[] archerAckSounds = new Sound[4];

    protected Sound[] peasantSelSounds = new Sound[4];
    protected Sound[] knightSelSounds = new Sound[4];
    protected Sound[] archerSelSounds = new Sound[4];

    protected Sound[] swordSounds = new Sound[3];
    protected Sound[] bowSounds = new Sound[2];

    protected Sound[] treeSounds = new Sound[4];
    protected Sound[] stoneSounds = new Sound[4];


    protected Random rando = new Random();

    public Unit() {
        //unitVector = new Vector<IndividualUnit>(50);
        unitMap = new HashMap<GameDataTypes.EPlayerColor, Vector<IndividualUnit>>();
        unitTextures = new HashMap<GameDataTypes.EUnitType, TextureAtlas>();
        deleteUnits = new Vector<IndividualUnit>(20);

        for (GameDataTypes.EPlayerColor c : GameDataTypes.EPlayerColor.values()) {
            unitMap.put(c, new Vector<IndividualUnit>());
        }

        unitTextures.put(GameDataTypes.EUnitType.Archer, new TextureAtlas(Gdx.files.internal("atlas/Archer.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Footman, new TextureAtlas(Gdx.files.internal("atlas/Footman.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Peasant, new TextureAtlas(Gdx.files.internal("atlas/Peasant.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Ranger, new TextureAtlas(Gdx.files.internal("atlas/Ranger.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Knight, new TextureAtlas(Gdx.files.internal("atlas/Knight.atlas")));

        for(int i = 0; i < 4; i++){
            peasantAckSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/peasant/acknowledge"+String.valueOf(i+1)+".wav"));
            knightAckSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/knight/acknowledge"+String.valueOf(i+1)+".wav"));
            archerAckSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/archer/acknowledge"+String.valueOf(i+1)+".wav"));
            peasantSelSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/peasant/selected"+String.valueOf(i+1)+".wav"));
            knightSelSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/knight/selected"+String.valueOf(i+1)+".wav"));
            archerSelSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/archer/selected"+String.valueOf(i+1)+".wav"));

            treeSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/misc/tree"+String.valueOf(i+1)+".wav"));
            stoneSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/misc/stone"+String.valueOf(i+1)+".wav"));

            if (i < 3){
                swordSounds[i] = Gdx.audio.newSound(Gdx.files.internal("snd/misc/sword"+String.valueOf(i+1)+".wav"));
            }
        }

        bowSounds[0] = Gdx.audio.newSound(Gdx.files.internal("snd/misc/bowfire.wav"));
        bowSounds[1] = Gdx.audio.newSound(Gdx.files.internal("snd/misc/bowhit.wav"));



    }

    public class IndividualUnit extends Actor {

        public GameDataTypes.EUnitType unitClass;
        public int maxHP = 40;
        public int curHP = 40;
        public int attackDamage = 3;
        public int piercingDamage = 6;
        public int speed = 10;
        public int range = 4;
        public int armor = 0;
        public int sight = 0;
        public int attackTime = 10;
        public int reloadTime = 10;
        public boolean isVisible = true;
        // Gold Cost
        // Lumber Cost
        // Stone Cost
        // Build Time
        // Attack Steps
        // Reload Steps
        // I think the above should be included in the buildings?
        // Or we can include it as requirements for addUnit, but that makes init harder
        public int foodConsumed = 1;
        public boolean hidden = false;

        public Sound[] ackSounds;
        public Sound[] selSounds;
        public Sound[] attackSounds;

        //public boolean selected = false;
        public boolean touched = false;
        public float currentxmove;
        public float currentymove;
        public float patrolxmove;
        public float patrolymove;

        public TilePosition buildPos = null;
        public StaticAsset inProgressBuilding = null;
        public int resourceAmount = 0;
        public GameDataTypes.EStaticAssetType toBuild =  null;

        public StaticAsset selectedAsset;
        public StaticAsset targetBuilding;
        public TilePosition selectedTilePosition;


        public boolean attackEnd = true;
        public float frameTime = 0.1f;
        public float animStart = 0;
        public IndividualUnit target;
        public GameDataTypes.EUnitState curState;
        public Animation<TextureRegion> curAnim;
        public TextureRegion curTexture;
        public Vector<GameDataTypes.EAssetCapabilityType> abilities;
        public GameDataTypes.EPlayerColor color;
        public GameDataTypes.EDirection direction;

        public String getDirection() {
            if (currentxmove == getMidX()) { // straight up or down
                if (currentymove <= getMidY()) { // if south
                    return "s";
                } else { // if north
                    return "n";
                }
            } else if (currentxmove <= getMidX()) { // if to the West
                System.out.println("currentxmove: "+currentxmove+"; currentymove:"+currentymove);
                if (currentymove == getMidY()) { // straight west
                    return "w";
                } else if (currentymove <= getMidY()) { // south west
                    return "sw";
                } else { // north west
                    return "nw";
                }
            } else { // if currentxmove > sprite OriginX
                if (currentymove == getMidY()) { // straight east
                    return "e";
                } else if (currentymove <= getMidY()) { // south east
                    return "se";
                } else { // south west
                    return "sw";
                }
            }
        }

        public void stopMovement() {
            curAnim = new Animation<TextureRegion>(frameTime, unitTextures.get(unitClass).findRegion(GameDataTypes.toString(color)+"-walk-"+GameDataTypes.toAbbr(direction), 0));
            curTexture = curAnim.getKeyFrame(0, false);
            curState = GameDataTypes.EUnitState.Idle;
        }

        public float getMidX() {
            return getX()+(getWidth()/2);
        }

        public float getMidY() {
            return getY()+(getHeight()/2);
        }

        @Override
        public void draw (Batch batch, float parentAlpha) {
            if (inProgressBuilding == null && !hidden && isVisible) {
                batch.draw(curTexture, getX(), getY());
            }
        }

        @Override
        public void act (float delta) {

            // TODO: Make this take over the unitstatehandler function maybe?
            //if (curState == GameDataTypes.EUnitState.Dead) {
            //    remove();
            //}
        }

        public boolean hasCapability(GameDataTypes.EAssetCapabilityType capabilityType) {
            return this.abilities.contains(capabilityType);
        }

    }

    public IndividualUnit AddUnit(float x_position, float y_position, GameDataTypes.EUnitType inUnit, GameDataTypes.EPlayerColor inColor) {
        final IndividualUnit newUnit = new IndividualUnit();
        newUnit.abilities = new Vector<GameDataTypes.EAssetCapabilityType>(5);
        TextureAtlas unitAtlas = unitTextures.get(inUnit);
        TextureRegion texture;
        Animation<TextureRegion> anim;
        newUnit.color = inColor;
        texture = unitAtlas.findRegion(GameDataTypes.toString(newUnit.color) + "-walk-n");
        anim = new Animation<TextureRegion>(0.1f, unitAtlas.findRegion(GameDataTypes.toString(newUnit.color) + "-walk-n", 0));
        newUnit.abilities.add(Move);
        newUnit.abilities.add(Attack);
        newUnit.abilities.add(StandGround);
        switch(inUnit) {
            case Peasant:
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.Mine);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.Convey);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.Repair);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildSimple);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildTownHall);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildFarm);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildBarracks);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildLumberMill);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildScoutTower);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildBlacksmith);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.BuildWall);
                newUnit.unitClass = GameDataTypes.EUnitType.Peasant;
                newUnit.curHP = 30;
                newUnit.maxHP = 30;
                newUnit.armor = 0;
                newUnit.sight = 4;
                newUnit.speed = 10;
                newUnit.attackTime = 10;
                newUnit.reloadTime = 0;
                newUnit.attackDamage = 3;
                newUnit.piercingDamage = 2;
                newUnit.range = 1;
                newUnit.foodConsumed = 1;
                newUnit.ackSounds = peasantAckSounds;
                newUnit.selSounds = peasantSelSounds;
                newUnit.attackSounds = treeSounds;
                break;
            case Footman:
                newUnit.abilities.add(Patrol);
                newUnit.unitClass = GameDataTypes.EUnitType.Footman;
                newUnit.curHP = 60;
                newUnit.maxHP = 60;
                newUnit.armor = 2;
                newUnit.sight = 4;
                newUnit.speed = 10;
                newUnit.attackTime = 10;
                newUnit.reloadTime = 0;
                newUnit.attackDamage = 6;
                newUnit.piercingDamage = 3;
                newUnit.range = 1;
                newUnit.foodConsumed = 1;
                newUnit.ackSounds = knightAckSounds;
                newUnit.selSounds = knightSelSounds;
                newUnit.attackSounds = swordSounds;
                break;
            case Archer:
                newUnit.abilities.add(Patrol);
                newUnit.unitClass = GameDataTypes.EUnitType.Archer;
                newUnit.curHP = 40;
                newUnit.maxHP = 40;
                newUnit.armor = 2;
                newUnit.sight = 5;
                newUnit.speed = 10;
                newUnit.attackTime = 10;
                newUnit.reloadTime = 10;
                newUnit.attackDamage = 3;
                newUnit.piercingDamage = 6;
                newUnit.range = 4;
                newUnit.foodConsumed = 1;
                newUnit.ackSounds = archerAckSounds;
                newUnit.selSounds = archerSelSounds;
                newUnit.attackSounds = bowSounds;
                break;
            case Ranger:
                newUnit.abilities.add(Patrol);
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.RangerScouting);
                newUnit.unitClass = GameDataTypes.EUnitType.Ranger;
                newUnit.curHP = 50;
                newUnit.maxHP = 50;
                newUnit.armor = 2;
                newUnit.sight = 5;
                newUnit.speed = 10;
                newUnit.attackTime = 10;
                newUnit.reloadTime = 10;
                newUnit.attackDamage = 3;
                newUnit.piercingDamage = 6;
                newUnit.range = 4;
                newUnit.foodConsumed = 1;
                newUnit.ackSounds = archerAckSounds;
                newUnit.selSounds = archerSelSounds;
                newUnit.attackSounds = bowSounds;
                break;
            case Knight:
                newUnit.abilities.add(Patrol);
                newUnit.unitClass = GameDataTypes.EUnitType.Knight;
                newUnit.curHP = 70;
                newUnit.maxHP = 70;
                newUnit.armor = 3;
                newUnit.sight = 4;
                newUnit.speed = 10;
                newUnit.attackTime = 10;
                newUnit.reloadTime = 0;
                newUnit.attackDamage = 6;
                newUnit.piercingDamage = 4;
                newUnit.range = 1;
                newUnit.foodConsumed = 1;
                newUnit.ackSounds = knightAckSounds;
                newUnit.selSounds = knightSelSounds;
                newUnit.attackSounds = swordSounds;
                break;
            default:
                newUnit.unitClass = GameDataTypes.EUnitType.Peasant;
                break;
        }

        newUnit.setX(x_position - texture.getRegionWidth());
        newUnit.setY(y_position - texture.getRegionHeight());
        newUnit.currentxmove = x_position*32;
        newUnit.currentymove = y_position*32;
        newUnit.curState = GameDataTypes.EUnitState.Idle;
        newUnit.curAnim = anim;
        newUnit.curTexture = texture;
        newUnit.direction = GameDataTypes.EDirection.North;

        newUnit.setTouchable(Touchable.enabled);
        newUnit.setWidth(texture.getRegionWidth());
        newUnit.setHeight(texture.getRegionHeight());
        newUnit.setBounds(newUnit.getX(), newUnit.getY(), newUnit.getWidth(), newUnit.getHeight());
        //newUnit.setDebug(true);
        newUnit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                newUnit.touched = true;
                System.out.println("Unit Listener");
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("I swear to fuck if this works");
            }
        });

        AddToMap(newUnit);

        return newUnit;
    }

    public IndividualUnit AddUnit(TilePosition tpos, GameDataTypes.EUnitType inUnit, GameDataTypes.EPlayerColor color){
        UnitPosition upos = new UnitPosition(tpos);
        // added fudge factor of half of unit tile height...
        return AddUnit((float)upos.X(), (float)upos.Y()+72/2, inUnit, color);
    }

    public void AddToMap(IndividualUnit in) {
        unitMap.get(in.color).add(in);
    }

    public void RemoveFromMap(IndividualUnit in) {
        unitMap.get(in.color).remove(in);
    }

    public void UnitStateHandler(float elapsedTime, GameData gData) {
        Vector<IndividualUnit> toDelete = new Vector<IndividualUnit>();
        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            for (IndividualUnit cur : unitMap.get(color)) {
                switch (cur.curState) {
                    case Idle:
                        break;
                    case Move:
                        UnitMoveState(cur, elapsedTime, gData);
                        break;
                    case Attack:
                        UnitAttackState(cur, cur.target, elapsedTime, gData);
                        break;
                    case AttackBuilding:
                        UnitAttackBuildingState(cur, cur.targetBuilding, elapsedTime, gData);
                        break;
                    case Patrol:
                        UnitPatrolState(cur, elapsedTime, gData);
                        break;
                    case Mine:
                        UnitMineState(cur, elapsedTime, gData);
                        break;
                    case Lumber:
                        UnitLumberState(cur, elapsedTime, gData);
                        break;
                    case ReturnLumber:
                        UnitReturnLumberState(cur, elapsedTime, gData);
                        break;
                    case ReturnMine:
                        UnitReturnMineState(cur, elapsedTime, gData);
                        break;
                    case Stone:
                        UnitStoneState(cur, elapsedTime, gData);
                        break;
                    case ReturnStone:
                        UnitReturnStoneState(cur, elapsedTime, gData);
                        break;
                    case Repair:
                        UnitRepairState(cur, elapsedTime, gData);
                        break;
                    case Dead:
                        if (UnitDeadState(cur, elapsedTime, gData)) {
                            cur.isVisible = false;
                            toDelete.add(cur);
                        }
                        break;
                    case BuildSimple:
                        UnitBuild(cur, cur.inProgressBuilding, elapsedTime, gData);
                        break;
                    default:
                        System.out.println("Invalid state");
                }
            }
        }
        for (IndividualUnit cur : toDelete) {
            RemoveFromMap(cur);
        }
        toDelete.removeAllElements();
    }
    private void UnitReturnStoneState(IndividualUnit cur, float totalTime, GameData gData) {
        if (InRange(cur, new UnitPosition(cur.currentxmove, cur.currentymove), PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.Blacksmith)*Position.tileWidth(),gData)) {
            cur.stopMovement();
            gData.playerData.get(GameDataTypes.to_underlying(cur.color)).IncrementStone(cur.resourceAmount);
            cur.resourceAmount = 0;
            cur.abilities.remove(GameDataTypes.EAssetCapabilityType.CarryingStone);
            cur.curState = GameDataTypes.EUnitState.Stone;
            UnitPosition temp = new UnitPosition(cur.selectedTilePosition);
            cur.currentxmove = temp.X();//+(Position.tileWidth()/2);
            cur.currentymove = temp.Y();//+(Position.tileHeight()/2);
        } else {
            cur.curTexture = cur.curAnim.getKeyFrame(totalTime, true);
            UnitMove(cur, "gold", totalTime, gData);
        }
    }

    private void UnitReturnMineState(IndividualUnit cur, float totalTime, GameData gData) {
        if (InRange(cur, new UnitPosition(cur.currentxmove, cur.currentymove), PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)*Position.tileHeight(),gData)) {
            gData.playerData.get(GameDataTypes.to_underlying(cur.color)).IncrementGold(cur.resourceAmount);
            cur.resourceAmount = 0;
            cur.abilities.remove(GameDataTypes.EAssetCapabilityType.CarryingGold);
            cur.curState = GameDataTypes.EUnitState.Mine;
            UnitPosition temp = new UnitPosition(cur.selectedAsset.tilePosition());
            cur.currentxmove = temp.X();//+(Position.tileWidth()/2);
            cur.currentymove = temp.Y();//+(Position.tileHeight()/2);
        } else {
            cur.curTexture = cur.curAnim.getKeyFrame(totalTime, true);
            UnitMove(cur, "gold", totalTime, gData);
        }
    }

    private void UnitReturnLumberState(IndividualUnit cur, float totalTime, GameData gData) {
        if (InRange(cur, new UnitPosition(cur.currentxmove, cur.currentymove), PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)*Position.tileHeight(),gData)) {
            gData.playerData.get(GameDataTypes.to_underlying(cur.color)).IncrementLumber(cur.resourceAmount);
            cur.resourceAmount = 0;
            cur.abilities.remove(GameDataTypes.EAssetCapabilityType.CarryingLumber);
            cur.curState = GameDataTypes.EUnitState.Lumber;
            UnitPosition temp = new UnitPosition(cur.selectedTilePosition);
            cur.currentxmove = temp.X();//+(Position.tileWidth()/2);
            cur.currentymove = temp.Y();//+(Position.tileHeight()/2);
        } else {
            cur.curTexture = cur.curAnim.getKeyFrame(totalTime, true);
            UnitMove(cur, "lumber", totalTime, gData);
        }
    }

    private void UnitMineState(IndividualUnit cur, float totalTime, GameData gData) {
        if (InRange(cur, new UnitPosition(round(cur.currentxmove), round(cur.currentymove)), PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.GoldMine)*Position.tileWidth(), gData)) {
            if (cur.attackEnd) {
                cur.selectedAsset.StartMining();
                cur.hidden = true;
                cur.attackEnd = false;
                cur.animStart = totalTime;
            }
            if (totalTime-cur.animStart >= 2) {
                cur.selectedAsset.EndMining();
                cur.resourceAmount += 100;
                cur.hidden = false;
                cur.abilities.add(CarryingGold);
                cur.curAnim = GenerateAnimation(cur, "gold");
                cur.attackEnd = true;
                if (SetReturnDest(cur,CarryingGold, totalTime, gData)) {
                    cur.curState = GameDataTypes.EUnitState.ReturnMine;
                } else {
                    System.out.println("No where to drop off resources, going Idle");
                    cur.curState = GameDataTypes.EUnitState.Idle;
                    cur.stopMovement();
                }
            }
        } else {
            UnitMove(cur, totalTime, gData);
        }
    }

    private void UnitLumberState(IndividualUnit cur, float totalTime, GameData gData) {
        if ((InRange(cur, new UnitPosition(round(cur.currentxmove), round(cur.currentymove)), PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.GoldMine)*Position.tileWidth(), gData))) {
            gData.RemoveLumber(cur.selectedTilePosition, new TilePosition(new UnitPosition(round(cur.getMidX()), round(cur.getMidY()))), 100);
            cur.resourceAmount += 100;

            int SoundToPlay = rando.nextInt(treeSounds.length - 1);
            treeSounds[SoundToPlay].play(Volume.getFxVolume()/100);

            if (gData.map.TerrainTileType(cur.selectedTilePosition) == TileTypes.ETerrainTileType.Forest || gData.map.TerrainTileType(cur.selectedTilePosition) == TileTypes.ETerrainTileType.ForestPartial) {
                cur.abilities.add(GameDataTypes.EAssetCapabilityType.CarryingLumber);
                cur.curAnim = GenerateAnimation(cur, "lumber");
                cur.curTexture = cur.curAnim.getKeyFrame(totalTime, false);
                if (SetReturnDest(cur,CarryingLumber, totalTime, gData)) {
                    cur.curState = GameDataTypes.EUnitState.ReturnLumber;
                } else {
                    System.out.println("No where to drop off resources, going Idle");
                    cur.curState = GameDataTypes.EUnitState.Idle;
                    cur.stopMovement();
                }
            } else {
                cur.stopMovement();
            }

        } else {
            UnitMove(cur, totalTime, gData);
        }
    }

    private void UnitStoneState(IndividualUnit cur, float totalTime, GameData gData) {
        if ((InRange(cur, new UnitPosition(round(cur.currentxmove), round(cur.currentymove)), PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.GoldMine)*Position.tileWidth(), gData))) {
            gData.RemoveStone(cur.selectedTilePosition, cur.selectedTilePosition, 10);
            cur.resourceAmount += 10;

            int SoundToPlay = rando.nextInt(stoneSounds.length - 1);
            stoneSounds[SoundToPlay].play(Volume.getFxVolume()/100);

            if (gData.map.TerrainTileType(cur.selectedTilePosition) == TileTypes.ETerrainTileType.Rock || gData.map.TerrainTileType(cur.selectedTilePosition) == TileTypes.ETerrainTileType.RockPartial) {
                cur.abilities.add(GameDataTypes.EAssetCapabilityType.CarryingStone);
                cur.curAnim = GenerateAnimation(cur, "gold");
                cur.curTexture = cur.curAnim.getKeyFrame(totalTime, false);
                if (SetReturnDest(cur,CarryingStone, totalTime, gData)) {
                    cur.curState = GameDataTypes.EUnitState.ReturnStone;
                } else {
                    System.out.println("No where to drop off resources, going Idle");
                    cur.curState = GameDataTypes.EUnitState.Idle;
                    cur.stopMovement();
                }
            } else {
                cur.stopMovement();
            }

        } else {
            UnitMove(cur, totalTime, gData);
        }
    }


    //changes based on what is being carried.
    private boolean SetReturnDest(IndividualUnit cur, GameDataTypes.EAssetCapabilityType carryType, float totalTime, GameData gData){
        UnitPosition upos = new UnitPosition(cur.selectedTilePosition);
        switch(carryType){

            case CarryingGold:
                StaticAsset nearestKeep = gData.map.FindNearestStaticAsset(upos, cur.color, GameDataTypes.EStaticAssetType.Keep);
                if(SetReturnDest(cur,nearestKeep,totalTime,gData)){
                    return SetReturnDest(cur,nearestKeep,totalTime,gData);
                }
                else {
                    StaticAsset nearestTownHall = gData.map.FindNearestStaticAsset(upos, cur.color, GameDataTypes.EStaticAssetType.TownHall);
                    return SetReturnDest(cur,nearestTownHall,totalTime,gData);

                }
            case CarryingLumber:
                StaticAsset nearestLumberMill = gData.map.FindNearestStaticAsset(upos, cur.color, GameDataTypes.EStaticAssetType.LumberMill);
                return SetReturnDest(cur,nearestLumberMill,totalTime,gData);
            case CarryingStone:
                StaticAsset nearestBlacksmith = gData.map.FindNearestStaticAsset(upos, cur.color, GameDataTypes.EStaticAssetType.Blacksmith);
                return SetReturnDest(cur,nearestBlacksmith,totalTime,gData);
            default:
                return false;
        }
    }

    private boolean SetReturnDest(IndividualUnit cur, StaticAsset nearestAsset, float totalTime, GameData gData) {

       // UnitPosition upos = new UnitPosition(cur.selectedTilePosition);
//
//        if (nearestKeep != null && nearestTownHall != null) {
//            if (distanceBetweenCoords((nearestKeep.positionX()+PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.Keep)/2),
//                    (nearestKeep.positionY()+PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.Keep)/2),
//                    round(cur.getMidX()),
//                    round(cur.getMidY()))
//                    >=
//                    distanceBetweenCoords((nearestTownHall.positionX()+PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)/2),
//                            (nearestTownHall.positionY()+PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)/2),
//                            round(cur.getMidX()),
//                            round(cur.getMidY()))) {
//                UnitPosition temp = new UnitPosition(nearestKeep.tilePosition());
//                cur.currentxmove = temp.X()+(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.Keep)/2*Position.tileWidth());
//                cur.currentymove = temp.Y()-(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.Keep)/2*Position.tileWidth());
//            } else {
//                UnitPosition temp = new UnitPosition(nearestTownHall.tilePosition());
//                cur.currentxmove = temp.X()+(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)/2*Position.tileWidth());
//                cur.currentymove = temp.Y()-(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)/2*Position.tileWidth());
//            }
//        } else if (nearestKeep != null) {
//            UnitPosition temp = new UnitPosition(nearestKeep.tilePosition());
//            cur.currentxmove = temp.X()+(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.Keep)/2*Position.tileWidth());
//            cur.currentymove = temp.Y()-(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.Keep)/2*Position.tileWidth());
//        } else if (nearestTownHall != null) {
//            UnitPosition temp = new UnitPosition(nearestTownHall.tilePosition());
//            cur.currentxmove = temp.X()+(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)/2*Position.tileWidth());
//            cur.currentymove = temp.Y()-(PlayerAssetType.StaticAssetSize(GameDataTypes.EStaticAssetType.TownHall)/2*Position.tileWidth());
//        } else {
//            return false;
//        }
//        return true;
        if (nearestAsset != null){
            UnitPosition temp = new UnitPosition(nearestAsset.tilePosition());
            cur.currentxmove = temp.X()+(nearestAsset.Size()/2*Position.tileWidth());
            cur.currentymove = temp.Y()-(nearestAsset.Size()/2*Position.tileWidth());
            return true;
        }
        else{
            return false;
        }
    }

    private boolean InRange(IndividualUnit cur, UnitPosition target, GameData gData) {
        return (distanceBetweenCoords(round(cur.getMidX()), round(cur.getMidY()), target.X(), target.Y()) <= cur.range * Position.tileWidth() + 72/2);
    }

    private void UnitRepairState(IndividualUnit cur, float deltaTime, GameData gData) {
        if (UnitMove(cur, deltaTime, gData)) {
            if (cur.selectedAsset.hitPoints() <= cur.selectedAsset.maxHitPoints()) {
                // TODO Repair Animation here and delay
                cur.selectedAsset.incrementHitPoints(10);
            }
            else {
                cur.curState = GameDataTypes.EUnitState.Idle;
            }
        }
    }

    private boolean InRange(IndividualUnit cur, UnitPosition target, float buildWidth, GameData gData) {
        return (distanceBetweenCoords(round(cur.getMidX()), round(cur.getMidY()), target.X(), target.Y()) <= sqrt(2)*buildWidth*.5); // or (sqrt(2)*buildWidth)*.5
    }

    private boolean InRange(IndividualUnit cur, IndividualUnit tar, GameData gData) {
        return InRange(cur, new UnitPosition(round(tar.getMidX()), round(tar.getMidY())), gData);
    }

    /*
    private boolean InRange(IndividualUnit cur, StaticAsset target, GameData gData) {
        UnitPosition temp = new UnitPosition(target.tilePosition());
        int halfAssetSize = PlayerAssetType.StaticAssetSize(target.staticAssetType())/2;
        return (distanceBetweenCoords(round(cur.getMidX()), round(cur.getMidY()), temp.X()+halfAssetSize, temp.Y()+halfAssetSize)  <= (cur.range * Position.tileWidth()) + halfAssetSize);
    }
    */
    private void UnitPatrolState(IndividualUnit cur, float totalTime, GameData gData) {
        if (UnitMove(cur, totalTime, gData)) {
            float tempxmove = cur.currentxmove;
            float tempymove = cur.currentymove;
            cur.currentxmove = cur.patrolxmove;
            cur.currentymove = cur.patrolymove;
            cur.patrolxmove = tempxmove;
            cur.patrolymove = tempymove;
        }
    }

    public double distanceBetweenCoords(int x1, int y1, int x2, int y2) {
        return abs(sqrt(pow((x1-x2), 2)  + pow((y1-y2), 2)));
    }

    private Animation<TextureRegion> GenerateAnimation(IndividualUnit cur, String middle) {
        return new Animation<TextureRegion>(cur.frameTime, unitTextures.get(cur.unitClass).findRegions(GameDataTypes.toString(cur.color)+"-"+middle+"-"+GameDataTypes.toAbbr(cur.direction)));
    }

    private Animation<TextureRegion> GenerateDeathAnimation(IndividualUnit cur) {
        return new Animation<TextureRegion>(cur.frameTime, unitTextures.get(cur.unitClass).findRegions(GameDataTypes.toString(cur.color)+"-death-"+GameDataTypes.toAbbrDeath(cur.direction)));
    }

    private void UnitAttackState(IndividualUnit cur, IndividualUnit tar, float totalTime, GameData gData) {
        //TODO if tar is null then move in direction of x,y land and if unit gets in range attack till dead then continue to direction
        if (tar.curHP > 0) { // maybe set this if to be if tar is not dead
            // check if tar within cur.range of cur
            if (InRange(cur, tar, gData)) {

                if (cur.attackEnd) {
                    cur.curAnim = GenerateAnimation(cur, "attack");
                    cur.attackEnd = false;
                    cur.animStart = totalTime;
                }
                if (cur.curAnim.isAnimationFinished(totalTime-cur.animStart)) {
                    int SoundToPlay = rando.nextInt(cur.attackSounds.length - 1);
                    cur.attackSounds[SoundToPlay].play(Volume.getFxVolume()/100);

                    tar.curHP -= cur.attackDamage;
                    cur.attackEnd = true;
                    System.out.println(String.format("Current Unit did "+cur.attackDamage+" damage to Target, Target now has "+tar.curHP+" health"));
                    System.out.println("Unit attacking target at " + (int)tar.getMidX() + " " + (int)tar.getMidY());
                    // TODO: make this use the projectiles

                } else {
                    cur.curTexture = cur.curAnim.getKeyFrame(totalTime-cur.animStart, false);
                }
            } else {
                cur.currentxmove = tar.getMidX();
                cur.currentymove = tar.getMidY();
                UnitMove(cur, totalTime, gData);
            }
            // if not, move closer, setting currentxmove and currentymove as needed
        } else {
            //deleteUnits.add(tar);
            tar.curState = GameDataTypes.EUnitState.Dead;
            cur.target = null;
            cur.stopMovement();
        }
    }
    private void UnitAttackBuildingState(IndividualUnit cur, StaticAsset target, float totalTime, GameData gData) {
        if (target == null || target.hitPoints() <= 0){
            //deleteUnits.add(tar);
            //tar.curState = GameDataTypes.EUnitState.Dead;
            cur.targetBuilding = null;
            cur.stopMovement();
            cur.curState = GameDataTypes.EUnitState.Idle;
            return; //dont do anything else.  this is like a break...
        }

       if (target.hitPoints() > 0) { // maybe set this if to be if tar is not dead
           UnitPosition upos = new UnitPosition();
           upos.setXFromTile(target.tilePositionX() + target.Size()/2);
           upos.setYFromTile(target.tilePositionY() + target.Size()/2);

           // check if tar within cur.range of cur
            if (InRange(cur, upos, gData)){

                if (cur.attackEnd) {
                    cur.curAnim = GenerateAnimation(cur, "attack");
                    cur.attackEnd = false;
                    cur.animStart = totalTime;
                }
                if (cur.curAnim.isAnimationFinished(totalTime-cur.animStart)) {

                    int SoundToPlay = rando.nextInt(cur.attackSounds.length - 1);
                    cur.attackSounds[SoundToPlay].play(Volume.getFxVolume()/100);

                    target.decrementHitPoints(cur.attackDamage);
                    cur.attackEnd = true;
                    System.out.println(String.format("Current Unit did "+cur.attackDamage+" damage to Target, Target now has "+String.valueOf(target.hitPoints()) +" health"));
                    // TODO: make this use the projectiles

                } else {
                    cur.curTexture = cur.curAnim.getKeyFrame(totalTime-cur.animStart, false);
                }
            } else {
                // Move position should already be set
                //cur.currentxmove = tar.getMidX();
                //cur.currentymove = tar.getMidY();
                UnitMove(cur, totalTime, gData);
            }
            // if not, move closer, setting currentxmove and currentymove as needed
        }
    }

    private boolean UnitDeadState(IndividualUnit cur, float totalTime, GameData gData) {
        if (cur.curHP <= 0 && cur.curHP >= -100) {
            // TODO: make a fucking function to return these animations
            cur.curAnim = GenerateDeathAnimation(cur);
            cur.curAnim.setPlayMode(Animation.PlayMode.NORMAL);
            cur.animStart = totalTime;
            cur.curHP = -101;
            cur.setTouchable(Touchable.disabled);
            //deleteUnits.add(this);
        }

        cur.curTexture = cur.curAnim.getKeyFrame(totalTime-cur.animStart, false);
        if (cur.curAnim.isAnimationFinished(totalTime-cur.animStart)) {
            return true;
        } else {
            return false;
        }
    }

    private void UnitMoveState(IndividualUnit cur, float totalTime, GameData gData) {
        if (UnitMove(cur, totalTime, gData)) {
            cur.curState = GameDataTypes.EUnitState.Idle;
            cur.stopMovement();
        }
    }

    public boolean UnitMove(IndividualUnit cur, float totalTime, GameData gData) {
        return UnitMove(cur, "walk", totalTime, gData);
    }

    public boolean pathable(float x, float y, GameData gData) {
        TilePosition tilePos = new TilePosition(new UnitPosition(round(x), round(y)));
        StaticAsset selectedAsset = gData.map.StaticAssetAt(tilePos);
        if (selectedAsset != null) {
            return false;
        }
        else if (gData.map.IsTraversable(gData.map.TileType(tilePos))) {
            return true;
        }
        else {
            return false;
        }
    }

    // Returns true if it's reached the destination, false if it hasn't
    public boolean UnitMove(IndividualUnit cur, String type, float totalTime, GameData gData) {
        if ((cur.getMidX() != cur.currentxmove) || (cur.getMidY() != cur.currentymove)) {
            boolean north, south, east, west;
            north = south = west = east = false;

            if (cur.getMidX() > cur.currentxmove && pathable((cur.getMidX() - cur.speed/10),cur.getMidY(), gData)) {
                cur.setX(cur.getX() - cur.speed/10);
                west = true;
            } else if (cur.getMidX() < cur.currentxmove && pathable((cur.getMidX() + cur.speed/10),cur.getMidY(), gData)) {
                cur.setX(cur.getX()+ cur.speed/10);
                east = true;
            } else {
                // stay in X
            }

            if (cur.getMidY() > cur.currentymove && pathable(cur.getMidX(),(cur.getMidY() - cur.speed/10), gData)) {
                cur.setY(cur.getY() - cur.speed/10);
                south = true;
            } else if (cur.getMidY() < cur.currentymove && pathable(cur.getMidX(),(cur.getMidY() + cur.speed/10), gData)) {
                cur.setY(cur.getY() + cur.speed/10);
                north = true;
            } else {
                // stay in Y
            }

            // This is bad and I should feel bad. - Sven
            if (north && west) {
                cur.direction = GameDataTypes.EDirection.NorthWest;
            } else if (north && east) {
                cur.direction = GameDataTypes.EDirection.NorthEast;
            } else if (north) {
                cur.direction = GameDataTypes.EDirection.North;
            } else if (south && west) {
                cur.direction = GameDataTypes.EDirection.SouthWest;
            } else if (south && east) {
                cur.direction = GameDataTypes.EDirection.SouthEast;
            } else if (south) {
                cur.direction = GameDataTypes.EDirection.South;
            } else if (east) {
                cur.direction = GameDataTypes.EDirection.East;
            } else if (west) {
                cur.direction = GameDataTypes.EDirection.West;
            } else {
                cur.stopMovement();
                return true;
            }

            cur.curAnim = GenerateAnimation(cur, type);
            cur.curTexture = cur.curAnim.getKeyFrame(totalTime, true);

            return false;
        } else {
            cur.curAnim = new Animation<TextureRegion>(cur.frameTime, unitTextures.get(cur.unitClass).findRegion(GameDataTypes.toString(cur.color)+"-walk-"+GameDataTypes.toAbbr(cur.direction), 0));
            cur.curTexture = cur.curAnim.getKeyFrame(totalTime, true);
            //cur.setPosition(cur.currentxmove, cur.currentymove);
            // Maybe 0 out currentxmove and currentymove at some point
            return true;
        }
    }

    private void UnitBuild(IndividualUnit cur, StaticAsset newBuilding, float totalTime, GameData gData) {
        //if (sqrt(pow((cur.currentxmove-cur.getMidX()), 2)  + pow((cur.currentymove-cur.getMidY()), 2)) <= (PlayerAssetType.StaticAssetSize(toBuild)/2) + cur.range * gData.TILE_WIDTH ) {
        if (InRange(cur, new UnitPosition(round(cur.currentxmove), round(cur.currentymove)), gData)) {
            // If unit is in range of the building

            if (cur.inProgressBuilding == null) {
                cur.hidden = true;
                if (cur.toBuild  == GameDataTypes.EStaticAssetType.Wall){
                    //special case for wall
                    StaticAsset buildySAsset = gData.map.StaticAssetAt(cur.buildPos);
                    if (buildySAsset != null){
                        cur.inProgressBuilding = buildySAsset;
                    }
                }
                else {
                    // if Construction hasn't started
                    gData.selectedUnits.remove(cur);
                    cur.inProgressBuilding = gData.playerData.get(GameDataTypes.to_underlying(cur.color)).ConstructStaticAsset(cur.buildPos, cur.toBuild, gData.map);
                }
            } else if (cur.inProgressBuilding == null ||
                    cur.inProgressBuilding.Action() == GameDataTypes.EAssetAction.None) {
                // If construction is completed or canceled, go idle after resetting position
                UnitPosition building = new UnitPosition(cur.inProgressBuilding.tilePosition());
                cur.setX(building.X());
                cur.setY(building.Y());
                cur.hidden = false;
                cur.inProgressBuilding = null;
                //gData.staticAssetRenderer.CreateShadowAsset(GameDataTypes.EStaticAssetType.ScoutTower, cur.color, cur.buildPos, gData.tiledMap, gData.map);
                cur.stopMovement();
                cur.curState = GameDataTypes.EUnitState.Idle;
            } else {
                // Construction is still in progress, keep building
            }
        } else {
            UnitMove(cur, totalTime, gData);
        }
    }

    public void updateVector() {
        if (!deleteUnits.isEmpty()) {
            for (IndividualUnit del : deleteUnits) {
                RemoveFromMap(del);
            }
            deleteUnits.removeAllElements();
        }
    }

    public Vector<IndividualUnit> GetAllUnits() {
        allUnits.clear();
        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            allUnits.addAll(unitMap.get(color));
        }
        return allUnits;
    }

}


