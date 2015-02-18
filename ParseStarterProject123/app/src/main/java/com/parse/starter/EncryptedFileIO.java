package com.parse.starter;

import android.content.Context;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Shawn on 2015-02-17.
 */
public class EncryptedFileIO {

    private String filename;
    private Patient patient;
    private FileOutputStream fileout;
    private FileInputStream filein;

    public EncryptedFileIO(Context context, Patient pat){
        filename = "progData";
        context = context.getApplicationContext();
        try{
            filein = context.openFileInput(filename);
            //TODO: Get info from file, store in patient object
        }catch(Exception e){//No file to open, create file
            try{
                fileout = context.openFileOutput(filename, context.MODE_PRIVATE);
                //TODO: Create file and put all relavent data into it
            }catch(Exception ex){//don't know why this would occur...
            }

        }
    }
}
