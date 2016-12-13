/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.Voter;
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
import javafx.scene.control.SelectionMode;

/**
 * FXML Controller class
 * Controller of the VoterSelection.fxml view
 * @author t.buerk
 */
public class VoterSelectionController implements Initializable, ControlledScreen {
    
    Election election;
    MainController parentController;
    
    @FXML
    private Button btNext;
    @FXML
    private Label lbSelectVoters;
    @FXML
    private ListView<Voter> lvVoters;
    @FXML
    private Button btBack;
      
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
     * stores the election object in a global variable
     * and gets a list of all current voters in order to display it
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        this.election = election;       
        List<Voter> voterlist = parentController.getAllARegisteredVoters();
        populateVoterListView(voterlist);
    }

    /**
     * btBack-Button Click-Event: sends the user back to the ElectionTitleCreation Screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.ELECTIONTITLECREATIONSCREENID);
    }

    /**
     * btNext-Button Click-Event: gets all selected voters form the lvVoters listview and stores them in the election object.
     * afterward the election object gets passed to the ElectionOptionCreation Screen via the mainController
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
        ObservableList<Voter> selectedVoters;
        selectedVoters = lvVoters.getSelectionModel().getSelectedItems();
        election.setVoterList(selectedVoters);
        parentController.setScreenWithElection(AdminApp.ELECTIONOPTIONCREATIONSCREENID, election);
    }
    
    /**
     * Displays the given list of Voters in the lvVoters ListView for selection
     * @param voterlist 
     */
    private void populateVoterListView(List<Voter> voterlist) {
        
        ObservableList<Voter> listViewList = FXCollections.observableArrayList(voterlist);
        lvVoters.setItems(listViewList);
        lvVoters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
    }

    
}
