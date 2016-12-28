/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.controllers;

import java.io.File;
import java.security.PrivateKey;
import org.jose4j.jws.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * controller for working with JWS and signature stuff
 * @author Sebastian Nellen <sebastian at nellen.it>
 */
public class SignatureController {
    
    /**
     * signs a given JSON object and returns it
     * @param jsonInput the JSON to be signed
     * @return a JWS (signed json object)
     * @throws Exception 
     */
    public JsonObject signJson (JsonObject jsonInput) throws Exception {
        
        // Create a new JsonWebSignature
        JsonWebSignature jws = new JsonWebSignature();

        // Set the payload, or signed content, on the JWS object
        jws.setPayload(jsonInput.toString());

        // Set the signature algorithm on the JWS that will integrity protect the payload
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Set the signing key on the JWS
        // Note that your application will need to determine where/how to get the key
        // and here we just use an example from the JWS spec
        String pathToKeyStore = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "abcVote" + File.separator + "test.jks";
        String keyStorePassword = "Bern2016";
        String stringKeyPassword = "Bern2016";
        String stringAlias = "alice-at-bfh.ch";
        PrivateKey privateKey = KeyStoreController.readPrivateKeyFromKeyStore(pathToKeyStore, keyStorePassword, stringKeyPassword, stringAlias);
        jws.setKey(privateKey);
        
        // Sign the JWS and produce the compact serialization or complete JWS representation, which
        // is a string consisting of three dot ('.') separated base64url-encoded
        // parts in the form Header.Payload.Signature
        String jwsCompactSerialization = jws.getCompactSerialization();
    
        JsonObjectBuilder job = Json.createObjectBuilder();

        job.add("signature", jwsCompactSerialization);
        JsonObject jsonObject = job.build();
        
        return jsonObject;
        
    }
    
}
