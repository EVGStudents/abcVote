/*
 * abcVote
 *
 *  abcVote - an e-voting prototype with everlasting privacy
 *  Copyright (c) 2017 Timo Buerk and Sebastian Nellen
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for abcVote may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and us.
 *
 *
 *   For further information contact <e-mail: burkt4@gmail.com> or <e-mail: sebastian@nellen.it>
 *
 *
 * Redistributions of files must retain the above copyright notice.
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
