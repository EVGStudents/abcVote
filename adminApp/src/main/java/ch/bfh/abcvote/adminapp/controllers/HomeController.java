/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.Election;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class for the home screen of the adminApp 
 *
 * @author t.buerk
 */
public class HomeController implements Initializable, ControlledScreen {

    MainController parentController;
    
    @FXML
    private Button btCreateElection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    /** Sets the parentController for the communication with other controllers
     * @param screenParent 
     */
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }
    
    /**
     * This interface method is not used by the HomeController
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        
    }

        
    /**
     * btCreateNew-Button Click-Event: Event to create a new election.
     * Gets the current Parameters of the Bulletin Board and creates a new Election object.
     * Afterwards newly created election gets passed to the ElectionTitleCreation Screen via MainController 
     */
    @FXML
    private void btcreateNewElectionClicked(ActionEvent event) {
        Parameters params = parentController.getParameters();
        Election election = new Election(params);
        //maincontroller gets instructed to change the scene to the voterselection screen and pass the newly created election
        parentController.setScreenWithElection(AdminApp.ELECTIONTITLECREATIONSCREENID, election);
    }


    
}
