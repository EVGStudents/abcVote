/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.model.Vote;
import ch.bfh.abcvote.util.model.VoteTopic;
import ch.bfh.abcvote.util.model.Voter;
import ch.bfh.unicrypt.UniCryptException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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
    @FXML
    private ListView<Voter> lvVoters;
    @FXML
    private ListView<String> lvOptions;
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbTopic;
    @FXML
    private Label lbPick;
    @FXML
    private Label lbVotingPeriod;

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
        displayVote(vote);
    }

    private void displayVote(Vote vote) {
        VoteTopic topic = vote.getTopic();
        String votingPeriodString = vote.getStartDate().toString() + " - " + vote.getEndDate().toString();
        lbTitle.setText(vote.getTitle());
        lbVotingPeriod.setText(votingPeriodString);
        populateVotersListView(vote.getVoterList());
        lbTopic.setText(topic.getTitle());
        lbPick.setText(String.valueOf(topic.getPick()));
        populateOptionsListView(topic.getOptions());
    }
    
    private void populateOptionsListView(List<String> optionsList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(optionsList);
        lvOptions.setItems(listViewList);
        
    }
    
    private void populateVotersListView(List<Voter> votersList) {
        
        ObservableList<Voter> listViewList = FXCollections.observableArrayList(votersList);
        lvVoters.setItems(listViewList);
        
    }
    
}
