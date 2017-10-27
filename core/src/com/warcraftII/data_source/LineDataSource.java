
package com.warcraftII.data_source;

import com.warcraftII.data_source.DataSource;

import java.io.IOException;

public class LineDataSource {
    private DataSource DDataSource;

    LineDataSource(DataSource source){
        DDataSource = source;
    }
        
    public String read() throws IOException {
        //TODO: Create and throw custom exception for "no data read"

        String tempCharString;
        String readLine = "";
        
        while(true) {
            tempCharString = DDataSource.read(1);
 
            if("\n".equals(tempCharString)) {
                return readLine;
            } else if(!("\r").equals(tempCharString)) {
                readLine =  readLine.concat(tempCharString);
            }
        }
    }
    //TODO: Add container function
    //CDataContainer Container();
}