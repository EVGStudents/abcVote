/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.model.Election;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 * Controller of the ElectionTitleCreation.fxml view
 * @author t.buerk
 */
public class ElectionTitleCreationController implements Initializable , ControlledScreen  {

    MainController parentController;
    Election election;
    
    @FXML
    private Button btBack;
    @FXML
    private TextField txtTitle;
    @FXML
    private Button btNext;


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
     * Takes the passes election and safes it in a global variabel and displays the title of the election
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        this.election = election; 
        txtTitle.setText(election.getTitle());
    }
    
    /**
     * btBack-Button Click-Event: send the user back to the Home screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.HOMESCREENID);
    }

    /**
     * btNext-Button Click-Event: Saves the election title entered by the user in the election object
     * and passes the election to the VoterSelection screen using the Maincontroller
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
        election.setTitle(txtTitle.getText());
        parentController.setScreenWithElection(AdminApp.VOTERSELECTIONSCREENID, election);
    }
    
}
