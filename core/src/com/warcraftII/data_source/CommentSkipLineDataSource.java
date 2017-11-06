
package com.warcraftII.data_source;

import com.warcraftII.data_source.DataSource;
import java.io.IOException;

public class CommentSkipLineDataSource extends LineDataSource
{
    protected char DCommentChar;

    public CommentSkipLineDataSource(DataSource source, char commentChar) {
        super(source);
        DCommentChar = commentChar;
    }

    @Override
    public String read(){
        //TODO: Create and throw custom exception for "no data read"

        while(true) {
            String tempLine = DFileAsLines[DLineNum];
            DLineNum++;

            if (tempLine.length() == 0 || tempLine.charAt(0) == DCommentChar) {
                continue;
            }

            if (tempLine.length() == 1) {
                return tempLine;
            }

            for (int i = 1; i < tempLine.length(); i++) {
                if (tempLine.charAt(i) == DCommentChar) {
                    return tempLine.substring(0, i);
                }
            }
            return tempLine; //if no comment char found in string of length > 1
        }
    }

} // end FileDataSource class