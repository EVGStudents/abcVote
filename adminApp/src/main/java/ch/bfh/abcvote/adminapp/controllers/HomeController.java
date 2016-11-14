/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.Controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.adminapp.model.Vote;
import ch.bfh.abcvote.adminapp.model.Generators;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class for the home screen of the adminApp 
 *
 * @author t.buerk
 */
public class HomeController implements Initializable, ControlledScreen {

    MainController parentController;
    
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }
    
    @FXML
    private Button btCreateVote;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    /**
     * Button press event to create a new vote. Gets the current Parameters of the Bulletin Board and creats a new Vote object.
     */
    @FXML
    private void createNewVote(ActionEvent event) {
        Generators voteInfo = parentController.getVoteInfo();
        Vote vote = new Vote();
        //maincontroller gets instructed to change the scene to the voterselection screen and pass the newly created vote
        parentController.setScreenWithVote(AdminApp.VOTERSELECTIONSCREENID, vote);
    }

    @Override
    public void setScene(Vote vote) {
        
    }


    
}
