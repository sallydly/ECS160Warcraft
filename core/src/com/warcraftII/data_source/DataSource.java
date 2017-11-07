package com.warcraftII.data_source;

import java.io.IOException;

public interface DataSource {
    public String read(int bytesToRead);
    public String readEntire();
    //TODO: Add container function
    //CDataContainer Container();
}