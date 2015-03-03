package com.parse.starter;

import android.content.Context;
import android.util.Base64;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by Shawn on 2015-02-17.
 */
public class EncryptedFileIO {

    private String filename1;
    private String filename2;
    private String filename3;
    private Patient patient;
    private FileOutputStream fileout;
    private FileInputStream filein;
    private boolean fileExists;
    private Cipher aesCipher;
    private PBEKeySpec kspec;
    private SecretKeyFactory sKey;
    private Context context;

    public EncryptedFileIO(Context context){
        filename1 = "progData";
        filename2 = "altData";
        filename3 = "userData";
        this.context = context;
        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            sKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (Exception e) {
            this.context = context;
        }
    }

    public EncryptedFileIO(Context context, String password){
        filename1 = "progData";
        filename2 = "altData";
        filename3 = "userData";
        kspec = new PBEKeySpec(password.toCharArray());
        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            sKey = SecretKeyFactory.getInstance("PBKD2WithHmacSHA1");
        } catch (Exception e) {}
        context = context.getApplicationContext();
        try{
            filein = context.openFileInput(filename1);
            fileExists = true;
            //TODO: Get info from file, store in patient object
            byte[] cipher = new byte[filein.available()];

            filein.read(cipher);
            byte[] decipher = decryptString(cipher);
            String plain = new String(decipher);
            patient = new Patient(plain);
            filein.close();
        }catch(Exception e){//No file to open, create file
            try{
                fileExists = false;
                fileout = context.openFileOutput(filename1, context.MODE_PRIVATE);
                //TODO: Create file and put all relavent data into it
                patient = new Patient();
                byte[] contents = encryptString((patient.toString() + password).getBytes());
                fileout.write(contents);
                fileout.flush();
                fileout.close();
            }catch(Exception ex){//don't know why this would occur...
                int j = 0;
            }
        }
    }

    public EncryptedFileIO(Context context, String password, Patient pat){
        filename1 = "progData";
        filename2 = "altData";
        filename3 = "userData";
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        kspec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            sKey = SecretKeyFactory.getInstance("PBKD2WithHmacSHA1");
        } catch (Exception e) {}
        context = context.getApplicationContext();
        try{
            fileExists = false;
            fileout = context.openFileOutput(filename1, context.MODE_PRIVATE);
            byte[] contents = encryptString(pat.toString().getBytes());
            fileout.write(contents);
            fileout.close();
        }catch(Exception ex){//don't know why this would occur...
        }
    }

    public boolean updateFile(Context context, Patient pat, String password){
        String fillString = "0123456789012345";
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        kspec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
        context = context.getApplicationContext();
        try{
            fileout = context.openFileOutput(filename3, context.MODE_PRIVATE);
            fileout.write(pat.getName().getBytes());
            fileout.flush();
            fileout.close();
            fileout = context.openFileOutput(filename2, context.MODE_PRIVATE);
            fileout.write(salt);
            fileout.flush();
            fileout.close();
            fileout = context.openFileOutput(filename1, context.MODE_PRIVATE);
            byte[] contents = encryptString((fillString + pat.toString() + password).getBytes());
            fileout.write(contents);
            fileout.flush();
            fileout.close();
            fileExists = true;
            return true;
        }catch(Exception ex){//don't know why this would occur...
            return false;
        }
    }

    private byte[] encryptString(byte[] plainText){//return ciphertext of given plaintext
        try {
            aesCipher.init(Cipher.ENCRYPT_MODE, sKey.generateSecret(kspec));
            return aesCipher.doFinal(plainText);

        }catch(Exception e){
            return null;
        }

    }

    private byte[] decryptString(byte[] ciphertext){//return ciphertext of given plaintext
        try {
            aesCipher.init(Cipher.DECRYPT_MODE, sKey.generateSecret(kspec));
            byte[] plaintext = aesCipher.doFinal(ciphertext);
            return plaintext;

        }catch(Exception e){
            return null;
        }
    }

    public boolean doesFileExists() {
        try {
            filein = context.openFileInput(filename1);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean validatePass(String password){
        try {
            filein = context.openFileInput(filename2);
            byte[] salt = new byte[filein.available()];
            filein.read(salt);

            kspec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);

            filein = context.openFileInput(filename1);
            byte[] cipher = new byte[filein.available()];

            filein.read(cipher);
            byte[] decipher = decryptString(cipher);
            String plain = new String(decipher);

            plain = plain.substring(15);

            patient = new Patient(plain);
            String[] parts = plain.split("\n");
            filein.close();

            if(parts[4].equals(password)){
                return true;
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }

    public Patient getPatient(){return patient;}

}
