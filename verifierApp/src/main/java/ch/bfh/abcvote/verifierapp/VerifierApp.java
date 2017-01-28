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
package ch.bfh.abcvote.verifierapp;

import ch.bfh.abcvote.verifierapp.controllers.MainController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * 
 * @author t.buerk
 */
public class VerifierApp extends Application {

    //IDs and rescourcefile paths of all Scenes are defined, so all scenecontrollers can refrence other scenes
    public static final String HOMESCREENID = "home";
    public static final String HOMESCREENFILE = "/fxml/Home.fxml";
    public static final String ELECTIONSOVERVIEWSCREENID = "electionsOverview";
    public static final String ELECTIONSOVERVIEWFILE = "/fxml/ElectionsOverview.fxml";
    public static final String RESULTOVERVIEWSCREENID = "resultOverview";
    public static final String RESULTOVERVIEWFILE = "/fxml/ResultOverview.fxml";
    public static final String TIMESTAMPVALIDATIONSCREENID = "timestampValidation";
    public static final String TIMESTAMPVALIDATIONFILE = "/fxml/TimeStampValidation.fxml";
    public static final String NIZKPVALIDATIONSCREENID = "NIZKPValidation";
    public static final String NIZKPVALIDATIONFILE = "/fxml/NIZKPValidation.fxml";    
    
    @Override
    public void start(Stage mainStage) throws Exception {
        //Maincontroller gets created and all defined Screens are passed to the Maincontroller to initialize and store
        MainController mainController = new MainController();
        mainController.loadScreen(VerifierApp.HOMESCREENID, VerifierApp.HOMESCREENFILE);
        mainController.loadScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID, VerifierApp.ELECTIONSOVERVIEWFILE);
        mainController.loadScreen(VerifierApp.RESULTOVERVIEWSCREENID, VerifierApp.RESULTOVERVIEWFILE);
        mainController.loadScreen(VerifierApp.TIMESTAMPVALIDATIONSCREENID, VerifierApp.TIMESTAMPVALIDATIONFILE);
        mainController.loadScreen(VerifierApp.NIZKPVALIDATIONSCREENID, VerifierApp.NIZKPVALIDATIONFILE);
        mainController.setScreen(VerifierApp.HOMESCREENID);
        
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
