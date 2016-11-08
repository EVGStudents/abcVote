/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp;

import ch.bfh.abcvote.adminapp.Controllers.MainController;
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
    public static final String VOTERSELECTIONSCREENID = "voterSelection";
    public static final String VOTERSELECTIONSCREENFILE = "/fxml/VoterSelection.fxml";
    public static final String VOTEOPTIONCREATIONSCREENID = "voteOptionCreation";
    public static final String VOTEOPTIONCREATIONSCREENFILE = "/fxml/VoteOptionCreation.fxml";
    
    @Override
    public void start(Stage mainStage) throws Exception {
        //Maincontroller gets created and all defined Screens are passed to the Maincontroller to initialize and store
        MainController mainController = new MainController();
        mainController.loadScreen(AdminApp.HOMESCREENID, AdminApp.HOMESCREENFILE);
        mainController.loadScreen(AdminApp.VOTERSELECTIONSCREENID, AdminApp.VOTERSELECTIONSCREENFILE);
        mainController.loadScreen(AdminApp.VOTEOPTIONCREATIONSCREENID, AdminApp.VOTEOPTIONCREATIONSCREENFILE);
        mainController.setScreen(AdminApp.HOMESCREENID);
        
        //Maincontroller is defined as root element of the stage
        Group root = new Group();
        root.getChildren().addAll(mainController);
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
