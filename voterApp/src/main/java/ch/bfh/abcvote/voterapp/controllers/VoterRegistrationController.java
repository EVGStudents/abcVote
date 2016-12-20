/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.voterapp.ControlledScreen;
import ch.bfh.abcvote.voterapp.VoterApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class VoterRegistrationController implements Initializable, ControlledScreen {

    MainController parentController;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btRegister;
    @FXML
    private TextField txtEmailAdress;
    
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
     * Method to set the screen with a given election. Method not used in this controller class
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        
    }
    
    /**
     * Method to set the screen with a given Ballot. Method not used in this controller class
     * @param ballot 
     */    
    @Override
    public void setScene(Ballot ballot) {
        
    }  
    
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. 
     */
    @Override
    public void setScene() {
        
    }    
    
    /**
     * btBack-Button Clicked-Event: Sent user back to home screen without registring
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.HOMESCREENID);
    }
    
    /**
     * btRegister-Button Clicked-Event: Registers the voter with the entered email adress and transfers the user back to the home screen
     * @param event 
     */
    @FXML
    private void btRegisterClicked(ActionEvent event) throws Exception {
        String email = txtEmailAdress.getText();
        parentController.registerNewVoter(email);
        parentController.setScreen(VoterApp.HOMESCREENID);
    }
  
}
