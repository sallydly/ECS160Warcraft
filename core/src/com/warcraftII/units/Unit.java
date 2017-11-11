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
        public int speed = 10;
        public float currentxmove;
        public float currentymove;
        public GameDataTypes.EUnitState curState;
        public Animation<TextureRegion> curAnim;

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
                default:
                    System.out.println("How'd you manage to get to that state?");
                    break;
            }
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


