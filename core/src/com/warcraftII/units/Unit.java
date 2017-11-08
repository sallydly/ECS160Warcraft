package com.warcraftII.units;

/**
 * Created by Ian on 10/29/2017.
 * Is the basis for all units.
 */
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.*;
import com.badlogic.gdx.graphics.Texture;

public class Unit {
    public Vector<IndividualUnit> unitVector;
    public int selectedUnitIndex;

    public Unit() {
        unitVector = new Vector<IndividualUnit>(50);
        selectedUnitIndex = 0;
    }

    public class IndividualUnit {
        public Sprite sprite;
        public float currentxmove;
        public float currentymove;
        public int curState;
        /*
        For now, state values are as follows. Feel free to convert to strings or something else later.
        0 = idle
        1 = moving
         */
    }
    public void AddUnit(float x_position, float y_position, Texture texture) {
        IndividualUnit newUnit = new IndividualUnit();
        newUnit.sprite = new Sprite(texture);
        newUnit.sprite.setSize(72,72);
        newUnit.sprite.setOriginCenter();
        System.out.println(String.format("Origin X: %f, Origin Y: %f", newUnit.sprite.getOriginX(), newUnit.sprite.getOriginY()));
        newUnit.sprite.setPosition(x_position,y_position);
        newUnit.currentxmove = x_position*32;
        newUnit.currentymove = y_position*32;
        newUnit.curState = 0;
        unitVector.add(newUnit);
    }

    public void UnitStateHandler() {
        for (int i = 0; i < unitVector.size(); i++) {
            switch (unitVector.elementAt(i).curState) {
                case 0: // idle
                    break;
                case 1: // moving
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
                cur.sprite.setCenterX(cur.sprite.getX()+36 + 1);
            if (cur.sprite.getX()+36 > cur.currentxmove)
                cur.sprite.setCenterX(cur.sprite.getX()+36 - 1);
            if (cur.sprite.getY()+36 < cur.currentymove)
                cur.sprite.setCenterY(cur.sprite.getY()+36 + 1);
            if (cur.sprite.getY()+36 > cur.currentymove)
                cur.sprite.setCenterY(cur.sprite.getY()+36 - 1);
        } else {
            cur.sprite.setCenter(cur.currentxmove, cur.currentymove);
            cur.curState = 0;
        }
    }

}


