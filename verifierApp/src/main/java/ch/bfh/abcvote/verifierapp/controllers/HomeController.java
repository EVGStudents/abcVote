/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import ch.bfh.abcvote.verifierapp.VerifierApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class HomeController implements Initializable, ControlledScreen {

    MainController parentController;
    
    @FXML
    private Button btVerifyElection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Sets the parentController for the communication with other controllers
     * @param screenParent 
     */
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    /**
     * Method to set the screen with a given ElectionResult. Method not used in this controller class
     * @param election 
     */
    @Override
    public void setScene(ElectionResult result) {
        
    }
    /**
     * Method to set the screen with a given election. Method not used in this controller class
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        
    }
    /**
     * Method to set the screen when the screen to corresponding to the controller is displayed.
     * Method not used in this controller class
     */
    @Override
    public void setScene() {
        
    }
    
    /**
     * btVerifyElection-Button Clicked-Event: Starts the verifing process and transfers the user to the ElectionOverview screen
     * @param event 
     */
    @FXML
    private void btVerifiyElectionClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID);
    }




    
}
