package com.warcraftII.units;

/**
 * Created by Ian on 10/29/2017.
 */
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.*;
import com.badlogic.gdx.graphics.Texture;

public class Unit {
    public Vector<IndividualUnit> unit_vector;
    public int selected_unit_index;

    public Unit() {
        unit_vector = new Vector<IndividualUnit>(50);
        selected_unit_index = 0;
    }

    public class IndividualUnit {
        public Sprite sprite;
        public float currentxmove;
        public float currentymove;
    }
    public void AddUnit(float x_position, float y_position, Texture texture) {
        IndividualUnit new_unit = new IndividualUnit();
        new_unit.sprite = new Sprite(texture);
        new_unit.sprite.setSize(72,72);
        new_unit.sprite.setPosition(x_position*32,y_position*32);
        new_unit.currentxmove = x_position*32;
        new_unit.currentymove = y_position*32;
        unit_vector.add(new_unit);
    }

    public void AllMovement() {
        for (int i = 0; i < unit_vector.size(); i++) {
            if ((unit_vector.elementAt(i).sprite.getX() != unit_vector.elementAt(i).currentxmove) && (unit_vector.elementAt(i).sprite.getY() != unit_vector.elementAt(i).currentymove)) {
                if (unit_vector.elementAt(i).sprite.getX() < unit_vector.elementAt(i).currentxmove)
                    unit_vector.elementAt(i).sprite.setX(unit_vector.elementAt(i).sprite.getX() + 1);
                if (unit_vector.elementAt(i).sprite.getX() > unit_vector.elementAt(i).currentxmove)
                    unit_vector.elementAt(i).sprite.setX(unit_vector.elementAt(i).sprite.getX() - 1);
                if (unit_vector.elementAt(i).sprite.getY() < unit_vector.elementAt(i).currentymove)
                    unit_vector.elementAt(i).sprite.setY(unit_vector.elementAt(i).sprite.getY() + 1);
                if (unit_vector.elementAt(i).sprite.getY() > unit_vector.elementAt(i).currentymove)
                    unit_vector.elementAt(i).sprite.setY(unit_vector.elementAt(i).sprite.getY() - 1);
            }
        }
    }
}


