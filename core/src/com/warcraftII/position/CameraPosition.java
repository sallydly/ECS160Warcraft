package com.warcraftII.position;

/**
 * Created by hqmai on 11/19/17.
 */

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 *
 */
/*
 * TODO: Camera Team might want to look at this class
 * HM: I don't fully understand fully how Orthographic camera works, so this class may not be exactly how we want it to works
 */
public class CameraPosition extends Position {

    //the current Orthographic camera that this class is using
    private OrthographicCamera DOrhtoCam;


    public CameraPosition(int x, int y, OrthographicCamera initCam) {
        super(x, y);
        DOrhtoCam = initCam;
    }

    /**
     * Construct a CameraPosition using the position at the middle of the tile
     * @param tilePosition
     */
    public CameraPosition(TilePosition tilePosition) {
        Vector3 tilePos = new Vector3(tilePosition.X()*DTileWidth+DHalfTileWidth, tilePosition.Y()*DTileHeight+DHalfTileHeight, 0);
        Vector3 camPos = DOrhtoCam.project(tilePos);
        DX = (int)camPos.x;
        DY = (int)camPos.y;
    }

    public CameraPosition(UnitPosition unitPosition) {
        Vector3 unitPos = new Vector3(unitPosition.X(), unitPosition.Y(), 0);
        Vector3 camPos = DOrhtoCam.project(unitPos);
        DX = (int)camPos.x;
        DY = (int)camPos.y;
    }

    public TilePosition getTilePosition() {
        Vector3 camPos = new Vector3(DX, DY, 0);
        Vector3 tilePos = DOrhtoCam.unproject(camPos);
        return new TilePosition((int)tilePos.x/32, (int)tilePos.y/32);
    }


    public void updateCamera(OrthographicCamera cam) {
        DOrhtoCam = cam;
    }

}
