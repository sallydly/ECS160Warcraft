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

    //the current Orthographic camera that this class is using, not sure if I want this to be public
    private OrthographicCamera DOrthoCam;

    public CameraPosition() {
        super();
    }

    public CameraPosition(int x, int y, OrthographicCamera initCam) {
        super(x, y);
        DOrthoCam = initCam;
    }

    public CameraPosition(CameraPosition cameraPosition) {
        super(cameraPosition);
        DOrthoCam = cameraPosition.DOrthoCam;
    }

    /**
     * Construct a CameraPosition using the position at the middle of the tile
     *
     * @param tilePosition
     */
    public CameraPosition(TilePosition tilePosition, OrthographicCamera camera) {
        super();
        DOrthoCam = camera;
        Vector3 tilePos = new Vector3(tilePosition.X() * DTileWidth + DHalfTileWidth, (DMapHeight - tilePosition.Y() - 1) * DTileHeight + DHalfTileHeight, 0);
        Vector3 camPos = DOrthoCam.project(tilePos);
        DX = (int) camPos.x;
        DY = (int) camPos.y;
    }

    public CameraPosition(UnitPosition unitPosition, OrthographicCamera camera) {
        super();
        DOrthoCam = camera;
        Vector3 unitPos = new Vector3(unitPosition.X(), unitPosition.Y(), 0);
        Vector3 camPos = DOrthoCam.project(unitPos);
        DX = (int) camPos.x;
        DY = DMapHeight * DTileWidth - (int) camPos.y - 1;
    }

    public TilePosition getTilePosition() {
        Vector3 camPos = new Vector3(DX, DY, 0);
        Vector3 tilePos = DOrthoCam.unproject(camPos);
        return new TilePosition((int) tilePos.x / DTileWidth, DMapHeight - (int) tilePos.y / DTileHeight - 1);
    }


    public void updateCamera(OrthographicCamera cam) {
        DOrthoCam = cam;
    }
}