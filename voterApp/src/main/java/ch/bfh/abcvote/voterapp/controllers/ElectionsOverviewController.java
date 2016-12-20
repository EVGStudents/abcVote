/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.voterapp.ControlledScreen;
import ch.bfh.abcvote.voterapp.VoterApp;
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
public class ElectionsOverviewController implements Initializable,ControlledScreen {

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
     * Method to set the screen with a given Ballot. Method not used in this controller class
     * @param ballot 
     */
    @Override
    public void setScene(Ballot ballot) {
        
    }
    
    /**
     * Gets the ElectionHeaders of all currently open elections and displays them 
     */
    @Override
    public void setScene() {
        //get Election Headers and display them in listView
        List<ElectionHeader> electionHeadersList = parentController.getElectionHeaders(ElectionFilterTyp.OPEN);
        populateElectionHeaderListView(electionHeadersList);
    }
    
    /**
     * btBack-Button Clicked-Event: Transfers the voter back to home screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.HOMESCREENID);
    }
    
    /**
     * btNext-Button Clicked-Event: Gets all the information to an election of the selected ElectionHeader
     * and passes the recieved election object to the PickOption Screen
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
        ObservableList<ElectionHeader> selectedElections;
        selectedElections = lvElections.getSelectionModel().getSelectedItems();
        if( !selectedElections.isEmpty()){
           ElectionHeader selectedElectionHeader = selectedElections.get(0);
           int electionId = selectedElectionHeader.getId();
           Election election = parentController.getElectionById(electionId);
           parentController.setScreenWithElection(VoterApp.PICKOPTIONSSCREENID, election);
        }
        

    }
    
    
    /**
     * Displays the given list of ElectionHeader in the ListView for selection
     * @param electionHeaderList 
     */
    private void populateElectionHeaderListView(List<ElectionHeader> electionHeaderList) {
        
        ObservableList<ElectionHeader> listViewList = FXCollections.observableArrayList(electionHeaderList);
        lvElections.setItems(listViewList);
        
    }
    

}
