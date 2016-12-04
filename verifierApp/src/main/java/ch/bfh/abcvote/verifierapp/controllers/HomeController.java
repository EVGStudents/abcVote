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

    @FXML
    private Button btVerifyElection;

    MainController parentController;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btVerifiyElectionClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID);
    }

    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Election election) {
        
    }

    @Override
    public void setScene() {
        
    }

    @Override
    public void setScene(ElectionResult result) {
        
    }
    
}
