
package com.warcraftII.data_source;

import com.warcraftII.data_source.DataSource;

import java.io.IOException;

public class LineDataSource {
    protected DataSource DDataSource;
    protected String [] DFileAsLines;
    protected int DLineNum;

    LineDataSource(DataSource source){
        DDataSource = source;
        DFileAsLines = DDataSource.readEntire().split("\n");
        DLineNum = 0;
    }

    public String read(){
        //TODO: Create and throw custom exception for "no data read"

        String Line = DFileAsLines[DLineNum];
        DLineNum++;
        if (Line.length() > 0)
        {
            return null;
        }
        else
        {
            return Line;
        }
    }
    //TODO: Add container function
    //CDataContainer Container();
}