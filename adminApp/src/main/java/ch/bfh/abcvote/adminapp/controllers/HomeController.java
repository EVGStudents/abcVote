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
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.Election;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class for the home screen of the adminApp 
 *
 * @author t.buerk
 */
public class HomeController implements Initializable, ControlledScreen {

    MainController parentController;
    
    @FXML
    private Button btCreateElection;
    
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
     * This interface method is not used by the HomeController
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        
    }

        
    /**
     * btCreateNew-Button Click-Event: Event to create a new election.
     * Gets the current Parameters of the Bulletin Board and creates a new Election object.
     * Afterwards newly created election gets passed to the ElectionTitleCreation Screen via MainController 
     */
    @FXML
    private void btcreateNewElectionClicked(ActionEvent event) {
        Parameters params = parentController.getParameters();
        Election election = new Election(params);
        //maincontroller gets instructed to change the scene to the voterselection screen and pass the newly created election
        parentController.setScreenWithElection(AdminApp.ELECTIONTITLECREATIONSCREENID, election);
    }


    
}
