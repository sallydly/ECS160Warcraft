package com.warcraftII.asset;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.warcraftII.data_source.CommentSkipLineDataSource;
import com.warcraftII.data_source.DataSource;

import java.util.Vector;

import static java.lang.Math.sqrt;

public class GraphicTileset {
    private Vector< String > DTileNames;
    private int DTileCount; //# of diff type of tiles of this tileset
//    protected int DTileWidth;
//    protected int DTileHeight;
//    protected int D32TileCount; //# of 32x32 tiles needed to create one tile
//    protected int DTileHalfWidth;
//    protected int DTileHalfHeight;

    GraphicTileset(){
        DTileCount = 0;
    }

    //TODO: unsure if needed (delete later if GroupNames is not needed
//    bool CGraphicTileset::ParseGroupName(const std::string &tilename, std::string &aniname, int &anistep){
//        size_t LastIndex = tilename.length();
//
//        if(!LastIndex){
//            return false;
//        }
//        do{
//            LastIndex--;
//            if(!isdigit(tilename[LastIndex])){
//                if(LastIndex + 1 == tilename.length()){
//                    return false;
//                }
//                aniname = tilename.substr(0,LastIndex+1);
//                anistep = std::stoi(tilename.substr(LastIndex+1));
//                return true;
//            }
//        }while(LastIndex);
//
//        return false;
//    }

    //TODO: unsure if needed (delete later if GroupNames is not needed
//    void CGraphicTileset::UpdateGroupNames(){
//        DGroupSteps.clear();
//        DGroupNames.clear();
//        for(int Index = 0; Index < DTileCount; Index++){
//            std::string GroupName;
//            int GroupStep;
//
//            if(ParseGroupName(DTileNames[Index], GroupName, GroupStep)){
//                if(DGroupSteps.find(GroupName) != DGroupSteps.end()){
//                    if(DGroupSteps.at(GroupName) <= GroupStep){
//                        DGroupSteps[GroupName] = GroupStep + 1;
//                    }
//                }
//                else{
//                    DGroupSteps[GroupName] = GroupStep + 1;
//                    DGroupNames.push_back(GroupName);
//                }
//            }
//        }
//    }

    //TODO: unsure what this does (looking at the code, does not seem to be used. if so, DELETE LATER
//    public int TileCount(int count){
//
//        if(0 > count){
//            return DTileCount;
//        }
//        if(!DTileWidth || !DTileHeight){
//            return DTileCount;
//        }
//        if(count < DTileCount){
//            auto Iterator = DMapping.begin();
//            DTileCount = count;
//
//            while(DMapping.end() != Iterator){
//                if(Iterator->second >= DTileCount){
//                    Iterator = DMapping.erase(Iterator);
//                }
//                else{
//                    Iterator++;
//                }
//            }
//            DTileNames.resize(DTileCount);
//            UpdateGroupNames();
//            return DTileCount;
//        }
//
//        auto TempSurface = CGraphicFactory::CreateSurface(DTileWidth, count * DTileHeight, DSurfaceTileset->Format());
//
//        if(!TempSurface){
//            return DTileCount;
//        }
//
//        DTileNames.resize(count);
//        TempSurface->Copy(DSurfaceTileset, 0, 0, -1, -1, 0, 0);
//        DSurfaceTileset = TempSurface;
//        DTileCount = count;
//        return DTileCount;
//    }

    //TODO: remove tile from map
//    bool CGraphicTileset::ClearTile(int index){
//
//        if((0 > index)||(index >= DTileCount)){
//            return false;
//        }
//        if(!DSurfaceTileset){
//            return false;
//        }
//
//        DSurfaceTileset->Clear(0, index * DTileHeight, DTileWidth, DTileHeight);
//        return true;
//    }

//    bool CGraphicTileset::DuplicateTile(int destindex, const std::string &tilename, int srcindex){
//        if((0 > srcindex)||(0 > destindex)||(srcindex >= DTileCount)||(destindex >= DTileCount)){
//            return false;
//        }
//        if(tilename.empty()){
//            return false;
//        }
//        ClearTile(destindex);
//
//        DSurfaceTileset->Copy(DSurfaceTileset, 0, destindex * DTileHeight, DTileWidth, DTileHeight, 0, srcindex * DTileHeight);
//
//        auto OldMapping = DMapping.find(DTileNames[destindex]);
//        if(DMapping.end() != OldMapping){
//            DMapping.erase(OldMapping);
//        }
//        DTileNames[destindex] = tilename;
//        DMapping[tilename] = destindex;
//
//        return true;
//    }
//
//    bool CGraphicTileset::DuplicateClippedTile(int destindex, const std::string &tilename, int srcindex, int clipindex){
//        if((0 > srcindex)||(0 > destindex)||(0 > clipindex)||(srcindex >= DTileCount)||(destindex >= DTileCount)||(clipindex >= DClippingMasks.size())){
//            return false;
//        }
//        if(tilename.empty()){
//            return false;
//        }
//        ClearTile(destindex);
//        DSurfaceTileset->CopyMaskSurface(DSurfaceTileset, 0, destindex * DTileHeight, DClippingMasks[clipindex], 0, srcindex * DTileHeight);
//        auto OldMapping = DMapping.find(DTileNames[destindex]);
//        if(DMapping.end() != OldMapping){
//            DMapping.erase(OldMapping);
//        }
//        DTileNames[destindex] = tilename;
//        DMapping[tilename] = destindex;
//        if(destindex >= DClippingMasks.size()){
//            DClippingMasks.resize(destindex + 1);
//        }
//        DClippingMasks[destindex] = CGraphicFactory::CreateSurface(DTileWidth, DTileHeight, CGraphicSurface::ESurfaceFormat::A1);
//        DClippingMasks[destindex]->Copy(DSurfaceTileset, 0, 0, DTileWidth, DTileHeight, 0, destindex * DTileHeight);
//
//        return true;
//    }

    //TODO: unsure what it does. Finds tile on map?
//    int CGraphicTileset::FindTile(const std::string &tilename) const{
//        auto Iterator = DMapping.find(tilename);
//        if(DMapping.end() != Iterator){
//            return Iterator->second;
//        }
//        return -1;
//    }

    //TODO: Delete later if GroupNames irrelevant
//    int CGraphicTileset::GroupCount() const{
//        return DGroupNames.size();
//    }

//    std::string CGraphicTileset::GroupName(int index) const{
//        if(0 > index){
//            return "";
//        }
//        if(DGroupNames.size() <= index){
//            return "";
//        }
//
//        return DGroupNames[index];
//    }

//    int CGraphicTileset::GroupSteps(int index) const{
//        if(0 > index){
//            return 0;
//        }
//        if(DGroupNames.size() <= index){
//            return 0;
//        }
//        return DGroupSteps.at(DGroupNames[index]);
//    }
//
//    int CGraphicTileset::GroupSteps(const std::string &Groupname) const{
//        if(DGroupSteps.find(Groupname) != DGroupSteps.end()){
//            return DGroupSteps.at(Groupname);
//        }
//        return 0;
//    }

    //TODO: Delete later. We most likely do not need to to use clipping masks.
//    void CGraphicTileset::CreateClippingMasks(){
//        if(DSurfaceTileset){
//            DClippingMasks.resize(DTileCount);
//            for(int Index = 0; Index < DTileCount; Index++){
//                DClippingMasks[Index] = CGraphicFactory::CreateSurface(DTileWidth, DTileHeight, CGraphicSurface::ESurfaceFormat::A1);
//                DClippingMasks[Index]->Copy(DSurfaceTileset, 0, 0, DTileWidth, DTileHeight, 0, Index * DTileHeight);
//            }
//        }
//    }


    public boolean LoadTileset(DataSource source) {
        CommentSkipLineDataSource LineSource = new CommentSkipLineDataSource(source, '#');
        String PNGPath, TempString;

        if(null == source){
            return false;
        }
        PNGPath = LineSource.read();
        if(null == PNGPath){
            //TODO: add when ported Debug class
//            log.error("Failed to get path.\n");
            return false;
        }

        TempString = LineSource.read(); //number of frames
        if(null == TempString){
            return false;
        }
        //TODO: Add number of 32x32 tiles to draw
        DTileCount = Integer.valueOf(TempString);
        DTileNames.setSize(DTileCount);

        for(int Index = 0; Index < DTileCount; Index++){
            TempString = LineSource.read();
            if(null == TempString){ //TODO: change to fit function requirements
                return false;
            }
//            DMapping[TempString] = Index;
            DTileNames.add(Index, TempString);
        }
//        UpdateGroupNames();
//        DTileHalfWidth = DTileWidth / 2;
//        DTileHalfHeight = DTileHeight / 2;

        return true;
    }

    public static void DrawTile(TextureAtlas atlas, TiledMapTileLayer assetLayer, int xpos, int ypos, String tileName) {
        //TODO: change last arg to tileindex, remove static once we get loading in tileset (.dat) files finished
        int numTiles, tilesPerRow, tilesPerCol, listIndex; //use to iterate through tiles
        TextureRegion region;
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(tileName); //gdx's Array class
//        String tileName = DTileNames.get(tileindex);

//        if ((0 > tileindex) || (tileindex >= DTileCount)) {
//            return;
//        }

        numTiles = regions.size;
        tilesPerCol = (int)sqrt((double)numTiles);
        tilesPerRow = tilesPerCol;

        for (int i = 0; i < tilesPerCol; i++) {
            for (int j=0; j < tilesPerRow; j++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                listIndex = (i * tilesPerCol) + j;
                region = regions.get(listIndex);
                cell.setTile(new StaticTiledMapTile(region));
                assetLayer.setCell(xpos + j, ypos - i, cell);
            }
        }
    }

    //TODO: Delete later. Most likely will not need to draw clipped tiles
//    void CGraphicTileset::DrawClipped(std::shared_ptr<CGraphicSurface> surface, int xpos, int ypos, int tileindex, uint32_t rgb){
//        if((0 > tileindex)||(tileindex >= DClippingMasks.size())){
//            return;
//        }
//        auto ResourceContext = surface->CreateResourceContext();
//
//        ResourceContext->SetSourceRGB(rgb);
//        ResourceContext->MaskSurface(DClippingMasks[tileindex], xpos, ypos);
//        ResourceContext->Fill();
//    }
}
