/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ch.bfh.abcvote.util.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * KeyStoreController - helper class for communication with Java KeyStores.
 * Based on "d-smith/KeyUtils.java" https://gist.github.com/d-smith/c6a9d84e33466a530ca8
 * 
 * @author Sebastian Nellen <sebastian at nellen.it>
 */
public class KeyStoreController {
    
    private static FileInputStream getFileInputStreamFromArg(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        return new FileInputStream(file);
    }
    
    public static KeyStore loadKeyStoreFromFile(String pathToFile, String keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(getFileInputStreamFromArg(pathToFile), keystorePassword.toCharArray());
        return keyStore;
    }
    
    public static void writeStringToKeyStore(String pathToKeyStore, String keyStorePassword, String stringPassword, String stringAlias, String stringToStore)
            throws Exception {
        
        KeyStore keyStore = loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(stringPassword.toCharArray());
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
        SecretKey generatedSecret =
                factory.generateSecret(new PBEKeySpec(
                        stringToStore.toCharArray(),
                        "oh we're salty allright".getBytes(),
                        13
                ));
        
        keyStore.setEntry(stringAlias, new KeyStore.SecretKeyEntry(
                generatedSecret), keyStorePP);
        
        FileOutputStream outputStream = new FileOutputStream(new File(pathToKeyStore));
        keyStore.store(outputStream, keyStorePassword.toCharArray());
    }
    
    public static String readStringFromKeyStore(KeyStore keyStore, String keyStorePassword, String stringPassword, String stringAlias) throws Exception {
        
        KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(stringPassword.toCharArray());
        
        KeyStore.SecretKeyEntry ske =
                (KeyStore.SecretKeyEntry)keyStore.getEntry(stringAlias, keyStorePP);
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
        PBEKeySpec keySpec = (PBEKeySpec)factory.getKeySpec(
                ske.getSecretKey(),
                PBEKeySpec.class);
        
        return new String(keySpec.getPassword());
    }
    
    public static PrivateKey readPrivateKeyFromKeyStore (String pathToKeyStore, String keyStorePassword, String stringKeyPassword, String stringAlias) throws Exception {
        
        KeyStore keyStore = loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        
        PrivateKey privateKey = (PrivateKey)keyStore.getKey(stringAlias, stringKeyPassword.toCharArray());
        
        return privateKey;
    }
    
    public static PublicKey readPublicKeyFromKeyStore (String pathToKeyStore, String keyStorePassword, String stringAlias) throws Exception {
        
        KeyStore keyStore = loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        
        Certificate cert = keyStore.getCertificate(stringAlias);
        PublicKey publicKey = cert.getPublicKey();
        
        return publicKey;
    }
    
}
