/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import ch.bfh.unicrypt.crypto.proofsystem.classes.DoubleDiscreteLogProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.EqualityPreimageProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.PolynomialMembershipProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.DiscreteLogarithmCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.List;

/**
 *
 * @author t.buerk
 */
public class Ballot {
    
    Vote vote;
    List<String> selectedOptions;
    Element c;
    Element d;
    Element u_Hat;
    
    Tuple pi1;
    Tuple pi2;
    Tuple pi3;

    public Ballot(Vote vote, List<String> selectedOptions) {
        this.vote = vote;
        this.selectedOptions = selectedOptions;
    }

    public Vote getVote() {
        return vote;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    public void calculateProves(PrivateCredentials privatCredentials) {
        Parameters parameters = vote.getGenerators();
        Element alpha =privatCredentials.getAlpha();
        Element beta =privatCredentials.getBeta();
        Element u = parameters.getZ_p().getElement(privatCredentials.getU().convertToBigInteger());
        
        //Commitment p
        Element r = parameters.getZ_p().getRandomElement();
        c = parameters.getCommitmentSchemeP().commit(u, r);
        //Commitment q
        Pair messageElements = Pair.getInstance(alpha, beta);
        Element s = parameters.getZ_q().getRandomElement();
        d = parameters.getCommitmentSchemeQ().commit(messageElements, s);
        //u_Hat
        DiscreteLogarithmCommitmentScheme discreteLogCommitmentScheme = DiscreteLogarithmCommitmentScheme.getInstance(vote.getH_Hat());
        u_Hat = discreteLogCommitmentScheme.commit(beta);
        
        //P1
	PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(vote.getCredentialPolynomial(), parameters.getCommitmentSchemeP());
	pi1 = pmps.generate(Pair.getInstance(u, r), c);
        
        //P2
        DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(parameters.getCommitmentSchemeP(), parameters.getCommitmentSchemeQ(), parameters.getSECURITY_FACTOR());
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
        EqualityPreimageProofSystem epps = EqualityPreimageProofSystem.getInstance(pedersenFunction, adaptedDiscreteLogFunction);
        pi3 = epps.generate(Tuple.getInstance(Pair.getInstance(alpha, beta), s), Pair.getInstance(d, u_Hat));

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
    
    
}
