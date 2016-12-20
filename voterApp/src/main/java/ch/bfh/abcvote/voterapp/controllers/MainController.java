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
        //new CommunicationController is created with the url of the bulletin board
        communicationController = new CommunicationController("http://abc.2488.ch/");   
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
    
    /**
     * This method exchanges the current Screen with the new Screen given by the name and calls the new screens setScene method without passing any information  
     * @param name
     * @return 
     */
    public boolean setScreen(final String name){
        changeScreen(name);
        screens.get(name).getKey().setScene();
        return true;
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
     * this method changes the current Screen to the given name
     * and afterwards calls that screens setScene Method to pass the given election object for display 
     * @param name
     * @param election
     * @return 
     */
    public boolean setScreenWithElection(String name, Election election) {
        
        changeScreen(name);
        screens.get(name).getKey().setScene(election);
        
        return true;
    }

    /**
     * Creates ne private credentials registers the corresponding public credential on the bulletin board using the given email address 
     * @param email
     * @throws Exception 
     */
    public void registerNewVoter(String email) throws Exception {
        //get Parameters
        Parameters parameters = communicationController.getParameters();
        
        //pick private Credentials
        PrivateCredentials privateCredentials = new PrivateCredentials(parameters);
        //calculate public credentials
        Element u = privateCredentials.getU();
        
        if (communicationController.registerNewVoter(email, parameters, u)) {
            //if the registration was successful the new private credentials are stored locally
            storePrivateCredentials(privateCredentials);
        } else {
            System.out.println("New Voter couldn't be registered.");
        };
    }
    
    /**
     * Gets a list of ElectionHeaders from the bulletin board. The selected ElectionHeaders depend on the passed filter option
     * @param filter
     * given filter option can be set to ALL, CLOSED or OPEN
     * @return 
     */
    public List<ElectionHeader> getElectionHeaders(ElectionFilterTyp filter) {
        List<ElectionHeader> electionHeadersList = communicationController.getElectionHeaders(filter);
        return electionHeadersList;
    }
    
    /**
     * Gets the election data for the given electionID from the bulletin board and rturns it as an election object
     * @param electionId
     * @return 
     */
    Election getElectionById(int electionId) {
        Election election = communicationController.getElectionById(electionId);
        return election;
    }
    
    /**
     * this method changes the current Screen to the given name
     * and afterwards calls that screens setScene Method to pass the given ballot object for display 
     * @param name
     * @param ballot
     * @return 
     */
    public boolean setScreenWithBallot(String name, Ballot ballot) {
        changeScreen(name);
        screens.get(name).getKey().setScene(ballot);
        return true;
    }

    /**
     * Retrieve the private credentials from a local key store
     * @return
     * @throws Exception 
     */
    public PrivateCredentials getPrivateCredentials() throws Exception {
        Parameters parameters = communicationController.getParameters();
        KeyStore keyStore = KeyStoreController.loadKeyStoreFromFile(pathToKeyStore, keyStorePassword);
        PrivateCredentials privateCredentials = new PrivateCredentials(parameters, KeyStoreController.readStringFromKeyStore(keyStore, keyStorePassword, stringPassword, "alpha"), KeyStoreController.readStringFromKeyStore(keyStore, keyStorePassword, stringPassword, "beta"));
        return privateCredentials;
    }
    
    /**
     * store the given private credentials in a local key store
     * @param privateCredentials
     * @throws Exception 
     */
    public void storePrivateCredentials(PrivateCredentials privateCredentials) throws Exception{
        //Store private Credentials
        KeyStoreController.writeStringToKeyStore(pathToKeyStore, keyStorePassword, stringPassword, "alpha", privateCredentials.getAlpha().convertToString());
        KeyStoreController.writeStringToKeyStore(pathToKeyStore, keyStorePassword, stringPassword, "beta", privateCredentials.getBeta().convertToString());
    }

    /**
     * Posts the given ballot to the bulletin board
     * @param ballot 
     */
    void postBallot(Ballot ballot) {
        communicationController.postBallot(ballot);
    }

}
