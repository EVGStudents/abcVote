/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
