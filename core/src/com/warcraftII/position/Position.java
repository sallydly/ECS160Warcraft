package com.warcraftII.position;

import com.warcraftII.GameDataTypes.*;

import java.util.Vector;

/**
 * CAUTION: This doesnt work as of 10/27/17
 * I'm in the middle of porting. Name refactoring will be done once it is all ported
 *
 * Created by ajc on 10/27/17.
 * Porting from position.cpp
 */

/*
 * TODO: Refactor names for all the following
 * HalfTileWidth()
 * HalfTileHeight()
 */

public class Position {

    protected:
        int DX;
        int DY;

        static int DTileWidth;
        static int DTileHeight;
        static int DHalfTileWidth;
        static int DHalfTileHeight;

        //TODO match these vectors to the ones in position.cpp
        static Vector<Vector<EDirection>> DOctant;
        static Vector< Vector < EDirection > > DTileDirections;

        Position(int x, int y) {
            DX = x;
            DY = y;
        }

        Position(Position pos);

        //Position &operator=(Position pos);

        bool equals(Position pos);

        /*
         * Not sure what this does / how it gets diretction to yet...
         * A.J. Collins 10/27
         */
        EDirection DirectionTo(Position pos){
            Position DeltaPosition(pos.DX - DX, pos.DY - DY);
            int DivX = DeltaPosition.DX / HalfTileWidth(); //How does this avoid divide by zero?
            int DivY = DeltaPosition.DY / HalfTileHeight();
            int Div;

            //makes sure they're positive
            DivX = 0 > DivX ? -DivX : DivX;
            DivY = 0 > DivY ? -DivY : DivY;

            //Sets div to the greater of divx and divy
            Div = DivX > DivY ? DivX : DivY;

            if(Div){
                DeltaPosition.DX /= Div;
                DeltaPosition.DY /= Div;
            }
            DeltaPosition.DX += HalfTileWidth();
            DeltaPosition.DY += HalfTileHeight();
            if(0 > DeltaPosition.DX){
                DeltaPosition.DX = 0;
            }
            if(0 > DeltaPosition.DY){
                DeltaPosition.DY = 0;
            }
            if(TileWidth() <= DeltaPosition.DX){
                DeltaPosition.DX = TileWidth() - 1;
            }
            if(TileHeight() <= DeltaPosition.DY){
                DeltaPosition.DY = TileHeight() - 1;
            }
            return DeltaPosition.TileOctant();
        }

        EDirection TileOctant();

        /*
         * Gets the distance squared between two positions
         */
        int DistanceSquared(Position pos) {
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
        int Distance(Position pos) {

            long distanceSquared, result, one;

            distanceSquared = DistanceSquared(pos);
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

    public:
    int X() {
        return DX;
    };

    int setX(int x){
        return DX = x;
    };

    int IncrementX(int x){
        DX += x;
        return DX;
    };

    int DecrementX(int x){
        DX -= x;
        return DX;
    };

    int Y(){
        return DY;
    };

    int setY(int y){
        return DY = y;
    };

    int IncrementY(int y){
        DY += y;
        return DY;
    };

    int DecrementY(int y){
        DY -= y;
        return DY;
    };

    static void SetTileDimensions(int width, int height){
        if((0 < width) && (0 < height)) {
            DTileWidth = width;
            DTileHeight = height;
            DHalfTileWidth = width / 2;
            DHalfTileHeight = height / 2;

            DOctant.resize(DTileHeight);

            //iterate through the vector
            for(auto &Row : DOctant){
                Row.resize(DTileWidth);
            }

            for(int Y = 0; Y < DTileHeight; Y++){
                for(int X = 0; X < DTileWidth; X++){
                    int XDistance = X - DHalfTileWidth;
                    int YDistance = Y - DHalfTileHeight;
                    bool NegativeX = XDistance < 0;
                    bool NegativeY = YDistance > 0; // Top of screen is 0
                    double SinSquared;

                    XDistance *= XDistance;
                    YDistance *= YDistance;

                    if(0 == (XDistance + YDistance)){
                        DOctant[Y][X] = EDirection::Max;
                        continue;
                    }
                    SinSquared = (double)YDistance / (XDistance + YDistance);

                    if(0.1464466094 > SinSquared){
                        // East or West
                        if(NegativeX){
                            DOctant[Y][X] = EDirection::West; // West
                        }
                        else{
                            DOctant[Y][X] = EDirection::East; // East
                        }
                    }
                    else if(0.85355339059 > SinSquared){
                        // NE, SE, SW, NW
                        if(NegativeY){
                            if(NegativeX){
                                DOctant[Y][X] = EDirection::SouthWest; // SW
                            }
                            else{
                                DOctant[Y][X] = EDirection::SouthEast; // SE
                            }
                        }
                        else{
                            if(NegativeX){
                                DOctant[Y][X] = EDirection::NorthWest; // NW
                            }
                            else{
                                DOctant[Y][X] = EDirection::NorthEast; // NE
                            }
                        }
                    }
                    else{
                        // North or South
                        if(NegativeY){
                            DOctant[Y][X] = EDirection::South; // South
                        }
                        else{
                            DOctant[Y][X] = EDirection::North; // North
                        }
                    }
                }
            }
        }
    }

    static int TileWidth(){
        return DTileWidth;
    };

    static int TileHeight(){
        return DTileHeight;
    };

    static int HalfTileWidth(){
        return DHalfTileWidth;
    };

    static int HalfTileHeight(){
        return DHalfTileHeight;
    };

}
