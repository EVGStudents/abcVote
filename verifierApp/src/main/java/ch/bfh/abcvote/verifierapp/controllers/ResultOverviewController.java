/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import ch.bfh.abcvote.verifierapp.VerifierApp;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * FXML Controller class for the ResultOverview.fxml Screen
 *
 * @author t.buerk
 */
public class ResultOverviewController implements Initializable, ControlledScreen {

    private MainController parentController;
    private ElectionResult result;
        
    @FXML
    private Button btBack;
    @FXML
    private Button btPublish;
    @FXML
    private Button btHome;
    @FXML
    private Label lbElectionTitle;
    @FXML
    private Label lbTopic;
    @FXML
    private ListView<String> lvResult;
    
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
     * Method to set the screen with a given election. Method not used in this controller class
     * @param election 
     */
    @Override
    public void setScene(Election election) {
        
    }

    /**
     * Method to set the screen with a given ElectionResult.
     * Method stores and displays the given result
     * @param result 
     */
    @Override
    public void setScene(ElectionResult result) {
        this.result = result;
        displayResult(result);
    }
    
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed.
     * Method not used in this controller class
     */
    @Override
    public void setScene() {
        
    }

    /**
     * btBack-Button Clicked-Event: transfers the voter back to the ElectionsOverview screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID);
    }

    /**
     * btPublish-Button Clicked-Event: Posts the calculated result to the bulletin board and transfers the user back to the home screen
     * @param event 
     */
    @FXML
    private void btPublishClicked(ActionEvent event) {
        parentController.postResult(result);
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }

    /**
     * btHome-Button Clicked-Event: transfers the user back to the home screen without posting the result
     * @param event 
     */
    @FXML
    private void btHomeClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }

    /**
     * Method to display the given result object in the view
     * @param result 
     */
    private void displayResult(ElectionResult result) {
        //Display Header information
        Election election = result.getElection();
        lbElectionTitle.setText(election.getTopic().getTitle());
        lbTopic.setText(election.getTitle());
        //display results
        HashMap<String,Integer> counterList =  result.getOptionCount();
        List<String> lvResultList = new ArrayList<String>();
        for ( String option : counterList.keySet()){
            lvResultList.add(option + ": " + counterList.get(option));
        }
        populateElectionHeaderListView(lvResultList);
        
    }
    
    /**
     * Displays the given list of result Strings in the lvResult-ListView
     * @param resultList 
     */
    private void populateElectionHeaderListView(List<String> resultList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(resultList);
        lvResult.setItems(listViewList);
        
    }
    
}
