/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.Vote;
import ch.bfh.abcvote.util.model.VoteTopic;
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

    @FXML
    private Button btBack;

    MainController parentController;
    @FXML
    private Button btNext;
    @FXML
    private TextField txtPick;
    @FXML
    private Label txtTopic;
    @FXML
    private ListView<String> lvOptions;
    
    private Vote vote;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.ELECTIONSOVERVIEWSCREENID);
    }


    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Vote vote) {
        this.vote = vote;
        VoteTopic topic = vote.getTopic();
        this.txtPick.setText(String.valueOf(topic.getPick()));
        this.txtTopic.setText(topic.getTitle());
        populateOptionsListView(topic.getOptions());
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        
        ObservableList<String> selectedOptions;
        selectedOptions = lvOptions.getSelectionModel().getSelectedItems();
        Ballot ballot = new Ballot(vote,selectedOptions);
        parentController.setScreenWithBallot(VoterApp.BALLOTSUMMARYSCREENID, ballot);

    }
    
    @Override
    public void setScene() {
        
    }
    
    private void populateOptionsListView(List<String> optionsList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(optionsList);
        lvOptions.setItems(listViewList);
        
    }
    @Override
    public void setScene(Ballot ballot) {
        
    }    
}
