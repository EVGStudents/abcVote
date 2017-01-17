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
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.classes.DoubleDiscreteLogProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.EqualityPreimageProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.PolynomialMembershipProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.DiscreteLogarithmCommitmentScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores the selected options to a specific election as well as all the proves and cryptographic parameters
 * @author t.buerk
 */
public class Ballot {
    
    private int id;
    
    private Election election;
    private List<String> selectedOptions;
    private Element c;
    private Element d;
    private Element u_Hat;
    
    private Tuple pi1;
    private Tuple pi2;
    private Tuple pi3;
    
    private LocalDateTime timeStamp;
    
    private boolean valid;
    private String reason;
    
    
    /**
     * Constructor for a new ballot where the corresponding election and a list of all the selected options are passed along 
     * @param election
     * @param selectedOptions 
     */
    public Ballot(Election election, List<String> selectedOptions) {
        this.election = election;
        this.selectedOptions = selectedOptions;
    }
    
    /**
     * Constructor thats restores a ballot from the jsonData of the ballot. the proves and cryptographic parameters are restored from thier string representations
     * @param id
     * @param election
     * @param selectedOptions
     * @param u_HatString
     * @param cString
     * @param dString
     * @param pi1String
     * @param pi2String
     * @param pi3String
     * @param timeStamp 
     */
    public Ballot(int id, Election election, List<String> selectedOptions,String u_HatString, String cString, String dString, String pi1String, String pi2String, String pi3String, LocalDateTime timeStamp) {
        this.id = id;
        this.election = election;
        this.selectedOptions = selectedOptions;
        this.timeStamp = timeStamp;
        this.valid = true;
        reason = "-";
        Parameters parameters = election.getGenerators();
        
        
        try {
            //restore the commitments and election Identifier
            c = parameters.getG_p().getElementFrom(cString);
            d = parameters.getG_q().getElementFrom(dString);
            u_Hat = parameters.getG_q().getElementFrom(u_HatString);
            
            Element e = StringMonoid.getInstance(Alphabet.ALPHANUMERIC).getElement(getSelectedOptionsString());
            
            //prepare ChallengeGenerators depending on the selectedOptions for the proofsystems
            SigmaChallengeGenerator fsscg = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getCommitmentSchemeP().getMessageSpace(), e);
            SigmaChallengeGenerator fsscg2 = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getZ_q(), e);
            
            //P1 generating pi1 Proofsystem to restore pi1 proof
            PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(fsscg, election.getCredentialPolynomial(), parameters.getCommitmentSchemeP());
            pi1 = pmps.getProofSpace().getElementFrom(pi1String);
            
            //P2 generating pi2 Proofsystem to restore pi2 proof
            DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(fsscg, parameters.getCommitmentSchemeP(), parameters.getCommitmentSchemeQ(), parameters.getSECURITY_FACTOR());
            pi2 = ddlps.getProofSpace().getElementFrom(pi2String);
            
            //P3 generating pi3 Proofsystem to restore pi3 proof
            DiscreteLogarithmCommitmentScheme discreteLogCommitmentScheme = DiscreteLogarithmCommitmentScheme.getInstance(election.getH_Hat());
            //Extract DicreteLogFunction
            Function discreteLogFunction = discreteLogCommitmentScheme.getCommitmentFunction();
            //Extract the the CommitmentFunction from the pedersenCommitmentScheme
            Function pedersenFunction = parameters.getCommitmentSchemeQ().getCommitmentFunction();
		
            //in order to put the two functions in the same ProofSystem they need to have the same Domains.	
            //define a selection Function with the same Domain as the PedersenCommitmentFunction that simply selects the Beta Element and returns it
            ProductSet space = (ProductSet) pedersenFunction.getDomain();		
            Function adapterFunction = SelectionFunction.getInstance(space, 0,1); //0,1 : Pfad für selektiertes Element 0 für MessageElements -> 1 für Beta
            // We chain the selection Function and the discreteLogFunction together. So the output of the selection function acts as input for the discreteLogFunction
            Function adaptedDiscreteLogFunction = CompositeFunction.getInstance(adapterFunction,discreteLogFunction);

            //Create a EqualityPreimageProofSystem with the PedersenCommitmentFunction and the adapted DicreteLogFunction
            EqualityPreimageProofSystem epps = EqualityPreimageProofSystem.getInstance(fsscg2, pedersenFunction, adaptedDiscreteLogFunction);
            pi3 = epps.getProofSpace().getElementFrom(pi3String);
            
        } catch (UniCryptException ex) {
            Logger.getLogger(Ballot.class.getName()).log(Level.SEVERE, null, ex);
            valid = false;
            reason = "unable to cast from json data";
        }

    }

    /**
     * Get the corresponding election of the ballot
     * @return the corresponding election of the ballot
     */
    public Election getElection() {
        return election;
    }

    /**
     * get the list of the selected options of the ballot
     * @return the list of the selected options of the ballot
     */
    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    /**
     * Takes the given privateCredentials as input and calculates all nessacairy proves for the ballot (c,d,u_Hat, pi1, pi2, pi3)
     * @param privatCredentials the private voter credentials to be used for proof calculation
     */
    public void calculateProves(PrivateCredentials privatCredentials) {
        Parameters parameters = election.getGenerators();
        Element alpha =privatCredentials.getAlpha();
        Element beta =privatCredentials.getBeta();
        Element u = parameters.getZ_p().getElement(privatCredentials.getU().convertToBigInteger());
        Element e = StringMonoid.getInstance(Alphabet.ALPHANUMERIC).getElement(getSelectedOptionsString());
        
        //Commitment c
        Element r = parameters.getZ_p().getRandomElement();
        c = parameters.getCommitmentSchemeP().commit(u, r);
        //Commitment d
        Pair messageElements = Pair.getInstance(alpha, beta);
        Element s = parameters.getZ_q().getRandomElement();
        d = parameters.getCommitmentSchemeQ().commit(messageElements, s);
        //election identifier u_Hat
        DiscreteLogarithmCommitmentScheme discreteLogCommitmentScheme = DiscreteLogarithmCommitmentScheme.getInstance(election.getH_Hat());
        u_Hat = discreteLogCommitmentScheme.commit(beta);
        
        //preparing ChallengeGenerators
        SigmaChallengeGenerator fsscg = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getCommitmentSchemeP().getMessageSpace(), e);
        SigmaChallengeGenerator fsscg2 = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getZ_q(), e);
        
        //P1 proof
	PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(fsscg, election.getCredentialPolynomial(), parameters.getCommitmentSchemeP());
	pi1 = pmps.generate(Pair.getInstance(u, r), c);
        
        //P2 proof
        DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(fsscg, parameters.getCommitmentSchemeP(), parameters.getCommitmentSchemeQ(), parameters.getSECURITY_FACTOR());
	pi2 = ddlps.generate(Tuple.getInstance(u, r, s, messageElements), Pair.getInstance(c, d));
        
        //P3 proof
        //Extract DicreteLogFunction
        Function discreteLogFunction = discreteLogCommitmentScheme.getCommitmentFunction();
        //Extract the the CommitmentFunction from the pedersenCommitmentScheme
	Function pedersenFunction = parameters.getCommitmentSchemeQ().getCommitmentFunction();
		
	//in order to put the two functions in the same ProofSystem they need to have the same Domains.	
	//define a selection Function with the same Domain as the PedersenCommitmentFunction that simply selects the Beta Element and returns it
	ProductSet space = (ProductSet) pedersenFunction.getDomain();		
	Function adapterFunction = SelectionFunction.getInstance(space, 0,1); //0,1 : Pfad für selektiertes Element 0 für MessageElements -> 1 für Beta
	// We chain the selection Function and the discreteLogFunction together. So the output of the selection function acts as input for the discreteLogFunction
	Function adaptedDiscreteLogFunction = CompositeFunction.getInstance(adapterFunction,discreteLogFunction);

	//Create a EqualityPreimageProofSystem with the PedersenCommitmentFunction and the adapted DicreteLogFunction
        EqualityPreimageProofSystem epps = EqualityPreimageProofSystem.getInstance(fsscg2, pedersenFunction, adaptedDiscreteLogFunction);
        pi3 = epps.generate(Tuple.getInstance(Pair.getInstance(alpha, beta), s), Pair.getInstance(d, u_Hat));

    }

    /**
     * Get the ballots id
     * @return the ballots id
     */
    public int getId() {
        return id;
    }  
    
    /**
     * get the Commitment c
     * @return the Commitment c
     */
    public Element getC() {
        return c;
    }

    /**
     * get the Commitment d
     * @return the Commitment d
     */
    public Element getD() {
        return d;
    }

    /**
     * get election identifier u_Hat
     * @return election credential u_Hat
     */
    public Element getU_Hat() {
        return u_Hat;
    }

    /**
     * Get the pi1 proof
     * @return the pi1 proof
     */
    public Tuple getPi1() {
        return pi1;
    }

    /**
     * Get the pi2 proof
     * @return the pi2 proof
     */
    public Tuple getPi2() {
        return pi2;
    }

    /**
     * Get the pi3 proof
     * @return the pi3 proof
     */
    public Tuple getPi3() {
        return pi3;
    }
    
    /**
     * Get the string repersentation of commitment c
     * @return the string repersentation of commitment c
     */
    public String getCString() {
        return c.convertToString();
    }

    /**
     * Get the string repersentation of commitment d
     * @return the string repersentation of commitment d
     */
    public String getDString() {
        return d.convertToString();
    }

    /**
     * Get the string repersentation of celection identifier u_Hat
     * @return the string repersentation of celection identifier u_Hat
     */
    public String getU_HatString() {
        return u_Hat.convertToString();
    }

    /**
     * Get the string repersentation of proof pi1
     * @return the string repersentation of proof pi1
     */
    public String getPi1String() {
        return pi1.convertToString();
    }

    /**
     * Get the string repersentation of proof pi2
     * @return the string repersentation of proof pi2
     */
    public String getPi2String() {
        return pi2.convertToString();
    }

    /**
     * Get the string repersentation of proof pi3
     * @return the string repersentation of proof pi3
     */
    public String getPi3String() {
        return pi3.convertToString();
    }  

    /**
     * get if the ballot is valid
     * @return validity of a ballot
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Changes the valid Flag to false and stores the given reason as string 
     * @param reason string indicating the reason of invalidity
     */
    public void setInvalid(String reason) {
        this.valid = false;
        this.reason = reason;
    }

    /**
     * get the timestamp of the ballot
     * @return the timestamp of the ballot
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    
    /**
     * get the string representation of the selected options
     * @return the string representation of the selected options
     */
    public String getSelectedOptionsString(){
        String result = "";
        //Collections.sort(selectedOptions);
        
        for (String option : selectedOptions){
            if (result == ""){
                result = option;
            }
            else{
                result += "," + option;
            }
        }
        
        return result;
    }
    
    /**
     * Get the String representation of the ballot for listing it
     * @return the String representation of the ballot for listing it
     */
    @Override
    public String toString(){
        String printedString = "ID: " + getId() + ", timestamp: " + timeStamp + ", Option: " + this.getSelectedOptionsString() + ", Vaild: " + isValid() + ", Reason: " + reason + ", u_Hat:" + this.u_Hat; 
        return printedString;
    }
    
    /**
     * get the reason String
     * @return the reason as String
     */
    public String getReason(){
        return reason;
    }
}
