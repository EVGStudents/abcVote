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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
        
        populateBallotsTableView(result.getBallots());
        
        lbTopic.setText(election.getTitle());
        //display results
        HashMap<String,Integer> counterList =  result.getOptionCount();
        List<String> lvResultList = new ArrayList<String>();
        for ( String option : counterList.keySet()){
            lvResultList.add(option + ": " + counterList.get(option));
        }
        populateElectionResultListView(lvResultList);
    }
    
    /**
     * Displays the given list of result Strings in the lvResult-ListView
     * @param resultList 
     */
    private void populateElectionResultListView(List<String> resultList) {
        
        ObservableList<String> listViewList = FXCollections.observableArrayList(resultList);
        lvResult.setItems(listViewList);
        
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
