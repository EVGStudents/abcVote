/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.model;

/**
 *
 * @author t.buerk
 */
public class Voter {
    
    String givenName;       
    String middleName;
    String surname;
    String location;
    String signature;
    String publicCredential;
    String appVersion;
    
    public Voter(String givenName, String middleName, String surname, String location, String signature, String publicCredential, String appVersion){
        this.givenName = givenName;
        this.middleName = middleName;
        this.surname = surname;
        this.location = location;
        this.signature = signature;
        this.publicCredential = publicCredential;
        this.appVersion = appVersion;
    }
    
    public String getGivenName(){
        return givenName;
    }
    
    public String getMiddleName(){
        return middleName;
    }
    
    public String getSurName(){
        return surname;
    }
    
    public String getLocation(){
        return location;
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
        String printedString = surname + ", " + givenName + " " + middleName;
        return printedString;
    }
}
