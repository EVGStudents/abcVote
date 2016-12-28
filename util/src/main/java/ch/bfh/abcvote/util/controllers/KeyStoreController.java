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
    
    /**
     * method returning a file input stream
     * @param filePath the file path
     * @return returns a file input stream
     * @throws FileNotFoundException 
     */
    private static FileInputStream getFileInputStreamFromArg(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        return new FileInputStream(file);
    }
    
    /**
     * loads a keystore into an object and returns it
     * @param pathToKeyStore path to keystore file
     * @param keystorePassword the keystore's password
     * @return returns a KeyStore object
     * @throws Exception 
     */
    public static KeyStore loadKeyStoreFromFile(String pathToKeyStore, String keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(getFileInputStreamFromArg(pathToKeyStore), keystorePassword.toCharArray());
        return keyStore;
    }
    
    /**
     * writes a string into the keystore
     * @param pathToKeyStore path to keystore file
     * @param keyStorePassword the keystore's password
     * @param stringPassword the string's password
     * @param stringAlias the string's alias within the keystore
     * @param stringToStore the real string to be stored
     * @throws Exception 
     */
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
    
    /**
     * reads a string from the keystore
     * @param keyStore the keystore as an object
     * @param keyStorePassword the keystore's password
     * @param stringPassword the string's password
     * @param stringAlias the string's alias within the keystore
     * @return returns the store string
     * @throws Exception 
     */
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
    
    /**
     * get a specific private key from the keystore
     * @param pathToKeyStore path to keystore file
     * @param keyStorePassword the keystore's password
     * @param stringKeyPassword the key's password
     * @param stringKeyAlias the key's alias
     * @return returns the private key from a given alias
     * @throws Exception 
     */
    public static PrivateKey readPrivateKeyFromKeyStore (String pathToKeyStore, String keyStorePassword, String stringKeyPassword, String stringKeyAlias) throws Exception {
        
        KeyStore keyStore = loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        
        PrivateKey privateKey = (PrivateKey)keyStore.getKey(stringKeyAlias, stringKeyPassword.toCharArray());
        
        return privateKey;
    }
    
    /**
     * get a specific public key from the keystore
     * @param pathToKeyStore path to keystore file
     * @param keyStorePassword the keystore's password
     * @param stringKeyAlias the key's alias
     * @return returns the public key from a given alias
     * @throws Exception 
     */
    public static PublicKey readPublicKeyFromKeyStore (String pathToKeyStore, String keyStorePassword, String stringKeyAlias) throws Exception {
        
        KeyStore keyStore = loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        
        Certificate cert = keyStore.getCertificate(stringKeyAlias);
        PublicKey publicKey = cert.getPublicKey();
        
        return publicKey;
    }
    
}
