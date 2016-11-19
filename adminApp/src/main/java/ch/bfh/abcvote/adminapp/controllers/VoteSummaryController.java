/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.model.Vote;
import ch.bfh.unicrypt.UniCryptException;
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
public class VoteSummaryController implements Initializable, ControlledScreen {

    MainController parentController;
    Vote vote;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btCreate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.VOTEINGPERIODSELECTIONSCREENID);
    }

    @FXML
    private void btCreateClicked(ActionEvent event) {
        try {
            //calcualte coefficents from selected voters
            vote.calculateCoefficients();
            vote.pickH_Hat();
            
            parentController.postVote(vote);
            parentController.setScreen(AdminApp.HOMESCREENID);
        } catch (UniCryptException ex) {
            System.out.println("Sending vote failed!");
        }
    }

    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Vote vote) {
        this.vote = vote;
    }
    
}
