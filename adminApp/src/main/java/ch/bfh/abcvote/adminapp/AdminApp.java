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
package ch.bfh.abcvote.adminapp;

import ch.bfh.abcvote.adminapp.controllers.MainController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author t.buerk
 */
public class AdminApp extends Application {
    
    //IDs and rescourcefile paths of all Scenes are defined, so all scenecontrollers can refrence other scenes
    public static final String HOMESCREENID = "home";
    public static final String HOMESCREENFILE = "/fxml/Home.fxml";
    public static final String ELECTIONTITLECREATIONSCREENID = "electionTitleCreation";
    public static final String ELECTIONTITLECREATIONSCREENFILE = "/fxml/ElectionTitleCreation.fxml";
    public static final String VOTERSELECTIONSCREENID = "voterSelection";
    public static final String VOTERSELECTIONSCREENFILE = "/fxml/VoterSelection.fxml";
    public static final String ELECTIONOPTIONCREATIONSCREENID = "electionOptionCreation";
    public static final String ELECTIONOPTIONCREATIONSCREENFILE = "/fxml/ElectionOptionCreation.fxml";
    public static final String VOTEINGPERIODSELECTIONSCREENID = "votingPeriodSelection";
    public static final String VOTEINGPERIODSELECTIONSCREENFILE = "/fxml/VotingPeriodSelection.fxml";
    public static final String ELECTIONSUMMARYSCREENID = "electionSummary";
    public static final String ELECTIONSUMMARYSCREENFILE = "/fxml/ElectionSummary.fxml"; 
    
    @Override
    public void start(Stage mainStage) throws Exception {
        //Maincontroller gets created and all defined Screens are passed to the Maincontroller to initialize and store
        MainController mainController = new MainController();
        mainController.loadScreen(AdminApp.HOMESCREENID, AdminApp.HOMESCREENFILE);
        mainController.loadScreen(AdminApp.ELECTIONTITLECREATIONSCREENID, AdminApp.ELECTIONTITLECREATIONSCREENFILE);
        mainController.loadScreen(AdminApp.VOTERSELECTIONSCREENID, AdminApp.VOTERSELECTIONSCREENFILE);
        mainController.loadScreen(AdminApp.ELECTIONOPTIONCREATIONSCREENID, AdminApp.ELECTIONOPTIONCREATIONSCREENFILE);
        mainController.loadScreen(AdminApp.VOTEINGPERIODSELECTIONSCREENID, AdminApp.VOTEINGPERIODSELECTIONSCREENFILE);
        mainController.loadScreen(AdminApp.ELECTIONSUMMARYSCREENID, AdminApp.ELECTIONSUMMARYSCREENFILE);
        mainController.setScreen(AdminApp.HOMESCREENID);
        
        //Maincontroller is defined as root element of the stage
        Group root = new Group();
        root.getChildren().addAll(mainController);
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setTitle("abcVote");
        mainStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
