/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import ch.bfh.abcvote.verifierapp.VerifierApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class ResultOverviewController implements Initializable, ControlledScreen {

    MainController parentController;
    @FXML
    private Button btBack;
    @FXML
    private Button btPublish;
    @FXML
    private Button btHome;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID);
    }


    @FXML
    private void btPublishClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }

    @FXML
    private void btHomeClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }
    
}
