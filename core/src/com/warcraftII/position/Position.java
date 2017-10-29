package com.warcraftII.position;

import com.warcraftII.GameDataTypes.*;

import java.util.ListIterator;
import java.util.Vector;

/**
 * Created by ajc on 10/27/17.
 * Ported from position.cpp
 */

public class Position {

    protected int DX;
    protected int DY;

    protected static int DTileWidth;
    protected static int DTileHeight;
    protected static int DHalfTileWidth;
    protected static int DHalfTileHeight;

    /*
     * Should call initVectors once a Position object is instantiated
     */
    static {
        initVectors();
    }

    protected static Vector< Vector < EDirection > > DOctant;
    protected static Vector< Vector < EDirection > > DTileDirections;

    /*
     * Fills the direction vectors in DOctant and DTileDirections
     * There is no initializer list construct for vectors
     */
    static protected void initVectors() {

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

        DTileDirections.add(temp2);
        DTileDirections.add(temp3);
        DTileDirections.add(temp4);
    }

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

    //Position &operator=(Position pos);

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
     * Not sure what this does / how it gets directionTo yet...
     * A.J. Collins 10/27
     */
    protected EDirection directionTo(Position pos){
        Position deltaPosition = new Position((pos.DX - DX), (pos.DY - DY));
        int divX = deltaPosition.DX / halfTileWidth(); //How does this avoid divide by zero?
        int divY = deltaPosition.DY / halfTileHeight();
        int div;

        //makes sure they're positive
        divX = 0 > divX ? -divX : divX;
        divY = 0 > divY ? -divY : divY;

        //Sets div to the greater of divx and divy
        div = divX > divY ? divX : divY;

        if(div != 0){
            deltaPosition.DX /= div;
            deltaPosition.DY /= div;
        }
        deltaPosition.DX += halfTileWidth();
        deltaPosition.DY += halfTileHeight();
        if(0 > deltaPosition.DX){
            deltaPosition.DX = 0;
        }
        if(0 > deltaPosition.DY){
            deltaPosition.DY = 0;
        }
        if(tileWidth() <= deltaPosition.DX){
            deltaPosition.DX = tileWidth() - 1;
        }
        if(tileHeight() <= deltaPosition.DY){
            deltaPosition.DY = tileHeight() - 1;
        }
        return deltaPosition.tileOctant();
    }

    protected EDirection tileOctant() {
        return DOctant.get(DY % DTileHeight).get(DX % DTileWidth);
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
        while(one > distanceSquared){
            one >>= 2;
        }

        while(0 != one){
            if(distanceSquared >= result + one){
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

    public int X(int x){
        return DX = x;
    }

    public int incrementX(int x){
        DX += x;
        return DX;
    }

    public int decrementX(int x){
        DX -= x;
        return DX;
    }

    public int Y(){
        return DY;
    }

    public int Y(int y){
        return DY = y;
    }

    public int incrementY(int y){
        DY += y;
        return DY;
    }

    public int decrementY(int y){
        DY -= y;
        return DY;
    }

    /*
     * FIXME. Having trouble resizing all vectors in DOctant
     */
    public static void setTileDimensions(int width, int height){
        if((0 < width) && (0 < height)) {
            DTileWidth = width;
            DTileHeight = height;
            DHalfTileWidth = width / 2;
            DHalfTileHeight = height / 2;

            DOctant.setSize(DTileHeight);

            //iterate through the vector, resize each inner row
            ListIterator itr = DOctant.listIterator();
            DOctant.get(0).setSize(DTileWidth);
            //FIXME
            //Need to resize every vector in DOctant
//            while(itr.hasNext()) {
//                (itr.next()).setSize(DTileWidth);
//            }

            for(int Y = 0; Y < DTileHeight; Y++){
                for(int X = 0; X < DTileWidth; X++){
                    int xDistance = X - DHalfTileWidth;
                    int yDistance = Y - DHalfTileHeight;
                    boolean negativeX = xDistance < 0;
                    boolean negativeY = yDistance > 0; // Top of screen is 0
                    double sinSquared;

                    xDistance *= xDistance;
                    yDistance *= yDistance;

                    if(0 == (xDistance + yDistance)){
                        DOctant.get(Y).set(X, EDirection.Max);
                        continue;
                    }
                    sinSquared = (double)yDistance / (xDistance + yDistance);

                    if(0.1464466094 > sinSquared){
                        // East or West
                        if(negativeX){
                            DOctant.get(Y).set(X, EDirection.West); // West
                        }
                        else{
                            DOctant.get(Y).set(X, EDirection.East); // East
                        }
                    }
                    else if(0.85355339059 > sinSquared){
                        // NE, SE, SW, NW
                        if(negativeY){
                            if(negativeX){
                                DOctant.get(Y).set(X, EDirection.SouthWest); // SW
                            }
                            else{
                                DOctant.get(Y).set(X, EDirection.SouthEast); // SE
                            }
                        }
                        else{
                            if(negativeX){
                                DOctant.get(Y).set(X, EDirection.NorthWest); // NW
                            }
                            else{
                                DOctant.get(Y).set(X, EDirection.NorthEast); // NE
                            }
                        }
                    }
                    else{
                        // North or South
                        if(negativeY){
                            DOctant.get(Y).set(X, EDirection.South); // South
                        }
                        else{
                            DOctant.get(Y).set(X, EDirection.North); // North
                        }
                    }
                }
            }
        }
    }

    public static int tileWidth(){
        return DTileWidth;
    }

    public static int tileHeight(){
        return DTileHeight;
    }

    public static int halfTileWidth(){
        return DHalfTileWidth;
    }

    public static int halfTileHeight(){
        return DHalfTileHeight;
    }

}
