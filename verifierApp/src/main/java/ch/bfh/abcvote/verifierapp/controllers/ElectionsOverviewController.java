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
     * btResult-Button Clicked-Event: Fetches the Election data of the selected electionHeader and calculates the results for this election.
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

    /**
     * btStepByStep-Button Clicked-Event: Fetches the Election data of the selected electionHeader and only preforms the validation process of the timestamp
     * Afterwards the result gets passed to the timestamp validation screen
     * @param event 
     */
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
