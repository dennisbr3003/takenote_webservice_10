package com.notemaster.android.ws.v1.notemasterweb.resource;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
    
	public String decrypt(String text) throws Exception {
        
		return decrypt(text, "Bar12345Bar12345");
		
    }

    public String encrypt(String text) throws Exception {
  	
    	return encrypt(text, "Bar12345Bar12345");
    }

    public String encrypt(String text, String EncryptionKey) throws Exception {
	   	
        try {
                        
            // Create key and cipher
            Key aesKey = new SecretKeySpec(EncryptionKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : encrypted) {
                sb.append((char) b);
            }

            // Base64 url-encode the encrypted String so it's usable in JSONs and URLS
            return Base64.getUrlEncoder().encodeToString(sb.toString().getBytes());
        }
        catch(Exception e){
        	throw e;
        }

    }    
    
	public String decrypt(String text, String EncryptionKey) throws Exception {

		// Encrypted strings must be Base64 encoded
		try {

			text = new String(Base64.getUrlDecoder().decode(text.getBytes()));
            
            // Create key and cipher
            Key aesKey = new SecretKeySpec(EncryptionKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");

            byte[] bb = new byte[text.length()];
            for (int i=0; i<text.length(); i++) {
                bb[i] = (byte) text.charAt(i);
            }
            
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            return new String(cipher.doFinal(bb));
                        
        }catch (Exception e){
        	throw e;
        }
    }    
    
}
