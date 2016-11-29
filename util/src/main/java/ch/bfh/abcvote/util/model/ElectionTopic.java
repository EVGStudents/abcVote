/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author t.buerk
 */
public class ElectionTopic {
    
    String title;
    List<String> options;
    int pick;
    
    public ElectionTopic(String title, int pick){
        this.title = title;
        this.pick = pick;
        this.options = new ArrayList<String>();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPick() {
        return pick;
    }

    public void setPick(int pick) {
        this.pick = pick;
    }

    public List<String> getOptions() {
        return options;
    }

    public void addOption(String option){
        options.add(option);
    }

    
}
