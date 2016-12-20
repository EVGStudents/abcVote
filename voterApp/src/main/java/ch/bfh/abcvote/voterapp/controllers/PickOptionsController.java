/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionTopic;
import ch.bfh.abcvote.voterapp.ControlledScreen;
import ch.bfh.abcvote.voterapp.VoterApp;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class PickOptionsController implements Initializable,ControlledScreen {

    MainController parentController;   
    private Election election;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private TextField txtPick;
    @FXML
    private Label txtTopic;
    @FXML
    private ListView<String> lvOptions;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * Sets the parentController for the communication with other controllers
     * @param screenParent 
     */
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    /**
     * Takes the given election object and displays its available otions to vote on
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        //display header information
        this.election = election;
        ElectionTopic topic = election.getTopic();
        this.txtPick.setText(String.valueOf(topic.getPick()));
        this.txtTopic.setText(topic.getTitle());
        //display options
        populateOptionsListView(topic.getOptions());
    }
    
    /**
     * Method to set the screen with a given Ballot. Method not used in this controller class
     * @param ballot 
     */
    @Override
    public void setScene(Ballot ballot) {
        
    } 
      
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. 
     */    
    @Override
    public void setScene() {
        
    }
    
    /**
     * btBack-Button Clicked-Event: Sents the user back to the Electionsoverview screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.ELECTIONSOVERVIEWSCREENID);
    } 
    
    /**
     * btNext-Button Clicked-Event: Gets the selected options and creates a new ballot object with these options.
     * the ballot object gets passed to the ballot summary screen
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
        
        ObservableList<String> selectedOptions;
        selectedOptions = lvOptions.getSelectionModel().getSelectedItems();
        Ballot ballot = new Ballot(election,selectedOptions);
        parentController.setScreenWithBallot(VoterApp.BALLOTSUMMARYSCREENID, ballot);

    }
    
    /**
     * Takes the list of options strings and displays them for selection
     * @param optionsList 
     */
    private void populateOptionsListView(List<String> optionsList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(optionsList);
        lvOptions.setItems(listViewList);
        
    }
   
}
