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
import ch.bfh.abcvote.util.model.PrivateCredentials;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class BallotSummaryController implements Initializable,ControlledScreen {

    private MainController parentController;
    private Ballot ballot;
        
    @FXML
    private Button btBack;
    @FXML
    private Button btCastBallot;
    @FXML
    private Label lbElectionTitle;
    @FXML
    private Label lbTopic;
    @FXML
    private Label lbPick;
    @FXML
    private ListView<String> lvSelectedOptions;
    @FXML
    private CheckBox chkTor;
    
    /**
     * Initializes the controller class.
     * @param url 
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
     * Method to set the screen with a given Ballot.
     * Stores the ballot in variable and displays it
     * @param ballot 
     */
    @Override
    public void setScene(Ballot ballot) {
        this.ballot = ballot;
        chkTor.setSelected(parentController.getTorUsage());
        displayBallot(ballot);
    }
    
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. 
     */
    @Override
    public void setScene() {
        
    }
    
    /**
     * btBack-Button Clicked-Event: sends user back to the PickOptions screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.PICKOPTIONSSCREENID);
    }

    /**
     * btNext-Button Clicked-Event: Calculates the proves for the ballot and passes the ballot to the maincontroller to post it to the bulletin board
     * @param event
     * @throws Exception 
     */
    @FXML
    private void btNextClicked(ActionEvent event) throws Exception {
        //TODO: Check if the prove calculations should be down/initiated by maincontroller
        PrivateCredentials privateCredentials = parentController.getPrivateCredentials();
        ballot.calculateProves(privateCredentials);
        parentController.postBallot(ballot);
        parentController.setScreen(VoterApp.HOMESCREENID);
    }

    /**
     * Displays the given ballot in the View
     * @param ballot 
     */
    private void displayBallot(Ballot ballot) {
        Election election = ballot.getElection();
        lbElectionTitle.setText(election.getTitle());
        lbTopic.setText(election.getTopic().getTitle());
        lbPick.setText(String.valueOf(election.getTopic().getPick()));
        populateSelectedOptionsListView(ballot.getSelectedOptions());
    }
    
    /**
     * Displays a list of selected Options strings in the lvSelectedOptions ListView
     * @param selectedOptionsList 
     */
    private void populateSelectedOptionsListView(List<String> selectedOptionsList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(selectedOptionsList);
        lvSelectedOptions.setItems(listViewList);
   
    }
    
}
