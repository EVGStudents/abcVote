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
 *
 * @author t.buerk
 */
public class ElectionTitleCreationController implements Initializable , ControlledScreen  {

    @FXML
    private Button btBack;
    @FXML
    private TextField txtTitle;
    @FXML
    private Button btNext;

    MainController parentController;
    Election election;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.HOMESCREENID);
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        election.setTitle(txtTitle.getText());
        parentController.setScreenWithElection(AdminApp.VOTERSELECTIONSCREENID, election);
    }

    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Election election) {
        this.election = election; 
        txtTitle.setText(election.getTitle());
    }
    
}
