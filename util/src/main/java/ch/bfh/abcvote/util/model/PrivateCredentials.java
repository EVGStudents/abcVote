/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 * Class to generate and store the private Credentials
 * @author t.buerk
 */
public class PrivateCredentials {
    
    private Element alpha;
    private Element beta;
    
    private Parameters parameters;
    
    /**
     * Creates new private Credentials with the given parameters.
     * @param parameters 
     */
    public PrivateCredentials(Parameters parameters){
        // pick Random Private Credentials
        ZMod Z_q = parameters.getZ_q();
        alpha = Z_q.getRandomElement();
        beta = Z_q.getRandomElement();
        this.parameters = parameters;
    }
    
    /**
     * Restores the private Credentials from the string repersentation of the alpha and beta elements
     * @param parameters
     * @param alphaString
     * @param betaString
     * @throws UniCryptException 
     */
    public PrivateCredentials(Parameters parameters, String alphaString, String betaString) throws UniCryptException{
        // restore private Credentials from String
        ZMod Z_q = parameters.getZ_q();
        alpha = Z_q.getElementFrom(alphaString);
        beta = Z_q.getElementFrom(betaString);
        this.parameters = parameters;
    }
    
    /**
     * get Alpha
     * @return 
     */
    public Element getAlpha() {
        return alpha;
    }
    
    /**
     * Get Beta
     * @return 
     */
    public Element getBeta() {
        return beta;
    }

    /**
     * Calculate and returns u
     * @return 
     */
    public Element getU() {        
        Element u = parameters.getH1().selfApply(alpha);
        u = u.apply(parameters.getH2().selfApply(beta));
        return u;
    }
    
    
}
