package com.warcraftII.data_source;

// Java packages

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// Libgdx packages
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class FileDataSource implements DataSource
{
    private FileHandle DFileHandle;
    private String DFileAsString;
    private boolean DCloseFile = false;


    public FileDataSource(String filename){
        //TODO: Implement CPath.  For now, we just fill in the actual filename
        //DFullPath = CPath::CurrentPath().Simplify(filename).ToString();
        DFileHandle = Gdx.files.internal(filename);
        DFileAsString = DFileHandle.readString();
    }

/* Using LIBGDX file system instead
    //Constructor from java.io.File object, instead of string
    public FileDataSource(File file) throws IOException {
        //TODO: Implement CPath.  For now, we just fill in the actual filename
        //DFullPath = CPath::CurrentPath().Simplify(filename).ToString();
        DFileHandle = file;
        DFileStream = new FileInputStream(DFileHandle);
    }
*/

    //Constructor from libgdx FileHandle object, instead of string
    public FileDataSource(FileHandle file) {
        DFileHandle = file;
        DFileAsString = DFileHandle.readString();
    }


    public String read(int bytesToRead) {
        // implement later
        String InString = "";
        return InString;
    }

    public String readEntire() {
        return DFileAsString;
    }


//TODO: Add Container() implementation 
/*
    std::shared_ptr< fCDataContainer > CFileDataSource::Container(){

        std::string ContainerName = CPath(DFullPath).Containing().ToString();

        

        if(ContainerName.length()){

            return std::make_shared< CDirectoryDataContainer >(ContainerName);

            

        }

        return nullptr;

    }
*/




} // end FileDataSource class