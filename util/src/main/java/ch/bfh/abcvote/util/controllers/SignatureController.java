/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.controllers;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Map.Entry;
import org.jose4j.jws.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.jose4j.lang.JoseException;

/**
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */
public class SignatureController {
    
    public JsonObject signJson (JsonObject jsonInput) throws JoseException, Exception{
        
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
        PrivateKey privateKey = certHelper.getPemPrivateKey("/Users/snellen/Documents/dev/abcVote/adminApp/src/main/resources/certificates/alice/alice_at_bfh.ch.privatekey.pkcs8.pem", "RSA");
        jws.setKey(privateKey);
        
        // Sign the JWS and produce the compact serialization or complete JWS representation, which
        // is a string consisting of three dot ('.') separated base64url-encoded
        // parts in the form Header.Payload.Signature
        String jwsCompactSerialization = jws.getCompactSerialization();
    
        JsonObjectBuilder job = Json.createObjectBuilder();

        for (Entry<String, JsonValue> entry : jsonInput.entrySet()) {
            job.add(entry.getKey(), entry.getValue());
        }
        
        job.add("signature", jwsCompactSerialization);
        JsonObject jsonObject = job.build();
        
        return jsonObject;
        
    }
    
    
    public boolean verify(JsonObject signedModel) throws JoseException, Exception{
        // The complete JWS representation, or compact serialization, is string consisting of
        // three dot ('.') separated base64url-encoded parts in the form Header.Payload.Signature

        // Create a new JsonWebSignature
        JsonWebSignature jws = new JsonWebSignature();

        // Build the String to be verified
        String jwsCompact = "";
        
        for (Map.Entry<String, JsonValue> entry : signedModel.entrySet()) {
            if (entry.getKey().equals("signature")) {
            jwsCompact = entry.getValue().toString();
            }
        }
        
        // Remove "-signs at the beginning and end
        jwsCompact = jwsCompact.substring(1);
        jwsCompact = jwsCompact.substring(0, jwsCompact.length()-1);
        
        // Set the compact serialization on the JWS
        jws.setCompactSerialization(jwsCompact);
        
        // Set the verification key
        // Note that your application will need to determine where/how to get the key
        // Here we use an example from the JWS spec
        CertificateController certController = new CertificateController();
        //PublicKey publicKey = certController.getPemPublicKey("/Users/snellen/Documents/dev/abcVote/adminApp/src/main/resources/certificates/alice/alice_at_bfh.ch.pubkey.pem", "RSA");
        PublicKey publicKey = certController.getPublicKeyFromX509Certificate("-----BEGIN CERTIFICATE-----\n" +
            "MIIDBTCCAe2gAwIBAgIBAjANBgkqhkiG9w0BAQsFADAsMR0wGwYDVQQDDBRuZWxs\n" +
            "czEtYmZoLWNsaWVudC1jYTELMAkGA1UEBhMCQ0gwHhcNMTYwMzE1MjA1NTMzWhcN\n" +
            "MTcwMzE1MjA1NTMzWjA0MRUwEwYDVQQDDAxhbGljZUBiZmguY2gxGzAZBgkqhkiG\n" +
            "9w0BCQEWDGFsaWNlQGJmaC5jaDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC\n" +
            "ggEBANuBphH7tMhI4YlonK4Ixwhcd9wJvJGPicYNTx/oypQc460V6SmYMcVCWFeP\n" +
            "wmRfI1mSagGcBjbEL/D6SVcFwKsl0V/mZN6GnZTiGzQ4YFeOTuDIT3YT+q6WzZ5a\n" +
            "owFpf814wLdCdggwK5DJLbpxgwpC9+YJe4ltZBLK1pcIx/vyfToMIurT7Q22sguq\n" +
            "y4aaoCpLpEhGwzj+iLNGFDjD9+b560tadOAtholyvk3+PoftnzhbQualKE1H2YYF\n" +
            "HCYciWoieNGm4sXePN7VsM37Zinw4/x45fzQ16GBDfuicjADeJUn63b01yf+tomC\n" +
            "9gi2ZQUEl1ANLvHHo/BvKc87iBsCAwEAAaMqMCgwDgYDVR0PAQH/BAQDAgeAMBYG\n" +
            "A1UdJQEB/wQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBCwUAA4IBAQBcOyxLJGHM\n" +
            "CSmAEOQLXM3vLArBuLhiqDeLmOqmiQBOFwO099ZdSDX2twoM9tkUJwQ1FSdlHQJr\n" +
            "Axef25n7SV7qZKFypXbzE2yjrKgYOH2Q4nlYync+pFB1Lalzxi12jo5hwmPXA6MC\n" +
            "2clFtBEVpNg6yfR8wGCl7T/2Ihse9uoYfxmACbL65AQQm/R9dHVoLjG+x/olgRpz\n" +
            "AJ/9Jzr0ghEci03ohOIqFOUWgYYBVG3vpjO0fghdefFX8JUaiIgQct4CovTbnafZ\n" +
            "L4FyQUas2ldXaWRltuiT2q+Qc47gWoBzribaMoVB0qSCHl9IwkHYvVhypfqWgCKE\n" +
            "gMXtSOpRHbuW\n" +
            "-----END CERTIFICATE-----", "RSA");
        jws.setKey(publicKey);
        
        // Check the signature
        boolean signatureVerified = jws.verifySignature();
        
        return signatureVerified;
    }
    
}
