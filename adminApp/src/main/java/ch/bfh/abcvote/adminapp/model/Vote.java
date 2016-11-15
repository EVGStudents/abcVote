/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.model;

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
        this.voterList = voterList;
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
    
}
