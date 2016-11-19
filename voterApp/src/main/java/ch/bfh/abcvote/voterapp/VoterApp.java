package ch.bfh.abcvote.voterapp;

import ch.bfh.abcvote.voterapp.controllers.MainController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class VoterApp extends Application {

    
    //IDs and rescourcefile paths of all Scenes are defined, so all scenecontrollers can refrence other scenes
    public static final String HOMESCREENID = "home";
    public static final String HOMESCREENFILE = "/fxml/Home.fxml";

    @Override
    public void start(Stage mainStage) throws Exception {
        //Maincontroller gets created and all defined Screens are passed to the Maincontroller to initialize and store
        MainController mainController = new MainController();
        mainController.loadScreen(VoterApp.HOMESCREENID, VoterApp.HOMESCREENFILE);
        mainController.setScreen(VoterApp.HOMESCREENID);
        
        //Maincontroller is defined as root element of the stage
        Group root = new Group();
        root.getChildren().addAll(mainController);
        Scene scene = new Scene(root);
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
