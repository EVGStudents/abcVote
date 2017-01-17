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
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author t.buerk
 */
public class ElectionTest {
    
    public ElectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calculateCoefficients method, of class Election.
     */
    @Test
    public void testCalculateCoefficients() throws Exception {
        System.out.println("calculateCoefficients");
        Parameters params = new Parameters("167",
                "83",
                "41",
                "70",
                "3",
                "33",
                "75");
        List<Voter> voters = new ArrayList<Voter>();
        Voter listedVoter1 = new Voter("hector@bfh.ch","81", "1.15");
        Voter listedVoter2 = new Voter("alice@bfh.ch","48", "1.15");
        voters.add(listedVoter1);
        voters.add(listedVoter2);
        Voter unlistedVoter1 = new Voter("alice@bfh.ch","7", "1.15");

        Election election = new Election(params);
        election.setVoterList(voters);
        election.calculateCoefficients();
        
        PolynomialElement credPolynom = election.getCredentialPolynomial();
        
        System.out.println("testing listed voter 1");
        assertTrue(params.getZ_p().getZeroElement().equals(credPolynom.evaluate(params.getZ_p().getElementFrom(listedVoter1.publicCredential))));
        
        System.out.println("testing listed voter 2");
        assertTrue(params.getZ_p().getZeroElement().equals(credPolynom.evaluate(params.getZ_p().getElementFrom(listedVoter2.publicCredential))));
        
        System.out.println("testing unlisted voter");
        assertFalse(params.getZ_p().getZeroElement().equals(credPolynom.evaluate(params.getZ_p().getElementFrom(unlistedVoter1.publicCredential))));
        
    }

    /**
     * Test of pickH_Hat method, of class Election.
     */
    @Test
    public void testPickH_Hat() throws UniCryptException {
        System.out.println("pickH_Hat");
        Parameters params = new Parameters("167",
                "83",
                "41",
                "70",
                "3",
                "33",
                "75");
        Election election = new Election(params);
        election.pickH_Hat();

        assertTrue(election.getH_Hat().isGenerator());
        
    }
    
}
