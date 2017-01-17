/*
 * abcVote
 *
 *  abcVote - an e-voting prototype with everlasting privacy
 *  Copyright (c) 2017 Timo Buerk and Sebastian Nellen
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for abcVote may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and us.
 *
 *
 *   For further information contact <e-mail: burkt4@gmail.com> or <e-mail: sebastian@nellen.it>
 *
 *
 * Redistributions of files must retain the above copyright notice.
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
 * @author Sebastian Nellen, sebastian@nellen.it
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
     * @throws Exception An Exception will be thrown in case of problems with the KeyStore (e.g. can't be found, wrong password, etc.)
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
     * @throws Exception An Exception will be thrown in case of problems with the KeyStore (e.g. can't be found, wrong password, etc.)
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
     * @throws Exception An Exception will be thrown in case of problems with the KeyStore (e.g. can't be found, wrong password, etc.)
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
     * @throws Exception An Exception will be thrown in case of problems with the KeyStore (e.g. can't be found, wrong password, etc.)
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
     * @throws Exception An Exception will be thrown in case of problems with the KeyStore (e.g. can't be found, wrong password, etc.)
     */
    public static PublicKey readPublicKeyFromKeyStore (String pathToKeyStore, String keyStorePassword, String stringKeyAlias) throws Exception {
        
        KeyStore keyStore = loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        
        Certificate cert = keyStore.getCertificate(stringKeyAlias);
        PublicKey publicKey = cert.getPublicKey();
        
        return publicKey;
    }
    
}
