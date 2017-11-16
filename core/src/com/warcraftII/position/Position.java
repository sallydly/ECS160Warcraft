
package com.warcraftII.position;

import com.warcraftII.GameDataTypes.*;

import java.util.ListIterator;
import java.util.Vector;

import javax.print.Doc;

/**
 * FIXME: Refactor to change (0,0) from top left to bottom left
 *      Maybe not ^^^ Kimi wants to leave it (11/5/17)
 *
 * This class is ported from Position.cpp
 * Important notes:
 *      -No overloaded operators in Java,that affects == and !=, and =
 *       ==   `if(pos1 == pos2)` needs to be changed to `if(pos1.equals(pos2))`
 *       !=   `if(pos1 != pos2)` --> `if(!pos1.equals(pos2))`
 *       =    `pos1 = pos2;`     --> `pos1.set(pos2);`
 *
 * Created by ajc on 10/27/17.
 */

public class Position {

    protected int DX;
    protected int DY;

    protected static int DTileWidth;
    protected static int DTileHeight;
    protected static int DHalfTileWidth;
    protected static int DHalfTileHeight;

    protected static int DMapHeight;
    protected static int DMapWidth;
    

    /*
     * Should call initVectors once a Position object is instantiated
     */
        /*
    * Should call initVectors once a Position object is instantiated
    */
    static {
        initVectors();
    }

    protected static Vector<Vector<EDirection>> DOctant;
    protected static Vector<Vector<EDirection>> DTileDirections;


    /*
     * Fills the direction vectors in DOctant and DTileDirections
     * There is no initializer list construct for vectors
     */
    static protected void initVectors() {
        DOctant = new Vector<Vector<EDirection>>();
        DTileDirections = new Vector<Vector<EDirection>>();

        //These temp vectors will be used to fill DOctant and DTileDirections
        Vector<EDirection> temp1 = new Vector<EDirection>(1);
        Vector<EDirection> temp2 = new Vector<EDirection>(3);
        Vector<EDirection> temp3 = new Vector<EDirection>(3);
        Vector<EDirection> temp4 = new Vector<EDirection>(3);

        //Initialize DOctant
        temp1.add(EDirection.Max);
        DOctant.add(temp1);

        //Create the inner vectors for DTileDirections
        //temp2 is the first inner vector
        temp2.add(EDirection.NorthWest);
        temp2.add(EDirection.North);
        temp2.add(EDirection.NorthEast);

        //temp3 is the second inner vector
        temp3.add(EDirection.West);
        temp3.add(EDirection.Max);
        temp3.add(EDirection.East);

        //temp4 is the third inner vector
        temp4.add(EDirection.SouthWest);
        temp4.add(EDirection.South);
        temp4.add(EDirection.SouthEast);

        //DTileDirections looks like this...
        //{ {NorthWest, North, NorthEast}
        //  {West, Max, East}
        //  {SouthWest, South, SouthEast}}
        DTileDirections.add(temp2);
        DTileDirections.add(temp3);
        DTileDirections.add(temp4);
    }

    /*
     * Three constructors
     */
    protected Position() {
        DX = 0;
        DY = 0;
    }

    protected Position(int x, int y) {
        DX = x;
        DY = y;
    }

    protected Position(Position pos) {
        DX = pos.DX;
        DY = pos.DY;
    }

    protected void set(int x, int y) {
        DX = x;
        DY = y;
    }

    protected void set(Position pos) {
        DX = pos.DX;
        DY = pos.DY;
    }


    /*
     * Replaces overloaded == from C++
     * Negate with ! to get != functionality
     */
    protected boolean equals(Position pos) {
        return (DX == pos.DX) && (DY == pos.DY);
    }

    /*
     * In progress...
     * A.J. Collins 10/27
     */
    protected EDirection directionTo(Position pos) {
        //divX and dixY get the change in X and Y in terms of halfTileWidth
        Position deltaPosition = new Position((pos.DX - DX), (pos.DY - DY));
        int divX = deltaPosition.DX / DHalfTileWidth;
        int divY = deltaPosition.DY / DHalfTileHeight;
        int div;

        //make sure they're positive
        divX = 0 > divX ? -divX : divX;
        divY = 0 > divY ? -divY : divY;

        //Sets div to the greater of divX and divY
        div = divX > divY ? divX : divY;

        if (div != 0) {
            deltaPosition.DX /= div;
            deltaPosition.DY /= div;
        }
        deltaPosition.DX += DHalfTileWidth;
        deltaPosition.DY += DHalfTileHeight;
        if (0 > deltaPosition.DX) {
            deltaPosition.DX = 0;
        }
        if (0 > deltaPosition.DY) {
            deltaPosition.DY = 0;
        }
        if (DTileWidth <= deltaPosition.DX) {
            deltaPosition.DX = DTileWidth - 1;
        }
        if (DTileHeight <= deltaPosition.DY) {
            deltaPosition.DY = DTileHeight - 1;
        }
        return deltaPosition.tileOctant();
    }

    /*
     *
     */
    protected EDirection tileOctant() {
        return DOctant.get((int)DY % DTileHeight).get((int)DX % DTileWidth);
    }

    /*
     * Gets the distance squared between two Positions
     */
    protected int distanceSquared(Position pos) {
        int deltaX = pos.DX - DX;
        int deltaY = pos.DY - DY;

        return deltaX * deltaX + deltaY * deltaY;
    }

    /*
     * @param Position
     * @return long
     *
     * A fast way to calculate the integer square root
     * A.J. Collins
     */
    protected long distance(Position pos) {

        long distanceSquared, result, one;

        distanceSquared = distanceSquared(pos);
        result = 0;

        /*
         * The following block sets one as the highest power of 4
         * that is also <= distanceSquared
         */
        one = 1 << 30; //equivalent to 1073741824 in decimal
        while (one > distanceSquared) {
            one >>= 2;
        }

        while (0 != one) {
            if (distanceSquared >= result + one) {
                distanceSquared -= result + one;
                result += one << 1;  // <-- faster than 2 * one
            }
            result >>= 1;
            one >>= 2;
        }
        return result;
    }

    public int X() {
        return DX;
    }

    public int X(int x) { return DX = x;
    }

    public int incrementX(int x) {
        DX += x;
        return DX;
    }

    public int decrementX(int x) {
        DX -= x;
        return DX;
    }

    public int Y() {
        return DY;
    }

    public int Y(int y) {
        return DY = y;
    }

    public int incrementY(int y) {
        DY += y;
        return DY;
    }

    public int decrementY(int y) {
        DY -= y;
        return DY;
    }


    /*
     * sets DTileWidth, DTileHeight, DHalfTileWidth, and DHalfTileHeight
     *
     */
    public static void setTileDimensions(int width, int height) {
        initVectors();

        if ((0 < width) && (0 < height)) {
            DTileWidth = width;
            DTileHeight = height;
            DHalfTileWidth = width / 2;
            DHalfTileHeight = height / 2;


            //iterate through the vector, resize each inner row
            DOctant.setSize(DTileHeight);
            for (int i = 0; i < DOctant.size(); i++) {
                Vector<EDirection> modRow =  new Vector<EDirection>();
                modRow.setSize(DTileWidth);
                DOctant.set(i,modRow);
            }


            for (int Y = 0; Y < DTileHeight; Y++) {
                Vector<EDirection> modRow = DOctant.get(Y);
                for (int X = 0; X < DTileWidth; X++) {
                    int xDistance = X - DHalfTileWidth;
                    int yDistance = Y - DHalfTileHeight;
                    boolean negativeX = xDistance < 0;
                    boolean negativeY = yDistance > 0; // FIXME: Top of screen is 0
                    double sinSquared;

                    xDistance *= xDistance;
                    yDistance *= yDistance;

                    if (0 == (xDistance + yDistance)) {
                        modRow.set(X, EDirection.Max);
                        continue;
                    }
                    sinSquared = (double) yDistance / (xDistance + yDistance);

                    if (0.1464466094 > sinSquared) {
                        // East or West
                        if (negativeX) {
                            modRow.set(X, EDirection.West); // West
                        } else {
                            modRow.set(X, EDirection.East); // East
                        }
                    } else if (0.85355339059 > sinSquared) {
                        // NE, SE, SW, NW
                        if (negativeY) {
                            if (negativeX) {
                                modRow.set(X, EDirection.SouthWest); // SW
                            } else {
                                modRow.set(X, EDirection.SouthEast); // SE
                            }
                        } else {
                            if (negativeX) {
                                modRow.set(X, EDirection.NorthWest); // NW
                            } else {
                                modRow.set(X, EDirection.NorthEast); // NE
                            }
                        }
                    } else {
                        // North or South
                        if (negativeY) {
                            modRow.set(X, EDirection.South); // South
                        } else {
                            modRow.set(X, EDirection.North); // North
                        }
                    }
                }
                DOctant.set(Y,modRow);
            }
        }
    }

    public static int tileWidth() {
        return DTileWidth;
    }

    public  static int tileHeight() {
        return DTileHeight;
    }

    public static int halfTileWidth() {
        return DHalfTileWidth;
    }

    public static int halfTileHeight() {
        return DHalfTileHeight;
    }

}

