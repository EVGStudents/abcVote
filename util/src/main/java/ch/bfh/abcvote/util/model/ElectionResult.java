/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author t.buerk
 */
public class ElectionResult {

    private HashMap<String, Integer> optionCount;
    private List<Ballot> ballots;
    private Election election;
    
    public ElectionResult(Election election) {
        this.election = election;
        optionCount = new HashMap<String, Integer>();
        
        for (String option : election.getTopic().getOptions()){
            optionCount.put(option, 0);
        }
    }

    public HashMap<String, Integer> getOptionCount() {
        return optionCount;
    }
    
    public void addToOptionCounter(String option){
        if(optionCount.containsKey(option)){
            optionCount.put(option, optionCount.get(option) + 1);
        }
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    public Election getElection() {
        return election;
    }
    
    
    
}
