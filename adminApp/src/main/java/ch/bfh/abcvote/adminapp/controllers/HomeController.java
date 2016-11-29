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
    
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @Override
    public void setScene(Election election) {
        
    }

        
    /**
     * Button press event to create a new election. Gets the current Parameters of the Bulletin Board and creats a new Election object.
     */
    @FXML
    private void btcreateNewElectionClicked(ActionEvent event) {
        Parameters params = parentController.getParameters();
        Election election = new Election(params);
        //maincontroller gets instructed to change the scene to the voterselection screen and pass the newly created election
        parentController.setScreenWithElection(AdminApp.ELECTIONTITLECREATIONSCREENID, election);
    }


    
}
