
package com.warcraft2;

import java.io.IOException;

public class CommentSkipLineDataSource extends LineDataSource
{
    private char DCommentCharacter;

    public CommentSkipLineDataSource(FileDataSource source) {
        super(source);
    }
            
    public String Read() throws IOException { 
        //TODO: Create and throw custom exception for "no data read"
        String ReadLine;
        String TempLine;
        
        while(true) {
            TempLine = super.Read();
        
            if(TempLine.length() > 0 || TempLine.charAt(0) != DCommentCharacter){

                ReadLine = TempLine;
                break;
        
            }
        
            if((2 <= TempLine.length())&&(TempLine.charAt(1) == DCommentCharacter)){
        
                ReadLine = TempLine.substring(1);
                break;
        
            }
        
        }
        
        return ReadLine;
        
    }


} // end FileDataSource class