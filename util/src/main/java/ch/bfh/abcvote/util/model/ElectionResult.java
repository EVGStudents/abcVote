/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.util.HashMap;
import java.util.List;

/**
 * Class to store the Results of an election
 * @author t.buerk
 */
public class ElectionResult {

    private HashMap<String, Integer> optionCount;
    private List<Ballot> ballots;
    private Election election;
    
    /**
     * Creates a new ElectioResult object for the given electipon
     * @param election 
     */
    public ElectionResult(Election election) {
        this.election = election;
        //initializes the optionCount Hashmap and creates an entry for every Option of the election
        optionCount = new HashMap<String, Integer>();
        
        for (String option : election.getTopic().getOptions()){
            optionCount.put(option, 0);
        }
    }

    /**
     * GEt the OptionCount Hashmap
     * @return 
     */
    public HashMap<String, Integer> getOptionCount() {
        return optionCount;
    }
    
    /**
     * Add one to the option counter to the given option 
     * @param option 
     */
    public void addToOptionCounter(String option){
        if(optionCount.containsKey(option)){
            optionCount.put(option, optionCount.get(option) + 1);
        }
    }

    /**
     * get a List of all the ballots
     * @return 
     */
    public List<Ballot> getBallots() {
        return ballots;
    }

    /**
     * Set the list off all the ballots
     * @param ballots 
     */
    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    /**
     * Get the election of the result
     * @return 
     */
    public Election getElection() {
        return election;
    }
    
    
    
}
