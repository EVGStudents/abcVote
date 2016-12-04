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
 * FXML Controller class
 *
 * @author t.buerk
 */
public class ElectionsOverviewController implements Initializable, ControlledScreen {

    MainController parentController;
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private ListView<ElectionHeader> lvElections;
    
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
        //get Election Headers and display them in listView
        List<ElectionHeader> electionHeadersList = parentController.getElectionHeaders(ElectionFilterTyp.ALL);
        populateElectionHeaderListView(electionHeadersList);
    }
    
    
        //Displays the given list of Voters in the ListView for selection
    private void populateElectionHeaderListView(List<ElectionHeader> electionHeaderList) {
        
        ObservableList<ElectionHeader> listViewList = FXCollections.observableArrayList(electionHeaderList);
        lvElections.setItems(listViewList);
        
    }

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        // get selected ElectionHeader
        ObservableList<ElectionHeader> selectedElections;
        selectedElections = lvElections.getSelectionModel().getSelectedItems();
        if( !selectedElections.isEmpty()){
           ElectionHeader selectedElectionHeader = selectedElections.get(0);
           int electionId = selectedElectionHeader.getId();
           // getElection
           Election election = parentController.getElectionById(electionId);           
           // getBallots
           List<Ballot> ballots = parentController.getBallotsByElection(election);
           //Calculate results
           ElectionResult result = parentController.calculateElectionResult(election, ballots);
           // pass election and ballots to Resultscreen
           parentController.setScreenWithResult(VerifierApp.RESULTOVERVIEWSCREENID, result);
        }
        
  
        
    }

    @Override
    public void setScene(ElectionResult result) {
        
    }
    
}
