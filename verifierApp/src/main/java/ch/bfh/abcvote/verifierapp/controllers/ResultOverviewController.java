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
 * FXML Controller class
 *
 * @author t.buerk
 */
public class ResultOverviewController implements Initializable, ControlledScreen {

    MainController parentController;
    @FXML
    private Button btBack;
    @FXML
    private Button btPublish;
    @FXML
    private Button btHome;
    
    private ElectionResult result;
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

    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Election election) {
        
    }

    @Override
    public void setScene() {
        
    }

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID);
    }


    @FXML
    private void btPublishClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }

    @FXML
    private void btHomeClicked(ActionEvent event) {
        parentController.setScreen(VerifierApp.HOMESCREENID);
    }

    @Override
    public void setScene(ElectionResult result) {
        this.result = result;
        displayResult(result);
    }

    private void displayResult(ElectionResult result) {
        Election election = result.getElection();
        lbElectionTitle.setText(election.getTopic().getTitle());
        lbTopic.setText(election.getTitle());
        HashMap<String,Integer> counterList =  result.getOptionCount();
        List<String> lvResultList = new ArrayList<String>();
        for ( String option : counterList.keySet()){
            lvResultList.add(option + ": " + counterList.get(option));
        }
        populateElectionHeaderListView(lvResultList);
        
    }
    
    private void populateElectionHeaderListView(List<String> resultList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(resultList);
        lvResult.setItems(listViewList);
        
    }
    
}
