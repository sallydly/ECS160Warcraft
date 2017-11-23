package com.warcraftII.units;

/**
 * Created by Ian on 10/29/2017.
 * Is the basis for all units.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.warcraftII.GameDataTypes;
import com.warcraftII.position.TilePosition;
import com.warcraftII.position.UnitPosition;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.warcraftII.terrain_map.AssetDecoratedMap;
import com.warcraftII.terrain_map.TileTypes;

import org.omg.CORBA.UNKNOWN;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Unit {
    public Vector<IndividualUnit> unitVector;
    public Vector<IndividualUnit> deleteUnits;
    public int selectedUnitIndex;
    /*private TextureAtlas[] unitTextures = {
        new TextureAtlas(Gdx.files.internal("atlas/Peasant.atlas")),
        new TextureAtlas(Gdx.files.internal("atlas/Footman.atlas")),
        new TextureAtlas(Gdx.files.internal("atlas/Archer.atlas")),
        new TextureAtlas(Gdx.files.internal("atlas/Ranger.atlas"))
    };*/

    private Map<GameDataTypes.EUnitType, TextureAtlas> unitTextures = new HashMap<GameDataTypes.EUnitType, TextureAtlas>();

    public Unit() {
        unitVector = new Vector<IndividualUnit>(50);
        deleteUnits = new Vector<IndividualUnit>(20);
        selectedUnitIndex = 0;
        unitTextures.put(GameDataTypes.EUnitType.Archer, new TextureAtlas(Gdx.files.internal("atlas/Archer.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Footman, new TextureAtlas(Gdx.files.internal("atlas/Footman.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Peasant, new TextureAtlas(Gdx.files.internal("atlas/Peasant.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Ranger, new TextureAtlas(Gdx.files.internal("atlas/Ranger.atlas")));
        unitTextures.put(GameDataTypes.EUnitType.Knight, new TextureAtlas(Gdx.files.internal("atlas/Knight.atlas")));
    }

    public class IndividualUnit extends Actor {
        public Sprite sprite;
        public GameDataTypes.EUnitType unitClass;
        public int unitTexInd;
        public int maxHP = 40;
        public int curHP = 40;
        public int attackDamage = 5;
        public int speed = 10;
        public int range = 1;
        public boolean selected = false;
        public float currentxmove;
        public float currentymove;
        public float patrolxmove;
        public float patrolymove;
        public IndividualUnit target;
        public GameDataTypes.EUnitState curState;
        public Animation<TextureRegion> curAnim;
        public Vector<GameDataTypes.EAssetCapabilityType> abilities;
        public GameDataTypes.EPlayerColor color;
        public GameDataTypes.EDirection direction;

        public String getDirection() {
            if (currentxmove == sprite.getX()+(sprite.getWidth()/2)) { // straight up or down
                if (currentymove <= sprite.getY()+(sprite.getHeight()/2)) { // if south
                    return "s";
                } else { // if north
                    return "n";
                }
            } else if (currentxmove <= sprite.getX()+(sprite.getWidth()/2)) { // if to the West
                System.out.println("currentxmove: "+currentxmove+"; currentymove:"+currentymove);
                if (currentymove == sprite.getY()+(sprite.getHeight()/2)) { // straight west
                    return "w";
                } else if (currentymove <= sprite.getY()+(sprite.getHeight()/2)) { // south west
                    return "sw";
                } else { // north west
                    return "nw";
                }
            } else { // if currentxmove > sprite OriginX
                if (currentymove == sprite.getY()+(sprite.getHeight()/2)) { // straight east
                    return "e";
                } else if (currentymove <= sprite.getY()+(sprite.getHeight()/2)) { // south east
                    return "se";
                } else { // south west
                    return "sw";
                }
            }
        }

        public void stopMovement() {
            curState = GameDataTypes.EUnitState.Idle;
            curAnim = new Animation<TextureRegion>(0.067f, unitTextures.get(unitClass).findRegion(GameDataTypes.toString(color)+"-walk-"+GameDataTypes.toAbbr(direction), 0));
        }

    }

    public void AddUnit(float x_position, float y_position, GameDataTypes.EUnitType inUnit, GameDataTypes.EPlayerColor inColor) {
        final IndividualUnit newUnit = new IndividualUnit();
        newUnit.abilities = new Vector<GameDataTypes.EAssetCapabilityType>(5);
        TextureAtlas unitAtlas = unitTextures.get(inUnit);
        TextureRegion texture;
        Animation<TextureRegion> anim;
        newUnit.color = inColor;
        texture = unitAtlas.findRegion(GameDataTypes.toString(newUnit.color) + "-walk-n");
        anim = new Animation<TextureRegion>(0.067f, unitAtlas.findRegion(GameDataTypes.toString(newUnit.color) + "-walk-n", 0));
        switch(inUnit) {
            case Peasant:
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.Mine);
                newUnit.unitClass = GameDataTypes.EUnitType.Peasant;
                break;
            case Footman:
                newUnit.unitClass = GameDataTypes.EUnitType.Footman;
                break;
            case Archer:
                newUnit.unitClass = GameDataTypes.EUnitType.Archer;
                break;
            case Ranger:
                newUnit.abilities.add(GameDataTypes.EAssetCapabilityType.RangerScouting);
                newUnit.unitClass = GameDataTypes.EUnitType.Ranger;
                break;
            case Knight:
                newUnit.unitClass = GameDataTypes.EUnitType.Knight;
                break;
            default:
                newUnit.unitClass = GameDataTypes.EUnitType.Peasant;
                break;
        }
        newUnit.sprite = new Sprite(texture);
        newUnit.sprite.setSize(72,72);
        newUnit.sprite.setOriginCenter();
        newUnit.sprite.setPosition(x_position,y_position);
        newUnit.currentxmove = x_position*32;
        newUnit.currentymove = y_position*32;
        newUnit.curState = GameDataTypes.EUnitState.Idle;
        newUnit.curAnim = anim;
        newUnit.direction = GameDataTypes.EDirection.North;
        newUnit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                newUnit.selected = true;
                System.out.println("Unit Listener");
                return true;
            }
        });
        unitVector.add(newUnit);
    }

    public void AddUnit(TilePosition tpos, GameDataTypes.EUnitType inUnit, GameDataTypes.EPlayerColor color){
        UnitPosition upos = new UnitPosition(tpos);
        AddUnit((float)upos.X(), (float)upos.Y(), inUnit, color);
    }



    public void UnitStateHandler(float elapsedTime, AssetDecoratedMap map) {
        for (int i = 0; i < unitVector.size(); i++) {
            if (unitVector.elementAt(i).curHP <= 0) {
                unitVector.remove(i);
            } else {
                switch (unitVector.elementAt(i).curState) {
                    case Idle:
                        break;
                    case Move:
                        UnitMoveState(unitVector.elementAt(i), map);
                        break;
                    case Attack:
                        UnitAttackState(unitVector.elementAt(i), unitVector.elementAt(i).target, map);
                        break;
                    case Patrol:
                        UnitPatrolState(unitVector.elementAt(i), map);
                        break;
                    case Mine:
                        UnitMineState(unitVector.elementAt(i), map);
                        break;
                    default:
                        System.out.println("How'd you manage to get to that state?");
                        break;
                }
            }
        }
    }

    private void UnitMineState(IndividualUnit cur, AssetDecoratedMap map) {
        if ((cur.sprite.getX()+36 != cur.currentxmove - 1) || (cur.sprite.getY()+36 != cur.currentymove - 1)) {
            // mine
        }
        else
            UnitMoveState(cur, map);
    }

    private void UnitPatrolState(IndividualUnit cur, AssetDecoratedMap map) {
        if (UnitMove(cur, map)) {
            float tempxmove = cur.currentxmove;
            float tempymove = cur.currentymove;
            cur.currentxmove = cur.patrolxmove;
            cur.currentymove = cur.patrolymove;
            cur.patrolxmove = tempxmove;
            cur.patrolymove = tempymove;
        }
    }

    private void UnitAttackState(IndividualUnit cur, IndividualUnit tar, AssetDecoratedMap map) {
        //TODO if tar is null then move in direction of x,y land and if unit gets in range attack till dead then continue to direction
        if (tar.curHP > 0) { // maybe set this if to be if tar is not dead
            // TODO: do animation, check current keyframe, and only then attack
            // check if tar within cur.range of cur
            if (sqrt(pow((tar.getX()+(tar.getWidth()/2))-(cur.getX()+(cur.getWidth()/2)), 2)  + pow((tar.getY()+(tar.getHeight()/2))-(cur.getY()+(cur.getHeight()/2)), 2)) <= cur.range*5) {
                tar.curHP -= cur.attackDamage;
                System.out.println(String.format("Current Unit did "+cur.attackDamage+" damage to Target, Target now has "+tar.curHP+" health"));
            } else {
                cur.currentxmove = tar.sprite.getX()+(tar.getWidth()/2);
                cur.currentymove = tar.sprite.getY()+(tar.getHeight()/2);
                UnitMove(cur, map);
            }
            // if not, move closer, setting currentxmove and currentymove as needed
        } else {
            deleteUnits.add(tar);
            cur.target = null;
            cur.stopMovement();
        }
    }

    private void UnitMoveState(IndividualUnit cur, AssetDecoratedMap map) {
        if (UnitMove(cur, map)) {
            cur.curState = GameDataTypes.EUnitState.Idle;
        }
    }

    // Returns true if it's reached the destination, false if it hasn't
    public boolean UnitMove(IndividualUnit cur, AssetDecoratedMap map) {
        if ((cur.sprite.getX()+36 != cur.currentxmove) || (cur.sprite.getY()+36 != cur.currentymove)) {
            // TODO: do actual pathfinding

            boolean north, south, east, west;
            north = south = west = east = false;

            if (cur.sprite.getX()+(cur.sprite.getWidth()/2) > cur.currentxmove) {
                cur.sprite.setCenterX(cur.sprite.getX()+(cur.sprite.getWidth()/2) - cur.speed/10);
                west = true;
            } else if (cur.sprite.getX()+(cur.sprite.getWidth()/2) < cur.currentxmove) {
                cur.sprite.setCenterX(cur.sprite.getX()+(cur.sprite.getWidth()/2) + cur.speed/10);
                east = true;
            } else {
                // stay in X
            }

            if (cur.sprite.getY()+(cur.sprite.getHeight()/2) > cur.currentymove) {
                cur.sprite.setCenterY(cur.sprite.getY()+(cur.sprite.getHeight()/2) - cur.speed/10);
                south = true;
            } else if (cur.sprite.getY()+(cur.sprite.getHeight()/2) < cur.currentymove) {
                cur.sprite.setCenterY(cur.sprite.getY()+(cur.sprite.getHeight()/2) + cur.speed/10);
                north = true;
            } else {
                // stay in Y
            }

            GameDataTypes.EDirection oldDir = cur.direction;
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

            cur.curAnim = new Animation<TextureRegion>(0.067f, unitTextures.get(cur.unitClass).findRegions(GameDataTypes.toString(cur.color)+"-walk-"+GameDataTypes.toAbbr(cur.direction)));

            return false;
        } else {
            cur.curAnim = new Animation<TextureRegion>(0.067f, unitTextures.get(cur.unitClass).findRegion(GameDataTypes.toString(cur.color)+"-walk-"+GameDataTypes.toAbbr(cur.direction), 0));
            cur.sprite.setCenter(cur.currentxmove, cur.currentymove);
            // Maybe 0 out currentxmove and currentymove at some point
            return true;
        }
    }

    public void updateVector() {
        if (!deleteUnits.isEmpty()) {
            for (IndividualUnit del : deleteUnits) {
                unitVector.remove(del);
            }
            deleteUnits.removeAllElements();
        }
    }

}


