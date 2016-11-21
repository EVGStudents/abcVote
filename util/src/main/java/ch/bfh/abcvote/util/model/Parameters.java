/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;

/**
 *
 * @author t.buerk
 */

public class Parameters {
    
    private BigInteger p;
    private BigInteger q;
    
    private GStarModSafePrime G_p;
    private GStarModSafePrime G_q;
    private ZMod Z_p;
    private ZMod Z_q;
    
    private Element h1;
    private Element h2;
    
    public Parameters(String pString, String qString, String h1String, String h2String) throws UniCryptException{
       p = new BigInteger(pString);
       q = new BigInteger(qString);
        
       G_p = GStarModSafePrime.getInstance(p);
       G_q =  GStarModSafePrime.getInstance(q);
       Z_p = G_p.getZModOrder();
       Z_q = G_q.getZModOrder();
        

       h1 = G_q.getElementFrom(h1String);
       h2 =  G_q.getElementFrom(h2String);
       
    }
    
    public GStarModSafePrime getG_p() {
        return G_p;
    }

    public GStarModSafePrime getG_q() {
        return G_q;
    }

    public ZMod getZ_p() {
        return Z_p;
    }
    
    public ZMod getZ_q() {
        return Z_q;
    }
    
    public Element getH1() {
        return h1;
    }

    public Element getH2() {
        return h2;
    }

    
}
