
package com.warcraft2;

import java.io.IOException;

public class LineDataSource {
    private DataSource DDataSource;

    LineDataSource(DataSource source){
        DDataSource = source;
    }
        
        
    public String Read() throws IOException {
        //TODO: Create and throw custom exception for "no data read"

        String TempCharString;
        String ReadLine = new String();
        
        while(true) {

            TempCharString = DDataSource.Read(1);
 
            if("\n".equals(TempCharString)){

                return ReadLine;
        
            }
        
            else if(!("\r").equals(TempCharString)){
        
                ReadLine =  ReadLine.concat(TempCharString);
        
            }
        }
        
            
        
    }
    //TODO: Add container function
    //CDataContainer Container();
}