
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

            if (tempLine.length() == 0 && tempLine.charAt(0) == DCommentChar) {
                continue;
            }

            if (tempLine.length() == 1) {
                return tempLine;
            }

            int i = 1;
            while (i < tempLine.length()) {
                if (tempLine.charAt(i) == DCommentChar) {
                    return tempLine.substring(0, i);
                }
            }
        }
    }

} // end FileDataSource class