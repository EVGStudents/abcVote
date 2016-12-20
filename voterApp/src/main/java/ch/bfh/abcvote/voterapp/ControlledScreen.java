/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.voterapp.controllers.MainController;

/**
 * Interface all scene controllers implement in order to create a uniform way to pass them the maincontroller and the election Object to display 
 * @author t.buerk
 */
public interface ControlledScreen {
    
    /**
     * This method allwos to pass the MainController to the controller of a screen 
     * @param screenParent 
     */
    public void setScreenParent(MainController screenParent);
    
    /**
     * This method will allow the maincontroller to pass the controller a election object to save and/or display
     * @param election 
     */
    public void setScene(Election election);
    
    /**
     * This method will allow the maincontroller to pass the controller a Ballot object to save and/or display
     * @param ballot 
     */
    public void setScene(Ballot ballot);
    
    /**
     * This method will allow the maincontroller to signal the controller that it is about to be displayed 
     */
    public void setScene();
}

