/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.Controllers;

import ch.bfh.abcvote.adminapp.AdminApp;
import ch.bfh.abcvote.adminapp.ControlledScreen;
import ch.bfh.abcvote.adminapp.model.Vote;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class VoteOptionCreationController implements Initializable, ControlledScreen  {

    MainController parentController;
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    
        @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @Override
    public void setScene(Vote vote) {
       
    }

    @FXML
    private void btBackClicked(ActionEvent event) {
         parentController.setScreen(AdminApp.VOTERSELECTIONSCREENID);
    }

    @FXML
    private void btNextClicked(ActionEvent event) {
    }
    
}
