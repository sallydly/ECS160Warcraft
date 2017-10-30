package com.warcraftII.position;

/**
 * Created by ajc on 10/28/17.
 */

public class PixelPosition extends Position {

    public PixelPosition() {
    }

    public PixelPosition(int x, int y) {
        super(x,y);
    }

    public PixelPosition(PixelPosition pos) {
        super(pos);
    }

    public boolean tileAligned() {
        return ((DX % DTileWidth) == DHalfTileWidth) &&
                ((DY != 0 && DTileHeight != 0) == (DHalfTileHeight != 0));
    }

    public void setFromTile(TilePosition pos) {
        DX = pos.X() * DTileWidth + DHalfTileWidth;
        DY = pos.Y() * DTileHeight + DHalfTileHeight;
    }

    public void setXFromTile(int x) {
        DX = x * DTileWidth + DHalfTileWidth;
    }

    public void setYFromTile(int y) {
        DY = y * DTileHeight + DHalfTileHeight;
    }

    public PixelPosition closestPosition(PixelPosition objPos, int objSize) {
        PixelPosition curPosition = new PixelPosition(objPos);
        PixelPosition bestPosition = new PixelPosition();
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

}
