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
import ch.bfh.abcvote.verifierapp.VerifierApp;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author t.buerk
 */
public class NIZKPValidationController implements Initializable, ControlledScreen {

    private MainController parentController;
    private ElectionResult result;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private TableView<Ballot> tvBallots;
    
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
        populateBallotsTableView(result.getBallots());
    }
    
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. Method not used in this controller class
     */
    @Override
    public void setScene() {
        
    }

    /**
     * btBack-Button Clicked-Event: Transfers the user back to the timestamp validation screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
         parentController.setScreen(VerifierApp.TIMESTAMPVALIDATIONSCREENID);
    }
    
    /**
     * btNext-Button Clicked-Event: Preforms the validation process of the NIZKProofs on the passed Electionresult 
     * Afterwards the result gets passed to the NIZKP Validation screen
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
           ElectionResult result = parentController.SelectFromValidBallotsAndCalculateResult(this.result);
           // pass electionResult to Resultscreen
           parentController.setScreenWithResult(VerifierApp.RESULTOVERVIEWSCREENID, result);
    }
    
    /**
     * Displays the given list of Ballots in the tvBallots-TableView
     * @param ballotList 
     */
    private void populateBallotsTableView(List<Ballot> ballotList) {
        
        ObservableList<Ballot> listViewList = FXCollections.observableArrayList(ballotList);
        //construct tableView
        tvBallots.getColumns().clear();
        TableColumn idColumn = new TableColumn("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn timestampColumn = new TableColumn("Timestamp");
        timestampColumn.setCellValueFactory(new PropertyValueFactory("timeStamp"));
        TableColumn optionColumn = new TableColumn("Option");
        optionColumn.setCellValueFactory(new PropertyValueFactory("selectedOptionsString"));
        TableColumn validColumn = new TableColumn("Valid");
        validColumn.setCellValueFactory(new PropertyValueFactory("valid"));
        TableColumn reasonColumn = new TableColumn("Reason");
        reasonColumn.setCellValueFactory(new PropertyValueFactory("reason"));        
        TableColumn identifierColumn = new TableColumn("Identifier");
        identifierColumn.setCellValueFactory(new PropertyValueFactory("u_HatString"));
        
        //add conditional coloring to validColumn cells
        validColumn.setCellFactory(new Callback<TableColumn<Ballot, Boolean>, TableCell<Ballot, Boolean>>() {
            @Override
            public TableCell<Ballot, Boolean> call(TableColumn<Ballot, Boolean> personStringTableColumn) {
                return new TableCell<Ballot, Boolean>() {
                    @Override
                    protected void updateItem(Boolean name, boolean empty) {
                        super.updateItem(name, empty);
                        if (name != null) {
                            setText(name.toString());
                            if (name){
                                setStyle("-fx-text-fill:green;");
                            }
                            else{
                                setStyle("-fx-text-fill:red;");
                            }
                            
                        }
                    }
                };
            }
        });

        tvBallots.getColumns().addAll(idColumn, timestampColumn, optionColumn, validColumn, reasonColumn, identifierColumn);
        tvBallots.setItems(listViewList);
        
    }
    
}
