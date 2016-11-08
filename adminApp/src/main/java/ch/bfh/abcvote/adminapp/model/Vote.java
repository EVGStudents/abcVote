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

    List<Voter> voterList;
    
    
    public void setVoters(List<Voter> selectedVoters) {
        voterList = selectedVoters;
    }
    
    public List<Voter> getVoters(){
        return voterList;
    }
    
}
