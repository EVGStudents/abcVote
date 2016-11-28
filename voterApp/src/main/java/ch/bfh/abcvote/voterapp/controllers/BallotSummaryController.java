/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.voterapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.PrivateCredentials;
import ch.bfh.abcvote.util.model.Vote;
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

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class BallotSummaryController implements Initializable,ControlledScreen {

    @FXML
    private Button btBack;

    MainController parentController;
    @FXML
    private Button btCastBallot;
    
    private Ballot ballot;
    @FXML
    private Label lbElectionTitle;
    @FXML
    private Label lbTopic;
    @FXML
    private Label lbPick;
    @FXML
    private ListView<String> lvSelectedOptions;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(VoterApp.PICKOPTIONSSCREENID);
    }


    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    @Override
    public void setScene(Vote vote) {
        
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        PrivateCredentials privatCredentials = parentController.getPrivateCredentials();
        ballot.calculateProves(privatCredentials);
        parentController.postBallot(ballot);
        parentController.setScreen(VoterApp.HOMESCREENID);
    }

    @Override
    public void setScene() {
        
    }

    @Override
    public void setScene(Ballot ballot) {
        this.ballot = ballot;
        displayBallot(ballot);
    }

    private void displayBallot(Ballot ballot) {
        Vote vote = ballot.getVote();
        lbElectionTitle.setText(vote.getTitle());
        lbTopic.setText(vote.getTopic().getTitle());
        lbPick.setText(String.valueOf(vote.getTopic().getPick()));
        populateSelectedOptionsListView(ballot.getSelectedOptions());
    }
    
        private void populateSelectedOptionsListView(List<String> selectedOptionsList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(selectedOptionsList);
        lvSelectedOptions.setItems(listViewList);
        
    }
    
}
