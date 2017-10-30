package com.warcraftII.asset;

import com.warcraftII.asset.player.PlayerAsset;

import java.util.List;
import java.util.Vector;

public class VisibilityMap{
    public enum ETileVisibility{
        None,
        PartialPartial,
        Partial,
        Visible,
        SeenPartial,
        Seen
    };

    protected Vector<Vector<ETileVisibility>> gameMap;
    protected int maxVisibility;
    protected int totalMapTiles;
    protected int unseenTiles;

    /**
     * Constructor, builds visibility gameMap based on input width and height
     * Begins with all tiles unseen
     *
     * @param width The width of the gameMap
     * @param height The height of the gameMap
     * @param maxVisibility The distance from an asset the user can see
     *
     */
    public VisibilityMap(int width, int height, int maxVisibility) {
        this.maxVisibility = maxVisibility;
        gameMap.setSize(height + 2 * maxVisibility);

        for(Vector row : gameMap) {
           row.setSize(width + 2 * maxVisibility);
           for(Object tile : row) {
               tile = ETileVisibility.None;
           }
        }

        totalMapTiles = width * height;
        unseenTiles = totalMapTiles;
    }

    /**
     * Constructor, copies data members from input visibility gameMap
     *
     * @param gameMap CVisibilityMap class object that you want to copy
     *
     */
    public VisibilityMap(final VisibilityMap gameMap){
        this.maxVisibility = gameMap.maxVisibility;
        this.gameMap = gameMap.gameMap;
        this.totalMapTiles = gameMap.totalMapTiles;
        this.unseenTiles = gameMap.unseenTiles;
    }

    /**
     * Returns the width of the map, less the added visibility tiles
     *
     * @return int width of the map
     *
     */
    public final int getWidth() {
        if(gameMap.size() > 0) {
            return gameMap.get(0).size() - 2 * maxVisibility;
        }

        return 0;
    }

    /**
     * Returns the height of the map, less the added visibility tiles
     *
     * @return int height of the map
     *
     */
    public final int getHeight() {
        return gameMap.size() - 2 * maxVisibility;
    }

    /**
     * Returns the percentage of tiles the user has seen
     *
     * @param max Usually 100, to convert decimal to percent
     *
     * @return int percent seen
     *
     */
    public final int seenPercent(int max) {
        return (max * (totalMapTiles - unseenTiles)) / totalMapTiles;

    }

    final ETileVisibility TileType(int xIndex, int yIndex) {
        if((-maxVisibility > xIndex)||(-maxVisibility > yIndex)){
            return ETileVisibility.None;
        }
        if(gameMap.size() <= yIndex+maxVisibility){
            return ETileVisibility.None;
        }
        if(gameMap.get(yIndex+maxVisibility).size() <= xIndex+maxVisibility){
            return ETileVisibility.None;
        }
        return gameMap.get(yIndex+maxVisibility).get(xIndex+maxVisibility);
    }

    public List<PlayerAsset> update(List<PlayerAsset> assets) {
        for(Vector row : gameMap) {
            for(Object tile : row) {
                if((ETileVisibility.Visible == tile) || ETileVisibility.Partial == tile) {
                    tile = ETileVisibility.Seen;
                } else if(ETileVisibility.PartialPartial == tile) {
                    tile = ETileVisibility.SeenPartial;
                }
        }

        for(PlayerAsset weakAsset : assets) {
            if
        }
        for(auto WeakAsset : assets){
            if(auto CurAsset = WeakAsset.lock()){
                CTilePosition Anchor = CurAsset->TilePosition();
                int Sight = CurAsset->EffectiveSight() + CurAsset->Size()/2;
                int SightSquared = Sight * Sight;
                Anchor.X(Anchor.X() + CurAsset->Size()/2);
                Anchor.Y(Anchor.Y() + CurAsset->Size()/2);
                for(int X = 0; X <= Sight; X++){
                    int XSquared = X * X;
                    int XSquared1 = X ? (X - 1) * (X - 1) : 0;

                    for(int Y = 0; Y <= Sight; Y++){
                        int YSquared = Y * Y;
                        int YSquared1 = Y ? (Y - 1) * (Y - 1) : 0;

                        if((XSquared + YSquared) < SightSquared){
                            // Visible
                            DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility] = ETileVisibility::Visible;
                            DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility] = ETileVisibility::Visible;
                            DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility] = ETileVisibility::Visible;
                            DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility] = ETileVisibility::Visible;
                        }
                        else if((XSquared1 + YSquared1) < SightSquared){
                            // Partial
                            ETileVisibility CurVis = DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility];
                            if(ETileVisibility::Seen == CurVis){
                                DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility] = ETileVisibility::Partial;
                            }
                            else if((ETileVisibility::None == CurVis)||(ETileVisibility::SeenPartial == CurVis)){
                                DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility] = ETileVisibility::PartialPartial;
                            }
                            CurVis = DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility];
                            if(ETileVisibility::Seen == CurVis){
                                DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility] = ETileVisibility::Partial;
                            }
                            else if((ETileVisibility::None == CurVis)||(ETileVisibility::SeenPartial == CurVis)){
                                DMap[Anchor.Y() - Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility] = ETileVisibility::PartialPartial;
                            }
                            CurVis = DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility];
                            if(ETileVisibility::Seen == CurVis){
                                DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility] = ETileVisibility::Partial;
                            }
                            else if((ETileVisibility::None == CurVis)||(ETileVisibility::SeenPartial == CurVis)){
                                DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() - X + DMaxVisibility] = ETileVisibility::PartialPartial;
                            }
                            CurVis = DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility];
                            if(ETileVisibility::Seen == CurVis){
                                DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility] = ETileVisibility::Partial;
                            }
                            else if((ETileVisibility::None == CurVis)||(ETileVisibility::SeenPartial == CurVis)){
                                DMap[Anchor.Y() + Y + DMaxVisibility][Anchor.X() + X + DMaxVisibility] = ETileVisibility::PartialPartial;
                            }
                        }
                    }
                }
            }
        }
        int MinX, MinY, MaxX, MaxY;
        MinY = DMaxVisibility;
        MaxY = DMap.size() - DMaxVisibility;
        MinX = DMaxVisibility;
        MaxX = DMap[0].size() - DMaxVisibility;
        DUnseenTiles = 0;
        for(int Y = MinY; Y < MaxY; Y++){
            for(int X = MinX; X < MaxX; X++){
                if(ETileVisibility::None == DMap[Y][X]){
                    DUnseenTiles++;
                }
            }
        }

    }    }

};

