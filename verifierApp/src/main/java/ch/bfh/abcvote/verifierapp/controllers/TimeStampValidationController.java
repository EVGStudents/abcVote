/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class for the TimeStampValidation.fxml Screen
 *
 * @author t.buerk
 */
public class TimeStampValidationController implements Initializable, ControlledScreen {

    private MainController parentController;
    private ElectionResult result;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private ListView<Ballot> lvBallots;
    
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
     * @param result 
     */    
    @Override
    public void setScene(ElectionResult result) {
        this.result = result;
        populateBallotsListView(result.getBallots());
    }
    
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. Method not used in this controller class
     */
    @Override
    public void setScene() {
        
    }

    @FXML
    private void btBackClicked(ActionEvent event) {
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
    }
    
    /**
     * Displays the given list of Ballots in the lvBallots-ListView
     * @param ballotList 
     */
    private void populateBallotsListView(List<Ballot> ballotList) {
        
        ObservableList<Ballot> listViewList = FXCollections.observableArrayList(ballotList);
        lvBallots.setItems(listViewList);
        
    }
    
}
