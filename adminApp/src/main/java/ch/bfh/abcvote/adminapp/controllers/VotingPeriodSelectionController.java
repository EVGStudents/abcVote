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
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 * Controller of the VotingPeriodSelection.fxml view
 * @author t.buerk
 */
public class VotingPeriodSelectionController implements Initializable, ControlledScreen {

    Election election;
    MainController parentController;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    
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
     * and sets the DatePicker-Controls to the current Date 
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        this.election = election;
        dateStart.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());
    }

    /**
     *  btBack-Button Click-Event: sends the user back to the ElectionOptionCreation Screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.ELECTIONOPTIONCREATIONSCREENID);
    }
    
    /**
     * btNext-Button Click-Event: Stores the entered VotingPeriod in the election object and 
     * passes it to the ElectionSummary Screen via the mainController
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
        LocalDate start = dateStart.getValue();
        LocalDate end = dateEnd.getValue();
        //parse LocalDate objects to  LocalDateTime objects
        LocalDateTime startDateTime = LocalDateTime.of(start, LocalTime.parse("00:00:00"));
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.parse("00:00:00"));
        election.setVotingPeriod(startDateTime, endDateTime);
        parentController.setScreenWithElection(AdminApp.ELECTIONSUMMARYSCREENID, election);
    }
    
}
