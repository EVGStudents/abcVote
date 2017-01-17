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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 * Controller of the ElectionOptionCreation.fxml view
 * @author t.buerk
 */
public class ElectionOptionCreationController implements Initializable, ControlledScreen  {

    MainController parentController;
    Election election;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private TextField txtVotingTopic;
    @FXML
    private TextField txtOption1;
    @FXML
    private TextField txtOption2;
    @FXML
    private TextField txtPick;
    
    
     /**
     * Initializes the controller class.
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
     * Takes the passed election Object and safes it in a global variabel
     * @param election 
     */
    @Override
    public void setScene(Election election) {
       this.election = election;
    }
    
    /**
    *btBack-Button Click-Event: sends user back to the VoterSelction-Screen
    * @param event 
    */
    @FXML
    private void btBackClicked(ActionEvent event) {
         parentController.setScreen(AdminApp.VOTERSELECTIONSCREENID);
    }
    
    /**
     * btNext-Button Click-Event: takes the user input from the UI and creates a new ElectionTopic object and adds it to the current election.
     * Afterwards the election object gets passed to the Votingperiodselection Screen.
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
        int pick = Integer.parseInt(txtPick.getText());
        ElectionTopic topic = new ElectionTopic(txtVotingTopic.getText(), pick);
        topic.addOption(txtOption1.getText());
        topic.addOption(txtOption2.getText());
        election.setTopic(topic);
        parentController.setScreenWithElection(AdminApp.VOTEINGPERIODSELECTIONSCREENID, election);
    }
    
}
