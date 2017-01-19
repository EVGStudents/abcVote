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
import java.security.PrivateKey;
import org.jose4j.jws.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * controller for working with JWS and signature stuff
 * @author Sebastian Nellen, sebastian@nellen.it
 */
public class SignatureController {
    
    /**
     * signs a given JSON object and returns it
     * @param jsonInput the JSON to be signed
     * @return a JWS (signed json object)
     * @throws Exception An Exception is either thrown from the KeyStoreController, or from the jws serialization stuff
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
        String pathToKeyStore = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "abcVote" + File.separator + "alice_at_bfh.ch-KeyStore.jks";
        String keyStorePassword = "Bern2016";
        String stringKeyPassword = "Bern2016";
        String stringAlias = "alice_at_bfh.ch";
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
