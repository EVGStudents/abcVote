/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;

/**
 *
 * @author t.buerk
 */

public class Parameters {
    
    
    private CyclicGroup G_p;
    private CyclicGroup G_q;
    
    private ZMod Z_p;
    private ZMod Z_q;
    
    private Element h0;
    private Element h1;
    private Element h2;
    
    private Element g0;
    private Element g1;
    
    public Parameters(String oString, String pString, String h0String, String h1String, String h2String, String g0String, String g1String) throws UniCryptException{
        G_p = GStarModPrime.getInstance(new BigInteger(oString, 10), new BigInteger(pString, 10));
	G_q = GStarModSafePrime.getInstance(new BigInteger(pString, 10));
        
       Z_p = G_p.getZModOrder();
       Z_q = G_q.getZModOrder();
        
       h0 = G_q.getElementFrom(h0String);
       h1 = G_q.getElementFrom(h1String);
       h2 =  G_q.getElementFrom(h2String);
       
       g0 = G_p.getElementFrom(g0String);
       g1 = G_p.getElementFrom(g1String);
    }
    
    public CyclicGroup getG_p() {
        return G_p;
    }

    public CyclicGroup getG_q() {
        return G_q;
    }

    public ZMod getZ_p() {
        return Z_p;
    }
    
    public ZMod getZ_q() {
        return Z_q;
    }
    
    public Element getH0() {
        return h0;
    }
    
    public Element getH1() {
        return h1;
    }

    public Element getH2() {
        return h2;
    }

    public Element getG0() {
        return g0;
    }

    public Element getG1() {
        return g1;
    }

    
}
