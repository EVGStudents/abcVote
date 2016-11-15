/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.model;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.time.LocalDate;
import java.util.List;



/**
 *
 * @author t.buerk
 */
public class Vote {

    String title;
    List<Voter> voterList;
    Parameters generators;
    VoteTopic topic;
    LocalDate startDate;
    LocalDate endDate;
    
    PolynomialElement credentialPolynomial;
    
    public Vote(Parameters generators){
        this.generators = generators;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }

    public List<Voter> getVoterList() {
        return voterList;
    }

    public void setVoterList(List<Voter> voterList) {
        try {
            this.voterList = voterList;
            calculateCoefficients();
        } catch (UniCryptException ex) {
            
        }
    }

    public Parameters getGenerators() {
        return generators;
    }

    public VoteTopic getTopic() {
        return topic;
    }

    public void setTopic(VoteTopic topic) {
        this.topic = topic;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setVotingPeriod(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    private void calculateCoefficients() throws UniCryptException{
        ZMod Z_p = generators.getZ_p();
        DualisticElement zero = Z_p.getZeroElement();
	DualisticElement one = Z_p.getOneElement();
        credentialPolynomial = null;
        for (Voter voter : voterList){
            Element u  = Z_p.getElementFrom(voter.publicCredential);
            //Element u  = Z_p.getRandomElement();
            System.out.println("U:");
            System.out.println(u.convertToString());
            Polynomial newRoot = Polynomial.getInstance(new DualisticElement[]{(DualisticElement) u.invert(), one}, zero, one);
            
            if (credentialPolynomial == null){
                
                credentialPolynomial = PolynomialSemiRing.getInstance(Z_p).getElement(newRoot);               
            }
            else{      
                credentialPolynomial = this.credentialPolynomial.multiply(PolynomialSemiRing.getInstance(Z_p).getElement(newRoot));
            }
        }
        System.out.println(credentialPolynomial.convertToString());
	Polynomial testPoly = credentialPolynomial.getValue();
        for (int i =0; i < testPoly.countCoefficients(); i++){
            System.out.println("index: " + i);
            System.out.println(testPoly.getCoefficient(i).toString());
            
        }
        credentialPolynomial = PolynomialSemiRing.getInstance(Z_p).getElementFrom(credentialPolynomial.convertToString());
        System.out.println(credentialPolynomial.convertToString());
        
    }
    
}
