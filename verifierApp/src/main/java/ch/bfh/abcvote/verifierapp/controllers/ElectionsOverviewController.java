/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import ch.bfh.abcvote.verifierapp.VerifierApp;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class for the ElectionsOverview screen
 *
 * @author t.buerk
 */
public class ElectionsOverviewController implements Initializable, ControlledScreen {

    MainController parentController;
    
    @FXML
    private Button btBack;
    @FXML
    private ListView<ElectionHeader> lvElections;
    @FXML
    private Button btResult;
    @FXML
    private Button btStepByStep;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Sets the parentController for the communication with other controllers
     * @param screenParent 
     */
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    /**
     * Method to set the screen with a given election. Method not used in this controller class
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        
    }
    
    /**
     * Method to set the screen with a given ElectionResult. Method not used in this controller class
     * @param result 
     */
    @Override
    public void setScene(ElectionResult result) {
        
    }

    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. 
     */
    @Override
    public void setScene() {
        //get a list of ElectionHeaders of all Election that have already ended and display it in listView
        List<ElectionHeader> electionHeadersList = parentController.getElectionHeaders(ElectionFilterTyp.ALL);
        populateElectionHeaderListView(electionHeadersList);
    }
    
    /**
     * btBack-Button Clicked-Event: Transfers the user back to the home screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }
        
    /**
     * btNext-Button Clicked-Event: Fetches the Election data of the selected electionHeader and calculates the results for this election.
     * Afterwards the result gets passed to the reult screen
     * @param event 
     */
    @FXML
    private void btResultClicked(ActionEvent event) {
                // get selected ElectionHeader from view
        ObservableList<ElectionHeader> selectedElections;
        selectedElections = lvElections.getSelectionModel().getSelectedItems();
        if( !selectedElections.isEmpty()){
           ElectionHeader selectedElectionHeader = selectedElections.get(0);
           int electionId = selectedElectionHeader.getId();
           // get election object from bulletin board
           Election election = parentController.getElectionById(electionId);           
           // get all the posted ballots for this election
           List<Ballot> ballots = parentController.getBallotsByElection(election);
           //Calculate the results of the election
           ElectionResult result = parentController.calculateElectionResult(election, ballots);
           // pass electionResult to Resultscreen
           parentController.setScreenWithResult(VerifierApp.RESULTOVERVIEWSCREENID, result);
        }
    }

    @FXML
    private void btStepByStepClicked(ActionEvent event) {
        // get selected ElectionHeader from view
        ObservableList<ElectionHeader> selectedElections;
        selectedElections = lvElections.getSelectionModel().getSelectedItems();
        if( !selectedElections.isEmpty()){
           ElectionHeader selectedElectionHeader = selectedElections.get(0);
           int electionId = selectedElectionHeader.getId();
           // get election object from bulletin board
           Election election = parentController.getElectionById(electionId);           
           // get all the posted ballots for this election
           List<Ballot> ballots = parentController.getBallotsByElection(election);
           //preform the timestamp validation process for all ballots
           ElectionResult result = parentController.doTimestampVaildation(election, ballots);
           // pass electionResult to Resultscreen
           parentController.setScreenWithResult(VerifierApp.TIMESTAMPVALIDATIONSCREENID, result);
        }
    }
    
    /**
     * Displays the given list of Election in the ListView for selection
     * @param electionHeaderList 
     */
    private void populateElectionHeaderListView(List<ElectionHeader> electionHeaderList) {
        
        ObservableList<ElectionHeader> listViewList = FXCollections.observableArrayList(electionHeaderList);
        lvElections.setItems(listViewList);
        
    }
 
}
