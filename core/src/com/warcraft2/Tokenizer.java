package com.warcraft2;

import java.io.IOException;
import java.util.Vector;

public class Tokenizer {

    private String DDelimiters;
    private DataSource DDataSource;

    public Tokenizer(DataSource source, String delimiters) {

        DDataSource = source;

        if(delimiters.length() > 0){
            DDelimiters = delimiters;
        }
        else {
            DDelimiters = " \t\r\n";
        }
    }


    public String Read() throws IOException {
        //TODO: Create and throw custom exception for "no data read"
        String TempCharString;
        String Token = new String();

        while(true){
            TempCharString = DDataSource.Read(1);

            if(-1 == DDelimiters.indexOf(TempCharString)){
                Token = Token.concat(TempCharString);

            } else if(Token.length() > 0){
                return Token;

            }

        }

    }

    public static Vector<String> Tokenize(String data, String delimiters){
            String Delimiters;
            String TempTokenString;
            Vector<String> Tokens =  new Vector<String>();

            if(delimiters.length() > 0){

                Delimiters = delimiters;

            } else{

                Delimiters = " \t\r\n";

            }

        TempTokenString = new String();

        for(int Index = 0; Index < data.length(); Index++){

            if(-1 == Delimiters.indexOf(data.charAt(Index))){

                TempTokenString = TempTokenString + data.charAt(Index);

            } else if(TempTokenString.length() > 0){

                Tokens.add(TempTokenString);

                TempTokenString = new String();

            }

        }

        if(TempTokenString.length() > 0){

            Tokens.add(TempTokenString);

        }

        return Tokens;
        } // end Tokenize() function


} // end class