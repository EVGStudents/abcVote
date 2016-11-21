/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.util.model.Vote;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class VotingPeriodSelectionController implements Initializable, ControlledScreen {

    Vote vote;
    MainController parentController;
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    
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
    public void setScene(Vote vote) {
        dateStart.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());
        this.vote = vote;
    }

    @FXML
    private void btBackClicked(ActionEvent event) {
        parentController.setScreen(AdminApp.VOTEOPTIONCREATIONSCREENID);
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
        LocalDate start = dateStart.getValue();
        LocalDate end = dateEnd.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(start, LocalTime.parse("00:00:00"));
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.parse("00:00:00"));
        vote.setVotingPeriod(startDateTime, endDateTime);
        parentController.setScreenWithVote(AdminApp.VOTESUMMARYSCREENID, vote);
    }
    
}
