/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.Controllers;

import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.adminapp.model.Vote;
import ch.bfh.abcvote.adminapp.model.VoteInfo;
import ch.bfh.abcvote.adminapp.model.Voter;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 *
 * @author t.buerk
 */
public class MainController extends StackPane {
    //Holds the screens to be displayed
    
    private HashMap<String, Pair<ControlledScreen,Node>> screens = new HashMap<>();
    private CommunicationController communicationController;
    
    public MainController(){
        super();
        communicationController = new CommunicationController();   
    }
    
    //Add the screen to the collection
    public void addScreen(String name, Pair<ControlledScreen,Node> screenPair){
        screens.put(name, screenPair);
    }
    
    //Returns the Node with the given name
    public Pair<ControlledScreen,Node> getScreen(String name) {
        return screens.get(name);
        
    }
    
    //load the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the Controller
    public boolean loadScreen(String name, String rescource){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rescource));
            Parent loadScreen = (Parent) loader.load();
            ControlledScreen screenController = (ControlledScreen) loader.getController();
            screenController.setScreenParent(this);
            Pair<ControlledScreen,Node> screenPair = new Pair<ControlledScreen,Node>(screenController, loadScreen);
            addScreen(name, screenPair);
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    //This method tries to display the screen with the given name
    //First it makes sure the screen has already been loaded.
    ///Then it adds the new Screen and if necessary removes the currently displayed screen.
    
    public boolean setScreen(final String name){
        if (screens.get(name) != null){
            final DoubleProperty opacity = opacityProperty();
            if ( !getChildren().isEmpty()){
               Timeline fade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
                    
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(0);
                        getChildren().add(0,screens.get(name).getValue());
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                        
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();   
            }
            else{
               setOpacity(0.0);
               getChildren().add(screens.get(name).getValue());
               Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
               fadeIn.play();
            }
            return true;
               
        }  
        else{
           System.out.println("screen hasn't been loaded! \n");
           return false;
        
        }

    }
    
    //Tis method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null){
            System.out.println("Screen didn't exist");
            return false;
        }
        else{
            return true;
        }
    }

    boolean setScreenWithVote(String name, Vote vote) {
        
        setScreen(name);
        screens.get(name).getKey().setScene(vote);
        
        return true;
    }

    public VoteInfo getVoteInfo() {
        return communicationController.getVoteInformation();
    }

    List<Voter> getAllARegisteredVoters() {
        return communicationController.getAllARegisteredVoters();
    }
}
