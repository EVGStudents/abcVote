/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.controllers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */
public class CertificateController {
    
    public PrivateKey getPemPrivateKey(String filename, String algorithm) throws Exception {
      File f = new File(filename);
      FileInputStream fis = new FileInputStream(f);
      DataInputStream dis = new DataInputStream(fis);
      byte[] keyBytes = new byte[(int) f.length()];
      dis.readFully(keyBytes);
      dis.close();

      String temp = new String(keyBytes);
      String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
      privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
      privKeyPEM = privKeyPEM.replaceAll("[^A-Za-z0-9/+]", "");

      Base64.Decoder decoder = Base64.getDecoder();
      byte [] decoded = decoder.decode(privKeyPEM);

      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      return kf.generatePrivate(spec);
      }

    public  PublicKey getPemPublicKey(String filename, String algorithm) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String temp = new String(keyBytes);
        String publicKeyPEM = temp.replace("-----BEGIN RSA PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END RSA PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replaceAll("[^A-Za-z0-9/+]", "");


        Base64.Decoder decoder = Base64.getDecoder();
        byte [] decoded = decoder.decode(publicKeyPEM);

        X509EncodedKeySpec spec =
              new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePublic(spec);
    }
    
    public  PublicKey getPublicKeyFromX509Certificate(String certificateString, String algorithm) throws Exception {
        InputStream inStream = new ByteArrayInputStream(certificateString.getBytes(StandardCharsets.UTF_8));

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)cf.generateCertificate(inStream);
        PublicKey pk = certificate.getPublicKey();
 
        return pk;
    }
}
