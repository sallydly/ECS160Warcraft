package com.warcraftII.player_asset;

//import com.warcraftII.asset.player.PlayerAsset;

import com.warcraftII.position.TilePosition;
import com.warcraftII.position.UnitPosition;
import com.warcraftII.units.Unit;

import java.util.List;
import java.util.Vector;

public class VisibilityMap {
    public enum ETileVisibility{
        None,
        PartialPartial,
        Partial,
        Visible,
        SeenPartial,
        Seen
    }

    public Vector<Vector<ETileVisibility>> gameMap;
    public int maxVisibility;
    public int totalMapTiles;
    public int unseenTiles;

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
        System.out.println("Width " + width);
        System.out.println("Height " + height);
        System.out.println("MaxVisibility " + maxVisibility);
        final int TARGET_WIDTH = width + 2 * maxVisibility;
        final int TARGET_HEIGHT =  height + 2 * maxVisibility;
        this.maxVisibility = maxVisibility;

        gameMap = new Vector<Vector<ETileVisibility>>();

        for(int i = 0; i < TARGET_HEIGHT; ++i) {
            gameMap.add(new Vector<ETileVisibility>(width + 2 * maxVisibility));
            for(int j = 0; j < TARGET_WIDTH; ++j) {
                gameMap.get(i).add(ETileVisibility.None);
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

    public final ETileVisibility TileType(int xIndex, int yIndex) {
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

    public void updateAssets(List<StaticAsset> assets) {
        for(int i = 0; i < gameMap.size(); ++i) {
            for(int j = 0; j < gameMap.get(i).size(); ++j) {
                ETileVisibility currentTile = gameMap.get(i).get(j);
                if ((ETileVisibility.Visible == currentTile) || (ETileVisibility.Partial == currentTile)) {
                    gameMap.get(i).set(j, ETileVisibility.Seen);
                } else if (ETileVisibility.PartialPartial == currentTile) {
                    gameMap.get(i).set(j, ETileVisibility.SeenPartial);
                }
            }
        }

        for(StaticAsset CurAsset : assets) {
            if(CurAsset != null) {
                //System.out.println("Altering VisibilityMap with StaticAsset");
                TilePosition Anchor = CurAsset.tilePosition();
                int Sight = CurAsset.EffectiveSight() + CurAsset.Size()/2;
                int SightSquared = Sight * Sight;
                //System.out.println("Anchor: " + Anchor.X() + " " + Anchor.Y());
                //System.out.println("Sight: " + Sight + " SightSquared " + SightSquared);
                //System.out.println("MaxVisibility: " + maxVisibility);
                int oldX = Anchor.X();
                int oldY = Anchor.Y();
                Anchor.X(Anchor.X() + CurAsset.Size()/2);
                Anchor.Y(Anchor.Y() + CurAsset.Size()/2);

                //System.out.println("Asset Size " + CurAsset.Size());
                //System.out.println("New Anchor: " + Anchor.X() + " " + Anchor.Y());

                for(int X = 0; X <= Sight; X++) {
                    int XSquared = X * X;
                    int XSquared1 = (X > 0) ? (X - 1) * (X - 1) : 0;

                    for(int Y = 0; Y <= Sight; Y++){
                        int YSquared = Y * Y;
                        int YSquared1 = (Y > 0) ? (Y - 1) * (Y - 1) : 0;

                        if((XSquared + YSquared) < SightSquared) {
                            // Visible
                            if(Anchor.Y() - Y >= 0 && Anchor.X() - X >= 0) {
                                gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Visible);
                                gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Visible);
                                gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Visible);
                                gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Visible);
                            }
                        } else if((XSquared1 + YSquared1) < SightSquared){
                            // Partial
                            if(Anchor.Y() - Y >= 0 && Anchor.X() - X >= 0) {
                                ETileVisibility CurVis = gameMap.get(Anchor.Y() - Y + maxVisibility).get(Anchor.X() - X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                                CurVis = gameMap.get(Anchor.Y() - Y + maxVisibility).get(Anchor.X() + X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                                CurVis = gameMap.get(Anchor.Y() + Y + maxVisibility).get(Anchor.X() - X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                                CurVis = gameMap.get(Anchor.Y() + Y + maxVisibility).get(Anchor.X() + X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                            }
                        }
                    }
                }
                Anchor.X(oldX);
                Anchor.Y(oldY);
            }
        }

        int MinX, MinY, MaxX, MaxY;
        MinY = maxVisibility;
        MaxY = gameMap.size() - maxVisibility;
        MinX = maxVisibility;
        MaxX = gameMap.get(0).size() - maxVisibility;
        unseenTiles = 0;
        for(int Y = MinY; Y < MaxY; Y++){
            for(int X = MinX; X < MaxX; X++){
                if(ETileVisibility.None == gameMap.get(Y).get(X)){
                    unseenTiles++;
                }
            }
        }
    }

    public void updateUnits(List<Unit.IndividualUnit> individualUnitList) {
        for(int i = 0; i < gameMap.size(); ++i) {
            for(int j = 0; j < gameMap.get(i).size(); ++j) {
                ETileVisibility currentTile = gameMap.get(i).get(j);
                if ((ETileVisibility.Visible == currentTile) || (ETileVisibility.Partial == currentTile)) {
                    gameMap.get(i).set(j, ETileVisibility.Seen);
                } else if (ETileVisibility.PartialPartial == currentTile) {
                    gameMap.get(i).set(j, ETileVisibility.SeenPartial);
                }
            }
        }

        for(Unit.IndividualUnit unit : individualUnitList) {
            if(unit != null) {
                unit.isVisible = true;
                //System.out.println("Altering VisibilityMap with IndividualUnit");
//                TilePosition Anchor = new TilePosition((int)unit.getX() / 32, getHeight() - (int)unit.getY() / 32 - 1);
                TilePosition Anchor = new TilePosition(new UnitPosition((int)(unit.getMidX()), (int)(unit.getMidY())));
                int Sight = unit.sight;
                int SightSquared = Sight * Sight;
                //System.out.println("Anchor: " + Anchor.X() + " " + Anchor.Y());
                //System.out.println("Sight: " + Sight + " SightSquared " + SightSquared);
                //System.out.println("MaxVisibility: " + maxVisibility);

                for(int X = 0; X <= Sight; X++) {
                    int XSquared = X * X;
                    int XSquared1 = (X > 0) ? (X - 1) * (X - 1) : 0;

                    for(int Y = 0; Y <= Sight; Y++){
                        int YSquared = Y * Y;
                        int YSquared1 = (Y > 0) ? (Y - 1) * (Y - 1) : 0;

                        if((XSquared + YSquared) < SightSquared) {
                            // Visible
                            if(Anchor.Y() - Y >= 0 && Anchor.X() - X >= 0) {
                                gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Visible);
                                gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Visible);
                                gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Visible);
                                gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Visible);
                            }
                        } else if((XSquared1 + YSquared1) < SightSquared){
                            // Partial
                            if(Anchor.Y() - Y >= 0 && Anchor.X() - X >= 0) {
                                ETileVisibility CurVis = gameMap.get(Anchor.Y() - Y + maxVisibility).get(Anchor.X() - X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                                CurVis = gameMap.get(Anchor.Y() - Y + maxVisibility).get(Anchor.X() + X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() - Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                                CurVis = gameMap.get(Anchor.Y() + Y + maxVisibility).get(Anchor.X() - X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() - X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                                CurVis = gameMap.get(Anchor.Y() + Y + maxVisibility).get(Anchor.X() + X + maxVisibility);
                                if (ETileVisibility.Seen == CurVis) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.Partial);
                                } else if ((ETileVisibility.None == CurVis) || (ETileVisibility.SeenPartial == CurVis)) {
                                    gameMap.get(Anchor.Y() + Y + maxVisibility).set(Anchor.X() + X + maxVisibility, ETileVisibility.PartialPartial);
                                }
                            }
                        }
                    }
                }
            }
        }

        int MinX, MinY, MaxX, MaxY;
        MinY = maxVisibility;
        MaxY = gameMap.size() - maxVisibility;
        MinX = maxVisibility;
        MaxX = gameMap.get(0).size() - maxVisibility;
        unseenTiles = 0;
        for(int Y = MinY; Y < MaxY; Y++){
            for(int X = MinX; X < MaxX; X++){
                if(ETileVisibility.None == gameMap.get(Y).get(X)){
                    unseenTiles++;
                }
            }
        }
    }

}
