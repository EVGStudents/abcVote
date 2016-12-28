/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

/**
 * Class that stores all the inforamation of a voter
 * @author t.buerk
 */
public class Voter {
    
    String email;
    String publicCredential;
    String appVersion;
    
    /**
     * Create a new voter object
     * @param email
     * @param publicCredential
     * @param appVersion 
     */
    public Voter(String email, String publicCredential, String appVersion){
        this.email = email;
        this.publicCredential = publicCredential;
        this.appVersion = appVersion;
    }
    
    /**
     * Get email adsress
     * @return 
     */
    public String getEmail(){
        return email;
    }
    
    /**
     * get publicCredentials u
     * @return 
     */
    public String getPublicCredential(){
        return publicCredential;
    }
    
    /**
     * Get AppVersion of the voter
     * @return 
     */
    public String getAppVersion(){
        return appVersion;
    }
    
    /**
     * Get the String representation of the voter for listing it
     * @return 
     */
    @Override
    public String toString(){
        String printedString = email + ", version: " + appVersion;
        return printedString;
    }
}
