/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author t.buerk
 */
public class PrivateCredentials {
    
    private Element alpha;
    private Element beta;
    
    private Parameters parameters;
    
    public PrivateCredentials(Parameters parameters){
        // pick Random Private Credentials
        ZMod Z_q = parameters.getZ_q();
        alpha = Z_q.getRandomElement();
        beta = Z_q.getRandomElement();
        this.parameters = parameters;
    }
    
    public Element getAlpha() {
        return alpha;
    }

    public Element getBeta() {
        return beta;
    }

    public Element getU() {        
        Element u = parameters.getH1().selfApply(alpha);
        u = u.apply(parameters.getH2().selfApply(beta));
        return u;
    }
    
    
}
