package com.warcraft2.data_source;

import java.io.IOException;

public interface DataSource {
    public String read(int bytesToRead) throws IOException;

    //TODO: Add container function
    //CDataContainer Container();
}