/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        }

    }

    /**
     * Get the corresponding election of the ballot
     * @return 
     */
    public Election getElection() {
        return election;
    }

    /**
     * get the list of the selected options of the ballot
     * @return 
     */
    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    /**
     * Takes the given privateCredentials as input and calculates all nessacairy proves for the ballot (c,d,u_Hat, pi1, pi2, pi3)
     * @param privatCredentials 
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
     * @return 
     */
    public int getId() {
        return id;
    }  
    
    /**
     * get the Commitment C
     * @return 
     */
    public Element getC() {
        return c;
    }

    /**
     * get the Commitment D
     * @return 
     */
    public Element getD() {
        return d;
    }

    /**
     * get Election identifier u_Hat
     * @return 
     */
    public Element getU_Hat() {
        return u_Hat;
    }

    /**
     * Get the pi1 proof
     * @return 
     */
    public Tuple getPi1() {
        return pi1;
    }

    /**
     * Get the pi2 proof
     * @return 
     */
    public Tuple getPi2() {
        return pi2;
    }

    /**
     * Get the pi3 proof
     * @return 
     */
    public Tuple getPi3() {
        return pi3;
    }
    
    /**
     * Get the string repersentation of commitment c
     * @return 
     */
    public String getCString() {
        return c.convertToString();
    }

    /**
     * Get the string repersentation of commitment d
     * @return 
     */
    public String getDString() {
        return d.convertToString();
    }

    /**
     * Get the string repersentation of celection identifier u_Hat
     * @return 
     */
    public String getU_HatString() {
        return u_Hat.convertToString();
    }

    /**
     * Get the string repersentation of proof pi1
     * @return 
     */
    public String getPi1String() {
        return pi1.convertToString();
    }

    /**
     * Get the string repersentation of proof pi2
     * @return 
     */
    public String getPi2String() {
        return pi2.convertToString();
    }

    /**
     * Get the string repersentation of proof pi3
     * @return 
     */
    public String getPi3String() {
        return pi3.convertToString();
    }  

    /**
     * get if the the ballot is valid
     * @return 
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * set if the ballot is still valid
     * @param valid 
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * get the timestamp of the ballot
     * @return 
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    
    /**
     * get the string representation of the selected options
     * @return 
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
    
}
