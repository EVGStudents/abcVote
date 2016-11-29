/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp;

import ch.bfh.abcvote.util.model.Vote;
import ch.bfh.abcvote.verifierapp.controllers.MainController;

/**
 *
 * @author t.buerk
 */
public interface ControlledScreen {
    //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(MainController screenParent);
    //This method will allow the maincontroller to pass the controller a vote object to display
    public void setScene(Vote vote);
    
    public void setScene();
}