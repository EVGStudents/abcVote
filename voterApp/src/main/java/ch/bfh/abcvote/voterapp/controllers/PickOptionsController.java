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
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionTopic;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class PickOptionsController implements Initializable,ControlledScreen {

    MainController parentController;   
    private Election election;
    private Boolean useTor = false;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private TextField txtPick;
    @FXML
    private Label txtTopic;
    @FXML
    private ListView<String> lvOptions;
    @FXML
    private CheckBox chkTor;
    
    
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
     * Takes the given election object and displays its available otions to vote on
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        //display header information
        this.election = election;
        ElectionTopic topic = election.getTopic();
        this.txtPick.setText(String.valueOf(topic.getPick()));
        this.txtTopic.setText(topic.getTitle());
        //display options
        populateOptionsListView(topic.getOptions());
        this.chkTor.setSelected(true);
        useTor = true;
    }
    
    /**
     * Method to set the screen with a given Ballot. Method not used in this controller class
     * @param ballot 
     */
    @Override
    public void setScene(Ballot ballot) {
        
    } 
      
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. 
     */    
    @Override
    public void setScene() {
        
    }
    
    /**
     * btBack-Button Clicked-Event: Sents the user back to the Electionsoverview screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.ELECTIONSOVERVIEWSCREENID);
    } 
    
    /**
     * btNext-Button Clicked-Event: Gets the selected options and creates a new ballot object with these options.
     * the ballot object gets passed to the ballot summary screen
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
        
        ObservableList<String> selectedOptions;
        selectedOptions = lvOptions.getSelectionModel().getSelectedItems();
        Ballot ballot = new Ballot(election,selectedOptions);
        parentController.setScreenWithBallot(VoterApp.BALLOTSUMMARYSCREENID, ballot, useTor);

    }
    
    /**
     * Takes the list of options strings and displays them for selection
     * @param optionsList 
     */
    private void populateOptionsListView(List<String> optionsList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(optionsList);
        lvOptions.setItems(listViewList);
        
    }
   
    
    /**
     * 
     * @param event 
     */
    @FXML
    private void chkTorChanged(ActionEvent event) {
        
        if (chkTor.isSelected()) {
            useTor = true;
        } else {
            useTor = false;
        }
    }
   
}
