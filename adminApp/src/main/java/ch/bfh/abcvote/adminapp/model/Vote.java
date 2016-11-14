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
    
    public Vote(Generators generators){
        this.generators = generators;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setVoters(List<Voter> selectedVoters) {
        voterList = selectedVoters;
    }
    
    public List<Voter> getVoters(){
        return voterList;
    }
    
}
