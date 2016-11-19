/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.controllers;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jose4j.jws.*;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */
public class SignatureController {
    
    public String signJson (JsonObject jsonInput) throws Exception{
        
        // Create a new JsonWebSignature
        JsonWebSignature jws = new JsonWebSignature();

        // Set the payload, or signed content, on the JWS object
        jws.setPayload(jsonInput.toString());

        // Set the signature algorithm on the JWS that will integrity protect the payload
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Set the signing key on the JWS
        // Note that your application will need to determine where/how to get the key
        // and here we just use an example from the JWS spec
        CertificateController certHelper = new CertificateController();
        PrivateKey privateKey = certHelper.getPemPrivateKey(getClass().getResource("/certificates/alice/alice_at_bfh.ch.privatekey.pkcs8.pem").toString(), "RSA");
        jws.setKey(privateKey);
        
        // Sign the JWS and produce the compact serialization or complete JWS representation, which
        // is a string consisting of three dot ('.') separated base64url-encoded
        // parts in the form Header.Payload.Signature
        String jwsCompactSerialization = jws.getCompactSerialization();
    
        // Do something useful with your JWS
        System.out.println(jwsCompactSerialization);
        return jwsCompactSerialization;
        
    }
    
}
