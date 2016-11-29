/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionTopic;
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
public class ElectionOptionCreationController implements Initializable, ControlledScreen  {

    MainController parentController;
    Election election;
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private TextField txtVotingTopic;
    @FXML
    private TextField txtOption1;
    @FXML
    private TextField txtOption2;
    @FXML
    private TextField txtPick;
    
        @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @Override
    public void setScene(Election election) {
       this.election = election;
    }

    @FXML
    private void btBackClicked(ActionEvent event) {
         parentController.setScreen(AdminApp.VOTERSELECTIONSCREENID);
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        int pick = Integer.parseInt(txtPick.getText());
        ElectionTopic topic = new ElectionTopic(txtVotingTopic.getText(), pick);
        topic.addOption(txtOption1.getText());
        topic.addOption(txtOption2.getText());
        election.setTopic(topic);
        parentController.setScreenWithElection(AdminApp.VOTEINGPERIODSELECTIONSCREENID, election);
    }
    
}