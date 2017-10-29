
package com.warcraftII.data_source;

import com.warcraftII.data_source.DataSource;
import java.io.IOException;

public class CommentSkipLineDataSource extends LineDataSource
{
    protected char commentChar;
    private FileDataSource fileDataSource;

    public CommentSkipLineDataSource(DataSource source, char commentChar) {
        super(source);
        this.commentChar = commentChar;
    }

    @Override
    public String read() throws IOException {
        //TODO: Create and throw custom exception for "no data read"
        String readLine;
        String tempLine;
        
        while(true) {
            tempLine = super.read();
            //If LineDataSource.read() == false in C++ code
            if(tempLine.length() == 0) {
                return null;
            }

            if(tempLine.length() > 0 || tempLine.charAt(0) != commentChar){
                readLine = tempLine;
                break;
            }
        
            if((2 <= tempLine.length()) && (tempLine.charAt(1) == commentChar)){
                readLine = tempLine.substring(1);
                break;
            }
        }
        return readLine;
    }


} // end FileDataSource class