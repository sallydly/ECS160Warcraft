package com.warcraft2;

import java.io.IOException;

public interface DataSource {
    public String Read(int bytesToRead) throws IOException;

    //TODO: Add container function
    //CDataContainer Container();
}