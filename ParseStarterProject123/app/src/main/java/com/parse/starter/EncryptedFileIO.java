package com.parse.starter;

import android.content.Context;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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
        patient = pat;
        context = context.getApplicationContext();
        try{
            filein = context.openFileInput(filename);
            //TODO: Get info from file, store in patient object
            byte[] cipher = new byte[filein.available()];

            filein.read(cipher);
            String plain = new String(dencryptString(cipher));
            patient = new Patient(plain);

        }catch(Exception e){//No file to open, create file
            try{
                fileout = context.openFileOutput(filename, context.MODE_PRIVATE);
                //TODO: Create file and put all relavent data into it
                byte[] contents = encryptString(patient.toString().getBytes());
                fileout.write(contents);
            }catch(Exception ex){//don't know why this would occur...
            }

        }
    }

    private byte[] encryptString(byte[] plainText){//return ciphertext of given plaintext
        try {
            Cipher aesCipher;
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecretKey sKey = keygen.generateKey();

            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, sKey);
            return aesCipher.doFinal(plainText);

        }catch(Exception e){
            return null;
        }

    }
    private byte[] dencryptString(byte[] ciphertext){//return ciphertext of given plaintext
        try {
            Cipher aesCipher;
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecretKey sKey = keygen.generateKey();

            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.DECRYPT_MODE, sKey);
            return aesCipher.doFinal(ciphertext);

        }catch(Exception e){
            return null;
        }
    }
}
