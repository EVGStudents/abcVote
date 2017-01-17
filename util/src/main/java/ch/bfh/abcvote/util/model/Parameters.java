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
package ch.bfh.abcvote.util.model;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;

/**
 * Class that stores all off the crypto parameters provided by the bulletin board
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
    
    private final int SECURITY_FACTOR = 80;
    
    /**
     * Takes the String representation of all the parameters and transforms them back into the corresponding unicrypt elements
     * @param oString
     * @param pString
     * @param h0String
     * @param h1String
     * @param h2String
     * @param g0String
     * @param g1String
     * @throws UniCryptException 
     */
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
    
    /**
     * Returns the Cyclic group G_p
     * @return 
     */
    public CyclicGroup getG_p() {
        return G_p;
    }

    /**
     * Returns the Cyclic group G_q
     * @return 
     */
    public CyclicGroup getG_q() {
        return G_q;
    }

    /**
     * Returns the ZMod group Z_p
     * @return 
     */
    public ZMod getZ_p() {
        return Z_p;
    }

    /**
     * Returns the ZMod group Z_q
     * @return 
     */
    public ZMod getZ_q() {
        return Z_q;
    }

    /**
     * Returns the random Generator of Group G_q 
     * @return 
     */
    public Element getH0() {
        return h0;
    }
    /**
     * Returns the first message Generator of Group G_q 
     * @return 
     */
    public Element getH1() {
        return h1;
    }

    /**
     * Returns the second message Generator of Group G_q 
     * @return 
     */
    public Element getH2() {
        return h2;
    }

    /**
     * Returns the random Generator of Group G_p
     * @return 
     */
    public Element getG0() {
        return g0;
    }
    /**
     * Returns the message Generator of Group G_p 
     * @return 
     */
    public Element getG1() {
        return g1;
    }

    /**
     * Returns the CommitmentScheme for Group G_p with one message input
     * @return 
     */
    public PedersenCommitmentScheme getCommitmentSchemeP(){
       PedersenCommitmentScheme com_P =  PedersenCommitmentScheme.getInstance(g0, g1);
       return com_P;
    }
    
    /**
     * Returns the CommitmentScheme for Group G_q with two message input
     * @return 
     */
    public GeneralizedPedersenCommitmentScheme getCommitmentSchemeQ(){
        Pair messageGenerators = Pair.getInstance(h1,h2);
        GeneralizedPedersenCommitmentScheme com_Q = GeneralizedPedersenCommitmentScheme.getInstance(h0,messageGenerators);
        return com_Q;
    }

    /**
     * Gets the Security factor for the pi2 proof
     * @return 
     */
    public int getSECURITY_FACTOR() {
        return SECURITY_FACTOR;
    }
    

    
}
