/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.model;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;

/**
 *
 * @author t.buerk
 */

public class Parameters {
    
    BigInteger p;
    BigInteger q;
    
    GStarModSafePrime G_p;
    GStarModSafePrime G_q;
    
    Element h1;
    Element h2;
    
    public Parameters(String pString, String qString, String h1String, String h2String) throws UniCryptException{
       p = new BigInteger(pString);
       q = new BigInteger(qString);
        
       G_p = GStarModSafePrime.getInstance(p);
       G_q =  GStarModSafePrime.getInstance(q);
        
       h1 = G_q.getElementFrom(h1String);
       h2 =  G_q.getElementFrom(h2String);
       
    }
    
}
