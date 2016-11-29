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

    @FXML
    private Button btBack;

    MainController parentController;
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

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.HOMESCREENID);
    }


    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Election election) {
        
    }

    @FXML
    private void btRegisterClicked(ActionEvent event) {
        String email = txtEmailAdress.getText();
        parentController.registerNewVoter(email);
        parentController.setScreen(VoterApp.HOMESCREENID);
    }
    
    @Override
    public void setScene() {
        
    }
    
    @Override
    public void setScene(Ballot ballot) {
        
    }    
}
