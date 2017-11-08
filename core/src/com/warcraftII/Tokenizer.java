package com.warcraftII;

import com.warcraftII.data_source.DataSource;

import java.util.StringTokenizer;
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
            TempCharString = DDataSource.read(1);

            if(-1 == DDelimiters.indexOf(TempCharString)){
                Token = Token.concat(TempCharString);

            } else if(Token.length() > 0){
                return Token;

            }

        }

    }

    // call Tokenize with default delimiters = ""
    public static Vector<String> Tokenize(String data) {
        return Tokenize(data, "");
    }

    public static Vector<String> Tokenize(String data, String delimiters){
        String Delimiters;

        Vector<String> Tokens =  new Vector<String>();

        if(delimiters.length() > 0){
            Delimiters = delimiters;
        } else{
            Delimiters = " \t\r\n";
        }

        StringTokenizer st = new StringTokenizer(data, Delimiters);

        while (st.hasMoreTokens()) {
            String TempTokenString = st.nextToken();
            Tokens.add(TempTokenString);
        }

        return Tokens;
    } // end Tokenize() function


} // end class