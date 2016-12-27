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
    
    
    @Override
    public void start(Stage mainStage) throws Exception {
        //Maincontroller gets created and all defined Screens are passed to the Maincontroller to initialize and store
        MainController mainController = new MainController();
        mainController.loadScreen(VerifierApp.HOMESCREENID, VerifierApp.HOMESCREENFILE);
        mainController.loadScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID, VerifierApp.ELECTIONSOVERVIEWFILE);
        mainController.loadScreen(VerifierApp.RESULTOVERVIEWSCREENID, VerifierApp.RESULTOVERVIEWFILE);
        mainController.loadScreen(VerifierApp.TIMESTAMPVALIDATIONSCREENID, VerifierApp.TIMESTAMPVALIDATIONFILE);
        mainController.setScreen(VerifierApp.HOMESCREENID);
        
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
