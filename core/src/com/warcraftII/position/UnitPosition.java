
package com.warcraftII.position;

import com.warcraftII.terrain_map.AssetDecoratedMap;

/**
 * Created by Kimi on 11/10/2017.
 * 
 * 
 */

public class UnitPosition extends Position {

    public UnitPosition() {
    }

    public UnitPosition(int x, int y) {
        super(x,y);
    }

    public UnitPosition(UnitPosition pos) {
        super(pos);
    }

    //SUPER IMPORTANT: Constructor from TilePosition
    public UnitPosition(TilePosition tpos) {
        DX = tpos.X() * DTileWidth + DHalfTileWidth;
        DY = (DMapHeight - tpos.Y()) * DTileHeight + DHalfTileHeight;
    }

    public boolean tileAligned() {
        return ((DX % DTileWidth) == DHalfTileWidth) &&
                ((DY != 0 && DTileHeight != 0) == (DHalfTileHeight != 0));
    }

    public void setFromTile(TilePosition pos) {
        DX = pos.X() * DTileWidth + DHalfTileWidth;
        DY = (DMapHeight - pos.Y()) * DTileHeight + DHalfTileHeight;
    }

    public void setXFromTile(int x) {
        DX = x * DTileWidth + DHalfTileWidth;
    }

    public void setYFromTile(int y) {
        DY = (DMapHeight - y) * DTileHeight + DHalfTileHeight;
    }

    public UnitPosition closestPosition(UnitPosition objPos, int objSize) {
        UnitPosition curPosition = new UnitPosition(objPos);
        UnitPosition bestPosition = new UnitPosition();
        int bestDistance = -1;

        for(int yIndex = 0; yIndex < objSize; yIndex++) {
            for(int xIndex = 0; xIndex < objSize; xIndex++) {
                int curDistance = curPosition.distanceSquared(this);
                if ((-1 == bestDistance) || curDistance < bestDistance) {
                    bestDistance = curDistance;
                    bestPosition.set(curPosition);
                }
                curPosition.incrementX(tileWidth());
            }

            curPosition.X(objPos.X());
            curPosition.incrementY(tileHeight());
        }

        return bestPosition;
    }


        /*
    Sets map dimensions to do the inversion.
     */

    public static void setMapDimensions(AssetDecoratedMap map){
        DMapHeight = map.Height();
        DMapWidth = map.Width();
    }





}

