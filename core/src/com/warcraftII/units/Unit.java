package com.warcraftII.units;

/**
 * Created by Ian on 10/29/2017.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.warcraftII.GameDataTypes;
import com.warcraftII.position.TilePosition;
import com.warcraftII.position.UnitPosition;

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
    }
    public void AddUnit(float x_position, float y_position, Texture texture) {
        IndividualUnit newUnit = new IndividualUnit();
        newUnit.sprite = new Sprite(texture);
        newUnit.sprite.setSize(72,72);
        newUnit.sprite.setPosition(x_position*32,y_position*32);
        newUnit.currentxmove = x_position*32;
        newUnit.currentymove = y_position*32;
        unitVector.add(newUnit);
    }

    public void AddUnit(TilePosition tilePosition, GameDataTypes.EAssetType type, GameDataTypes.EPlayerColor player) {
        IndividualUnit newUnit = new IndividualUnit();
        if (type == GameDataTypes.EAssetType.Peasant) {
            Texture texture = new Texture(Gdx.files.internal("img/PeasantStatic.png"));
            newUnit.sprite = new Sprite(texture);
            newUnit.sprite.setSize(72, 72);
            UnitPosition upos = new UnitPosition(tilePosition);
            newUnit.sprite.setPosition(upos.X(), upos.Y());
            newUnit.currentxmove = upos.X();
            newUnit.currentymove = upos.Y();
            unitVector.add(newUnit);
        }
    }

    public void AllMovement() {
        for (int i = 0; i < unitVector.size(); i++) {
            if ((unitVector.elementAt(i).sprite.getX() != unitVector.elementAt(i).currentxmove) && (unitVector.elementAt(i).sprite.getY() != unitVector.elementAt(i).currentymove)) {
                if (unitVector.elementAt(i).sprite.getX() < unitVector.elementAt(i).currentxmove)
                    unitVector.elementAt(i).sprite.setX(unitVector.elementAt(i).sprite.getX() + 1);
                if (unitVector.elementAt(i).sprite.getX() > unitVector.elementAt(i).currentxmove)
                    unitVector.elementAt(i).sprite.setX(unitVector.elementAt(i).sprite.getX() - 1);
                if (unitVector.elementAt(i).sprite.getY() < unitVector.elementAt(i).currentymove)
                    unitVector.elementAt(i).sprite.setY(unitVector.elementAt(i).sprite.getY() + 1);
                if (unitVector.elementAt(i).sprite.getY() > unitVector.elementAt(i).currentymove)
                    unitVector.elementAt(i).sprite.setY(unitVector.elementAt(i).sprite.getY() - 1);
            }
        }
    }
}


