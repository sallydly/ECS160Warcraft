package com.warcraftII.position;

import com.warcraftII.GameDataTypes.*;

/**
 * Created by ajc on 10/28/17.
 */

public class TilePosition extends Position{

    public TilePosition() {
        super();
    }

    public TilePosition(int x, int y) {
        super(x,y);
    }

    public TilePosition(TilePosition pos) {
        super(pos);
    }

    //SUPER IMPORTANT: Constructor from UnitPosition
    public TilePosition(UnitPosition upos){
        DX = (int) (upos.X() / DTileWidth);
        DY = (int)(DMapHeight - upos.Y() / DTileHeight);
    }



    public void SetFromUnit(UnitPosition upos) {
        DX = (int) (upos.X() / DTileWidth);
        DY = DMapHeight - (int) (upos.Y() / DTileHeight);
    }

    public void setXFromUnit(int x) {
        DX = (int) (x / DTileWidth);
    }

    public void setYFromUnit(int y) {
        DY = DMapHeight - (int) (y / DTileHeight);
    }



    public void setFromPixel(PixelPosition pos) {
        DX = pos.X() / DTileWidth;
        DY = pos.Y() / DTileHeight;
    }

    public void setXFromPixel(int x) {
        DX = x / DTileWidth;
    }

    public void setYFromPixel(int y) {
        DY = y / DTileHeight;
    }

    public EDirection adjacentTileDirection(TilePosition pos, int objSize) {
        if(1 == objSize){
            int deltaX = pos.DX - DX;
            int deltaY = pos.DY - DY;

            if((1 < (deltaX * deltaX))||(1 < (deltaY * deltaY))){
                return EDirection.Max;
            }

            return (DTileDirections.get(deltaY+1)).get(deltaX+1);
        }
        else{
            PixelPosition thisPixelPosition = new PixelPosition();
            PixelPosition targetPixelPosition = new PixelPosition();
            TilePosition targetTilePosition = new TilePosition();

            thisPixelPosition.setFromTile(this);
            targetPixelPosition.setFromTile(pos);

            targetTilePosition.setFromPixel( thisPixelPosition.closestPosition(targetPixelPosition,
                                                                                objSize) );
            return adjacentTileDirection(targetTilePosition, 1);
        }
    }
}
