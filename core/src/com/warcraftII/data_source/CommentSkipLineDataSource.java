
package com.warcraftII.data_source;

import com.badlogic.gdx.utils.Logger;
import com.warcraftII.data_source.DataSource;
import java.io.IOException;

public class CommentSkipLineDataSource extends LineDataSource
{

    protected char DCommentChar;
    /**
     * Indicates that whether the previous line read, was a line that follows a comment
     * Set to true whenever read() method encounter a comment; false when read() method read a full line that contains no comment
     */
    protected boolean DIsAfterComment;

    protected Logger log;

    public CommentSkipLineDataSource(DataSource source, char commentChar) {
        super(source);
        log = new Logger("Comment", 2);
        DCommentChar = commentChar;
        DIsAfterComment = false;
    }

    /**
     *
     * @return
     */
    @Override
    public String read(){
        //TODO: Create and throw custom exception for "no data read"
        DIsAfterComment = false;
        while(true) {
            String tempLine = DFileAsLines[DLineNum];
            DLineNum++;

            if (tempLine.length() == 0) {
                continue;
            }

            if(tempLine.charAt(0) == DCommentChar) {
                DIsAfterComment = true;
                log.info("Line is a comment");
                continue;
            }

            if (tempLine.length() == 1) {
                return tempLine;
            }

            for (int i = 1; i < tempLine.length(); i++) {
                if (tempLine.charAt(i) == DCommentChar) {
                    //DIsAfterComment = true;
                    return tempLine.substring(0, i);
                }
            }
            log.info(tempLine);
            //DIsAfterComment = false;
            return tempLine; //if no comment char found in string of length > 1
        }
    }

    public boolean isDIsAfterComment() {
        return DIsAfterComment;
    }

} // end FileDataSource class