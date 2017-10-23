package com.warcraft2;

// Java packages
import java.io.*;

public class FileDataSource implements DataSource
{
    private File DFileHandle;
    private FileInputStream DFileStream;
    private boolean DCloseFile = false;
   

    public FileDataSource(String filename) throws IOException {
        //TODO: Implement CPath.  For now, we just fill in the actual filename
        //DFullPath = CPath::CurrentPath().Simplify(filename).ToString();
            DFileHandle = new File(filename);
            DFileStream = new FileInputStream(DFileHandle);
    }

    public String Read(int bytesToRead) throws IOException {
            String InString = "";
            byte[] InBytes = new byte[bytesToRead];
            int BytesRead = DFileStream.read(InBytes);

                if(0 < BytesRead){
                    InString = new String(InBytes);
                }
                else
                {
                 //TODO: Create and throw custom exception for "no data read"
                }
            return InString;
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