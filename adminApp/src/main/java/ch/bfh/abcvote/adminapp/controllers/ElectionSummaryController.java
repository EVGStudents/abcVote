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
 * Controller of the ElectionSummary.fxml view
 * @author t.buerk
 */
public class ElectionSummaryController implements Initializable, ControlledScreen {

    MainController parentController;
    Election election;
    
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
        
    }    
    
    /** Sets the parentController for the communication with other controllers
     * @param screenParent 
     */
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }
    
    /**
     * Takes the passes election and safes it in a global variabel and displays the election
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        this.election = election;
        displayElection(election);
    }
    
    /**
     * btBack-Button Click-Event: sends the user back to the VotingperiodSelection-Screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.VOTEINGPERIODSELECTIONSCREENID);
    }
    
    /**
     * btNext-Button Click Event: orders the election to calculate the oefficients and pick a election generator.
     * Afterwards the election object gets passed to the maincontroller in order to post it to the bulletin board
     * @param event 
     */
    @FXML
    private void btCreateClicked(ActionEvent event) {
        try {
            //calcualte coefficents from selected voters
            election.calculateCoefficients();
            election.pickH_Hat();
            //pass election to maincontroller and go back to HomeScreen if successful 
            parentController.postElection(election);
            parentController.setScreen(AdminApp.HOMESCREENID);
        } catch (UniCryptException ex) {
            System.out.println("Sending election failed!");
        }
    }
    
    /**
     * Displays the passed election in the Summary Screen 
     * @param election 
     */
    private void displayElection(Election election) {
        ElectionTopic topic = election.getTopic();
        String votingPeriodString = election.getStartDate().toString() + " - " + election.getEndDate().toString();
        //display header Data
        lbTitle.setText(election.getTitle());
        lbVotingPeriod.setText(votingPeriodString);
        //Display voterlist
        populateVotersListView(election.getVoterList());
        //display votingtopic Data
        lbTopic.setText(topic.getTitle());
        lbPick.setText(String.valueOf(topic.getPick()));
        populateOptionsListView(topic.getOptions());
    }
    
    /**
     * Takes a List of Voter obejcts and displays it in the lvVoters listview
     * @param votersList 
     */
    private void populateVotersListView(List<Voter> votersList) {     
        ObservableList<Voter> listViewList = FXCollections.observableArrayList(votersList);
        lvVoters.setItems(listViewList);     
    }
    
     /**
     * Takes a List of Strings and displays them in the lvOptions listview
     * @param optionsList 
     */
    private void populateOptionsListView(List<String> optionsList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(optionsList);
        lvOptions.setItems(listViewList);
        
    }
    
}
