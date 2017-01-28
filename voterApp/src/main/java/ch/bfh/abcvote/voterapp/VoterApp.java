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
package ch.bfh.abcvote.voterapp;

import ch.bfh.abcvote.voterapp.controllers.MainController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * VoterApp
 * @author t.buerk
 */
public class VoterApp extends Application {

    //IDs and rescourcefile paths of all Scenes are defined, so all scenecontrollers can refrence other scenes

    /**
     * home screen's id
     */
    public static final String HOMESCREENID = "home";

    /**
     * home screen's fxml
     */
    public static final String HOMESCREENFILE = "/fxml/Home.fxml";

    /**
     * voter registration screen's id
     */
    public static final String VOTERREGISTRATIONSCREENID = "voterRegistration";

    /**
     * voter registration screen's fxml 
     */
    public static final String VOTERREGISTRATIONFILE = "/fxml/VoterRegistration.fxml";

    /**
     * election overview screen's id
     */
    public static final String ELECTIONSOVERVIEWSCREENID = "electionsOverview";

    /**
     * election overview screen's fxml
     */
    public static final String ELECTIONSOVERVIEWFILE = "/fxml/ElectionsOverview.fxml";

    /**
     * pick option screen's id
     */
    public static final String PICKOPTIONSSCREENID = "pickOptions";

    /**
     * pick option screen's fxml
     */
    public static final String PICKOPTIONSFILE = "/fxml/PickOptions.fxml";

    /**
     * ballot summary screen's id
     */
    public static final String BALLOTSUMMARYSCREENID = "ballotSummary";

    /**
     * ballot summary screen's fxml
     */
    public static final String BALLOTSUMMARYFILE = "/fxml/BallotSummary.fxml";
    
    @Override
    public void start(Stage mainStage) throws Exception {
        //Maincontroller gets created and all defined Screens are passed to the Maincontroller to initialize and store
        MainController mainController = new MainController();
        mainController.loadScreen(VoterApp.HOMESCREENID, VoterApp.HOMESCREENFILE);
        mainController.loadScreen(VoterApp.VOTERREGISTRATIONSCREENID, VoterApp.VOTERREGISTRATIONFILE);
        mainController.loadScreen(VoterApp.ELECTIONSOVERVIEWSCREENID, VoterApp.ELECTIONSOVERVIEWFILE);
        mainController.loadScreen(VoterApp.PICKOPTIONSSCREENID, VoterApp.PICKOPTIONSFILE);
        mainController.loadScreen(VoterApp.BALLOTSUMMARYSCREENID, VoterApp.BALLOTSUMMARYFILE);
        mainController.setScreen(VoterApp.HOMESCREENID);
        
        //Maincontroller is defined as root element of the stage
        Group root = new Group();
        root.getChildren().addAll(mainController);
        Scene scene = new Scene(root);
        mainStage.setTitle("abcVote");
        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
