/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

/**
 *
 * @author t.buerk
 */
public class Voter {
    
    String email;
    String signature;
    String publicCredential;
    String appVersion;
    
    public Voter(String email, String signature, String publicCredential, String appVersion){
        this.email = email;
        this.signature = signature;
        this.publicCredential = publicCredential;
        this.appVersion = appVersion;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getSignature(){
        return signature;
    }
    
    public String getPublicCredential(){
        return publicCredential;
    }
    
    public String getAppVersion(){
        return appVersion;
    }
    
    @Override
    public String toString(){
        String printedString = email + ", version: " + appVersion;
        return printedString;
    }
}
