/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.Controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.adminapp.model.Vote;
import ch.bfh.abcvote.adminapp.model.Voter;
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
 *
 * @author t.buerk
 */
public class VoterSelectionController implements Initializable, ControlledScreen {

    
    Vote vote;
    MainController parentController;
    
    @FXML
    private Button btNext;
    @FXML
    private Label lbSelectVoters;
    @FXML
    private ListView<Voter> lvVoters;
        @FXML
    private Button btBack;
        
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

    //stores the vote object in a global variable
    //and gets a list of all current voters in order to display it
    @Override
    public void setScene(Vote vote) {
        this.vote = vote;
        
        List<Voter> voterlist = parentController.getAllARegisteredVoters();
        populateVoterListView(voterlist);
    }

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.HOMESCREENID);
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        ObservableList<Voter> selectedVoters;
        selectedVoters = lvVoters.getSelectionModel().getSelectedItems();
        vote.setVoters(selectedVoters);
        parentController.setScreenWithVote(AdminApp.VOTEOPTIONCREATIONSCREENID, vote);
    }
    
    //Displays the given list of Voters in the ListView for selection
    private void populateVoterListView(List<Voter> voterlist) {
        
        ObservableList<Voter> listViewList = FXCollections.observableArrayList(voterlist);
        lvVoters.setItems(listViewList);
        lvVoters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
    }

    
}
