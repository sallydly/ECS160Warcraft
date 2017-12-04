package com.warcraftII.units;

/**
 * Created by Ian on 10/29/2017.
 * Is the basis for all units.
 */
import com.badlogic.gdx.Gdx;
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
import com.warcraftII.GameDataTypes;
import com.warcraftII.position.RouterMap;
import com.warcraftII.position.TilePosition;
import com.warcraftII.position.UnitPosition;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.warcraftII.GameData;
import com.warcraftII.terrain_map.TileTypes;

//import org.omg.CORBA.UNKNOWN;

import static com.warcraftII.GameDataTypes.EAssetCapabilityType.BuildSimple;
import static com.warcraftII.GameDataTypes.EAssetCapabilityType.Repair;
import static com.warcraftII.GameDataTypes.EAssetCapabilityType.StandGround;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Unit {
    //public Vector<IndividualUnit> unitVector;
    public Vector<IndividualUnit> deleteUnits;
    public Map<GameDataTypes.EPlayerColor, Vector<IndividualUnit>> unitMap;
    public int selectedUnitIndex;

    private Map<GameDataTypes.EUnitType, TextureAtlas> unitTextures;

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
        // Gold Cost
        // Lumber Cost
        // Stone Cost
        // Build Time
        // Attack Steps
        // Reload Steps
        // I think the above should be included in the buildings?
        // Or we can include it as requirements for addUnit, but that makes init harder
        public int foodConsumed = 1;

        public boolean selected = false;
        public boolean touched = false;
        public float currentxmove;
        public float currentymove;
        public float patrolxmove;
        public float patrolymove;
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
            batch.draw(curTexture, getX(), getY());
            if (selected) {
                Texture sel = new Texture(Gdx.files.internal("img/select.png"));
                batch.draw(sel, getX(), getY());
            }
        }

        @Override
        public void act (float delta) {

            // TODO: Make this take over the unitstatehandler function maybe?
            //if (curState == GameDataTypes.EUnitState.Dead) {
            //    remove();
            //}
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
                break;
            case Footman:
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
                break;
            case Archer:
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
                break;
            case Ranger:
                newUnit.unitClass = GameDataTypes.EUnitType.Ranger;
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.RangerScouting);
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
                break;
            case Knight:
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
                break;
            default:
                newUnit.unitClass = GameDataTypes.EUnitType.Peasant;
                break;
        }

        newUnit.setX(x_position);
        newUnit.setY(y_position);
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
        newUnit.setDebug(true);
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

    public Vector<IndividualUnit> GetAllUnits() {
        Vector<IndividualUnit> unitVector = new Vector<IndividualUnit>();
        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            for (IndividualUnit cur : unitMap.get(color)) {
                unitVector.add(cur);
            }
        }
        return unitVector;
    }

    public IndividualUnit AddUnit(TilePosition tpos, GameDataTypes.EUnitType inUnit, GameDataTypes.EPlayerColor color){
        UnitPosition upos = new UnitPosition(tpos);
        return AddUnit((float)upos.X(), (float)upos.Y(), inUnit, color);
    }

    public void AddToMap(IndividualUnit in) {
        unitMap.get(in.color).add(in);
    }

    public void RemoveFromMap(IndividualUnit in) {
        unitMap.get(in.color).remove(in);
    }

    public void UnitStateHandler(float elapsedTime, GameData gameData) {
        Vector<IndividualUnit> toDelete = new Vector<IndividualUnit>();
        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            for (IndividualUnit cur : unitMap.get(color)) {
                switch (cur.curState) {
                    case Idle:
                        break;
                    case Move:
                        UnitMoveState(cur, elapsedTime, gameData);
                        break;
                    case Attack:
                        UnitAttackState(cur, cur.target, elapsedTime, gameData);
                        break;
                    case Patrol:
                        UnitPatrolState(cur, elapsedTime, gameData);
                        break;
                    case Mine:
                        UnitMineState(cur, elapsedTime, gameData);
                        break;
                    case Dead:
                        if (UnitDeadState(cur, elapsedTime, gameData)) {
                            toDelete.add(cur);
                        }
                        break;
                    default:
                        System.out.println("Invalid state");
                        break;
                }
            }
        }
        for (IndividualUnit cur : toDelete) {
            RemoveFromMap(cur);
        }
        toDelete.removeAllElements();
    }

    private void UnitMineState(IndividualUnit cur, float deltaTime, GameData gameData) {
        if ((cur.getMidX() != cur.currentxmove - 1) || (cur.getMidY() != cur.currentymove - 1)) {
            // mine
        }
        else
            UnitMoveState(cur, deltaTime, gameData);
    }

    private void UnitPatrolState(IndividualUnit cur, float deltaTime, GameData gameData) {
        if (UnitMove(cur, deltaTime, gameData)) {
            float tempxmove = cur.currentxmove;
            float tempymove = cur.currentymove;
            cur.currentxmove = cur.patrolxmove;
            cur.currentymove = cur.patrolymove;
            cur.patrolxmove = tempxmove;
            cur.patrolymove = tempymove;
        }
    }

    private void UnitAttackState(IndividualUnit cur, IndividualUnit tar, float deltaTime, GameData gameData) {
        //TODO if tar is null then move in direction of x,y land and if unit gets in range attack till dead then continue to direction
        if (tar.curHP > 0) { // maybe set this if to be if tar is not dead
            // TODO: do animation, check current keyframe, and only then attack
            // check if tar within cur.range of cur
            if (sqrt(pow((tar.getMidX()-cur.getMidX()), 2)  + pow((tar.getMidY()-cur.getMidY()), 2)) <= cur.range*50) {
                if (cur.attackEnd) {
                    cur.curAnim = new Animation<TextureRegion>(cur.frameTime, unitTextures.get(cur.unitClass).findRegions(GameDataTypes.toString(cur.color)+"-attack-"+GameDataTypes.toAbbr(cur.direction)));
                    cur.attackEnd = false;
                    cur.animStart = deltaTime;
                }
                if (cur.curAnim.isAnimationFinished(deltaTime-cur.animStart)) {
                    tar.curHP -= cur.attackDamage;
                    cur.attackEnd = true;
                    System.out.println(String.format("Current Unit did "+cur.attackDamage+" damage to Target, Target now has "+tar.curHP+" health"));
                    // TODO: make this use the projectiles

                } else {
                    cur.curTexture = cur.curAnim.getKeyFrame(deltaTime-cur.animStart, false);
                }
            } else {
                cur.currentxmove = tar.getMidX();
                cur.currentymove = tar.getMidY();
                UnitMove(cur, deltaTime, gameData);
            }
            // if not, move closer, setting currentxmove and currentymove as needed
        } else {
            //deleteUnits.add(tar);
            tar.curState = GameDataTypes.EUnitState.Dead;
            cur.target = null;
            cur.stopMovement();
        }
    }

    private boolean UnitDeadState(IndividualUnit cur, float deltaTime, GameData gameData) {
        if (cur.curHP <= 0 && cur.curHP >= -100) {
            // TODO: make a fucking function to return these animations
            cur.curAnim = new Animation<TextureRegion>(cur.frameTime, unitTextures.get(cur.unitClass).findRegions(GameDataTypes.toString(cur.color)+"-death-"+GameDataTypes.toAbbrDeath(cur.direction)));
            cur.curAnim.setPlayMode(Animation.PlayMode.NORMAL);
            cur.animStart = deltaTime;
            cur.curHP = -101;
            cur.setTouchable(Touchable.disabled);
            //deleteUnits.add(this);
        }
        cur.selected = false;
        cur.curTexture = cur.curAnim.getKeyFrame(deltaTime-cur.animStart, false);
        if (cur.curAnim.isAnimationFinished(deltaTime-cur.animStart)) {
            return true;
        } else {
            return false;
        }
    }

    private void UnitMoveState(IndividualUnit cur, float deltaTime, GameData gameData) {
        if (UnitMove(cur, deltaTime, gameData)) {
            cur.curState = GameDataTypes.EUnitState.Idle;
        }
    }

    // Returns true if it's reached the destination, false if it hasn't
    public boolean UnitMove(IndividualUnit cur, float deltaTime, GameData gameData) {
        if ((cur.getMidX() != cur.currentxmove) || (cur.getMidY() != cur.currentymove)) {
            // TODO: do actual pathfinding
            RouterMap routerMap = RouterMap.RouterMap();
            GameDataTypes.EDirection direction = routerMap.FindRoute(gameData, GetAllUnits(), cur);
            Gdx.app.log("Unit >> RouterMap", "Move " + direction.name());

            /*
            cur.direction = direction;
            //TODO: switch statement depending on direction
            if (cur.direction == GameDataTypes.EDirection.NorthWest) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.NorthEast) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.North) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.SouthWest) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.SouthEast) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.South) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.East) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.West) {
                //TODO: do smth!
            } else if (cur.direction == GameDataTypes.EDirection.Max){

            }*/

            //TODO: delete start
            boolean north, south, east, west;
            north = south = west = east = false;

            if (cur.getMidX() > cur.currentxmove) {
                cur.setX(cur.getX() - cur.speed/10);
                west = true;
            } else if (cur.getMidX() < cur.currentxmove) {
                cur.setX(cur.getX()+ cur.speed/10);
                east = true;
            } else {
                // stay in X
            }

            if (cur.getMidY() > cur.currentymove) {
                cur.setY(cur.getY() - cur.speed/10);
                south = true;
            } else if (cur.getMidY() < cur.currentymove) {
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
            }
            //TODO: delete end

            cur.curAnim = new Animation<TextureRegion>(cur.frameTime, unitTextures.get(cur.unitClass).findRegions(GameDataTypes.toString(cur.color)+"-walk-"+GameDataTypes.toAbbr(cur.direction)));
            cur.curTexture = cur.curAnim.getKeyFrame(deltaTime, true);

            return false;
        } else {
            cur.curAnim = new Animation<TextureRegion>(cur.frameTime, unitTextures.get(cur.unitClass).findRegion(GameDataTypes.toString(cur.color)+"-walk-"+GameDataTypes.toAbbr(cur.direction), 0));
            cur.curTexture = cur.curAnim.getKeyFrame(deltaTime, true);
            //cur.setPosition(cur.currentxmove, cur.currentymove);
            // Maybe 0 out currentxmove and currentymove at some point
            return true;
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

}


