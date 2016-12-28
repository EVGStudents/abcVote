/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.controllers.CommunicationController;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.Voter;
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
 * The MainController Class acts as the root element of the Stage.
 * It acts as a hub for the communication between the diffrent Screencontrollers
 * and between the Screencontrollers and the other Controller-Classes
 * @author t.buerk
 */
public class MainController extends StackPane {
    
    //Hashmap that holds all the registred Controllers and thier Screens
    private HashMap<String, Pair<ControlledScreen,Node>> screens = new HashMap<>();
    private CommunicationController communicationController;
    
    public MainController(){
        super();
        //new CommunicationController is created with the url of the bulletin board
        communicationController = new CommunicationController("https://abc.2488.ch/");   
    }
    
    
    /**
     * Adds a new Controller and Screen Pair to the Hashmap
     * @param name 
     * key value to store the screenPair in the hashmap
     * @param screenPair 
     * Pair<ControlledScreen,Node> containing the Controller and the corepsonding Screen 
     */
    public void addScreen(String name, Pair<ControlledScreen,Node> screenPair){
        screens.put(name, screenPair);
    }
    
    /**
     * Returns the Controller and Screen Pair for the given name
     * @param name
     * key value to reference the element to be return from the hashmap
     * @return 
     */
    public Pair<ControlledScreen,Node> getScreen(String name) {
        return screens.get(name);       
    }
    
    
    /**
     * Loads the fxml file and add its Controller and Screen Pair and stores it with the key name in the Hashmap for further reference
     * @param name
     * key value under which the created Controller and Screen Pair gets stored
     * @param rescource
     * contains path of the referenced fxml file
     * @return 
     */
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
    
    /**
     * This method exchanges the current Screen with the new Screen given by the name  
     * @param name
     * @return 
     */
    public boolean setScreen(final String name){
        //Checks if there is a Stored Screnn for the given name
        if (screens.get(name) != null){
            final DoubleProperty opacity = opacityProperty();
            //checks if there is screen that is already displayed
            if ( !getChildren().isEmpty()){
                //if there is an old screen present it gets faded out before the new screen gets faded in
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
               //if there is no old screen the new screen just gets faded in
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
    
    /**
     * This method will remove the Controller and screen  pair with the given name from the collection of Pairs
     * @param name
     * @return 
     */
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null){
            System.out.println("Screen didn't exist");
            return false;
        }
        else{
            return true;
        }
    }

    
    /**
     * this method changes the current Screen to the Screen which is stored under given name
     * and afterwards calls that screens setScene method to pass the given election object
     * @param name
     * reference to the new screen to be displayed
     * @param election
     * election object gets passed to the new screens controller
     * @return 
     */
    public boolean setScreenWithElection(String name, Election election) {
        
        setScreen(name);
        screens.get(name).getKey().setScene(election);
        
        return true;
    }

    /**
     * Gets the parameters from the communicationcontroller and returns them 
     * @return 
     */
    public Parameters getParameters() {
        return communicationController.getParameters();
    }

    /**
     * gets a list of all currently registred voters from the communicationcontroller and returns it
     * @return 
     */
    List<Voter> getAllARegisteredVoters() {
        return communicationController.getAllARegisteredVoters();
    }

    /**
     * Passes the given election object to the communicationcontroller to post it to the bulletin board
     * @param election 
     * election to be posted
     */
    void postElection(Election election) {
        communicationController.postElection(election);
    }
}
