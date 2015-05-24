package edu.sjsu.cmpe.cache.client;

import java.security.*;

public class HashFunction{
    public String hash(String nodeString) throws NoSuchAlgorithmException{
        MessageDigest messageDigest =  MessageDigest.getInstance("SHA-256");
        messageDigest.update(nodeString.getBytes());
        
        byte byteData[] = messageDigest.digest();
        
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }    
        
        return sb.toString();
    }
}