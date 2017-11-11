package com.warcraftII.units;

/**
 * Created by Ian on 10/29/2017.
 * Is the basis for all units.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.warcraftII.GameDataTypes;

public class Unit {
    public Vector<IndividualUnit> unitVector;
    public int selectedUnitIndex;
    private TextureAtlas[] unitTextures = {
        new TextureAtlas(Gdx.files.internal("atlas/Peasant.atlas")),
        new TextureAtlas(Gdx.files.internal("atlas/Footman.atlas")),
        new TextureAtlas(Gdx.files.internal("atlas/Archer.atlas")),
        new TextureAtlas(Gdx.files.internal("atlas/Ranger.atlas"))
    };

    public Unit() {
        unitVector = new Vector<IndividualUnit>(50);
        selectedUnitIndex = 0;
    }

    public class IndividualUnit {
        public Sprite sprite;
        public int maxHP = 40;
        public int curHP = 40;
        public int attackDamage = 5;
        public int speed = 10;
        public int range = 1;
        public float currentxmove;
        public float currentymove;
        public float patrolxmove;
        public float patrolymove;
        public IndividualUnit target;
        public GameDataTypes.EUnitState curState;
        public Animation<TextureRegion> curAnim;

    }

    public void stopMovement() {
        unitVector.elementAt(selectedUnitIndex).curState = GameDataTypes.EUnitState.Idle;
    }
    public void AddUnit(float x_position, float y_position, GameDataTypes.EUnitType inUnit) {
        IndividualUnit newUnit = new IndividualUnit();
        TextureRegion texture;
        Animation<TextureRegion> anim;
        switch(inUnit) {
            case Peasant:
                texture = unitTextures[0].findRegion("walk-n");
                anim = new Animation<TextureRegion>(0.067f, unitTextures[0].findRegions("walk-n"));
                break;
            case Footman:
                texture = unitTextures[1].findRegion("walk-n");
                anim = new Animation<TextureRegion>(0.067f, unitTextures[1].findRegions("walk-n"));
                break;
            case Archer:
                texture = unitTextures[2].findRegion("walk-n");
                anim = new Animation<TextureRegion>(0.067f, unitTextures[2].findRegions("walk-n"));
                break;
            case Ranger:
                texture = unitTextures[3].findRegion("walk-n");
                anim = new Animation<TextureRegion>(0.067f, unitTextures[3].findRegions("walk-n"));
                break;
            default:
                texture = unitTextures[0].findRegion("walk-n");
                anim = new Animation<TextureRegion>(0.067f, unitTextures[0].findRegions("walk-n"));
        }
        newUnit.sprite = new Sprite(texture);
        newUnit.sprite.setSize(72,72);
        newUnit.sprite.setOriginCenter();
        newUnit.sprite.setPosition(x_position,y_position);
        newUnit.currentxmove = x_position*32;
        newUnit.currentymove = y_position*32;
        newUnit.curState = GameDataTypes.EUnitState.Idle;
        newUnit.curAnim = anim;
        unitVector.add(newUnit);
    }

    public void UnitStateHandler(float elapsedTime) {
        for (int i = 0; i < unitVector.size(); i++) {
            switch (unitVector.elementAt(i).curState) {
                case Idle:
                    break;
                case Move:
                    UnitMoveState(unitVector.elementAt(i));
                    break;
                case Attack:
                    UnitAttackState(unitVector.elementAt(i), unitVector.elementAt(i).target);
                    break;
                case Patrol:
                    UnitPatrolState(unitVector.elementAt(i));
                default:
                    System.out.println("How'd you manage to get to that state?");
                    break;
            }
        }
    }

    private void UnitPatrolState(IndividualUnit cur) {
        if ((cur.sprite.getX()+36 != cur.currentxmove) || (cur.sprite.getY()+36 != cur.currentymove)) {
            System.out.println(String.format("X: %f, Y: %f, desX: %f, desY: %f", cur.sprite.getX(), cur.sprite.getY(), cur.currentxmove, cur.currentymove));
            // TODO: If another unit gets in curr.range attack the other unit until dead, once unit dead go back to patrol
            if (cur.sprite.getX()+36 < cur.currentxmove)
                cur.sprite.setCenterX(cur.sprite.getX()+36 + cur.speed/10);
            if (cur.sprite.getX()+36 > cur.currentxmove)
                cur.sprite.setCenterX(cur.sprite.getX()+36 - cur.speed/10);
            if (cur.sprite.getY()+36 < cur.currentymove)
                cur.sprite.setCenterY(cur.sprite.getY()+36 + cur.speed/10);
            if (cur.sprite.getY()+36 > cur.currentymove)
                cur.sprite.setCenterY(cur.sprite.getY()+36 - cur.speed/10);
        } else {
            cur.sprite.setCenter(cur.currentxmove, cur.currentymove);
            float tempxmove = cur.currentxmove;
            float tempymove = cur.currentymove;
            cur.currentxmove = cur.patrolxmove;
            cur.currentymove = cur.patrolymove;
            cur.patrolxmove = tempxmove;
            cur.patrolymove = tempymove;
        }
    }

    private void UnitAttackState(IndividualUnit cur, IndividualUnit tar) {
        //TODO if tar is null then move in direction of x,y land and if unit gets in range attack till dead then continue to direction
        if ((cur.sprite.getX()+36 != tar.sprite.getX()+36 - cur.range) || (cur.sprite.getY()+36 != tar.sprite.getY() + 36 - cur.range)) {
            System.out.println(String.format("X: %f, Y: %f, desX: %f, desY: %f", cur.sprite.getX(), cur.sprite.getY(), cur.currentxmove, cur.currentymove));
            if (cur.sprite.getX()+36 < tar.sprite.getX()+36 - cur.range)
                cur.sprite.setCenterX(cur.sprite.getX()+36 + cur.speed/10);
            if (cur.sprite.getX()+36 > tar.sprite.getX()+36 - cur.range)
                cur.sprite.setCenterX(cur.sprite.getX()+36 - cur.speed/10);
            if (cur.sprite.getY()+36 < tar.sprite.getY() + 36 - cur.range)
                cur.sprite.setCenterY(cur.sprite.getY()+36 + cur.speed/10);
            if (cur.sprite.getY()+36 > tar.sprite.getY() + 36 - cur.range)
                cur.sprite.setCenterY(cur.sprite.getY()+36 - cur.speed/10);
        } else {
            cur.sprite.setCenter(cur.currentxmove, cur.currentymove);
            tar.curHP = tar.curHP - cur.attackDamage;
            System.out.println(String.format("Current Unit did %f damage to Target, Target now has %f health", cur.attackDamage, tar.curHP));
            if (tar.curHP <= 0)
                cur.curState = GameDataTypes.EUnitState.Idle;
        }
    }
    
    private void UnitMoveState(IndividualUnit cur) {
        if ((cur.sprite.getX()+36 != cur.currentxmove) || (cur.sprite.getY()+36 != cur.currentymove)) {
            System.out.println(String.format("X: %f, Y: %f, desX: %f, desY: %f", cur.sprite.getX(), cur.sprite.getY(), cur.currentxmove, cur.currentymove));
            if (cur.sprite.getX()+36 < cur.currentxmove)
                cur.sprite.setCenterX(cur.sprite.getX()+36 + cur.speed/10);
            if (cur.sprite.getX()+36 > cur.currentxmove)
                cur.sprite.setCenterX(cur.sprite.getX()+36 - cur.speed/10);
            if (cur.sprite.getY()+36 < cur.currentymove)
                cur.sprite.setCenterY(cur.sprite.getY()+36 + cur.speed/10);
            if (cur.sprite.getY()+36 > cur.currentymove)
                cur.sprite.setCenterY(cur.sprite.getY()+36 - cur.speed/10);
        } else {
            cur.sprite.setCenter(cur.currentxmove, cur.currentymove);
            cur.curState = GameDataTypes.EUnitState.Idle;
        }
    }
}


