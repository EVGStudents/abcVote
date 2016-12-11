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
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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
    
    

    public Ballot(Election election, List<String> selectedOptions) {
        this.election = election;
        this.selectedOptions = selectedOptions;
    }
    
    public Ballot(int id, Election election, List<String> selectedOptions,String u_HatString, String cString, String dString, String pi1String, String pi2String, String pi3String, LocalDateTime timeStamp) {
        this.id = id;
        this.election = election;
        this.selectedOptions = selectedOptions;
        this.timeStamp = timeStamp;
        this.valid = true;
        Parameters parameters = election.getGenerators();
        
        
        try {
            c = parameters.getG_p().getElementFrom(cString);
            d = parameters.getG_q().getElementFrom(dString);
            u_Hat = parameters.getG_q().getElementFrom(u_HatString);
            Element e = StringMonoid.getInstance(Alphabet.ALPHANUMERIC).getElement(getSelectedOptionsString());
            
            //ChallengeGenerators
            SigmaChallengeGenerator fsscg = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getCommitmentSchemeP().getMessageSpace(), e);
            SigmaChallengeGenerator fsscg2 = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getZ_q(), e);
            
            //P1
            PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(fsscg, election.getCredentialPolynomial(), parameters.getCommitmentSchemeP());
            pi1 = pmps.getProofSpace().getElementFrom(pi1String);
            
            //P2
            DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(fsscg, parameters.getCommitmentSchemeP(), parameters.getCommitmentSchemeQ(), parameters.getSECURITY_FACTOR());
            pi2 = ddlps.getProofSpace().getElementFrom(pi2String);
            
            //P3
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

    public Election getElection() {
        return election;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    public void calculateProves(PrivateCredentials privatCredentials) {
        Parameters parameters = election.getGenerators();
        Element alpha =privatCredentials.getAlpha();
        Element beta =privatCredentials.getBeta();
        Element u = parameters.getZ_p().getElement(privatCredentials.getU().convertToBigInteger());
        Element e = StringMonoid.getInstance(Alphabet.ALPHANUMERIC).getElement(getSelectedOptionsString());
        
        //Commitment p
        Element r = parameters.getZ_p().getRandomElement();
        c = parameters.getCommitmentSchemeP().commit(u, r);
        //Commitment q
        Pair messageElements = Pair.getInstance(alpha, beta);
        Element s = parameters.getZ_q().getRandomElement();
        d = parameters.getCommitmentSchemeQ().commit(messageElements, s);
        //u_Hat
        DiscreteLogarithmCommitmentScheme discreteLogCommitmentScheme = DiscreteLogarithmCommitmentScheme.getInstance(election.getH_Hat());
        u_Hat = discreteLogCommitmentScheme.commit(beta);
        
        //ChallengeGenerators
        SigmaChallengeGenerator fsscg = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getCommitmentSchemeP().getMessageSpace(), e);
        SigmaChallengeGenerator fsscg2 = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getZ_q(), e);
        
        //P1
	PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(fsscg, election.getCredentialPolynomial(), parameters.getCommitmentSchemeP());
	pi1 = pmps.generate(Pair.getInstance(u, r), c);
        
        //P2
        DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(fsscg, parameters.getCommitmentSchemeP(), parameters.getCommitmentSchemeQ(), parameters.getSECURITY_FACTOR());
	pi2 = ddlps.generate(Tuple.getInstance(u, r, s, messageElements), Pair.getInstance(c, d));
        
        //P3
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

    public int getId() {
        return id;
    }  
    
    public Element getC() {
        return c;
    }

    public Element getD() {
        return d;
    }

    public Element getU_Hat() {
        return u_Hat;
    }

    public Tuple getPi1() {
        return pi1;
    }

    public Tuple getPi2() {
        return pi2;
    }

    public Tuple getPi3() {
        return pi3;
    }
    
      public String getCString() {
        return c.convertToString();
    }

    public String getDString() {
        return d.convertToString();
    }

    public String getU_HatString() {
        return u_Hat.convertToString();
    }

    public String getPi1String() {
        return pi1.convertToString();
    }

    public String getPi2String() {
        return pi2.convertToString();
    }

    public String getPi3String() {
        return pi3.convertToString();
    }  

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    
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
