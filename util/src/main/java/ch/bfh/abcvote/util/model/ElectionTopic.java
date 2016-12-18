/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores the topic and the available options off an election as well as the number of options a voter can pick
 * @author t.buerk
 */
public class ElectionTopic {
    
    String title;
    List<String> options;
    int pick;
    
    /**
     * Creates a new ElectionTopic object with the given title and pick and an empty options list
     * @param title
     * @param pick 
     */
    public ElectionTopic(String title, int pick){
        this.title = title;
        this.pick = pick;
        this.options = new ArrayList<String>();
    }
    
    /**
     * returns the topic Title
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the topic of the Topic
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the number of options that can be picked by the voter
     * @return 
     */
    public int getPick() {
        return pick;
    }
    /**
     * Set the number of options that can be picked by the voter
     * @param pick 
     */
    public void setPick(int pick) {
        this.pick = pick;
    }

    /**
     * Gets a list of all the options of the topic
     * @return 
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * add the given String to the options list
     * @param option 
     */
    public void addOption(String option){
        options.add(option);
    }

    
}
