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
     * @param url
     * @param rb
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
