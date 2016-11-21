/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.util.List;

/**
 *
 * @author t.buerk
 */
public class Ballot {
    
    Vote vote;
    List<String> selectedOptions;

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
        System.out.println("Alpha: " + privatCredentials.getAlpha());
        System.out.println("Beta: " + privatCredentials.getBeta());
    }
    
    
}
