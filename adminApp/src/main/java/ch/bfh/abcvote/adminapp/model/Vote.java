/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.model;

import java.util.List;



/**
 *
 * @author t.buerk
 */
public class Vote {

    String title;
    List<Voter> voterList;
    Generators generators;
    VoteTopic topic;
    
    public Vote(Generators generators){
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

    public Generators getGenerators() {
        return generators;
    }

    public VoteTopic getTopic() {
        return topic;
    }

    public void setTopic(VoteTopic topic) {
        this.topic = topic;
    }
    
}
