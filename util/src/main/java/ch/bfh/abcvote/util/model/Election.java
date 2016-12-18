/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.time.LocalDateTime;
import java.util.List;




/**
 * The Election class hold all the information of an election
 * @author t.buerk
 */
public class Election {
    
    private int id;
    private String title;
    private List<Voter> voterList;
    private Parameters generators;
    private ElectionTopic topic;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Element h_Hat;
    private String appVersion;
    
    PolynomialElement credentialPolynomial;
    
    /**
     * Creates a new empty election with the parameters from the bulletin board
     * @param generators 
     */
    public Election(Parameters generators){
        this.generators = generators;
    }

    /**
     * Constructor to restore an election from the jsonData
     * @param id
     * @param title
     * @param voterList
     * @param generators
     * @param startDate
     * @param endDate
     * @param topic
     * @param appVersion
     * @param h_HatString
     * @param credentialPolynomialString 
     */
    public Election(int id, String title, List<Voter> voterList, Parameters generators, LocalDateTime startDate, LocalDateTime endDate, ElectionTopic topic, String appVersion, String h_HatString, String credentialPolynomialString) {
        this.id = id;
        this.title = title;
        this.voterList = voterList;
        this.generators = generators;
        this.startDate = startDate;
        this.endDate = endDate;
        this.topic = topic;
        this.appVersion = appVersion;
        this.setH_Hat(h_HatString);
        this.setCredentialPolynomial(credentialPolynomialString);
    }
    
    /**
     * Get the title of the election
     * @return 
     */
    public String getTitle(){
        return title;
    }
    /**
     * Set the title of the election
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Get the voter list of the election
     * @return 
     */
    public List<Voter> getVoterList() {
        return voterList;
    }

    /**
     * set the voter list of the election
     */
    public void setVoterList(List<Voter> voterList) {
            this.voterList = voterList;
    }

    /**
     * Get the crypto parameters of the election
     * @return 
     */
    public Parameters getGenerators() {
        return generators;
    }

    /**
     * Get the electionTopic of the election
     * @return 
     */
    public ElectionTopic getTopic() {
        return topic;
    }

    /**
     * Set the electionTopic of the election
     */
    public void setTopic(ElectionTopic topic) {
        this.topic = topic;
    }

    /**
     * Get the starting date of the election
     * @return 
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Get the ending date of the election
     * @return 
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    /**
     * Set start and end date of the voting period
     * @param startDate
     * @param endDate 
     */
    public void setVotingPeriod(LocalDateTime startDate, LocalDateTime endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * Calculates the coefficents of the polynom based on the list of voters
     * @throws UniCryptException 
     */
    public void calculateCoefficients() throws UniCryptException{
        ZMod Z_p = generators.getZ_p();
        DualisticElement zero = Z_p.getZeroElement();
	DualisticElement one = Z_p.getOneElement();
        credentialPolynomial = null;
        for (Voter voter : voterList){
            Element u  = Z_p.getElementFrom(voter.publicCredential);
            Polynomial newRoot = Polynomial.getInstance(new DualisticElement[]{(DualisticElement) u.invert(), one}, zero, one);
            
            if (credentialPolynomial == null){
                
                credentialPolynomial = PolynomialSemiRing.getInstance(Z_p).getElement(newRoot);               
            }
            else{      
                credentialPolynomial = this.credentialPolynomial.multiply(PolynomialSemiRing.getInstance(Z_p).getElement(newRoot));
            }
        }  
        
    }

    /**
     * pick a new election generator H_Hat at random
     */
    public void pickH_Hat() {
        h_Hat = generators.getG_q().getRandomGenerator();
    }
    
    /**
     * GEt election generator H_Hat
     * @return 
     */
    public Element getH_Hat() {
        return h_Hat;
    }
    
    /**
     * Get the string repersentation of the election generator H_Hat
     * @return 
     */
    public String getH_HatString() {
        return h_Hat.convertToString();
    }

    /**
     * Restores the election generator H_Hat from the given String
     * @param h_HatString 
     */
    public void setH_Hat(String h_HatString) {
        try {
            this.h_Hat = generators.getG_q().getElementFrom(h_HatString);
        } catch (UniCryptException ex) {
            
        }
    }
    
    /**
     * GEt the CredentialPolynomial of the election
     * @return 
     */
    public PolynomialElement getCredentialPolynomial() {
        return credentialPolynomial;
    }

    /**
     * Get the string representation of the CredentialPolynomial
     * @return 
     */
    public String getCredentialPolynomialString() {
        return credentialPolynomial.convertToString();
    }

    /**
     * Restores the CredentialPolynomial from the given String
     * @param credentialPolynomialString 
     */
    public void setCredentialPolynomial(String credentialPolynomialString) {
        ZMod Z_p = generators.getZ_p();
        try { 
            credentialPolynomial = PolynomialSemiRing.getInstance(Z_p).getElementFrom(credentialPolynomialString);
        } catch (UniCryptException ex) {
            
        }
    }

    /**
     * get the id of the election
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * get the appversion of the election
     * @return 
     */
    public String getAppVersion() {
        return appVersion;
    }
    
    
    
}
