/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp.controllers;

import ch.bfh.abcvote.util.controllers.CommunicationController;
import ch.bfh.abcvote.util.controllers.KeyStoreController;
import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.PrivateCredentials;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.voterapp.ControlledScreen;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.io.File;
import java.security.KeyStore;
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
 * and between the Screencontrollers and the CommunicationController
 * @author t.buerk
 */

public class MainController extends StackPane {


    
    //Hashmap that holds all the registred Controllers and thie Screens
    private HashMap<String, Pair<ControlledScreen,Node>> screens = new HashMap<>();
    private CommunicationController communicationController;
    
    private final String pathToKeyStore = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "abcVote" + File.separator + "test.jks";
    private final String keyStorePassword = "Bern2016";
    private final String stringPassword = "Bern2016";
    
    public MainController(){
        super();
        communicationController = new CommunicationController("http://abc.2488.ch/");   
    }
    
    //Adds a new a new Controller and Screen Pair to the Hashmap
    public void addScreen(String name, Pair<ControlledScreen,Node> screenPair){
        screens.put(name, screenPair);
    }
    
    //Returns the Controller and Screen Pair for the given name
    public Pair<ControlledScreen,Node> getScreen(String name) {
        return screens.get(name);
        
    }
    
    //load the fxml file, and add its Controller and Screen Pair to the Hashmap for further refrence
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
    
    //This method exchanges the current Screen with the new Screen given by the name
    private boolean changeScreen(final String name){
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
    
    //This method exchanges the current Screen with the new Screen given by the name
    //And afterwards calls its setScrene Method
    public boolean setScreen(final String name){
        changeScreen(name);
        screens.get(name).getKey().setScene();
        return true;
    }
    
    //This method will remove the Controller and screen  pair with the given name from the collection of Pairs
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null){
            System.out.println("Screen didn't exist");
            return false;
        }
        else{
            return true;
        }
    }

    
    //this method changes the current Screen to the given name
    //and afterwards calls that screens setScene Method to pass the given election object for display 
    public boolean setScreenWithElection(String name, Election election) {
        
        changeScreen(name);
        screens.get(name).getKey().setScene(election);
        
        return true;
    }

    public void registerNewVoter(String email) throws Exception {
        //get Parameters
        Parameters parameters = communicationController.getParameters();
        
        //pick private Credentials
        PrivateCredentials privateCredentials = new PrivateCredentials(parameters);
        //calculate public credentials
        Element u = privateCredentials.getU();
        
        if (communicationController.registerNewVoter(email, parameters, u)) {
            storePrivateCredentials(privateCredentials);
        } else {
            System.out.println("New Voter couldn't be registered.");
        };
    }
    
    public List<ElectionHeader> getElectionHeaders(ElectionFilterTyp filter) {
        List<ElectionHeader> electionHeadersList = communicationController.getElectionHeaders(filter);
        return electionHeadersList;
    }

    Election getElectionById(int electionId) {
        Election election = communicationController.getElectionById(electionId);
        return election;
    }

    public boolean setScreenWithBallot(String name, Ballot ballot) {
        changeScreen(name);
        screens.get(name).getKey().setScene(ballot);
        return true;
    }

    public PrivateCredentials getPrivateCredentials() throws Exception {
        Parameters parameters = communicationController.getParameters();
        KeyStore keyStore = KeyStoreController.loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        PrivateCredentials privateCredentials = new PrivateCredentials(parameters, KeyStoreController.readStringFromKeyStore(keyStore, keyStorePassword, stringPassword, "alpha"), KeyStoreController.readStringFromKeyStore(keyStore, keyStorePassword, stringPassword, "beta"));
        return privateCredentials;
    }
    
    public void storePrivateCredentials(PrivateCredentials privateCredentials) throws Exception{
        //Store private Credentials
        KeyStoreController.writeStringToKeyStore(pathToKeyStore, keyStorePassword, stringPassword, "alpha", privateCredentials.getAlpha().convertToString());
        KeyStoreController.writeStringToKeyStore(pathToKeyStore, keyStorePassword, stringPassword, "beta", privateCredentials.getBeta().convertToString());
    }

    void postBallot(Ballot ballot) {
        communicationController.postBallot(ballot);
    }

}
