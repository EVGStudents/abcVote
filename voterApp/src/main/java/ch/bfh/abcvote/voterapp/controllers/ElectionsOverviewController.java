/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.Vote;
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

    @FXML
    private Button btBack;

    MainController parentController;
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

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.HOMESCREENID);
    }


    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Vote vote) {
        
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        ObservableList<ElectionHeader> selectedElections;
        selectedElections = lvElections.getSelectionModel().getSelectedItems();
        if( !selectedElections.isEmpty()){
           ElectionHeader selectedElectionHeader = selectedElections.get(0);
           int electionId = selectedElectionHeader.getId();
           Vote vote = parentController.getElectionById(electionId);
           parentController.setScreenWithVote(VoterApp.PICKOPTIONSSCREENID, vote);
        }
        

    }
    
    @Override
    public void setScene() {
        //get Election Headers and display them in listView
        List<ElectionHeader> electionHeadersList = parentController.getElectionHeaders(ElectionFilterTyp.OPEN);
        populateElectionHeaderListView(electionHeadersList);
    }
    
    
        //Displays the given list of Voters in the ListView for selection
    private void populateElectionHeaderListView(List<ElectionHeader> electionHeaderList) {
        
        ObservableList<ElectionHeader> listViewList = FXCollections.observableArrayList(electionHeaderList);
        lvElections.setItems(listViewList);
        
    }
    
    @Override
    public void setScene(Ballot ballot) {
        
    }
}
