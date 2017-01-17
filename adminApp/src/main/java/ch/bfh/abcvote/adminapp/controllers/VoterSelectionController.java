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
